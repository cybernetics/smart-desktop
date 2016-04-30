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

	public FSColumnFilter(final FSTableColumn column) {
		this.column = column;
	}

	public FSTableColumn getColumn() {
		return this.column;
	}

	public FilterType getType() {
		return this.type;
	}

	public Vector getValues() {
		return this.values;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setColumn(final FSTableColumn column) {
		this.column = column;
	}

	public void setRequired(final boolean required) {
		this.required = required;
	}

	public void setType(final FilterType type) {
		this.type = type;
	}

	public void setValues(final Vector values) {
		this.values = values;
	}

	public String toQueryString() {
		final StringBuffer buf = new StringBuffer();
		final Object value1 = this.values.get(0);
		buf.append(this.column.getName());
		switch (this.type) {
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
