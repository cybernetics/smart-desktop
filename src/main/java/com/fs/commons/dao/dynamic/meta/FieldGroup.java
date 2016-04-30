package com.fs.commons.dao.dynamic.meta;

import java.util.Vector;

public class FieldGroup {
	int index;
	String name;
	String title;
	Vector<FieldMeta> fields = new Vector<FieldMeta>();
	int rowCount;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Vector<FieldMeta> getFields() {
		return fields;
	}

	public void setFields(Vector<FieldMeta> fields) {
		this.fields = fields;
	}

	public void addField(FieldMeta field) {
		fields.add(field);
	}

	public int getRowCount() {
		if (rowCount > getFields().size()) {
			return getFields().size();
		}
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

}
