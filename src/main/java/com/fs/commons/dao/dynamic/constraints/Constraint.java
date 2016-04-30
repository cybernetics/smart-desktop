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

	public void validate(Record record) throws ConstraintException, DaoException {
	}

	public TableMeta getTableMeta() {
		return tableMeta;
	}

	public void setTableMeta(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		dao = new DynamicDao(getTableMeta());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addField(FieldMeta field) {
		fields.add(field);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Constraint name " + getName() + ",");
		buf.append(fields);
		return buf.toString();
	}

	public ArrayList<FieldMeta> getFields() {
		return fields;
	}

	public void setFields(ArrayList<FieldMeta> fields) {
		this.fields = fields;
	}

	public String getTypeString() {
		return "default";
	}
}
