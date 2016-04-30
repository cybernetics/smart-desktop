package com.fs.commons.dao.sql.query;

import com.fs.commons.dao.dynamic.meta.FieldMeta;

public class FieldCondition implements Condition {

	String fieldName;
	Operator operator = Operator.EQUAL;
	Object value;

	public FieldCondition() {
	}
	
	public FieldCondition(FieldMeta fieldMeta, Object value) {
		setFieldName(fieldMeta.getFullQualifiedName());
		setValue(value);
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public Object toQueryElement() {
		return getFieldName()+getOperator().toQueryElement()+getValue().toString();
	}

	@Override
	public boolean isInline() {
		return false;
	}

}
