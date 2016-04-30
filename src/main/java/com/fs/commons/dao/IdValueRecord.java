package com.fs.commons.dao;

import java.io.Serializable;
import java.util.Vector;

public class IdValueRecord implements Serializable {
	Object id;

	Object value;

	public IdValueRecord() {
	}

	public String toString() {
		if(value!=null){
			return value.toString(); 
		}
		return id.toString();
	}

	public Object getId() {
		return id;
	}
	
	public int getIdAsInteger(){
		String id = getId().toString();
		if(id==null || id.trim().equals("")){
			return -1;
		}
		return Integer.parseInt(id);
	}

	public Object getValue() {
		return value;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static Vector<IdValueRecord> createList(int[] ids, Object[] values) {
		Vector<IdValueRecord> records=new Vector<IdValueRecord>(); 
		for (int  i=0;i<ids.length ; i++) {
			IdValueRecord rec=new IdValueRecord();
			rec.setId(ids[i]);
			rec.setValue(values[i]);
			records.add(rec);
		}		
		return records;
	}

	public static Vector<IdValueRecord> createList(Object[] values) {
		int ids[]=new int[values.length];
		for (int i=0;i<values.length;i++) {
			ids[i]=i;
		}
		return createList(ids, values);
	}
}
