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
package com.fs.commons.desktop.swing.comp.model;

import java.util.ArrayList;
import java.util.Vector;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.desktop.swing.dao.QueryTableModel;

public class RecordsTableModel extends QueryTableModel {

	/**
	 *
	 */
	private static final long serialVersionUID = 8973345033644376613L;
	private ArrayList<Record> records = new ArrayList<Record>();

	public RecordsTableModel(final ArrayList<Record> records) {
		this.records = records;
		loadData();
	}

	@Override
	public String getActualColumnName(final int i) {
		if (this.records != null && this.records.size() > 0) {
			return this.records.get(0).getField(i).getFieldName();
		}

		return super.getActualColumnName(i);
	}

	@Override
	public int getColumnCount() {
		if (this.records != null && this.records.size() > 0) {
			return this.records.get(0).getFieldsCount() - 1;
		}
		return super.getColumnCount();
	}

	@Override
	public void loadData() {
		final Vector<ArrayList<Object>> cache = new Vector<ArrayList<Object>>();
		if (this.records != null) {
			for (final Record record : this.records) {
				cache.addElement(record.getFieldsValues());
			}
		}
		// afterLoad(true);
		fireTableChanged(null); // notify everyone that we have a new
	}

}
