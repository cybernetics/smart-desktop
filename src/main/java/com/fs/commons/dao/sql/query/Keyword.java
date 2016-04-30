package com.fs.commons.dao.sql.query;

public class Keyword implements QueryComponent {
	public static final QueryComponent SELECT = new Keyword("SELECT");
	public static final QueryComponent FROM = new Keyword("FROM");
	public static final QueryComponent AND = new Keyword("AND", true);
	public static final QueryComponent OR = new Keyword("OR", true);
	public static final QueryComponent LEFT_JOIN = new Keyword("LEFT JOIN", true);
	public static final QueryComponent RIGHT_JOIN = new Keyword("RIGHT JOIN", true);
	public static final QueryComponent WHERE = new Keyword("WHERE");
	public static final QueryComponent INSERT = new Keyword("INSERT", true);
	public static final QueryComponent VALUES = new Keyword("VALUES");
	public static final QueryComponent INTO = new Keyword("INTO", true);
	public static final QueryComponent COMMA = new Keyword(",", true);
	public static final QueryComponent VARIABLE = new Keyword("?", true);
	public static final QueryComponent LEFT_PARENTHESES = new Keyword("(", true);
	public static final QueryComponent RIGHT_PARENTHESES = new Keyword(")");
	String name;
	boolean inline;

	public Keyword() {
		// TODO Auto-generated constructor stub
	}

	public Keyword(String name) {
		this.name = name;
	}

	public Keyword(String name, boolean inline) {
		this.name = name;
		this.inline = inline;
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

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

}
