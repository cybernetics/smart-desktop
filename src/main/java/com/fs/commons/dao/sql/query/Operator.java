package com.fs.commons.dao.sql.query;

public class Operator implements QueryComponent {
	public static final Operator EQUAL = new Operator("=");
	public static final Operator COMMA = new Operator(",");
	public static final Operator MORE_THAN = new Operator(">");
	public static final Operator LESS_THAN = new Operator("<");

	String name;

	public Operator(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Object toQueryElement() {
		return getName();
	}
	
	@Override
	public String toString() {
		return toQueryElement().toString();
	}

	@Override
	public boolean isInline() {
		return true;
	}
}
