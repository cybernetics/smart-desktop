package com.fs.commons.dao.dynamic.meta;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.dynamic.trigger.FieldTrigger;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.dao.sql.query.QueryComponent;
import com.fs.commons.desktop.dynform.ui.ComponentFactory;
import com.fs.commons.desktop.swing.comp.editors.FSBindingComponentEditor;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.renderers.FSBindingComponentRenderer;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.fs.commons.util.ReflicationUtil;

public class FieldMeta implements Serializable, QueryComponent {
	public static final boolean ENABLED = true;

	public static final boolean ALLOW_UPDATE = true;

	public static final boolean VISIBLE = true;

	public static final int WIDTH = 0;

	public static final boolean REQUIRED = true;

	public static final int FIELD_TYPE = Types.VARCHAR;

	public static final int MAX_LENGHT = 50;

	String name;

	boolean required = REQUIRED;

	int type = FIELD_TYPE;

	int width = WIDTH;

	boolean visible = VISIBLE;

	boolean allowUpdate = ALLOW_UPDATE;

	int maxLength = MAX_LENGHT;

	boolean confirmInput;

	TableMeta parentTable;

	boolean enabled = ENABLED;

	String caption;

	boolean summaryField;
	String defaultValue;

	private int indx;
	String propertyName;
	private String filteredBy;

	private String optionsQuery;
	private ArrayList<FieldTrigger> triggers = new ArrayList<FieldTrigger>();

	private boolean lookupNumber;

	private boolean keepLastValue;

	private int visibleHeight=300;

	private int visibleWidth=300;

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getCalculatedDefaultValue() throws DaoException {
		if (defaultValue != null && defaultValue.toUpperCase().startsWith("SELECT")) {
			try {
				return DaoUtil.exeuteSingleOutputQuery(defaultValue).toString();
			} catch (RecordNotFoundException e) {
				return null;
			}
		}
		return defaultValue;

	}

	public int getDefaultValueAsInteger() {
		return defaultValue == null || defaultValue.equals("") || defaultValue.equals("null") ? -1 : Integer.parseInt(defaultValue);
	}

	/**
	 * @param defaultValue
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue.toString();
	}

	/**
	 * @return the summaryField
	 */
	public boolean isSummaryField() {
		return this.summaryField;
	}

	/**
	 * @param summaryField
	 *            the summaryField to set
	 */
	public void setSummaryField(boolean summaryField) {
		this.summaryField = summaryField;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public TableMeta getParentTable() {
		return parentTable;
	}

	public void setParentTable(TableMeta parentTable) {
		this.parentTable = parentTable;
	}

	public boolean isConfirmInput() {
		return confirmInput;
	}

	public void setConfirmInput(boolean confirmInput) {
		this.confirmInput = confirmInput;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * This is to set the name of the field
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param optional
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Field Name = " + getName() + " , " + "Type = " + type + " , " + "Required = " + required + " Max-length =" + maxLength;
	}

	public FieldMeta copy() throws Exception {
		return (FieldMeta) GeneralUtility.copy(this);
	}

	public int getWidth() {
		return width;
	}

	public boolean isAllowUpdate() {
		return allowUpdate;
	}

	public void setAllowUpdate(boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		if (maxLength > 0) {
			this.maxLength = maxLength;
		}
	}

	/**
	 * @return
	 */
	public String getCaption() {
		String str = caption == null || caption.equals("") ? getName() : caption;
		return str;
		// return Lables.get(str);
	}

	/**
	 * @return
	 */
	public String getCaptionValue() {
		return caption;
	}

	@Override
	public boolean equals(Object obj) {
		return ((FieldMeta) obj).getFullQualifiedName().equals(this.getFullQualifiedName());
	}

	/**
	 * @return
	 */
	public Class getFieldClass() {
		switch (getType()) {
		case Types.NUMERIC:
		case Types.INTEGER:
			return Integer.class;
		case Types.FLOAT:
		case Types.REAL:
		case Types.DOUBLE:
		case Types.DECIMAL:
			return Float.class;
		case Types.VARCHAR:
			return String.class;
		case Types.DATE:
			return Date.class;
		case Types.TIME:
			return Timestamp.class;
		case FSTypes.PASSWORD:
			return String.class;
		case Types.BINARY:
		case Types.LONGVARBINARY:
			return byte[].class;
		case Types.CHAR:
			return char.class;
		case Types.BOOLEAN:
			return Boolean.class;
		default:
			return Object.class;
		}
	}

	/**
	 * @return
	 * @throws DaoException
	 * @throws DaoException
	 */
	public String calculateDefaultValue() throws DaoException {
		if (defaultValue.toUpperCase().startsWith("SELECT")) {
			try {
				Object obj = DaoUtil.exeuteSingleOutputQuery(defaultValue);
				if (obj == null) {
					return null;
				}
				return obj.toString();
			} catch (RecordNotFoundException e) {
				return null;
			}
		}
		return defaultValue;
	}

	public String getFullQualifiedName() {
		return getFullQualifiedName("");
	}

	public String getFullQualifiedName(String aliasNamePostFix) {
		if(getParentTable()==null){
			System.out.println("Failed with parent id null : "+getName());
		}

		return getParentTable().getTableName() + aliasNamePostFix + "." + getName();
	}

	public void setIndex(int indx) {
		this.indx = indx;
	}

	/**
	 * @return the indx
	 */
	public int getIndx() {
		return indx;
	}

	/**
	 * @return
	 */
	public String getPropertyName() {
		if (propertyName == null) {
			return GeneralUtility.fixPropertyName(getName());
		}
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @param query
	 */
	public void setOptionsQuery(String optionsQuery) {
		this.optionsQuery = optionsQuery;
	}

	public String getOptionsQuery() {
		return optionsQuery;
	}

	@Override
	public Object toQueryElement() {
		return getFullQualifiedName();
	}

	public ArrayList<FieldTrigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(ArrayList<FieldTrigger> triggers) {
		this.triggers = triggers;
	}

	public void addTrigger(String triggerClassName) throws Exception {
		ReflicationUtil<FieldTrigger> reflicationUtil = new ReflicationUtil<FieldTrigger>();
		triggers.add(reflicationUtil.getInstance(triggerClassName, FieldTrigger.class));
	}

	public String getFilteredBy() {
		return filteredBy;
	}

	public void setFilteredBy(String filteredBy) {
		this.filteredBy = filteredBy;//getParentTable().getField(filteredBy);
	}

//	public void setFilteredBy(FieldMeta filteredBy) {
//		this.filteredBy = filteredBy;
//	}

	@Override
	public boolean isInline() {
		return true;
	}

	public void setLookupNumber(boolean lookupNumber) {
		this.lookupNumber = lookupNumber;
	}

	public boolean isLookupNumber() {
		return lookupNumber;
	}

	public FSTableColumn toFSTableColumn() throws TableMetaNotFoundException, DaoException {
		FSTableColumn col = new FSTableColumn();
		col.setName(getName());
		col.setHumanName(Lables.get(col.getName(), true));
		col.setVisible(isVisible());
		col.setEditable(isEnabled());
		col.setColumnType(getType());
		col.setPreferredWidth(getWidth());
		col.setEditor(new FSBindingComponentEditor(ComponentFactory.createComponent(this, true)));
		col.setRenderer(new FSBindingComponentRenderer(ComponentFactory.createComponent(this, true)));
		col.setDefaultValue(getDefaultValue());
		// col.setMaximumLength(getMaxLength());
		return col;
	}

	public boolean isKeepLastValue() {
		return keepLastValue;
	}

	public void setKeepLastValue(boolean keepLastValue) {
		this.keepLastValue = keepLastValue;
	}

	public int getVisibleHeight() {
		return visibleHeight;
	}

	public int getVisibleWidth() {
		return visibleWidth;
	}
	
	public void setVisibleHeight(int visibleHeight) {
		this.visibleHeight = visibleHeight;
	}
	
	public void setVisibleWidth(int visibleWidth) {
		this.visibleWidth = visibleWidth;
	}

}
