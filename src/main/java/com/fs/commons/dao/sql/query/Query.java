package com.fs.commons.dao.sql.query;

import java.util.ArrayList;

public class Query {
	ArrayList<QueryComponent> structor = new ArrayList<QueryComponent>();

	// ///////////////////////////////////////////////////////////////////
	public void addComponent(QueryComponent component) {
		structor.add(component);
	}

	// ///////////////////////////////////////////////////////////////////
	public String compile() {
		StringBuffer buf = new StringBuffer();
		for (QueryComponent comp : structor) {
			buf.append(comp.toQueryElement());
			buf.append(comp.isInline() ? " " : "\n");
		}
		return buf.toString();
	}

	// ///////////////////////////////////////////////////////////////////
	public void addComponent(QueryComponent component, int count, QueryComponent separator, boolean surroundWithParenchthisis) {
		ArrayList<QueryComponent> components = new ArrayList<QueryComponent>();
		for (int i = 0; i < count; i++) {
			components.add(component);
		}
		addComponents(components, separator, surroundWithParenchthisis);
	}

	// ///////////////////////////////////////////////////////////////////
	public void addComponents(ArrayList<QueryComponent> components, QueryComponent separator, boolean surroundWithParenchthisis) {
		if (surroundWithParenchthisis) {
			addComponent(Keyword.LEFT_PARENTHESES);
		}
		for (int i = 0; i < components.size(); i++) {
			if (i != 0) {
				addComponent(separator);
			}
			addComponent(components.get(i));
		}
		if (surroundWithParenchthisis) {
			addComponent(Keyword.RIGHT_PARENTHESES);
		}

	}

	// ///////////////////////////////////////////////////////////////////
	public void addValue(Object value) {
		addComponent(new ObjectComponent(value));
	}

	// ///////////////////////////////////////////////////////////////////
	public void addValues(ArrayList<Object> values, QueryComponent separator, boolean surroundWithParenchthisis) {
		ArrayList<QueryComponent> components = new ArrayList<QueryComponent>();
		for (Object value : values) {
			components.add(new ObjectComponent(value));
		}
		addComponents(components, separator, surroundWithParenchthisis);
	}
	// ///////////////////////////////////////////////////////////////////
}