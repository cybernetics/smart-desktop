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

import java.util.ArrayList;

public class Query {
	ArrayList<QueryComponent> structor = new ArrayList<QueryComponent>();

	// ///////////////////////////////////////////////////////////////////
	public void addComponent(final QueryComponent component) {
		this.structor.add(component);
	}

	// ///////////////////////////////////////////////////////////////////
	public void addComponent(final QueryComponent component, final int count, final QueryComponent separator,
			final boolean surroundWithParenchthisis) {
		final ArrayList<QueryComponent> components = new ArrayList<QueryComponent>();
		for (int i = 0; i < count; i++) {
			components.add(component);
		}
		addComponents(components, separator, surroundWithParenchthisis);
	}

	// ///////////////////////////////////////////////////////////////////
	public void addComponents(final ArrayList<QueryComponent> components, final QueryComponent separator, final boolean surroundWithParenchthisis) {
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
	public void addValue(final Object value) {
		addComponent(new ObjectComponent(value));
	}

	// ///////////////////////////////////////////////////////////////////
	public void addValues(final ArrayList<Object> values, final QueryComponent separator, final boolean surroundWithParenchthisis) {
		final ArrayList<QueryComponent> components = new ArrayList<QueryComponent>();
		for (final Object value : values) {
			components.add(new ObjectComponent(value));
		}
		addComponents(components, separator, surroundWithParenchthisis);
	}
	// ///////////////////////////////////////////////////////////////////

	// ///////////////////////////////////////////////////////////////////
	public String compile() {
		final StringBuffer buf = new StringBuffer();
		for (final QueryComponent comp : this.structor) {
			buf.append(comp.toQueryElement());
			buf.append(comp.isInline() ? " " : "\n");
		}
		return buf.toString();
	}
}