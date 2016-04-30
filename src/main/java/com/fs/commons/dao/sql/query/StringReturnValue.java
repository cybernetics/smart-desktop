package com.fs.commons.dao.sql.query;

public class StringReturnValue implements ReturnValue {
	String returnValue;

	public StringReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}

	@Override
	public Object toQueryElement() {
		return getReturnValue();
	}

	@Override
	public boolean isInline() {
		return true;
	}

}
