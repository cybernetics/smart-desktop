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
package com.fs.commons.dao.dynamic;

import java.util.ArrayList;
import java.util.List;

import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.sql.query.Keyword;
import com.fs.commons.dao.sql.query.Operator;
import com.fs.commons.dao.sql.query.Query;

public class SqlBuilder {
	private final TableMeta tableMeta;

	public SqlBuilder(final TableMeta tableMeta) {
		this.tableMeta = tableMeta;
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDefaultReportSql() {
		if (this.tableMeta.isSingleRecord()) {
			return "SELECT * FROM " + this.tableMeta.getTableName();
		}
		final StringBuffer buffer = new StringBuffer("SELECT ");
		buffer.append("\n" + this.tableMeta.getIdField().getFullQualifiedName());
		final List<FieldMeta> list = this.tableMeta.getFieldList();
		// Build fields
		// TODO : add support for multiple values from same table()alias support
		int foriegnKeyFieldCount = 0;
		for (final FieldMeta fieldMeta : list) {
			if (fieldMeta instanceof ForiegnKeyFieldMeta) {
				final ForiegnKeyFieldMeta foriegnKeyFieldMeta = (ForiegnKeyFieldMeta) fieldMeta;
				foriegnKeyFieldMeta.setAliasNamePostFix(foriegnKeyFieldCount++);
				final String str = buildSqlFields(foriegnKeyFieldMeta);
				buffer.append(str);
			} else {
				// normal field
				buffer.append(",\n" + fieldMeta.getFullQualifiedName());
			}
		}
		buffer.append("\n");
		// from
		buffer.append("FROM " + this.tableMeta.getTableName());
		// inner joins
		final ArrayList<ForiegnKeyFieldMeta> foriegnKeyFields = this.tableMeta.lstForiegnKeyFields();
		for (final ForiegnKeyFieldMeta foriegnKeyFieldMeta : foriegnKeyFields) {
			buffer.append("\n" + foriegnKeyFieldMeta.getLeftJoinStatement());
		}
		return buffer.toString();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDefaultShortSql() {
		final Query q = new Query();
		q.addComponent(Keyword.SELECT);
		final List<FieldMeta> fieldList = this.tableMeta.getFieldList();
		q.addComponent(this.tableMeta.getIdField());
		for (final FieldMeta fieldMeta : fieldList) {
			q.addComponent(Operator.COMMA);
			q.addComponent(fieldMeta);
		}
		q.addComponent(Keyword.FROM);
		q.addComponent(this.tableMeta);
		return q.compile();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDelete() {
		final StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM " + this.tableMeta.getTableName());
		return sql.toString();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDelete(final Field field) {
		final StringBuffer sql = new StringBuffer();
		sql.append(buildDelete());
		sql.append(" WHERE " + field.getSqlEquality());
		return sql.toString();
	}

	public String buildFatInsert(final ArrayList<Record> records) {
		final StringBuffer buf = new StringBuffer();
		buildInsert(buf);
		for (int i = 0; i < records.size(); i++) {
			final Record record = records.get(i);
			buildValues(buf, record);
			if (i < records.size() - 1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	/**
	 * @param id
	 * @return
	 */
	public String buildFindByFilter(final Record filter) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM " + this.tableMeta.getTableName());
		buffer.append("\nWHERE 1=1 ");
		for (int i = 0; i < filter.getFieldsCount(); i++) {
			final Field field = filter.getField(i);
			if (field.getValue() != null) {
				buffer.append(" AND " + field.toSqlEquality());
			}
		}
		// +tableMeta.getIdField().getName()+" = '"+id + "'");
		return buffer.toString();
	}

	/**
	 * @param id
	 * @return
	 */
	public String buildFindById(final String id) {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM " + this.tableMeta.getTableName());
		buffer.append("\nWHERE " + this.tableMeta.getIdField().getName() + " = '" + id + "'");
		return buffer.toString();
	}

	/**
	 * @param record
	 * @return
	 */
	public String buildInsert() {
		final StringBuffer sql = new StringBuffer();
		this.tableMeta.getFieldList();
		buildInsert(sql);
		buildValues(sql, null);
		return sql.toString();
	}

	private void buildInsert(final StringBuffer sql) {
		final List<FieldMeta> fieldList = this.tableMeta.getFieldList();
		sql.append("INSERT INTO " + this.tableMeta.getTableName() + " ( ");
		if (!this.tableMeta.getIdField().isAutoIncrement()) {
			sql.append("`" + this.tableMeta.getIdField().getName() + "`");
			if (this.tableMeta.getFieldsCount() > 0) {
				sql.append(" , ");
			}
		}

		for (int i = 0; i < fieldList.size(); i++) {
			if (i > 0) {
				sql.append(" ,");
			}
			sql.append("`" + fieldList.get(i).getName() + "`");
		}
		sql.append(" ) values ");
	}

	// ///////////////////////////////////////////////////////////////
	private String buildSqlFields(final ForiegnKeyFieldMeta fieldMeta) {
		final List<FieldMeta> summaryFields = fieldMeta.getReferenceTableMeta().lstSummaryFields();
		if (summaryFields.size() == 1) {
			return ",\n" + summaryFields.get(0).getFullQualifiedName(fieldMeta.getAliasNamePostFix());
		}
		final StringBuffer buf = new StringBuffer(",CONCAT_WS(' '");
		for (final FieldMeta summaryField : summaryFields) {
			buf.append("," + summaryField.getFullQualifiedName(fieldMeta.getAliasNamePostFix()));
		}
		buf.append(") ");
		return buf.toString();
	}

	/**
	 * @param record
	 * @return
	 */
	public String buildUpdate(final Record record) {
		final StringBuffer sql = new StringBuffer();
		sql.append("Update " + this.tableMeta.getTableName() + " \n SET \n ");
		for (int i = 0; i < record.getFieldsCount(); i++) {
			if (i > 0) {
				sql.append(" , \n ");
			}
			sql.append("`" + record.getField(i).getMeta().getName() + "`=?");
		}
		sql.append(" \n WHERE " + record.getIdField().getSqlEquality());
		return sql.toString();
	}

	///////////////////////////////////////////////////////////////////////////////////////////
	private void buildValues(final StringBuffer sql, final Record values) {
		final List<FieldMeta> fieldList = this.tableMeta.getFieldList();
		sql.append("(");
		if (!this.tableMeta.getIdField().isAutoIncrement()) {
			sql.append(values == null ? "?" : values.getIdField().getDatabaseString());
			if (fieldList.size() > 0) {
				sql.append(" , ");
			}
		}
		for (int i = 0; i < fieldList.size(); i++) {
			if (i > 0) {
				sql.append(" ,");
			}
			sql.append(values == null ? "?" : values.getField(i).getDatabaseString());
		}
		sql.append(" )\n");
	}
}
