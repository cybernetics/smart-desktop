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
package com.fs.commons.dao.dynamic.meta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.dynamic.MetaSqlBuilder;
import com.fs.commons.dao.dynamic.constraints.Constraint;
import com.fs.commons.dao.dynamic.constraints.TableDataValidator;
import com.fs.commons.dao.dynamic.constraints.TableDataValidatorFactory;
import com.fs.commons.dao.dynamic.trigger.Trigger;
import com.fs.commons.dao.sql.query.QueryComponent;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.security.JKPrivilige;
import com.jk.security.JKSecurityManager;

public class TableMeta implements Serializable, QueryComponent {
	private static final long serialVersionUID = 2829460516590650258L;

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

	private transient JKDataSource dataSource;

	private String source;

	// private int priviligeId;

	Vector<FieldGroup> groups = new Vector();

	private JKPrivilige parentPrivilige;

	// number of visible rows at a time

	// used to indicate weather to allow management of this table from detail
	// tables
	// hint : add edit button to combo box

	/**
	 * @param field
	 */
	public void addDetailField(final ForiegnKeyFieldMeta field) {
		if (this.detailFields.indexOf(field) == -1) {
			this.detailFields.add(field);
		}

	}

	public void addDetailTable(final TableMeta tableMeta) {
		// adduming the foriegn key in the detail table is the same primary key
		// name in master table
		addDetailField((ForiegnKeyFieldMeta) tableMeta.getField(getIdField().getName()));
	}

	public void addDetailTables(final String... detailTableNames) {
		for (final String tableMeta : detailTableNames) {
			addDetailTable(AbstractTableMetaFactory.getTableMeta(tableMeta));
		}
	}

	/**
	 *
	 * @param field
	 */
	public void addField(final FieldMeta field) {
		// dont allow dupliacte fields
		if (this.fields.get(field.getName()) != null) {
			return;
		}
		field.setParentTable(this);
		this.fields.put(field.getName(), field);
		this.fieldList.add(field);
	}

	public void addFields(final ArrayList<ForiegnKeyFieldMeta> foriegnFields) {
		for (int i = 0; i < foriegnFields.size(); i++) {
			addField(foriegnFields.get(i));
		}
	}

	// /////////////////////////////////////////////////////
	public void addGroup(final FieldGroup group) {
		this.groups.add(group);
		final Vector<FieldMeta> fields = group.getFields();
		for (final FieldMeta fieldMeta : fields) {
			if (fieldMeta instanceof IdFieldMeta) {
				setIdField((IdFieldMeta) fieldMeta);
			} else {
				addField(fieldMeta);
			}
		}
	}

	/**
	 *
	 * @param triggerName
	 */
	public void addTriggerName(final String triggerName) {
		this.triggerNames.add(triggerName);
	}

	public TableMeta copy() throws Exception {
		return (TableMeta) GeneralUtility.copy(this);
	}

	// //////////////////////////////////////////////////////
	private FieldGroup createDefaultGroup() {
		final FieldGroup group = new FieldGroup();
		group.addField(this.idField);
		final Vector<FieldMeta> fieldList = getFieldList();
		for (final FieldMeta fieldMeta : fieldList) {
			group.addField(fieldMeta);
		}
		return group;
	}

	public Record createEmptyRecord() {
		return createEmptyRecord(false);
	}

	/**
	 *
	 * @return
	 */
	public Record createEmptyRecord(final boolean setDefaultValues) {
		final Record record = new Record(this);
		record.setIdField(new Field(this.idField));
		for (int i = 0; i < getFieldList().size(); i++) {
			final Field field = new Field(getFieldList().get(i));
			if (setDefaultValues) {
				field.setValue(field.getMeta().getDefaultValue());
			}
			record.addField(field);
		}
		record.setNewRecord(true);
		return record;
	}

	public Record createEmptyRecord(final boolean setDefaultValues, final Record defaults) {
		final Record record = createEmptyRecord(setDefaultValues);
		record.setValues(true, defaults);
		return record;
	}

	@Override
	public boolean equals(final Object obj) {
		final TableMeta that = (TableMeta) obj;
		return that.getTableName().equals(getTableName());
	}

	public Vector<FieldMeta> getAllFields() {
		final Vector<FieldMeta> fields = new Vector();
		fields.add(getIdField());
		fields.addAll(getFieldList());
		return fields;
	}

	public String getBeanName() {
		return this.beanName;
	}

	/**
	 * @return
	 */
	public String getCaption() {
		return this.caption == null || this.caption.equals("") ? getTableId() : this.caption;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<Constraint> getConstraints() {
		return this.constraints;
	}

	// ///////////////////////////////////////////////////////////////////////
	public JKDataSource getDataSource() {
		if (this.dataSource == null) {
			return JKDataSourceFactory.getDefaultDataSource();
		}
		return this.dataSource;
	}

	/**
	 *
	 * @return
	 */
	private String getDefaultSql() {
		// return new MetaSqlBuilder(this).buildDefaultReportSql();
		return "SELECT * FROM " + getTableName();
	}

	public int getDefaultUIRowCount() {
		return this.defaultUIRowCount;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<ForiegnKeyFieldMeta> getDetailFields() {
		return this.detailFields;
	}

	/**
	 *
	 * @param fieldName
	 * @return
	 */
	public FieldMeta getField(final String fieldName) {
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
	public FieldMeta getField(final String fieldName, final boolean includingId) {
		if (includingId) {
			if (this.idField != null && getIdField().getName().equalsIgnoreCase(fieldName)) {
				return getIdField();
			}
		}
		return this.fields.get(fieldName);
	}

	public Vector<FieldMeta> getFieldList() {
		return this.fieldList;
	}

	/**
	 *
	 * @return
	 */
	public Hashtable<String, FieldMeta> getFields() {
		return this.fields;
	}

	public int getFieldsCount() {
		return getFieldList().size();
	}

	public int[] getFilters() {
		return this.filters;
	}

	/**
	 * @return
	 */
	public String getFiltersAsString() {
		String str = "";
		for (int i = 0; i < this.filters.length; i++) {
			if (i != 0) {
				str += ",";
			}
			str += this.filters[i];
		}
		return str;
	}

	// //////////////////////////////////////////////////////
	public Vector<FieldGroup> getGroups() {
		if (this.groups.size() == 0) {
			addGroup(createDefaultGroup());
		}
		return this.groups;
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	/**
	 *
	 * @return
	 */
	public IdFieldMeta getIdField() {
		if (this.idField == null) {
			throw new IllegalStateException("Primary field is required on table : " + getTableName());
		}
		return this.idField;
	}

	public String getListSql() {
		// String sql=GeneralUtility.loadSqlFromDatabase("List_"+getTableId());
		// if(sql!=null){
		// return sql;
		// }

		if (this.listSql == null) {
			return getShortReportSql();
		}
		return this.listSql;
	}

	public FieldMeta getLookupNumberField() {
		if (getIdField().isLookupNumber()) {
			return getIdField();
		}
		for (final FieldMeta field : this.fieldList) {
			if (field.isLookupNumber()) {
				return field;
			}
		}
		// if no lookip field is set , then we choose the second field to be
		// this field
		// TODO : make the below smarter
		return this.fieldList.get(0);
	}

	/**
	 * @return the managePanelClassName
	 */
	public String getManagePanelClassName() {
		return this.managePanelClassName;
	}

	public int getMaxRecordsCount() {
		return this.maxRecordsCount;
	}

	/**
	 * @return the pageRowCount
	 */
	public int getPageRowCount() {
		return this.pageRowCount;
	}

	/**
	 *
	 * @return
	 */
	public String getPanelClassName() {
		return this.panelClassName;
	}

	public JKPrivilige getParentPrivilige() {
		return this.parentPrivilige;
	}

	public JKPrivilige getPrivilige() {
		return JKSecurityManager.createPrivilige(Lables.get(getTableName(),true), getParentPrivilige());
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
		if (this.reportSql != null && this.reportSql.startsWith("@")) {
			final String sql = GeneralUtility.getSqlFile(this.reportSql.substring(1));
			if (sql != null) {
				return sql;
			} else {
				return getDefaultSql();
			}
		}
		if (this.reportSql == null || this.reportSql.equals("")) {
			return getDefaultSql();
		}
		return this.reportSql;
	}

	public String getShortReportSql() {
		// final String sql = GeneralUtility.loadSqlFromDatabase("Short_" +
		// getTableId());
		// if (sql != null) {
		// return sql;
		// }
		if (this.shortReportSql == null) {
			return new MetaSqlBuilder(this).buildDefaultShortSql();
		}
		return this.shortReportSql;
	}

	// //////////////////////////////////////////////////////
	public String getSource() {
		return this.source;
	}

	/**
	 *
	 * @return
	 */
	public TableDataValidator getTableDataValidator() {
		if (this.tableDataValidator == null) {
			this.tableDataValidator = TableDataValidatorFactory.createValidator();
		}
		return this.tableDataValidator;
	}

	/**
	 * @return the tableId
	 */
	public String getTableId() {
		return this.tableId == null ? getTableName() : this.tableId;
	}

	/*
	 *
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 *
	 * @return
	 */
	public HashSet<String> getTriggerNames() {
		return this.triggerNames;
	}

	/**
	 * @return the triggers
	 */
	public ArrayList<Trigger> getTriggers() {
		if (this.triggers == null) {
			this.triggers = new ArrayList<Trigger>();
			if (this.triggerNames.size() > 0) {
				for (final String triggerName : this.triggerNames) {
					try {
						final Trigger trigger = (Trigger) Class.forName(triggerName).newInstance();
						this.triggers.add(trigger);
					} catch (final Exception e) {
						JKExceptionUtil.handle(e);
					}
				}
			}
		}
		return this.triggers;
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<FieldMeta> getVisibleFields() {
		if (this.visibleFields == null) {
			this.visibleFields = new ArrayList<FieldMeta>();
			if (getIdField().isVisible()) {
				this.visibleFields.add(getIdField());
			}
			for (final FieldMeta field : this.fieldList) {
				if (field.isVisible()) {
					this.visibleFields.add(field);
				}
			}
		}
		return this.visibleFields;

	}

	/**
	 *
	 * @return
	 */
	public int getVisibleFieldsCount() {
		return getVisibleFields().size();
	}

	/**
	 * @return the allowAdd
	 */
	public boolean isAllowAdd() {
		try {
			// JKSecurityManager.checkAllowedPrivilige(new
			// Privilige((getTableName() + ADD_RECORD_PRIVILIGE).hashCode(),
			// "ADD", getPrivilige()));
			return this.allowAdd;
		} catch (final Exception e) {
			return false;
		}
	}

	/**
	 * @return the allowDelete
	 */
	public boolean isAllowDelete() {
		try {
			// JKSecurityManager.checkAllowedPrivilige(new
			// Privilige((getTableName() + DELETE_RECORD_PRIVILIGE).hashCode(),
			// "DELETE", getPrivilige()));
			return this.allowDelete;
		} catch (final Exception e) {
			return false;
		}
	}

	public boolean isAllowDeleteAll() {
		try {
			// JKSecurityManager.checkAllowedPrivilige(getDeleteAllPriviligeId());
			return isAllowDelete();
		} catch (final Exception e) {
			return false;
		}

	}

	public boolean isAllowManage() {
		return this.allowManage;
	}

	/**
	 *
	 * @return
	 */
	public boolean isAllowUpdate() {
		try {
			// JKSecurityManager.checkAllowedPrivilige(getUpdatePriviligeId());
			return this.allowUpdate;
		} catch (final Exception e) {
			return false;
		}
	}

	public boolean isAutoIncrementId() {
		return getIdField() != null && getIdField().isAutoIncrement();
	}

	public boolean isCaptionNull() {
		return this.caption == null || this.caption.trim().equals("");
	}

	/**
	 *
	 * @return
	 */
	public boolean isCrossTable() {
		return this.crossTable;
	}

	@Override
	public boolean isInline() {
		return true;
	}

	public boolean isListSqlNull() {
		return this.listSql == null || this.listSql.equals("") || this.listSql.toUpperCase().equals(getDefaultSql().toUpperCase());
	}

	public boolean isReportSqlNull() {
		return this.reportSql == null || this.reportSql.equals("") || this.reportSql.toUpperCase().equals(getDefaultSql().toUpperCase());
	}

	public boolean isShortSqlNull() {
		return this.shortReportSql == null || this.shortReportSql.equals("")
				|| this.shortReportSql.toUpperCase().equals(getDefaultSql().toUpperCase());
	}

	public boolean isSingleRecord() {
		return getMaxRecordsCount() == 1;
	}

	public boolean isTableIdNull() {
		return this.tableId == null || this.tableId.trim().equals("") || this.tableId.equals(getTableName());
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<ForiegnKeyFieldMeta> lstForiegnKeyFields() {
		final ArrayList<ForiegnKeyFieldMeta> list = new ArrayList<ForiegnKeyFieldMeta>();
		for (int i = 0; i < this.fieldList.size(); i++) {
			if (this.fieldList.get(i) instanceof ForiegnKeyFieldMeta) {
				list.add((ForiegnKeyFieldMeta) this.fieldList.get(i));
			}
		}
		return list;
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
		final Vector<FieldMeta> summaryFields = new Vector<FieldMeta>();
		for (int i = 0; i < this.fieldList.size(); i++) {
			final FieldMeta field = this.fieldList.get(i);

			if (field.isSummaryField()) {
				summaryFields.add(field);
			}
		}
		if (summaryFields.size() == 0) {
			summaryFields.add(this.fieldList.get(0));
		}
		return summaryFields;
	}

	public void setAllowAdd(final boolean allowAdd) {
		this.allowAdd = allowAdd;
	}

	public void setAllowDelete(final boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public void setAllowManage(final boolean allowManage) {
		this.allowManage = allowManage;
	}

	public void setAllowUpdate(final boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}

	// /**
	// *
	// * @param trigger
	// */
	// public void addTrigger(Trigger trigger) {
	// triggers.add(trigger);
	// }

	public void setBeanName(final String beanName) {
		this.beanName = beanName;
	}

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
	 *
	 * @param constraints
	 */
	public void setConstraints(final ArrayList<Constraint> constraints) {
		this.constraints = constraints;

	}

	/**
	 *
	 *
	 */
	public void setCorssTable(final boolean crossTable) {
		this.crossTable = crossTable;
	}

	/**
	 *
	 * @param crossTable
	 */
	public void setCrossTable(final boolean crossTable) {
		this.crossTable = crossTable;
	}

	// ///////////////////////////////////////////////////////////////////////
	public void setDataSource(final JKDataSource connectionManager) {
		this.dataSource = connectionManager;
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

	public void setDefaultUIRowCount(final int defaultUIRowCount) {
		this.defaultUIRowCount = defaultUIRowCount;
	}

	/**
	 *
	 * @param detailFields
	 */
	public void setDetailFields(final ArrayList<ForiegnKeyFieldMeta> detailFields) {
		this.detailFields = detailFields;
	}

	public void setFieldList(final Vector<FieldMeta> fieldList) {
		this.fieldList = fieldList;
		for (int i = 0; i < fieldList.size(); i++) {
			final FieldMeta meta = fieldList.get(i);
			meta.setParentTable(this);
			this.fields.put(meta.getName(), meta);
		}
	}

	/**
	 *
	 * @param fields
	 */
	public void setFields(final Hashtable<String, FieldMeta> fields) {
		this.fields = fields;
	}

	/**
	 *
	 * @param strings
	 */
	public void setFilters(final String[] strings) {
		this.filters = new int[strings.length];
		for (int i = 0; i < strings.length; i++) {
			this.filters[i] = Integer.parseInt(strings[i]);
		}

	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(final String iconName) {
		this.iconName = iconName;
	}

	/**
	 *
	 * @param idField
	 */
	public void setIdField(final IdFieldMeta idField) {
		this.idField = idField;
		if (idField != null) {
			idField.setParentTable(this);
		}
	}

	public void setListSql(final String listSql) {
		this.listSql = listSql;
	}

	/**
	 * @param managePanelClassName
	 *            the managePanelClassName to set
	 */
	public void setManagePanelClassName(final String managePanelClassName) {
		this.managePanelClassName = managePanelClassName;
	}

	public void setMaxRecordsCount(final int maxRecordsCount) {
		this.maxRecordsCount = maxRecordsCount;
	}

	/**
	 * @param pageRowCount
	 *            the pageRowCount to set
	 */
	public void setPageRowCount(final int pageRowCount) {
		this.pageRowCount = pageRowCount;
	}

	/**
	 *
	 * @param panelClassName
	 */
	public void setPanelClassName(final String panelClassName) {
		this.panelClassName = panelClassName;
	}

	public void setParentPrivilige(final JKPrivilige parentPrivilige) {
		this.parentPrivilige = parentPrivilige;
	}

	/**
	 *
	 * @param reportSql
	 */
	public void setReportSql(final String reportSql) {
		this.reportSql = reportSql;
	}

	public void setShortReportSql(final String shortReportSql) {
		this.shortReportSql = shortReportSql;
	}

	// //////////////////////////////////////////////////////
	public void setSource(final String source) {
		this.source = source;
	}

	/**
	 *
	 * @param tableDataValidator
	 */
	public void setTableDataValidator(final TableDataValidator tableDataValidator) {
		this.tableDataValidator = tableDataValidator;
	}

	public void setTableId(final String tableId) {
		this.tableId = tableId;
	}

	/**
	 *
	 * @param tableName
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 *
	 * @param triggerNames
	 */
	public void setTriggerNames(final HashSet<String> triggerNames) {
		this.triggerNames = triggerNames;
	}

	@Override
	public Object toQueryElement() {
		return getTableName();
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Table name : " + this.tableName);
		buf.append(" , " + this.idField);
		buf.append(" ,\n\t " + this.fields);
		return buf.toString();
	}

	/**
	 *
	 * @param record
	 * @throws ValidationException
	 */
	public void validateData(final Record record) throws ValidationException {
		getTableDataValidator().validate(this, record);
	}

}
