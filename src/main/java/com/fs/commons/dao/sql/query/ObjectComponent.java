package com.fs.commons.dao.sql.query;

public class ObjectComponent implements QueryComponent {

	private final Object object;

	public ObjectComponent(Object object) {
		this.object = object;
	}

	@Override
	public Object toQueryElement() {
		return object;
	}

	@Override
	public boolean isInline() {
		return true;
	}

}
