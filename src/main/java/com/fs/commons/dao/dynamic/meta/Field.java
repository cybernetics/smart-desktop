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

import java.sql.Types;
import java.util.Date;

import org.apache.commons.lang.StringEscapeUtils;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.sql.query.Operator;
import com.fs.commons.dao.sql.query.QueryComponent;

public class Field implements QueryComponent {
	FieldMeta meta;

	Object value;

	/**
	 * @param meta
	 */
	public Field(final FieldMeta meta) {
		this.meta = meta;
	}

	public Field(final FieldMeta meta, final Object value) {
		setMeta(meta);
		setValue(value);
	}

	public String getDatabaseString() {
		if (getValue() == null) {
			return "NULL";
		}
		return "'" + StringEscapeUtils.escapeSql(getValue()) + "'";
	}

	/**
	 * @return
	 */
	public String getFieldName() {
		return getMeta().getName();
	}

	/**
	 * @return
	 */
	public FieldMeta getMeta() {
		return this.meta;
	}

	/**
	 * @return
	 */
	public String getSqlEquality() {
		if (getValue() == null) {
			return "" + this.meta.getName() + " IS NULL ";
		}
		return "" + this.meta.getName() + " = '" + getValue().toString() + "'";
	}

	/**
	 * @return
	 */
	public String getValue() {
		return getValue(null);
	}

	/**
	 * @return
	 */
	public String getValue(final String defalutValue) {
		if (this.value == null) {
			return defalutValue;
		}
		return this.value.toString();
	}

	public Boolean getValueAsBoolean() {
		if (getValue() == null) {
			return false;
		}
		if (getValueObject() instanceof Boolean) {
			return ((Boolean) getValueObject()).booleanValue();
		}
		return getValueObject().toString().equals("1");
		// return null;
	}

	public double getValueAsDouble() {
		return getValueAsDouble(-1);
	}

	public double getValueAsDouble(final float defaultValue) {
		if (getValue() == null || getValue().trim().equals("")) {
			return defaultValue;
		}
		if (getMeta().getType() == Types.DATE || getMeta().getType() == Types.TIME) {
			final Date date = (Date) getValueObject();
			return date.getTime();
		}
		return Double.parseDouble(getValue());
	}

	public float getValueAsFloat() {
		return getValueAsFloat(-1);
	}

	/**
	 * @return
	 */
	public float getValueAsFloat(final float defaultValue) {
		if (getValue() == null || getValue().trim().equals("")) {
			return defaultValue;
		}
		if (getMeta().getType() == Types.DATE || getMeta().getType() == Types.TIME) {
			final Date date = (Date) getValueObject();
			return date.getTime();
		}
		return Float.parseFloat(getValue());
	}

	public int getValueAsInteger() {
		return getValueAsInteger(-1);
	}

	/**
	 * @return
	 */
	public int getValueAsInteger(final int defaultValue) {
		return getValue() == null || getValue().trim().equals("") ? defaultValue : Integer.parseInt(getValue());
	}

	/**
	 * @return
	 */
	public Object getValueObject() {
		return this.value;
	}

	public Object getValueObject(final Object defaultValue) {
		return getValueObject() == null ? defaultValue : getValueObject();
	}

	/**
	 * @return
	 */
	public boolean isEmpty() {
		return getValue() == null || getValue().toString().equals("");
	}

	@Override
	public boolean isInline() {
		return true;
	}

	public boolean isVisible() {
		return getMeta().isVisible();
	}

	/**
	 * @param meta
	 */
	public void setMeta(final FieldMeta meta) {
		this.meta = meta;
	}

	/**
	 * @param value
	 */
	public void setValue(final Object value) {
		this.value = value;
	}

	@Override
	public Object toQueryElement() {
		return getMeta().getFullQualifiedName() + Operator.EQUAL + getValue();
	}

	/**
	 * @return
	 */
	public String toSqlEquality() {
		final String value = DaoUtil.fixStringValue(getValue());
		if (getValue() == null || getValue().equals("")) {
			return this.meta.getName() + "='' OR " + this.meta.getName() + " IS NULL";
		} else {
			return this.meta.getName() + "='" + (getValue() == null ? "" : value) + "'";
		}
	}

	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append(this.meta.toString() + " - Value = ");
		buffer.append(this.value);
		return buffer.toString();
	}
}
