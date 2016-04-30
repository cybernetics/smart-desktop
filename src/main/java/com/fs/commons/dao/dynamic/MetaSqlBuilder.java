package com.fs.commons.dao.dynamic;

import java.util.ArrayList;
import java.util.Vector;

import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.sql.query.Keyword;
import com.fs.commons.dao.sql.query.Operator;
import com.fs.commons.dao.sql.query.Query;
import com.fs.commons.dao.sql.query.QueryComponent;
import com.fs.commons.util.CollectionUtil;

public class MetaSqlBuilder {
	private final TableMeta tableMeta;

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	public MetaSqlBuilder(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	public String buildInsert(Record record) {
		Query query = new Query();
		query.addComponent(Keyword.INSERT);
		query.addComponent(Keyword.INTO);
		query.addComponent(tableMeta);

		ArrayList<QueryComponent> fields = getFieldsInInsert(record);

		query.addComponents(fields, Keyword.COMMA, true);
		query.addComponent(Keyword.VALUES);
		query.addComponent(Keyword.VARIABLE, fields.size(), Keyword.COMMA, true);
		return query.compile();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	public String buildFatInsert(ArrayList<Record> records) {
		Query query = new Query();
		query.addComponent(Keyword.INSERT);
		query.addComponent(Keyword.INTO);
		query.addComponent(tableMeta);

		ArrayList<QueryComponent> fields = getFieldsInInsert(tableMeta.createEmptyRecord());

		query.addComponents(fields, Keyword.COMMA, true);
		query.addComponent(Keyword.VALUES);

		for (Record record : records) {
			IdFieldMeta idField = tableMeta.getIdField();
			if (idField.isParticpateInInsert()) {
				query.addValue(record.getIdValue());
			}
			query.addValues(record.getFieldValues(), Keyword.COMMA, true);
		}
		return query.toString();
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	private ArrayList<QueryComponent> getFieldsInInsert(Record record) {
		ArrayList<QueryComponent> fields = new ArrayList<QueryComponent>();

		IdFieldMeta idField = tableMeta.getIdField();
		if (record.getIdValue() != null) {
			fields.add(idField);
		}

		fields.addAll(tableMeta.getFieldList());
		return fields;
	}

	// //
	// /////////////////////////////////////////////////////////////////////////////////////////
	// private void buildValues(StringBuffer sql, Record values) {
	// ArrayList<FieldMeta> fieldList = tableMeta.getFieldList();
	// sql.append("(");
	// if (!tableMeta.getIdField().isAutoIncrement()) {
	// sql.append(values == null ? "?" :
	// values.getIdField().getDatabaseString());
	// if (fieldList.size() > 0) {
	// sql.append(" , ");
	// }
	// }
	// for (int i = 0; i < fieldList.size(); i++) {
	// if (i > 0) {
	// sql.append(" ,");
	// }
	// sql.append(values == null ? "?" :
	// values.getField(i).getDatabaseString());
	// }
	// sql.append(" )\n");
	// }
	//
	// public String buildFatInsert(ArrayList<Record> records) {
	// StringBuffer buf = new StringBuffer();
	// buildInsert(buf);
	// for (int i = 0; i < records.size(); i++) {
	// Record record = records.get(i);
	// buildValues(buf, record);
	// if (i < records.size() - 1) {
	// buf.append(",");
	// }
	// }
	// return buf.toString();
	// }

	/**
	 * @param record
	 * @return
	 */
	public String buildUpdate(Record record) {
		StringBuffer sql = new StringBuffer();
		sql.append("Update " + tableMeta.getTableName() + " \n SET \n ");
		for (int i = 0; i < record.getFieldsCount(); i++) {
			if (i > 0) {
				sql.append(" , \n ");
			}
			sql.append(escapeField( record.getField(i).getMeta().getName()) + "=?");
		}
		sql.append(" \n WHERE " + record.getIdField().getSqlEquality());
		return sql.toString();
	}

	protected String escapeField(String name) {
		String scape = "";
		if (System.getProperty(CollectionUtil.fixPropertyKey("DB_ESCAPE_FIELDS"),"true").equals("true")) {
			scape = "`";
		}
		return scape+name+scape;
	}

	/**
	 * @param id
	 * @return
	 */
	public String buildFindById(String id) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM " + tableMeta.getTableName());
		buffer.append("\nWHERE " + tableMeta.getIdField().getName() + " = '" + id + "'");
		return buffer.toString();
	}

	/**
	 * @param id
	 * @return
	 */
	public String buildFindByFilter(Record filter) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM " + tableMeta.getTableName());
		buffer.append("\nWHERE 1=1 ");
		for (int i = 0; i < filter.getFieldsCount(); i++) {
			Field field = filter.getField(i);
			if (field.getValue() != null) {
				buffer.append(" AND " + field.toSqlEquality());
			}
		}
		// +tableMeta.getIdField().getName()+" = '"+id + "'");
		return buffer.toString();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDelete(Field field) {
		StringBuffer sql = new StringBuffer();
		sql.append(buildDelete());
		sql.append(" WHERE " + field.getSqlEquality());
		return sql.toString();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDelete() {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM " + tableMeta.getTableName());
		return sql.toString();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDefaultReportSql() {
		if (tableMeta.isSingleRecord()) {
			return "SELECT * FROM " + tableMeta.getTableName();
		}
		boolean firstField = true;
		StringBuffer buffer = new StringBuffer("SELECT ");
		if (tableMeta.getIdField() != null) {
			buffer.append("\n" + tableMeta.getIdField().getFullQualifiedName());
			firstField = false;
		}
		Vector<FieldMeta> list = tableMeta.getFieldList();
		// Build fields
		// TODO : add support for multiple values from same table()alias support
		int foriegnKeyFieldCount = 0;
		for (FieldMeta fieldMeta : list) {
			if (fieldMeta instanceof ForiegnKeyFieldMeta) {
				ForiegnKeyFieldMeta foriegnKeyFieldMeta = (ForiegnKeyFieldMeta) fieldMeta;
				foriegnKeyFieldMeta.setAliasNamePostFix(foriegnKeyFieldCount++);
				String str = buildSqlFields(foriegnKeyFieldMeta);
				buffer.append(str);
			} else {
				if (firstField) {
					firstField = false;
				} else {
					buffer.append(",");
				}
				// normal field
				buffer.append("\n" + fieldMeta.getFullQualifiedName());
			}
		}
		buffer.append("\n");
		// from
		buffer.append("FROM " + tableMeta.getTableName());
		// inner joins
		ArrayList<ForiegnKeyFieldMeta> foriegnKeyFields = tableMeta.lstForiegnKeyFields();
		for (ForiegnKeyFieldMeta foriegnKeyFieldMeta : foriegnKeyFields) {
			buffer.append("\n" + foriegnKeyFieldMeta.getLeftJoinStatement());
		}
		return buffer.toString();
	}

	// ///////////////////////////////////////////////////////////////
	private String buildSqlFields(ForiegnKeyFieldMeta fieldMeta) {
		Vector<FieldMeta> summaryFields = fieldMeta.getReferenceTableMeta().lstSummaryFields();
		if (summaryFields.size() == 1) {
			return ",\n" + summaryFields.get(0).getFullQualifiedName(fieldMeta.getAliasNamePostFix());
		}
		StringBuffer buf = new StringBuffer(",CONCAT_WS(' '");
		for (FieldMeta summaryField : summaryFields) {
			buf.append("," + summaryField.getFullQualifiedName(fieldMeta.getAliasNamePostFix()));
		}
		buf.append(") ");
		return buf.toString();
	}

	// ///////////////////////////////////////////////////////////////
	public String buildDefaultShortSql() {
		Query q = new Query();
		q.addComponent(Keyword.SELECT);
		Vector<FieldMeta> fieldList = tableMeta.getFieldList();
		q.addComponent(tableMeta.getIdField());
		for (FieldMeta fieldMeta : fieldList) {
			q.addComponent(Operator.COMMA);
			q.addComponent(fieldMeta);
		}
		q.addComponent(Keyword.FROM);
		q.addComponent(tableMeta);
		return q.compile();
	}

	// ///////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		TableMeta meta = AbstractTableMetaFactory.getTableMeta("sec_users");
		MetaSqlBuilder s = new MetaSqlBuilder(meta);
		System.out.println(s.buildInsert(meta.createEmptyRecord()));
	}

}
