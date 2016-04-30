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

	public Keyword(final String name) {
		this.name = name;
	}

	public Keyword(final String name, final boolean inline) {
		this.name = name;
		this.inline = inline;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public boolean isInline() {
		return this.inline;
	}

	public void setInline(final boolean inline) {
		this.inline = inline;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public Object toQueryElement() {
		return getName();
	}

}
