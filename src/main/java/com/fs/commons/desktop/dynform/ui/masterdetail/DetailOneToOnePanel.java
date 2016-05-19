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
package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.awt.BorderLayout;
import java.util.ArrayList;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.jk.exceptions.handler.JKExceptionUtil;

/**
 * @author u087
 *
 */
public class DetailOneToOnePanel extends JKMainPanel implements DetailPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final ForiegnKeyFieldMeta foriegnKeyFieldMeta;
	DynDaoPanel pnlDetail;
	private Object masterIdValue;

	/**
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 *
	 */
	public DetailOneToOnePanel(final ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws TableMetaNotFoundException, JKDataAccessException {
		this.foriegnKeyFieldMeta = foriegnKeyFieldMeta;
		this.foriegnKeyFieldMeta.setEnabled(false);
		this.pnlDetail = new DynDaoPanel(getDetailTableMeta());
		this.pnlDetail.setAllowClose(false);
		this.pnlDetail.setAllowClear(false);
		init();
		setMasterIdValue(null);
	}

	/**
	 *
	 */
	@Override
	public void addDynDaoActionListener(final DynDaoActionListener listener) {
		this.pnlDetail.addDynDaoActionListener(listener);

	}

	/**
	 * @param masterIdValue2
	 * @return
	 * @throws JKDataAccessException
	 */
	private Record findByMasterId(final Object masterIdValue) throws JKRecordNotFoundException, JKDataAccessException {
		final DynamicDao dao = new DynamicDao(getDetailTableMeta());
		final ArrayList<Record> records = dao.findByFieldValue(this.foriegnKeyFieldMeta.getName(), masterIdValue);
		if (records.size() == 0) {
			throw new JKRecordNotFoundException();
		}
		// Guaranteed to be only one record because it is OneToOne relation ,
		// right?
		return records.get(0);
	}

	/**
	 * @return the pnlDetail
	 */
	public DynDaoPanel getDetailPanel() {
		return this.pnlDetail;
	}

	/**
	 *
	 * @return
	 */
	private TableMeta getDetailTableMeta() {
		return this.foriegnKeyFieldMeta.getParentTable();
	}

	/**
	 *
	 */
	@Override
	public void handleFind(final Object idValue) throws JKDataAccessException {
		this.pnlDetail.handleFindRecord(idValue);
		this.pnlDetail.setMode(DynDaoMode.VIEW);// TODO : check the purpose of
												// this statement
	}

	/**
	 *
	 */
	private void init() {
		addDynDaoActionListener(new DynDaoActionAdapter() {
			@Override
			public void afterAddRecord(final Record record) {
				try {
					setMasterIdValue(DetailOneToOnePanel.this.masterIdValue);
					// change to find mode
				} catch (final JKDataAccessException e) {
					JKExceptionUtil.handle(e);
				}
			}

			@Override
			public void afterDeleteRecord(final Record record) {
				// without the following call , the dynPanel will lost the old
				// master value
				try {
					setMasterIdValue(DetailOneToOnePanel.this.masterIdValue);
				} catch (final JKDataAccessException e) {
					JKExceptionUtil.handle(e);
				}
			}
		});
		add(this.pnlDetail, BorderLayout.NORTH);
	}

	/**
	 *
	 */
	@Override
	public void resetComponents() throws JKDataAccessException {
		setMasterIdValue(null);
	}

	/**
	 *
	 */
	@Override
	public void setMasterIdValue(final Object masterIdValue) throws JKDataAccessException {
		if (masterIdValue == null || masterIdValue.toString().trim().equals("")) {
			this.masterIdValue = null;
			this.pnlDetail.resetComponents();
			setMode(DynDaoMode.ADD);
			setEnabled(false);
		} else {
			this.masterIdValue = masterIdValue;
			// we enable it , the internal enable disable fnctioanlity will
			// manged locally inside that class
			// pnlDetail.setEnabled(true);
			setEnabled(true);
			try {
				final Record record = findByMasterId(masterIdValue);
				handleFind(record.getIdValue());
			} catch (final JKRecordNotFoundException e) {
				setMode(DynDaoMode.ADD);
				this.pnlDetail.setComponentValue(this.foriegnKeyFieldMeta.getName(), masterIdValue);
			}
		}
	}

	/**
	 *
	 */
	@Override
	public void setMode(final DynDaoMode mode) {
		this.pnlDetail.setMode(mode);
	}

	public void setShowButtons(final boolean show) {
		this.pnlDetail.setShowButtons(show);
	}

}
