package com.fs.commons.dao.dynamic.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.MetaSqlBuilder;
import com.fs.commons.dao.dynamic.constraints.Constraint;
import com.fs.commons.dao.dynamic.constraints.TableDataValidator;
import com.fs.commons.dao.dynamic.constraints.TableDataValidatorFactory;
import com.fs.commons.dao.dynamic.trigger.Trigger;
import com.fs.commons.dao.sql.query.QueryComponent;
import com.fs.commons.security.Privilige;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class TableMeta implements Serializable, QueryComponent {
	public static final int PAGE_ROW_COUNT = 25;

	public static final boolean ALLOW_MANAGE = false;

	public static final int UI_COLUMN_COUNT = 12;

	private static final String ADD_RECORD_PRIVILIGE = "ADD";

	private static final String UPDATE_RECORD_PRIVILIGE = "UPDATE";

	private static final String DELETE_RECORD_PRIVILIGE = "DELETE";

	private static final String DELETE_ALL_RECORD_PRIVILIGE = "DELETE_ALL";

	String beanName;
	String tableName;

	IdFieldMeta idField;

	Hashtable<String, FieldMeta> fields = new Hashtable<String, FieldMeta>();

	Vector<FieldMeta> fieldList = new Vector<FieldMeta>();

	// used to maintain the order

	String reportSql;// Used in master management table

	String shortReportSql;// used in Cross tables

	String listSql;// for combo box and lists

	TableDataValidator tableDataValidator;

	int maxRecordsCount;

	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();

	private int[] filters = new int[0];

	// array of indices on sql statement that should be filtred by default

	ArrayList<ForiegnKeyFieldMeta> detailFields = new ArrayList<ForiegnKeyFieldMeta>();

	private boolean crossTable;

	boolean allowManage = ALLOW_MANAGE;

	String iconName;

	String caption;

	int pageRowCount = PAGE_ROW_COUNT;

	private int defaultUIRowCount = UI_COLUMN_COUNT;

	/**
	 * used for dynamic panel injection especially for detail panel
	 */
	private String panelClassName;

	/**
	 * Used for DaoComboBoxWithManage component
	 */
	private String managePanelClassName;

	private boolean allowDelete = true;

	private boolean allowAdd = true;

	private boolean allowUpdate = true;

	private String tableId;

	HashSet<String> triggerNames = new HashSet<String>();

	ArrayList<Trigger> triggers = null;// new ArrayList<Trigger>();

	private ArrayList<FieldMeta> visibleFields;

	private transient DataSource dataSource;

	private String source;

	// private int priviligeId;

	Vector<FieldGroup> groups = new Vector();

	private Privilige parentPrivilige;

	// number of visible rows at a time

	// used to indicate weather to allow management of this table from detail
	// tables
	// hint : add edit button to combo box

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		if (caption != null && caption.trim().equals("\n")) {
			caption = null;
		}
		this.caption = caption;
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<ForiegnKeyFieldMeta> getDetailFields() {
		return detailFields;
	}

	/**
	 * 
	 * @param detailFields
	 */
	public void setDetailFields(ArrayList<ForiegnKeyFieldMeta> detailFields) {
		this.detailFields = detailFields;
	}

	/**
	 * 
	 * @return
	 */
	public Hashtable<String, FieldMeta> getFields() {
		return fields;
	}

	/**
	 * 
	 * @param fields
	 */
	public void setFields(Hashtable<String, FieldMeta> fields) {
		this.fields = fields;
	}

	/*
	 * 
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * 
	 * @param tableName
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * 
	 * @param field
	 */
	public void addField(FieldMeta field) {
		// dont allow dupliacte fields
		if (fields.get(field.getName()) != null) {
			return;
		}
		field.setParentTable(this);
		fields.put(field.getName(), field);
		fieldList.add(field);
	}

	public Vector<FieldMeta> getFieldList() {
		return fieldList;
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public FieldMeta getField(String fieldName) {
		// return fields.get(fieldName);
		return getField(fieldName, false);
	}

	/**
	 * try return the field weather its id field or normal field
	 * 
	 * @param fieldName
	 * @param includingId
	 * @return
	 */
	public FieldMeta getField(String fieldName, boolean includingId) {
		if (includingId) {
			if (idField != null && getIdField().getName().equalsIgnoreCase(fieldName)) {
				return getIdField();
			}
		}
		return fields.get(fieldName);
	}

	/**
	 * 
	 * @return
	 */
	public IdFieldMeta getIdField() {
		if (idField == null) {
			throw new IllegalStateException("Primary field is required on table : " + getTableName());
		}
		return idField;
	}

	/**
	 * 
	 * @param idField
	 */
	public void setIdField(IdFieldMeta idField) {
		this.idField = idField;
		if (idField != null) {
			idField.setParentTable(this);
		}
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Table name : " + tableName);
		buf.append(" , " + idField);
		buf.append(" ,\n\t " + fields);
		return buf.toString();
	}

	public Record createEmptyRecord() {
		return createEmptyRecord(false);
	}

	/**
	 * 
	 * @return
	 */
	public Record createEmptyRecord(boolean setDefaultValues) {
		Record record = new Record(this);
		record.setIdField(new Field(idField));
		for (int i = 0; i < getFieldList().size(); i++) {
			Field field = new Field(getFieldList().get(i));
			if (setDefaultValues) {
				field.setValue(field.getMeta().getDefaultValue());
			}
			record.addField(field);
		}
		record.setNewRecord(true);
		return record;
	}

	/**
	 * 
	 * @return
	 */
	public String getReportSql() {
		// String
		// sql=GeneralUtility.loadSqlFromDatabase("Report_"+getTableId());
		// if(sql!=null){
		// return sql;
		// }
		if (reportSql != null && reportSql.startsWith("@")) {
			String sql = GeneralUtility.getSqlFile(reportSql.substring(1));
			if (sql != null) {
				return sql;
			} else {
				return getDefaultSql();
			}
		}
		if (reportSql == null || reportSql.equals("")) {
			return getDefaultSql();
		}
		return reportSql;
	}

	/**
	 * 
	 * @return
	 */
	private String getDefaultSql() {
		// return new MetaSqlBuilder(this).buildDefaultReportSql();
		return "SELECT * FROM " + getTableName();
	}

	/**
	 * 
	 * @return
	 */
	public int getVisibleFieldsCount() {
		return getVisibleFields().size();
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<FieldMeta> getVisibleFields() {
		if (visibleFields == null) {
			visibleFields = new ArrayList<FieldMeta>();
			if (getIdField().isVisible()) {
				visibleFields.add(getIdField());
			}
			for (FieldMeta field : fieldList) {
				if (field.isVisible()) {
					visibleFields.add(field);
				}
			}
		}
		return visibleFields;

	}

	/**
	 * 
	 * @param reportSql
	 */
	public void setReportSql(String reportSql) {
		this.reportSql = reportSql;
	}

	/**
	 * 
	 * @return
	 */
	public TableDataValidator getTableDataValidator() {
		if (tableDataValidator == null) {
			tableDataValidator = TableDataValidatorFactory.createValidator();
		}
		return tableDataValidator;
	}

	/**
	 * 
	 * @param tableDataValidator
	 */
	public void setTableDataValidator(TableDataValidator tableDataValidator) {
		this.tableDataValidator = tableDataValidator;
	}

	/**
	 * 
	 * @param record
	 * @throws ValidationException
	 */
	public void validateData(Record record) throws ValidationException {
		getTableDataValidator().validate(this, record);
	}

	/**
	 * 
	 * @param constraints
	 */
	public void setConstraints(ArrayList<Constraint> constraints) {
		this.constraints = constraints;

	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Constraint> getConstraints() {
		return constraints;
	}

	/**
	 * 
	 * @param strings
	 */
	public void setFilters(String[] strings) {
		filters = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			filters[i] = Integer.parseInt(strings[i]);
		}

	}

	public int[] getFilters() {
		return filters;
	}

	public int getMaxRecordsCount() {
		return maxRecordsCount;
	}

	public void setMaxRecordsCount(int maxRecordsCount) {
		this.maxRecordsCount = maxRecordsCount;
	}

	public TableMeta copy() throws Exception {
		return (TableMeta) GeneralUtility.copy(this);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<ForiegnKeyFieldMeta> lstForiegnKeyFields() {
		ArrayList<ForiegnKeyFieldMeta> list = new ArrayList<ForiegnKeyFieldMeta>();
		for (int i = 0; i < fieldList.size(); i++) {
			if (fieldList.get(i) instanceof ForiegnKeyFieldMeta) {
				list.add((ForiegnKeyFieldMeta) fieldList.get(i));
			}
		}
		return list;
	}

	/**
	 * 
	 * 
	 */
	public void setCorssTable(boolean crossTable) {
		this.crossTable = crossTable;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isCrossTable() {
		return crossTable;
	}

	/**
	 * 
	 * @param crossTable
	 */
	public void setCrossTable(boolean crossTable) {
		this.crossTable = crossTable;
	}

	public boolean isListSqlNull() {
		return listSql == null || listSql.equals("") || listSql.toUpperCase().equals(getDefaultSql().toUpperCase());
	}

	public boolean isReportSqlNull() {
		return reportSql == null || reportSql.equals("") || reportSql.toUpperCase().equals(getDefaultSql().toUpperCase());
	}

	public boolean isShortSqlNull() {
		return shortReportSql == null || shortReportSql.equals("") || shortReportSql.toUpperCase().equals(getDefaultSql().toUpperCase());
	}

	public String getListSql() {
		// String sql=GeneralUtility.loadSqlFromDatabase("List_"+getTableId());
		// if(sql!=null){
		// return sql;
		// }

		if (listSql == null) {
			return getShortReportSql();
		}
		return listSql;
	}

	public void setListSql(String listSql) {
		this.listSql = listSql;
	}

	public String getShortReportSql() {
		String sql = GeneralUtility.loadSqlFromDatabase("Short_" + getTableId());
		if (sql != null) {
			return sql;
		}
		if (shortReportSql == null) {
			return new MetaSqlBuilder(this).buildDefaultShortSql();
		}
		return shortReportSql;
	}

	public void setShortReportSql(String shortReportSql) {
		this.shortReportSql = shortReportSql;
	}

	public void setFieldList(Vector<FieldMeta> fieldList) {
		this.fieldList = fieldList;
		for (int i = 0; i < fieldList.size(); i++) {
			FieldMeta meta = fieldList.get(i);
			meta.setParentTable(this);
			fields.put(meta.getName(), meta);
		}
	}

	public void addFields(ArrayList<ForiegnKeyFieldMeta> foriegnFields) {
		for (int i = 0; i < foriegnFields.size(); i++) {
			addField(foriegnFields.get(i));
		}
	}

	public boolean isAllowManage() {
		return allowManage;
	}

	public void setAllowManage(boolean allowManage) {
		this.allowManage = allowManage;
	}

	public boolean isCaptionNull() {
		return caption == null || caption.trim().equals("");
	}

	/**
	 * @return
	 */
	public String getCaption() {
		return caption == null || caption.equals("") ? getTableId() : caption;
	}

	/**
	 * @return the pageRowCount
	 */
	public int getPageRowCount() {
		return this.pageRowCount;
	}

	/**
	 * @param pageRowCount
	 *            the pageRowCount to set
	 */
	public void setPageRowCount(int pageRowCount) {
		this.pageRowCount = pageRowCount;
	}

	/**
	 * @param field
	 */
	public void addDetailField(ForiegnKeyFieldMeta field) {
		if (detailFields.indexOf(field) == -1) {
			detailFields.add(field);
		}

	}

	/**
	 * @return
	 */
	public String getFiltersAsString() {
		String str = "";
		for (int i = 0; i < filters.length; i++) {
			if (i != 0) {
				str += ",";
			}
			str += filters[i];
		}
		return str;
	}

	public int getDefaultUIRowCount() {
		return this.defaultUIRowCount;
	}

	public void setDefaultUIRowCount(int defaultUIRowCount) {
		this.defaultUIRowCount = defaultUIRowCount;
	}

	/**
	 * 
	 * @param panelClassName
	 */
	public void setPanelClassName(String panelClassName) {
		this.panelClassName = panelClassName;
	}

	/**
	 * 
	 * @return
	 */
	public String getPanelClassName() {
		return panelClassName;
	}

	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public void setAllowAdd(boolean allowAdd) {
		this.allowAdd = allowAdd;
	}

	/**
	 * @return the allowDelete
	 */
	public boolean isAllowDelete() {
		try {
			// SecurityManager.checkAllowedPrivilige(new
			// Privilige((getTableName() + DELETE_RECORD_PRIVILIGE).hashCode(),
			// "DELETE", getPrivilige()));
			return allowDelete;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @return the allowAdd
	 */
	public boolean isAllowAdd() {
		try {
			// SecurityManager.checkAllowedPrivilige(new
			// Privilige((getTableName() + ADD_RECORD_PRIVILIGE).hashCode(),
			// "ADD", getPrivilige()));
			return allowAdd;
		} catch (Exception e) {
			return false;
		}
	}

	public Privilige getPrivilige() {
		return new Privilige(getTableName().hashCode(), getTableName(), getParentPrivilige());
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public boolean isTableIdNull() {
		return tableId == null || tableId.trim().equals("") || tableId.equals(getTableName());
	}

	/**
	 * @return the tableId
	 */
	public String getTableId() {
		return tableId == null ? getTableName() : tableId;
	}

	/**
	 * 
	 * @param triggerName
	 */
	public void addTriggerName(String triggerName) {
		triggerNames.add(triggerName);
	}

	/**
	 * 
	 * @return
	 */
	public HashSet<String> getTriggerNames() {
		return triggerNames;
	}

	/**
	 * 
	 * @param triggerNames
	 */
	public void setTriggerNames(HashSet<String> triggerNames) {
		this.triggerNames = triggerNames;
	}

	// /**
	// *
	// * @param trigger
	// */
	// public void addTrigger(Trigger trigger) {
	// triggers.add(trigger);
	// }

	/**
	 * @return the triggers
	 */
	public ArrayList<Trigger> getTriggers() {
		if (triggers == null) {
			triggers = new ArrayList<Trigger>();
			if (triggerNames.size() > 0) {
				for (String triggerName : triggerNames) {
					try {
						Trigger trigger = (Trigger) Class.forName(triggerName).newInstance();
						triggers.add(trigger);
					} catch (Exception e) {
						ExceptionUtil.handleException(e);
					}
				}
			}
		}
		return triggers;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isAllowUpdate() {
		try {
			// SecurityManager.checkAllowedPrivilige(getUpdatePriviligeId());
			return allowUpdate;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isAllowDeleteAll() {
		try {
			// SecurityManager.checkAllowedPrivilige(getDeleteAllPriviligeId());
			return isAllowDelete();
		} catch (Exception e) {
			return false;
		}

	}

	public void setAllowUpdate(boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}

	public boolean isSingleRecord() {
		return getMaxRecordsCount() == 1;
	}

	@Override
	public boolean equals(Object obj) {
		TableMeta that = (TableMeta) obj;
		return that.getTableName().equals(this.getTableName());
	}

	// /**
	// *
	// * @param tableMetaName
	// * @param detailField
	// */
	// public void addDetailField(String tableMetaName, String detailField) {
	// addDetailField(tableMetaName, detailField, Relation.ONE_TO_ONE);
	// }

	// /**
	// *
	// * @param tableMetaName
	// * @param detailField
	// */
	// public void addDetailField(String tableMetaName, String detailField,
	// Relation relation) {
	// TableMeta tableMeta =
	// AbstractTableMetaFactory.getTableMeta(tableMetaName);
	// ForiegnKeyFieldMeta field = (ForiegnKeyFieldMeta)
	// tableMeta.getField(detailField);
	// field.setRelation(relation);
	// if (relation == Relation.MANY_TO_MANY) {
	// tableMeta.setCorssTable(true);
	// }
	// addDetailField(field);
	// }

	// public void clearDetailsFields() {
	// detailFields.clear();
	// }

	public Record createEmptyRecord(boolean setDefaultValues, Record defaults) {
		Record record = createEmptyRecord(setDefaultValues);
		record.setValues(true, defaults);
		return record;
	}

	/**
	 * @return the managePanelClassName
	 */
	public String getManagePanelClassName() {
		return managePanelClassName;
	}

	/**
	 * @param managePanelClassName
	 *            the managePanelClassName to set
	 */
	public void setManagePanelClassName(String managePanelClassName) {
		this.managePanelClassName = managePanelClassName;
	}

	// ///////////////////////////////////////////////////////////////////////
	public DataSource getDataSource() {
		if (dataSource == null) {
			return DataSourceFactory.getDefaultDataSource();
		}
		return dataSource;
	}

	// ///////////////////////////////////////////////////////////////////////
	public void setDataSource(DataSource connectionManager) {
		this.dataSource = connectionManager;
	}

	/**
	 * @param triggers
	 *            the triggers to set
	 */
	// public void setTriggers(ArrayList<Trigger> triggers) {
	// this.triggers = triggers;
	// triggerNames=new ArrayList<String>();
	// for (int i = 0; i < triggers.size(); i++) {
	// String triggerName = triggers.get(i).getClass().getName();
	// triggerNames.add(triggerName);
	// }
	// }

	public Vector<FieldMeta> lstSummaryFields() {
		Vector<FieldMeta> summaryFields = new Vector<FieldMeta>();
		for (int i = 0; i < fieldList.size(); i++) {
			FieldMeta field = fieldList.get(i);

			if (field.isSummaryField()) {
				summaryFields.add(field);
			}
		}
		if (summaryFields.size() == 0) {
			summaryFields.add(fieldList.get(0));
		}
		return summaryFields;
	}

	public int getFieldsCount() {
		return getFieldList().size();
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}

	@Override
	public Object toQueryElement() {
		return getTableName();
	}

	public boolean isAutoIncrementId() {
		return getIdField() != null && getIdField().isAutoIncrement();
	}

	@Override
	public boolean isInline() {
		return true;
	}

	public FieldMeta getLookupNumberField() {
		if (getIdField().isLookupNumber()) {
			return getIdField();
		}
		for (FieldMeta field : this.fieldList) {
			if (field.isLookupNumber()) {
				return field;
			}
		}
		// if no lookip field is set , then we choose the second field to be
		// this field
		// TODO : make the below smarter
		return fieldList.get(0);
	}

	// //////////////////////////////////////////////////////
	public void setSource(String source) {
		this.source = source;
	}

	// //////////////////////////////////////////////////////
	public String getSource() {
		return source;
	}

	// //////////////////////////////////////////////////////
	public Vector<FieldGroup> getGroups() {
		if (groups.size() == 0) {
			addGroup(createDefaultGroup());
		}
		return groups;
	}

	// //////////////////////////////////////////////////////
	private FieldGroup createDefaultGroup() {
		FieldGroup group = new FieldGroup();
		group.addField(idField);
		Vector<FieldMeta> fieldList = getFieldList();
		for (FieldMeta fieldMeta : fieldList) {
			group.addField(fieldMeta);
		}
		return group;
	}

	// /////////////////////////////////////////////////////
	public void addGroup(FieldGroup group) {
		groups.add(group);
		Vector<FieldMeta> fields = group.getFields();
		for (FieldMeta fieldMeta : fields) {
			if (fieldMeta instanceof IdFieldMeta) {
				setIdField((IdFieldMeta) fieldMeta);
			} else {
				addField(fieldMeta);
			}
		}
	}

	public Privilige getParentPrivilige() {
		return parentPrivilige;
	}

	public void setParentPrivilige(Privilige parentPrivilige) {
		this.parentPrivilige = parentPrivilige;
	}

	public Vector<FieldMeta> getAllFields() {
		Vector<FieldMeta> fields = new Vector();
		fields.add(getIdField());
		fields.addAll(getFieldList());
		return fields;
	}

	public void addDetailTables(String... detailTableNames) {
		for (String tableMeta : detailTableNames) {
			addDetailTable(AbstractTableMetaFactory.getTableMeta(tableMeta));
		}
	}

	public void addDetailTable(TableMeta tableMeta) {
		// adduming the foriegn key in the detail table is the same primary key
		// name in master table
		addDetailField((ForiegnKeyFieldMeta) tableMeta.getField(getIdField().getName()));
	}

}
