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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.GridLayout;
import java.util.ArrayList;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.util.ExceptionUtil;

/**
 *
 * @author Mohamed Kiswani
 *
 */
public abstract class PnlMappingFields extends JKPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 7955549176746271513L;
	public static final String MAPPING_FIELD_NAME = "mapping_name";
	////////////////////////////////////////////////////////////////////////////////////
	protected QueryJTable source = null;
	protected QueryJTable target = null;
	protected JKButton addAsNewBtn = new JKButton("ADD_AS_NEW");
	protected JKButton removeRecordBtn = new JKButton("REMOVE");

	protected JKButton autoMapBtn = new JKButton("AUTO_MAP");
	protected JKButton mapBtn = new JKButton("MAP");

	protected JKButton removeBtn = new JKButton("REMOVE_MAPPING");
	protected TableMeta sourceMeta;
	protected TableMeta targetMeta;
	protected DynamicDao sourceDao;
	protected DynamicDao targetDao;

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public PnlMappingFields() {
		this.sourceMeta = AbstractTableMetaFactory.getTableMeta(getSourceTableMetaName());
		this.targetMeta = AbstractTableMetaFactory.getTableMeta(getTargetTableMetaName());
		this.source = new QueryJTable(this.sourceMeta, "EXCEL_TABLE", false);
		this.sourceDao = new DynamicDao(this.sourceMeta);
		this.targetDao = new DynamicDao(this.targetMeta);
		this.target = new QueryJTable(this.targetMeta, "FS_TABLE", false);
		init();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void addBtnsListeners() {
		SwingUtility.addActionListener(this.addAsNewBtn, this, "handleAdd");
		SwingUtility.addActionListener(this.removeRecordBtn, this, "handleRemove");
		SwingUtility.addActionListener(this.autoMapBtn, this, "handleAutoMap");
		SwingUtility.addActionListener(this.mapBtn, this, "handleMap");
		SwingUtility.addActionListener(this.removeBtn, this, "handleRemoveMapping");

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected final JKPanel getBtnsPnl() {
		addBtnsListeners();
		final JKPanel container = new JKPanel();
		final JKPanel pnl = new JKPanel();
		pnl.setLayout(new GridLayout(5, 1));
		pnl.add(this.addAsNewBtn);
		this.addAsNewBtn.setIcon("db_add.png");
		// pnl.add(removeRecordBtn);
		pnl.add(this.autoMapBtn);
		this.autoMapBtn.setIcon("automatic_map.png");
		pnl.add(this.mapBtn);
		this.mapBtn.setIcon("map.png");
		pnl.add(this.removeBtn);
		this.removeBtn.setIcon("remove_mapping.png");
		container.add(pnl);
		return container;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected final JKPanel getCenterPnl() {
		final JKPanel pnl = new JKPanel();
		this.source.setPreferredSize(370, 300);
		this.target.setPreferredSize(370, 300);
		pnl.add(this.source);
		pnl.add(getBtnsPnl());
		pnl.add(this.target);
		return pnl;
	}

	protected abstract String getSourceFieldName();

	////////////////////////////////////////////////////////////////////////////////////
	protected abstract String getSourceTableMetaName();

	protected abstract String getTargetMainFieldName();

	protected abstract String getTargetTableMetaName();

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// this method is called by reflection by SwingUtility.addActionListener
	protected void handleAdd() {
		try {
			this.source.checkEmptySelection();
			for (final String id : this.source.getSelectedIds()) {
				final Record sourceRecord = this.sourceDao.findRecord(id);
				final Record newRecord = this.targetMeta.createEmptyRecord();
				Field field = newRecord.getField(getTargetMainFieldName());
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				field = newRecord.getField(MAPPING_FIELD_NAME);
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				setAdditionalValues(sourceRecord, newRecord);
				this.targetDao.insertRecord(newRecord);
			}

			this.target.reloadData();
			this.source.reloadData();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}

	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// this method is called by reflection by SwingUtility.addActionListener
	protected void handleAutoMap() {
		try {
			final ArrayList<Record> sourceRecords = this.sourceDao.lstRecordsByReportSql();
			for (final Record record : sourceRecords) {
				final Object sourceFieldValue = record.getFieldValue(getSourceFieldName());
				final ArrayList<Record> targetRecords = this.targetDao.findByFieldValue(getTargetMainFieldName(), sourceFieldValue);
				for (final Record targetRecord : targetRecords) {
					targetRecord.getField(MAPPING_FIELD_NAME).setValue(record.getFieldValue(getSourceFieldName()));
					this.targetDao.updateRecord(targetRecord);
				}
			}
			this.target.reloadData();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// this method is called by reflection by SwingUtility.addActionListener
	protected void handleMap() {
		try {
			this.source.checkEmptySelection();
			this.target.checkEmptySelection();
			final Record sourceRecord = this.sourceDao.findRecord(this.source.getSelectedIdAsInteger());
			final Record targetRecord = this.targetDao.findRecord(this.target.getSelectedIdAsInteger());
			targetRecord.getField(MAPPING_FIELD_NAME).setValue(sourceRecord.getFieldValue(getSourceFieldName()));
			this.targetDao.updateRecord(targetRecord);
			this.target.reloadData();
			this.source.reloadData();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// this method is called by reflection by SwingUtility.addActionListener
	protected void handleRemove() {
		try {
			this.target.checkEmptySelection();
			for (final String id : this.target.getSelectedIds()) {
				this.targetDao.deleteRecord(id);
			}
			this.target.reloadData();
			this.source.reloadData();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// this method is called by reflection by SwingUtility.addActionListener
	protected void handleRemoveMapping() {
		try {
			for (final String id : this.target.getSelectedIds()) {
				final Record targetRecord = this.targetDao.findRecord(id);
				targetRecord.getField(MAPPING_FIELD_NAME).setValue("");
				this.targetDao.updateRecord(targetRecord);
			}
			this.target.reloadData();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected final void init() {
		this.source.setPreferredSize(450, 250);
		this.target.setPreferredSize(450, 250);
		final JKPanel continer = new JKPanel();
		continer.add(getCenterPnl());
		add(continer);
	}

	/**
	 *
	 * @param newRecord
	 * @param newRecord2
	 * @return
	 */
	protected void setAdditionalValues(final Record source, final Record target) {
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////

}
