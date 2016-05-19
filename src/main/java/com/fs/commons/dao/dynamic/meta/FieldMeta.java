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
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.trigger.FieldTrigger;
import com.fs.commons.dao.sql.query.QueryComponent;
import com.fs.commons.desktop.dynform.ui.ComponentFactory;
import com.fs.commons.desktop.swing.comp.editors.FSBindingComponentEditor;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.renderers.FSBindingComponentRenderer;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.fs.commons.util.ReflicationUtil;

public class FieldMeta implements Serializable, QueryComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = -5145553846250082701L;

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

	private int visibleHeight = 300;

	private int visibleWidth = 300;

	public void addTrigger(final String triggerClassName) throws Exception {
		final ReflicationUtil<FieldTrigger> reflicationUtil = new ReflicationUtil<FieldTrigger>();
		this.triggers.add(reflicationUtil.getInstance(triggerClassName, FieldTrigger.class));
	}

	/**
	 * @return
	 * @throws JKDataAccessException
	 * @throws JKDataAccessException
	 */
	public String calculateDefaultValue() throws JKDataAccessException {
		if (this.defaultValue.toUpperCase().startsWith("SELECT")) {
				final Object obj = DaoUtil.exeuteSingleOutputQuery(this.defaultValue);
				if (obj == null) {
					return null;
				}
				return obj.toString();
		}
		return this.defaultValue;
	}

	public FieldMeta copy() throws Exception {
		return (FieldMeta) GeneralUtility.copy(this);
	}

	@Override
	public boolean equals(final Object obj) {
		return ((FieldMeta) obj).getFullQualifiedName().equals(this.getFullQualifiedName());
	}

	public String getCalculatedDefaultValue() throws JKDataAccessException {
		if (this.defaultValue != null && this.defaultValue.toUpperCase().startsWith("SELECT")) {
			return DaoUtil.exeuteSingleOutputQuery(this.defaultValue).toString();
		}
		return this.defaultValue;

	}

	/**
	 * @return
	 */
	public String getCaption() {
		final String str = this.caption == null || this.caption.equals("") ? getName() : this.caption;
		return str;
		// return Lables.get(str);
	}

	/**
	 * @return
	 */
	public String getCaptionValue() {
		return this.caption;
	}

	public String getDefaultValue() {
		return this.defaultValue;
	}

	public int getDefaultValueAsInteger() {
		return this.defaultValue == null || this.defaultValue.equals("") || this.defaultValue.equals("null") ? -1
				: Integer.parseInt(this.defaultValue);
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

	public String getFilteredBy() {
		return this.filteredBy;
	}

	public String getFullQualifiedName() {
		return getFullQualifiedName("");
	}

	public String getFullQualifiedName(final String aliasNamePostFix) {
		if (getParentTable() == null) {
			System.out.println("Failed with parent id null : " + getName());
		}

		return getParentTable().getTableName() + aliasNamePostFix + "." + getName();
	}

	/**
	 * @return the indx
	 */
	public int getIndx() {
		return this.indx;
	}

	public int getMaxLength() {
		return this.maxLength;
	}

	/**
	 * @return
	 */
	public String getName() {
		return this.name;
	}

	public String getOptionsQuery() {
		return this.optionsQuery;
	}

	public TableMeta getParentTable() {
		return this.parentTable;
	}

	/**
	 * @return
	 */
	public String getPropertyName() {
		if (this.propertyName == null) {
			return GeneralUtility.fixPropertyName(getName());
		}
		return this.propertyName;
	}

	public ArrayList<FieldTrigger> getTriggers() {
		return this.triggers;
	}

	public int getType() {
		return this.type;
	}

	public int getVisibleHeight() {
		return this.visibleHeight;
	}

	public int getVisibleWidth() {
		return this.visibleWidth;
	}

	public int getWidth() {
		return this.width;
	}

	public boolean isAllowUpdate() {
		return this.allowUpdate;
	}

	public boolean isConfirmInput() {
		return this.confirmInput;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public boolean isInline() {
		return true;
	}

	public boolean isKeepLastValue() {
		return this.keepLastValue;
	}

	public boolean isLookupNumber() {
		return this.lookupNumber;
	}

	/**
	 * @return
	 */
	public boolean isRequired() {
		return this.required;
	}

	/**
	 * @return the summaryField
	 */
	public boolean isSummaryField() {
		return this.summaryField;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setAllowUpdate(final boolean allowUpdate) {
		this.allowUpdate = allowUpdate;
	}

	/**
	 * @param caption
	 *            the caption to set
	 */
	public void setCaption(final String caption) {
		this.caption = caption;
	}

	public void setConfirmInput(final boolean confirmInput) {
		this.confirmInput = confirmInput;
	}

	/**
	 * @param defaultValue
	 */
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue.toString();
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public void setFilteredBy(final String filteredBy) {
		this.filteredBy = filteredBy;// getParentTable().getField(filteredBy);
	}

	public void setIndex(final int indx) {
		this.indx = indx;
	}

	public void setKeepLastValue(final boolean keepLastValue) {
		this.keepLastValue = keepLastValue;
	}

	public void setLookupNumber(final boolean lookupNumber) {
		this.lookupNumber = lookupNumber;
	}

	public void setMaxLength(final int maxLength) {
		if (maxLength > 0) {
			this.maxLength = maxLength;
		}
	}

	/**
	 * This is to set the name of the field
	 *
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param query
	 */
	public void setOptionsQuery(final String optionsQuery) {
		this.optionsQuery = optionsQuery;
	}

	public void setParentTable(final TableMeta parentTable) {
		this.parentTable = parentTable;
	}

	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @param optional
	 */
	public void setRequired(final boolean required) {
		this.required = required;
	}

	// public void setFilteredBy(FieldMeta filteredBy) {
	// this.filteredBy = filteredBy;
	// }

	/**
	 * @param summaryField
	 *            the summaryField to set
	 */
	public void setSummaryField(final boolean summaryField) {
		this.summaryField = summaryField;
	}

	public void setTriggers(final ArrayList<FieldTrigger> triggers) {
		this.triggers = triggers;
	}

	public void setType(final int type) {
		this.type = type;
	}

	public void setVisible(final boolean visible) {
		this.visible = visible;
	}

	public void setVisibleHeight(final int visibleHeight) {
		this.visibleHeight = visibleHeight;
	}

	public void setVisibleWidth(final int visibleWidth) {
		this.visibleWidth = visibleWidth;
	}

	public void setWidth(final int width) {
		this.width = width;
	}

	public FSTableColumn toFSTableColumn() throws TableMetaNotFoundException, JKDataAccessException {
		final FSTableColumn col = new FSTableColumn();
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

	@Override
	public Object toQueryElement() {
		return getFullQualifiedName();
	}

	@Override
	public String toString() {
		return "Field Name = " + getName() + " , " + "Type = " + this.type + " , " + "Required = " + this.required + " Max-length =" + this.maxLength;
	}

}
