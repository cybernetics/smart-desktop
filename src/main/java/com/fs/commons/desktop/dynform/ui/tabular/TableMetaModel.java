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
package com.fs.commons.desktop.dynform.ui.tabular;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;

public class TableMetaModel extends FSTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = -7079138157220460091L;
	private final TableMeta tableMeta;
	private final DynamicDao dao;

	/**
	 *
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public TableMetaModel(final TableMeta tableMeta) throws TableMetaNotFoundException, DaoException {
		this.tableMeta = tableMeta;
		this.dao = DaoFactory.createDynamicDao(tableMeta);
		init();
	}

	@Override
	public FSTableRecord addRecord() {
		final FSTableRecord record = super.addRecord();
		final Vector<FieldMeta> fieldList = this.tableMeta.getAllFields();
		for (final FieldMeta fieldMeta : fieldList) {
			record.setColumnValue(fieldMeta.getName(), fieldMeta.getDefaultValue());
		}
		return record;
	}

	public List<Record> getAllDaoRecords() throws TableMetaNotFoundException, DaoException {
		final Vector<FSTableRecord> records = getRecords();
		final List<Record> daoRecords = new Vector();
		for (final FSTableRecord fsTableRecord : records) {
			final Record daoRecord = this.tableMeta.createEmptyRecord();
			daoRecord.populateFrom(fsTableRecord);
			daoRecords.add(daoRecord);
		}
		return daoRecords;
	}

	/**
	 *
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	private void init() throws TableMetaNotFoundException, DaoException {
		final Vector<FieldMeta> fieldList = this.tableMeta.getAllFields();
		for (final FieldMeta fieldMeta : fieldList) {
			addFSTableColumn(fieldMeta.toFSTableColumn());
		}
	}

	public void setFilterValue(final String fieldName, final Object value) throws DaoException {
		clearRecords();
		final ArrayList<Record> records = this.dao.findByFieldValue(fieldName, value);
		for (final Record record : records) {
			final FSTableRecord fsRecord = createEmptyRecord();
			record.populateTo(fsRecord);
			addRecord(fsRecord);
		}
	}
}
