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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.jk.exceptions.handler.JKExceptionUtil;

/**
 *
 * @author mkiswani
 *
 */
public abstract class PnlMappingWithLookUp extends PnlMappingFields {
	/**
	 *
	 */
	private static final long serialVersionUID = -2579316669536232000L;

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) {
		final ApplicationManager instance = ApplicationManager.getInstance();
		try {
			instance.init();
			SwingUtility.testPanel(new PnlMappingWithLookUp() {
				@Override
				protected String getLookupTableName() {
					return "conf_countries";
				}

				@Override
				protected String getSourceFieldName() {
					return "city";
				}

				@Override
				protected String getSourceTableMetaName() {
					return "tmp_cities";
				}

				@Override
				protected String getTargetFildName() {
					return "country_id";
				}

				@Override
				protected String getTargetMainFieldName() {
					return "city_name";
				}

				@Override
				protected String getTargetTableMetaName() {
					return "conf_cities";
				}
			});
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private final TableMeta meta = AbstractTableMetaFactory.getTableMeta(getLookupTableName());
	private final DaoComboBox cmd = new DaoComboBox(this.meta);
	private final JKButton addBtn = new JKButton("ADD");

	public PnlMappingWithLookUp() throws JKDataAccessException {

	}

	@Override
	protected void addBtnsListeners() {
		SwingUtility.addActionListener(this.addAsNewBtn, this, "processAdd");
		SwingUtility.addActionListener(this.removeRecordBtn, this, "handleRemove");
		SwingUtility.addActionListener(this.autoMapBtn, this, "handleAutoMap");
		SwingUtility.addActionListener(this.mapBtn, this, "handleMap");
		SwingUtility.addActionListener(this.removeBtn, this, "handleRemoveMapping");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected ActionListener getAddWithLookupListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAddWithLookUp();
			}
		};
	}

	private JKPanel getLookupPnl() {
		final JKPanel panel = new JKPanel();
		panel.add(new JKLabledComponent("DEFALUT_" + this.meta.getField(getTargetFildName(), true).getName(), this.cmd));
		panel.add(this.addBtn);
		this.addBtn.setIcon("db_add.png");
		this.addBtn.addActionListener(getAddWithLookupListener());
		return panel;

	}

	protected abstract String getLookupTableName();

	protected abstract String getTargetFildName();

	private void handleAddWithLookUp() {
		try {
			this.source.checkEmptySelection();
			this.cmd.checkEmpty();
			for (final String id : this.source.getSelectedIds()) {
				final Record sourceRecord = this.sourceDao.findRecord(id);
				final Record newRecord = this.targetMeta.createEmptyRecord();
				Field field = newRecord.getField(getTargetMainFieldName());
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				field = newRecord.getField(MAPPING_FIELD_NAME);
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				field = newRecord.getField(getTargetFildName());
				field.setValue(this.cmd.getSelectedIdValue());

				this.targetDao.insertRecord(newRecord);
			}

			this.target.reloadData();
			this.source.reloadData();
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	protected void processAdd() {
		SwingUtility.showPanelInDialog(getLookupPnl(), "PLEASE_SELECT" + this.meta.getCaption(), true);
	}

}
