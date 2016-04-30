package com.fs.commons.desktop.swing.comp.model;

import java.util.ArrayList;
import java.util.Vector;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.desktop.swing.dao.QueryTableModel;

public class RecordsTableModel extends QueryTableModel {

	private ArrayList<Record> records = new ArrayList<Record>();

	public RecordsTableModel(ArrayList<Record> records) {
		this.records = records;
		loadData();
	}

	@Override
	public void loadData() {
		Vector<ArrayList<Object>> cache = new Vector<ArrayList<Object>>();
		if(records != null){
			for (Record record : records) {
				cache.addElement(record.getFieldsValues());
			}
		}
		//afterLoad(true);
		fireTableChanged(null); // notify everyone that we have a new
	}
	
	@Override
	public String getActualColumnName(int i) {
		if(records != null && records.size() > 0){
			return records.get(0).getField(i).getFieldName();
		}

		return super.getActualColumnName(i);
	}
	
	@Override
	public int getColumnCount() {
		if(records != null && records.size() > 0){
			return records.get(0).getFieldsCount()-1;
		}
		return super.getColumnCount();
	}
	
	
}
