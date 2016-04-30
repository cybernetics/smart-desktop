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
package com.fs.commons.dao.dynamic.constraints;

import java.util.ArrayList;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;

public class Constraint {
	String name;

	TableMeta tableMeta;

	protected ArrayList<FieldMeta> fields = new ArrayList<FieldMeta>();

	protected DynamicDao dao;

	public void addField(final FieldMeta field) {
		this.fields.add(field);
	}

	public ArrayList<FieldMeta> getFields() {
		return this.fields;
	}

	public String getName() {
		return this.name;
	}

	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	public String getTypeString() {
		return "default";
	}

	public void setFields(final ArrayList<FieldMeta> fields) {
		this.fields = fields;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setTableMeta(final TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		this.dao = new DynamicDao(getTableMeta());
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		buf.append("Constraint name " + getName() + ",");
		buf.append(this.fields);
		return buf.toString();
	}

	public void validate(final Record record) throws ConstraintException, DaoException {
	}
}
