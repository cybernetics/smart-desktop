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
package com.fs.commons.dao.dynamic.meta;

import java.util.Vector;

public class FieldGroup {
	int index;
	String name;
	String title;
	Vector<FieldMeta> fields = new Vector<FieldMeta>();
	int rowCount;

	public void addField(final FieldMeta field) {
		this.fields.add(field);
	}

	public Vector<FieldMeta> getFields() {
		return this.fields;
	}

	public int getIndex() {
		return this.index;
	}

	public String getName() {
		return this.name;
	}

	public int getRowCount() {
		if (this.rowCount > getFields().size()) {
			return getFields().size();
		}
		return this.rowCount;
	}

	public String getTitle() {
		return this.title;
	}

	public void setFields(final Vector<FieldMeta> fields) {
		this.fields = fields;
	}

	public void setIndex(final int index) {
		this.index = index;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setRowCount(final int rowCount) {
		this.rowCount = rowCount;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

}
