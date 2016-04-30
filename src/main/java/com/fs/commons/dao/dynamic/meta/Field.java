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
	public Field(FieldMeta meta) {
		this.meta = meta;
	}

	public Field(FieldMeta meta, Object value) {
		setMeta(meta);
		setValue(value);
	}

	/**
	 * @return
	 */
	public FieldMeta getMeta() {
		return meta;
	}

	/**
	 * @param meta
	 */
	public void setMeta(FieldMeta meta) {
		this.meta = meta;
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
	public String getValue(String defalutValue) {
		if (value == null)
			return defalutValue;
		return value.toString();
	}

	/**
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return
	 */
	public Object getValueObject() {
		return value;
	}

	/**
	 * @return
	 */
	public String getSqlEquality() {
		if (getValue() == null) {
			return "" + meta.getName() + " IS NULL ";
		}
		return "" + meta.getName() + " = '" + getValue().toString() + "'";
	}

	/**
	 * @return
	 */
	public String toSqlEquality() {
		String value = DaoUtil.fixStringValue(getValue());
		if (getValue() == null || getValue().equals("")) {
			return meta.getName() + "='' OR " + meta.getName() + " IS NULL";
		} else {
			return meta.getName() + "='" + (getValue() == null ? "" : value) + "'";
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(meta.toString() + " - Value = ");
		buffer.append(value);
		return buffer.toString();
	}

	public int getValueAsInteger() {
		return getValueAsInteger(-1);
	}

	/**
	 * @return
	 */
	public int getValueAsInteger(int defaultValue) {
		return getValue() == null || getValue().trim().equals("") ? defaultValue : Integer.parseInt(getValue());
	}

	public float getValueAsFloat() {
		return getValueAsFloat(-1);
	}

	/**
	 * @return
	 */
	public float getValueAsFloat(float defaultValue) {
		if (getValue() == null || getValue().trim().equals("")) {
			return defaultValue;
		}
		if (getMeta().getType() == Types.DATE || getMeta().getType() == Types.TIME) {
			Date date = (Date) getValueObject();
			return date.getTime();
		}
		return Float.parseFloat(getValue());
	}

	public double getValueAsDouble() {
		return getValueAsDouble(-1);
	}

	public double getValueAsDouble(float defaultValue) {
		if (getValue() == null || getValue().trim().equals("")) {
			return defaultValue;
		}
		if (getMeta().getType() == Types.DATE || getMeta().getType() == Types.TIME) {
			Date date = (Date) getValueObject();
			return date.getTime();
		}
		return Double.parseDouble(getValue());
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
	public boolean isEmpty() {
		return getValue() == null || getValue().toString().equals("");
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

	public String getDatabaseString() {
		if (getValue() == null) {
			return "NULL";
		}
		return "'" + StringEscapeUtils.escapeSql(getValue()) + "'";
	}

	@Override
	public Object toQueryElement() {
		return getMeta().getFullQualifiedName() + Operator.EQUAL + getValue();
	}

	public boolean isVisible() {
		return getMeta().isVisible();
	}

	@Override
	public boolean isInline() {
		return true;
	}

	public Object getValueObject(Object defaultValue) {
		return getValueObject()==null?defaultValue:getValueObject();
	}
}
