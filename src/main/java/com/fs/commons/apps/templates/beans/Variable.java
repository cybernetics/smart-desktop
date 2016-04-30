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
	public int getVarId() {
		return varId;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVarId(int varId) {
		this.varId = varId;
	}

	// ///////////////////////////////////////////////////////////////
	public String getVarName() {
		return varName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVarName(String varName) {
		this.varName = varName;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTableName() {
		return tableName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	// ///////////////////////////////////////////////////////////////
	public String getFieldName() {
		return fieldName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	// ///////////////////////////////////////////////////////////////
	public FieldMeta toFieldMeta() {
		TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(getTableName());
		 FieldMeta field = tableMeta.getField(getFieldName(), true);
		 if(field==null){
			 throw new IllegalArgumentException(getFieldName()+" is not available at table "+getTableName());
		 }
		 return field;
	}

	public void setQuery(Query query) {
		this.query = query;

	}

	public Query getQuery() {
		return query;
	}

}
