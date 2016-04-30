/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.commons.dao.dynamic.meta.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fs.commons.dao.dynamic.constraints.Constraint;
import com.fs.commons.dao.dynamic.constraints.DataRangeConstraint;
import com.fs.commons.dao.dynamic.constraints.DuplicateDataConstraint;
import com.fs.commons.dao.dynamic.constraints.IdenticalFieldsContraint;
import com.fs.commons.dao.dynamic.constraints.LessThanContsraint;
import com.fs.commons.dao.dynamic.meta.FieldGroup;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.Relation;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.ViewMode;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.trigger.FieldTrigger;
import com.fs.commons.util.GeneralUtility;

public class TableMetaXmlParser {

	// private final String fileName;

	/**
	 *
	 * @param args
	 * @throws JKXmlException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void main(final String[] args) throws JKXmlException {
		// TablesCofigParser p = new TablesCofigParser();;
	}

	/**
	 * @param abstractModule
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 *
	 *
	 */
	public TableMetaXmlParser() {
	}

	/**
	 *
	 * @param fileName
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	// public TablesCofigParser(String fileName) throws JKXmlException {
	// this.fileName = fileName;
	// }
	/**
	 *
	 * @param source
	 * @param fileName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Hashtable<String, TableMeta> parse(final InputStream in, final String source) throws JKXmlException {
		try {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document doc = builder.parse(in);
			final NodeList tablesNode = doc.getElementsByTagName("table");

			final Hashtable<String, TableMeta> tablesHash = new Hashtable<String, TableMeta>();
			for (int i = 0; i < tablesNode.getLength(); i++) {
				final Element table = (Element) tablesNode.item(i);
				final TableMeta tableInstance = TableMeta.class.newInstance();
				tableInstance.setTableName(table.getAttribute("name"));
				tableInstance.setSource(source);
				if (!table.getAttribute("id").equals("")) {
					tableInstance.setTableId(table.getAttribute("id"));
				}
				if (!table.getAttribute("max-records_count").equals("")) {
					tableInstance.setMaxRecordsCount(Integer.parseInt(table.getAttribute("max-records_count")));
				}
				if (!table.getAttribute("icon-image").equals("")) {
					tableInstance.setIconName(table.getAttribute("icon-image"));
				}
				if (!table.getAttribute("caption").equals("")) {
					tableInstance.setCaption(table.getAttribute("caption"));
				}
				if (!table.getAttribute("allow-manage").equals("")) {
					tableInstance.setAllowManage(Boolean.parseBoolean(table.getAttribute("allow-manage")));
				}
				if (!table.getAttribute("page-row-count").equals("")) {
					tableInstance.setPageRowCount(Integer.parseInt(table.getAttribute("page-row-count")));
				}
				if (!table.getAttribute("filter-indices").equals("")) {
					tableInstance.setFilters(table.getAttribute("filter-indices").split(","));
				}
				// TODO :ui-colunm-count is depracted , should be replaced with
				// ui-row-count
				if (!table.getAttribute("ui-colunm-count").equals("")) {
					tableInstance.setDefaultUIRowCount(Integer.parseInt(table.getAttribute("ui-colunm-count")));
				}
				if (!table.getAttribute("ui-row-count").equals("")) {
					tableInstance.setDefaultUIRowCount(Integer.parseInt(table.getAttribute("ui-row-count")));
				}
				if (!table.getAttribute("panel-class").equals("")) {
					tableInstance.setPanelClassName(table.getAttribute("panel-class"));
				}
				if (!table.getAttribute("manage-panel-class").equals("")) {
					tableInstance.setManagePanelClassName(table.getAttribute("manage-panel-class"));
				}
				if (!table.getAttribute("allow-delete").equals("")) {
					tableInstance.setAllowDelete(Boolean.parseBoolean(table.getAttribute("allow-delete")));
				}
				if (!table.getAttribute("allow-update").equals("")) {
					tableInstance.setAllowUpdate(Boolean.parseBoolean(table.getAttribute("allow-update")));
				}
				if (!table.getAttribute("allow-add").equals("")) {
					tableInstance.setAllowAdd(Boolean.parseBoolean(table.getAttribute("allow-add")));
				}
				if (!table.getAttribute("class").equals("")) {
					tableInstance.setBeanName(table.getAttribute("class"));
				}
				// if (!table.getAttribute("privilige-id").equals("")) {
				// tableInstance.setPriviligeId(Integer.parseInt(table.getAttribute("privilige-id")));
				// }
				// parse Default Group
				// fields
				parseGroup(table, tableInstance);
				for (int j = 0; j < table.getChildNodes().getLength(); j++) {
					final Node node = table.getChildNodes().item(j);
					if (node.getNodeName().equals("group")) {
						parseGroup((Element) node, tableInstance);
						// } else if (node.getNodeName().equals("id-field")) {
						// IdFieldMeta id = parseIdField((Element) node,
						// tableInstance);
						// // id.setParentTable(tableInstance);
						// tableInstance.setIdField(id);
						// defaultGroup.addField(id);
						// } else if (node.getNodeName().equals("field")) {
						// FieldMeta field = parseField((Element) node,
						// tableInstance);
						// tableInstance.addField(field);
						// defaultGroup.addField(field);
					} else if (node.getNodeName().equals("constraints")) {
						final ArrayList<Constraint> constraints = parseConstraints(tableInstance, node);
						tableInstance.setConstraints(constraints);
						// } else if
						// (node.getNodeName().equals("filter-fields")) {
						// ArrayList<String> filters =
						// parseFilters(tableInstance, node);
						// tableInstance.setFilters(filters);
					} else if (node.getNodeName().equals("report-sql")) {
						tableInstance.setReportSql(node.getTextContent().trim());
					} else if (node.getNodeName().equals("short-sql")) {
						tableInstance.setShortReportSql(node.getTextContent().trim());
					} else if (node.getNodeName().equals("list-sql")) {
						tableInstance.setListSql(node.getTextContent().trim());
					} else if (node.getNodeName().equals("triggers")) {
						tableInstance.setTriggerNames(parseTriggers(node));
					}

				}
				tablesHash.put(tableInstance.getTableId(), tableInstance);
			}
			return tablesHash;
		} catch (final Exception e) {
			throw new JKXmlException(e);
		}
	}

	/**
	 *
	 * @param node
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private Constraint parseConstraint(final TableMeta tableMeta, final Element element)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Constraint instance;
		if (!element.getAttribute("class").equals("")) {
			instance = (Constraint) Class.forName(element.getAttribute("class")).newInstance();
		} else {
			final String constType = element.getAttribute("type");

			if (constType.equals("no-duplicate")) {
				instance = DuplicateDataConstraint.class.newInstance();
			} else if (constType.equals("range")) {
				final DataRangeConstraint cons = DataRangeConstraint.class.newInstance();
				cons.setValueFrom(Float.parseFloat(element.getAttribute("value-from")));
				cons.setValueTo(Float.parseFloat(element.getAttribute("value-to")));
				instance = cons;
			} else if (constType.equals("less-than")) {
				final LessThanContsraint cons = LessThanContsraint.class.newInstance();
				instance = cons;
			} else if (constType.equals("no-idenetical")) {
				final IdenticalFieldsContraint cons = IdenticalFieldsContraint.class.newInstance();
				instance = cons;
			} else {
				instance = Constraint.class.newInstance();
			}
		}
		instance.setName(element.getAttribute("name"));
		for (int i = 0; i < element.getChildNodes().getLength(); i++) {
			final Node n = element.getChildNodes().item(i);
			if (n instanceof Element) {
				final Element fieldNode = (Element) n;
				if (fieldNode.getTagName().equals("field")) {
					final String fieldName = fieldNode.getAttribute("name");
					final FieldMeta field = tableMeta.getField(fieldName);
					instance.addField(field);
				}
			}
		}
		instance.setTableMeta(tableMeta);
		return instance;
	}

	// /**
	// *
	// * @param tableInstance
	// * @param node
	// * @return
	// */
	// private ArrayList<String> parseFilters(TableMeta tableInstance, Node
	// node) {
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

	/**
	 *
	 * @param element
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private ArrayList<Constraint> parseConstraints(final TableMeta tableMeta, final Node constraintsNode)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final ArrayList<Constraint> constraints = new ArrayList<Constraint>();
		for (int i = 0; i < constraintsNode.getChildNodes().getLength(); i++) {
			final Node node = constraintsNode.getChildNodes().item(i);
			if (node.getNodeName().equals("constraint")) {
				final Constraint cons = parseConstraint(tableMeta, (Element) node);
				constraints.add(cons);
			}
		}
		return constraints;
	}

	/**
	 *
	 * @param element
	 * @param tableInstance
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private FieldMeta parseField(final Element element, final TableMeta tableInstance) throws Exception {
		FieldMeta instance;
		if (!element.getAttribute("reference_table").equals("")) {
			instance = ForiegnKeyFieldMeta.class.newInstance();
			((ForiegnKeyFieldMeta) instance).setReferenceTable(element.getAttribute("reference_table"));
			((ForiegnKeyFieldMeta) instance).setReferenceField(element.getAttribute("reference_field"));
			if (!element.getAttribute("relation").equals("")) {
				((ForiegnKeyFieldMeta) instance).setRelation(Relation.valueOf(element.getAttribute("relation")));
			}
			if (!element.getAttribute("view-mode").equals("")) {
				((ForiegnKeyFieldMeta) instance).setViewMode(ViewMode.valueOf(element.getAttribute("view-mode")));
			}

		} else {
			instance = FieldMeta.class.newInstance();
		}

		parseFieldProperties(element, tableInstance, instance);
		return instance;
	}

	// ///////////////////////////////////////////////////////////////////
	private void parseFieldProperties(final Element element, final TableMeta tableInstance, final FieldMeta instance) throws Exception {
		instance.setParentTable(tableInstance);
		instance.setName(element.getAttribute("name"));
		instance.setCaption(element.getAttribute("caption"));
		if (!element.getAttribute("type").equals("")) {
			instance.setType(Integer.parseInt(element.getAttribute("type")));
		}
		if (!element.getAttribute("property").equals("")) {
			instance.setPropertyName(element.getAttribute("property"));
		}

		if (!element.getAttribute("required").equals("")) {
			instance.setRequired(Boolean.parseBoolean(element.getAttribute("required")));
		}
		if (!element.getAttribute("width").equals("")) {
			instance.setWidth(Integer.parseInt(element.getAttribute("width")));
		}
		if (!element.getAttribute("visible").equals("")) {
			instance.setVisible(Boolean.parseBoolean(element.getAttribute("visible")));
		}
		if (!element.getAttribute("allow-update").equals("")) {
			instance.setAllowUpdate(Boolean.parseBoolean(element.getAttribute("allow-update")));
		}
		if (!element.getAttribute("max-length").equals("")) {
			instance.setMaxLength(Integer.parseInt(element.getAttribute("max-length")));
		}
		if (!element.getAttribute("confirm-input").equals("")) {
			instance.setConfirmInput(Boolean.parseBoolean(element.getAttribute("confirm-input")));
		}
		if (!element.getAttribute("enabled").equals("")) {
			instance.setEnabled(Boolean.parseBoolean(element.getAttribute("enabled")));
		}
		if (!element.getAttribute("summary-field").equals("")) {
			instance.setSummaryField(Boolean.parseBoolean(element.getAttribute("summary-field")));
		}
		if (!element.getAttribute("default-value").equals("")) {
			instance.setDefaultValue(element.getAttribute("default-value"));
		}
		if (!element.getAttribute("options-query").equals("")) {
			instance.setOptionsQuery(element.getAttribute("options-query"));
		}
		if (!element.getAttribute("filtered-by").equals("")) {
			instance.setFilteredBy(element.getAttribute("filtered-by"));
		}
		if (!element.getAttribute("lookup-number").equals("")) {
			instance.setLookupNumber(Boolean.parseBoolean(element.getAttribute("lookup-number")));
		}
		if (!element.getAttribute("keep-last").equals("")) {
			instance.setKeepLastValue(Boolean.parseBoolean(element.getAttribute("keep-last")));
		}
		if (!element.getAttribute("visible-width").equals("")) {
			instance.setVisibleWidth(Integer.parseInt(element.getAttribute("visible-width")));
		}
		if (!element.getAttribute("visible-height").equals("")) {
			instance.setVisibleWidth(Integer.parseInt(element.getAttribute("visible-height")));
		}
		setFieldTriggers(element, instance);
	}

	/**
	 *
	 * @param tableInstance
	 * @param node
	 * @return
	 * @throws Exception
	 */
	private void parseGroup(final Element groupNode, final TableMeta tableInstance) throws Exception {
		final FieldGroup group = new FieldGroup();
		if (!groupNode.getAttribute("name").equals("")) {
			group.setName(groupNode.getAttribute("name"));
		}
		if (!groupNode.getAttribute("ui-row-count").equals("")) {
			group.setRowCount(Integer.parseInt(groupNode.getAttribute("ui-row-count")));
		}
		for (int j = 0; j < groupNode.getChildNodes().getLength(); j++) {
			final Node node = groupNode.getChildNodes().item(j);
			if (node.getNodeName().equals("id-field")) {
				final IdFieldMeta id = parseIdField((Element) node, tableInstance);
				// id.setParentTable(tableInstance);
				// tableInstance.setIdField(id);
				group.addField(id);
			} else if (node.getNodeName().equals("field")) {
				final FieldMeta field = parseField((Element) node, tableInstance);
				// tableInstance.addField(field);
				group.addField(field);
			}
		}
		tableInstance.addGroup(group);
	}

	/**
	 *
	 * @param element
	 * @param tableInstance
	 * @return
	 * @throws Exception
	 */
	private IdFieldMeta parseIdField(final Element element, final TableMeta tableInstance) throws Exception {
		final IdFieldMeta instance = IdFieldMeta.class.newInstance();
		// instance.setName(element.getAttribute("name"));
		// instance.setCaption(element.getAttribute("caption"));
		// if (!element.getAttribute("type").equals("")) {
		// instance.setType(Integer.parseInt(element.getAttribute("type")));
		// }
		// if (!element.getAttribute("property").equals("")) {
		// instance.setPropertyName(element.getAttribute("property"));
		// }
		if (!element.getAttribute("auto-increment").equals("")) {
			instance.setAutoIncrement(Boolean.parseBoolean(element.getAttribute("auto-increment")));
		}
		// if (!element.getAttribute("width").equals("")) {
		// instance.setWidth(Integer.parseInt(element.getAttribute("width")));
		// }
		// if (!element.getAttribute("visible").equals("")) {
		// instance.setVisible(Boolean.parseBoolean(element.getAttribute("visible")));
		// }
		// if (!element.getAttribute("max-length").equals("")) {
		// instance.setMaxLength(Integer.parseInt(element.getAttribute("max-length")));
		// }
		parseFieldProperties(element, tableInstance, instance);
		return instance;
	}

	/**
	 *
	 * @param node
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private HashSet<String> parseTriggers(final Node node) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		final HashSet<String> triggers = new HashSet<String>();
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			final Node n = node.getChildNodes().item(i);
			if (n.getNodeName().equals("trigger")) {
				final String triggerClassName = n.getTextContent().trim();
				if (GeneralUtility.isEmpty(triggerClassName)) {
					continue;
				}
				// Trigger
				// trigger=(Trigger)Class.forName(triggerClassName).newInstance();
				triggers.add(triggerClassName);
			}
		}
		return triggers;
	}

	private ArrayList<FieldTrigger> setFieldTriggers(final Element element, final FieldMeta instance) throws Exception {
		final ArrayList<FieldTrigger> triggers = new ArrayList<FieldTrigger>();
		final NodeList nodes = element.getChildNodes();
		for (int j = 0; j < nodes.getLength(); j++) {
			final Node node = nodes.item(j);
			if (node.getNodeName().equals("triggers")) {
				for (int i = 0; i < node.getChildNodes().getLength(); i++) {
					final Node n = node.getChildNodes().item(i);
					if (n.getNodeName().equals("trigger")) {
						final String triggerClassName = node.getTextContent().trim();
						instance.addTrigger(triggerClassName);
					}
				}
			}
		}
		return triggers;
	}

}
