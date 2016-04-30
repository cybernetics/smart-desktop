package com.fs.commons.dao.sql.query;

public interface QueryComponent {

	public Object toQueryElement();
	public boolean isInline();

}
