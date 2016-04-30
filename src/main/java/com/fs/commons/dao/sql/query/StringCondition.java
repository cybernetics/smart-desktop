package com.fs.commons.dao.sql.query;

public class StringCondition implements Condition{
	String condition;
	
	public StringCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public Object toQueryElement() {
		return condition;
	}

	@Override
	public boolean isInline() {
		return true;
	}

}
