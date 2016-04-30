package com.fs.commons.desktop.swing.comp.model;

import java.util.Vector;

public class FSColumnFilter {
	enum FilterType {
		EQUALS, CONTAINS, MORE_THAN, LESS_THAN, BETWEEN, DOESNOT_CONTAINS, STARTS_WIDTH, ENDS_WITH;
		public int requiredFieldsCount() {
			if (this == BETWEEN) {
				return 2;
			}
			return 1;
		}
	}

	FilterType type = FilterType.CONTAINS;
	Vector values = new Vector();
	FSTableColumn column;
	boolean required;

	public FSColumnFilter(FSTableColumn column) {
		this.column = column;
	}

	public FilterType getType() {
		return type;
	}

	public void setType(FilterType type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public FSTableColumn getColumn() {
		return column;
	}

	public void setColumn(FSTableColumn column) {
		this.column = column;
	}

	public Vector getValues() {
		return values;
	}

	public void setValues(Vector values) {
		this.values = values;
	}

	public String toQueryString() {
		StringBuffer buf = new StringBuffer();
		Object value1 = values.get(0);
		buf.append(column.getName());
		switch (type) {
		case STARTS_WIDTH:
			buf.append(" like '" + value1 + "%'");
			break;
		case CONTAINS:
			buf.append(" like '%" + value1 + "%'");
			break;
		case ENDS_WITH:
			buf.append(" like '%" + value1 + "'");
			break;
		case DOESNOT_CONTAINS:
			buf.append(" not like '%" + value1 + "%'");
			break;
		case MORE_THAN:
			buf.append(" > '" + value1 + "'");
			break;
		case LESS_THAN:
			buf.append(" < '" + value1 + "'");
			break;
		}
		return buf.toString();
	}
}
