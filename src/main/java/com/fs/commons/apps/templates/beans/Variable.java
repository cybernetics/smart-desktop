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
package com.fs.commons.apps.templates.beans;

import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;

public class Variable {

	int varId;
	String varName;
	String tableName;
	String fieldName;
	private Query query;

	// ///////////////////////////////////////////////////////////////
	public String getFieldName() {
		return this.fieldName;
	}

	public Query getQuery() {
		return this.query;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTableName() {
		return this.tableName;
	}

	// ///////////////////////////////////////////////////////////////
	public int getVarId() {
		return this.varId;
	}

	// ///////////////////////////////////////////////////////////////
	public String getVarName() {
		return this.varName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setFieldName(final String fieldName) {
		this.fieldName = fieldName;
	}

	public void setQuery(final Query query) {
		this.query = query;

	}

	// ///////////////////////////////////////////////////////////////
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVarId(final int varId) {
		this.varId = varId;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVarName(final String varName) {
		this.varName = varName;
	}

	// ///////////////////////////////////////////////////////////////
	public FieldMeta toFieldMeta() {
		final TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(getTableName());
		final FieldMeta field = tableMeta.getField(getFieldName(), true);
		if (field == null) {
			throw new IllegalArgumentException(getFieldName() + " is not available at table " + getTableName());
		}
		return field;
	}

}
