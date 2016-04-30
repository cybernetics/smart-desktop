// package com.fs.commons.dao.dynamic.meta.xml;
//
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.Hashtable;
//
// import javax.xml.parsers.DocumentBuilder;
// import javax.xml.parsers.DocumentBuilderFactory;
// import javax.xml.parsers.ParserConfigurationException;
//
// import org.w3c.dom.Document;
// import org.w3c.dom.Element;
// import org.w3c.dom.Node;
// import org.w3c.dom.NodeList;
// import org.xml.sax.SAXException;
//
// import com.fs.commons.dao.DaoUtil;
// import com.fs.commons.dao.dynamic.constraints.Constraint;
// import com.fs.commons.dao.dynamic.constraints.DataRangeConstraint;
// import com.fs.commons.dao.dynamic.constraints.DuplicateDataConstraint;
// import com.fs.commons.dao.dynamic.constraints.IdenticalFieldsContraint;
// import com.fs.commons.dao.dynamic.constraints.LessThanContsraint;
// import com.fs.commons.dao.dynamic.meta.FieldMeta;
// import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
// import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.Relation;
// import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.ViewMode;
// import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
// import com.fs.commons.dao.dynamic.meta.TableMeta;
// import com.fs.commons.dao.dynamic.trigger.FieldTrigger;
// import com.fs.commons.util.GeneralUtility;
//
// public class MetaXmlParser {
//
// // private final String fileName;
//
// /**
// * @param abstractModule
// * @throws ParserConfigurationException
// * @throws IOException
// * @throws SAXException
// * @throws IllegalAccessException
// * @throws InstantiationException
// *
// *
// */
// public MetaXmlParser() {
// }
//
// /**
// *
// * @param fileName
// * @throws IOException
// * @throws SAXException
// * @throws ParserConfigurationException
// * @throws IllegalAccessException
// * @throws InstantiationException
// */
// // public TablesCofigParser(String fileName) throws JKXmlException {
// // this.fileName = fileName;
// // }
// /**
// *
// * @param string
// * @param fileName
// * @throws ParserConfigurationException
// * @throws SAXException
// * @throws IOException
// * @throws InstantiationException
// * @throws IllegalAccessException
// */
// public Hashtable<String, TableMeta> parse(InputStream in, String source)
// throws JKXmlException {
// try {
// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
// DocumentBuilder builder = factory.newDocumentBuilder();
// Document doc = builder.parse(in);
// NodeList tablesNode = doc.getElementsByTagName("table");
//
// Hashtable<String, TableMeta> tablesHash = new Hashtable<String, TableMeta>();
// for (int i = 0; i < tablesNode.getLength(); i++) {
// Element table = (Element) tablesNode.item(i);
// TableMeta tableInstance = TableMeta.class.newInstance();
// tableInstance.setTableName(table.getAttribute("name"));
// if (!table.getAttribute("id").equals("")) {
// tableInstance.setTableId(table.getAttribute("id"));
// }
// if (!table.getAttribute("max-records_count").equals("")) {
// tableInstance.setMaxRecordsCount(Integer.parseInt(table.getAttribute("max-records_count")));
// }
// if (!table.getAttribute("icon-image").equals("")) {
// tableInstance.setIconName(table.getAttribute("icon-image"));
// }
// if (!table.getAttribute("caption").equals("")) {
// tableInstance.setCaption(table.getAttribute("caption"));
// }
// if (!table.getAttribute("allow-manage").equals("")) {
// tableInstance.setAllowManage(Boolean.parseBoolean(table.getAttribute("allow-manage")));
// }
// if (!table.getAttribute("page-row-count").equals("")) {
// tableInstance.setPageRowCount(Integer.parseInt(table.getAttribute("page-row-count")));
// }
// if (!table.getAttribute("filter-indices").equals("")) {
// tableInstance.setFilters(table.getAttribute("filter-indices").split(","));
// }
// if (!table.getAttribute("ui-colunm-count").equals("")) {
// System.err.println("'ui-colunm-count' property is depracted : kindly use
// ui-row-count .");
// tableInstance.setDefaultUIRowCount(Integer.parseInt(table.getAttribute("ui-colunm-count")));
// }
// if (!table.getAttribute("ui-row-count").equals("")) {
// tableInstance.setDefaultUIRowCount(Integer.parseInt(table.getAttribute("ui-row-count")));
// }
//
// if (!table.getAttribute("panel-class").equals("")) {
// tableInstance.setPanelClassName(table.getAttribute("panel-class"));
// }
// if (!table.getAttribute("manage-panel-class").equals("")) {
// tableInstance.setManagePanelClassName(table.getAttribute("manage-panel-class"));
// }
// if (!table.getAttribute("allow-delete").equals("")) {
// tableInstance.setAllowDelete(Boolean.parseBoolean(table.getAttribute("allow-delete")));
// }
// if (!table.getAttribute("allow-update").equals("")) {
// tableInstance.setAllowUpdate(Boolean.parseBoolean(table.getAttribute("allow-update")));
// }
// if (!table.getAttribute("allow-add").equals("")) {
// tableInstance.setAllowAdd(Boolean.parseBoolean(table.getAttribute("allow-add")));
// }
// if (!table.getAttribute("class").equals("")) {
// tableInstance.setBeanName(table.getAttribute("class"));
// }
// // fields
// for (int j = 0; j < table.getChildNodes().getLength(); j++) {
// Node node = table.getChildNodes().item(j);
// if (node.getNodeName().equals("id-field")) {
// IdFieldMeta id = parseIdField((Element) node);
// id.setParentTable(tableInstance);
// tableInstance.setIdField(id);
// } else if (node.getNodeName().equals("field")) {
// FieldMeta field = parseField((Element) node, tableInstance);
// tableInstance.addField(field);
// } else if (node.getNodeName().equals("constraints")) {
// ArrayList<Constraint> constraints = parseConstraints(tableInstance, node);
// tableInstance.setConstraints(constraints);
// // } else if
// // (node.getNodeName().equals("filter-fields")) {
// // ArrayList<String> filters =
// // parseFilters(tableInstance, node);
// // tableInstance.setFilters(filters);
// } else if (node.getNodeName().equals("report-sql")) {
// tableInstance.setReportSql(node.getTextContent().trim());
// } else if (node.getNodeName().equals("short-sql")) {
// tableInstance.setShortReportSql(node.getTextContent().trim());
// } else if (node.getNodeName().equals("list-sql")) {
// tableInstance.setListSql(node.getTextContent().trim());
// } else if (node.getNodeName().equals("triggers")) {
// tableInstance.setTriggerNames(parseTriggers(node));
// }
//
// }
// tableInstance.setSource(source);
// tablesHash.put(tableInstance.getTableId(), tableInstance);
// }
// return tablesHash;
// } catch (Exception e) {
// throw new JKXmlException(e);
// }
// }
//
// /**
// *
// * @param node
// * @return
// * @throws ClassNotFoundException
// * @throws IllegalAccessException
// * @throws InstantiationException
// */
// private HashSet<String> parseTriggers(Node node) throws
// InstantiationException, IllegalAccessException, ClassNotFoundException {
// HashSet<String> triggers = new HashSet<String>();
// for (int i = 0; i < node.getChildNodes().getLength(); i++) {
// Node n = node.getChildNodes().item(i);
// if (n.getNodeName().equals("trigger")) {
// String triggerClassName = n.getTextContent().trim();
// if (GeneralUtility.isEmptyString(triggerClassName)) {
// continue;
// }
// // Trigger
// // trigger=(Trigger)Class.forName(triggerClassName).newInstance();
// triggers.add(triggerClassName);
// }
// }
// return triggers;
// }
//
// /**
// *
// * @param tableInstance
// * @param node
// * @return
// */
// private ArrayList<String> parseFilters(TableMeta tableInstance, Node node) {
// ArrayList<String> filters = new ArrayList<String>();
// for (int i = 0; i < node.getChildNodes().getLength(); i++) {
// Node n = node.getChildNodes().item(i);
// if (n.getNodeName().equals("field")) {
// Element e = (Element) n;
// filters.add(e.getAttribute("name"));
// }
// }
// return filters;
// }
//
// /**
// *
// * @param element
// * @return
// * @throws IllegalAccessException
// * @throws InstantiationException
// * @throws ClassNotFoundException
// */
// private ArrayList<Constraint> parseConstraints(TableMeta tableMeta, Node
// constraintsNode) throws InstantiationException, IllegalAccessException,
// ClassNotFoundException {
// ArrayList<Constraint> constraints = new ArrayList<Constraint>();
// for (int i = 0; i < constraintsNode.getChildNodes().getLength(); i++) {
// Node node = constraintsNode.getChildNodes().item(i);
// if (node.getNodeName().equals("constraint")) {
// Constraint cons = parseConstraint(tableMeta, (Element) node);
// constraints.add(cons);
// }
// }
// return constraints;
// }
//
// /**
// *
// * @param node
// * @return
// * @throws InstantiationException
// * @throws IllegalAccessException
// * @throws ClassNotFoundException
// */
// private Constraint parseConstraint(TableMeta tableMeta, Element element)
// throws InstantiationException, IllegalAccessException,
// ClassNotFoundException {
// Constraint instance;
// if (!element.getAttribute("class").equals("")) {
// instance = (Constraint)
// Class.forName(element.getAttribute("class")).newInstance();
// } else {
// String constType = element.getAttribute("type");
//
// if (constType.equals("no-duplicate")) {
// instance = DuplicateDataConstraint.class.newInstance();
// } else if (constType.equals("range")) {
// DataRangeConstraint cons = DataRangeConstraint.class.newInstance();
// cons.setValueFrom(Float.parseFloat(element.getAttribute("value-from")));
// cons.setValueTo(Float.parseFloat(element.getAttribute("value-to")));
// instance = cons;
// } else if (constType.equals("less-than")) {
// LessThanContsraint cons = LessThanContsraint.class.newInstance();
// instance = cons;
// } else if (constType.equals("no-idenetical")) {
// IdenticalFieldsContraint cons = IdenticalFieldsContraint.class.newInstance();
// instance = cons;
// } else {
// instance = Constraint.class.newInstance();
// }
// }
// instance.setName(element.getAttribute("name"));
// for (int i = 0; i < element.getChildNodes().getLength(); i++) {
// Node n = element.getChildNodes().item(i);
// if (n instanceof Element) {
// Element fieldNode = (Element) n;
// if (fieldNode.getTagName().equals("field")) {
// String fieldName = fieldNode.getAttribute("name");
// FieldMeta field = tableMeta.getField(fieldName);
// instance.addField(field);
// }
// }
// }
// instance.setTableMeta(tableMeta);
// return instance;
// }
//
// /**
// *
// * @param element
// * @return
// * @throws IllegalAccessException
// * @throws InstantiationException
// */
// private IdFieldMeta parseIdField(Element element) throws
// InstantiationException, IllegalAccessException {
// IdFieldMeta instance = IdFieldMeta.class.newInstance();
// instance.setName(element.getAttribute("name"));
// instance.setCaption(element.getAttribute("caption"));
// if (!element.getAttribute("type").equals("")) {
// instance.setType(Integer.parseInt(element.getAttribute("type")));
// }
// if (!element.getAttribute("property").equals("")) {
// instance.setPropertyName(element.getAttribute("property"));
// }
// if (!element.getAttribute("auto-increment").equals("")) {
// (instance).setAutoIncrement(Boolean.parseBoolean(element.getAttribute("auto-increment")));
// }
// if (!element.getAttribute("width").equals("")) {
// instance.setWidth(Integer.parseInt(element.getAttribute("width")));
// }
// if (!element.getAttribute("visible").equals("")) {
// instance.setVisible(Boolean.parseBoolean(element.getAttribute("visible")));
// }
// if (!element.getAttribute("max-length").equals("")) {
// instance.setMaxLength(Integer.parseInt(element.getAttribute("max-length")));
// }
// return instance;
// }
//
// /**
// *
// * @param element
// * @param tableInstance
// * @return
// * @throws InstantiationException
// * @throws IllegalAccessException
// */
// private FieldMeta parseField(Element element, TableMeta tableInstance) throws
// Exception {
// FieldMeta instance;
// if (!element.getAttribute("reference_table").equals("")) {
// instance = ForiegnKeyFieldMeta.class.newInstance();
// ((ForiegnKeyFieldMeta)
// instance).setReferenceTable(element.getAttribute("reference_table"));
// ((ForiegnKeyFieldMeta)
// instance).setReferenceField(element.getAttribute("reference_field"));
// if (!element.getAttribute("relation").equals("")) {
// ((ForiegnKeyFieldMeta)
// instance).setRelation(Relation.valueOf(element.getAttribute("relation")));
// }
// if (!element.getAttribute("view-mode").equals("")) {
// ((ForiegnKeyFieldMeta)
// instance).setViewMode(ViewMode.valueOf(element.getAttribute("view-mode")));
// }
//
// } else {
// instance = FieldMeta.class.newInstance();
// }
//
// instance.setParentTable(tableInstance);
// instance.setName(element.getAttribute("name"));
// instance.setCaption(element.getAttribute("caption"));
// if (!element.getAttribute("type").equals("")) {
// instance.setType(Integer.parseInt(element.getAttribute("type")));
// }
// if (!element.getAttribute("property").equals("")) {
// instance.setPropertyName(element.getAttribute("property"));
// }
//
// if (!element.getAttribute("required").equals("")) {
// instance.setRequired(Boolean.parseBoolean(element.getAttribute("required")));
// }
// if (!element.getAttribute("width").equals("")) {
// instance.setWidth(Integer.parseInt(element.getAttribute("width")));
// }
// if (!element.getAttribute("visible").equals("")) {
// instance.setVisible(Boolean.parseBoolean(element.getAttribute("visible")));
// }
// if (!element.getAttribute("allow-update").equals("")) {
// instance.setAllowUpdate(Boolean.parseBoolean(element.getAttribute("allow-update")));
// }
// if (!element.getAttribute("max-length").equals("")) {
// instance.setMaxLength(Integer.parseInt(element.getAttribute("max-length")));
// }
// if (!element.getAttribute("confirm-input").equals("")) {
// instance.setConfirmInput(Boolean.parseBoolean(element.getAttribute("confirm-input")));
// }
// if (!element.getAttribute("enabled").equals("")) {
// String enabledValue = element.getAttribute("enabled");
// if (enabledValue != null && enabledValue.toUpperCase().startsWith("SELECT"))
// {
// try {
// enabledValue = DaoUtil.exeuteSingleOutputQuery(enabledValue).toString();
// } catch (Exception e) {
// }
// }
// instance.setEnabled(Boolean.parseBoolean(enabledValue));
// }
// if (!element.getAttribute("summary-field").equals("")) {
// instance.setSummaryField(Boolean.parseBoolean(element.getAttribute("summary-field")));
// }
// if (!element.getAttribute("default-value").equals("")) {
// instance.setDefaultValue(element.getAttribute("default-value"));
// }
// if (!element.getAttribute("options-query").equals("")) {
// instance.setOptionsQuery(element.getAttribute("options-query"));
// }
// if (!element.getAttribute("filtered-by").equals("")) {
// instance.setFilteredBy(element.getAttribute("filtered-by"));
// }
// if (!element.getAttribute("keep-last").equals("")) {
// instance.setKeepLastValue(Boolean.parseBoolean(element.getAttribute("keep-last")));
// }
// setFieldTriggers(element, instance);
// return instance;
// }
//
// private ArrayList<FieldTrigger> setFieldTriggers(Element element, FieldMeta
// instance) throws Exception {
// ArrayList<FieldTrigger> triggers = new ArrayList<FieldTrigger>();
// NodeList nodes = element.getChildNodes();
// for (int j = 0; j < nodes.getLength(); j++) {
// Node node = nodes.item(j);
// if (node.getNodeName().equals("triggers")) {
// for (int i = 0; i < node.getChildNodes().getLength(); i++) {
// Node n = node.getChildNodes().item(i);
// if (n.getNodeName().equals("trigger")) {
// String triggerClassName = node.getTextContent().trim();
// instance.addTrigger(triggerClassName);
// }
// }
// }
// }
// return triggers;
// }
//
// /**
// *
// * @param args
// * @throws JKXmlException
// * @throws SAXException
// * @throws IOException
// * @throws ParserConfigurationException
// * @throws InstantiationException
// * @throws IllegalAccessException
// */
// public static void main(String[] args) throws JKXmlException {
// // TablesCofigParser p = new TablesCofigParser();;
// }
//
// }
