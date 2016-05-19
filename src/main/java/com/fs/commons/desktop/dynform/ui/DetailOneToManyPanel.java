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
package com.fs.commons.desktop.dynform.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.event.RecordActionAdapter;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.dynform.ui.masterdetail.DetailPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

/**
 * @author u087
 *
 */
public class DetailOneToManyPanel extends JKMainPanel implements DetailPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final ForiegnKeyFieldMeta foriegnKeyFieldMeta;

	private final TableMeta masterTableMeta;

	QueryJTable queryTable = new QueryJTable("", "");;

	Object masterIdValue;

	DynDaoPanel pnlDetail;

	// extra condition to be used with the query table in reloadData
	String extraWhereCondition;

	JKButton btnAdd = new JKButton("ADD_RECORD", "alt A");
	JKButton btnDelete = new JKButton("DELETE", "alt D");

	/**
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 *
	 */
	public DetailOneToManyPanel(final ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws TableMetaNotFoundException, JKDataAccessException {
		// fix me
		this.masterTableMeta = foriegnKeyFieldMeta.getReferenceTableMeta();
		this.foriegnKeyFieldMeta = foriegnKeyFieldMeta;
		this.foriegnKeyFieldMeta.setAllowUpdate(false);
		this.foriegnKeyFieldMeta.setEnabled(false);
		this.queryTable.getModel().setDataSource(foriegnKeyFieldMeta.getParentTable().getDataSource());
		// queryTable.setShowRecordsCount(true);
		this.pnlDetail = createDetailPanel();
		init();
		resetComponents();
	}

	public DetailOneToManyPanel(final String detailTableName, final String foriegKeyFieldName) throws TableMetaNotFoundException, JKDataAccessException {
		this((ForiegnKeyFieldMeta) AbstractTableMetaFactory.getTableMeta(detailTableName).getField(foriegKeyFieldName));
	}

	public void addDaoListener(final DynDaoActionListener listener) {
		this.pnlDetail.addDynDaoActionListener(listener);
	}

	/**
	 *
	 */
	@Override
	public void addDynDaoActionListener(final DynDaoActionListener listener) {
		this.pnlDetail.addDynDaoActionListener(listener);
	}

	/**
	 * p
	 *
	 * @return
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 */
	protected DynDaoPanel createDetailPanel() throws TableMetaNotFoundException, JKDataAccessException {
		final DynDaoPanel pnlDetail = new DynDaoPanel(getDetailTableMeta());
		pnlDetail.setAllowClose(false);
		pnlDetail.setAllowDuplicate(true);
		return pnlDetail;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		final JKPanel pnl = new JKPanel(new FlowLayout(FlowLayout.LEADING));
		if (isShowDaoPanelInDialog()) {
			pnl.add(this.btnAdd);
		}
		pnl.add(this.btnDelete);
		this.btnDelete.setShowProgress(true);
		this.btnDelete.setIcon("db_remove.png");
		this.btnAdd.setIcon("add_commons_system_icon.gif");
		return pnl;
	}

	/**
	 *
	 * @return
	 */
	private String getButtonsPanelLocation() {
		if (isShowDaoPanelInDialog()) {
			return BorderLayout.NORTH;
		}
		return getDaoPanelLocation().equals(BorderLayout.NORTH) ? BorderLayout.SOUTH : BorderLayout.NORTH;
	}

	private Component getCenterPanel() {
		final JKPanel pnl = new JKPanel(new BorderLayout());
		this.queryTable.setShowRecordsCount(true);
		this.queryTable.setPreferredSize(new Dimension(700, 300));
		pnl.add(getButtonsPanel(), getButtonsPanelLocation());
		pnl.add(this.queryTable, BorderLayout.CENTER);
		return pnl;
	}

	/**
	 *
	 * @return
	 */
	protected String getDaoPanelLocation() {
		return BorderLayout.NORTH;
	}

	/**
	 *
	 * @return
	 */
	public TableMeta getDetailTableMeta() {
		return this.foriegnKeyFieldMeta.getParentTable();
	}

	/**
	 * @return the extraWhereCondition
	 */
	public String getExtraWhereCondition() {
		return this.extraWhereCondition;
	}

	/**
	 *
	 * @return
	 */
	public ForiegnKeyFieldMeta getForiegnKeyFieldMeta() {
		return this.foriegnKeyFieldMeta;
	}

	/**
	 *
	 * @return
	 */
	public Object getMasterIdValue() {
		return this.masterIdValue;
	}

	/**
	 *
	 * @return
	 */
	protected int getMasterIdValueAsInteger() {
		if (getMasterIdValue() == null) {
			return 0;
		}
		return Integer.parseInt(getMasterIdValue().toString());
	}

	/**
	 *
	 * @return
	 */
	public TableMeta getMasterTableMeta() {
		return this.masterTableMeta;
	}

	/**
	 *
	 * @return
	 */
	public DynDaoPanel getPnlDetail() {
		return this.pnlDetail;
	}

	/**
	 *
	 * @return
	 */
	public QueryJTable getQueryTable() {
		return this.queryTable;
	}

	/**
	 *
	 */
	protected void handleDelete() {
		final int[] ids = this.queryTable.getSelectedIdsAsInteger();
		if (ids.length == 0) {
			SwingUtility.showUserErrorDialog("PLEASE_SELECT_RECORDS_FROM_TABLE");
		}
		if (SwingUtility.showConfirmationDialog("YOU_ARE_ABOUT_TO_DELETE_ALL_SELECTED_RECORDS,ARE_YOU_SURE?")) {
			for (final Integer id : ids) {
				try {
					this.pnlDetail.handleFindRecord(id);
					this.pnlDetail.handleDelete(false);
				} catch (final JKDataAccessException e) {
					JKExceptionUtil.handle(e);
				}
			}
		}
		reloadTableData();
	}

	/**
	 * @param recordId
	 */
	@Override
	public void handleFind(final Object recordId) {
		try {
			this.pnlDetail.handleFindRecord(recordId);
			if (isShowDaoPanelInDialog()) {
				SwingUtility.showPanelInDialog(this.pnlDetail, "EDIT_RECORD");
			}
		} catch (final JKDataAccessException e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 */
	protected void handleImport() {
		final JFileChooser fs = new JFileChooser();
		fs.showOpenDialog(null);
		final File file = fs.getSelectedFile();
		if (file != null && !file.isFile()) {
			try {
				GeneralUtility.readFile(file);
			} catch (final IOException e) {
				JKExceptionUtil.handle(e);
			}
		}
	}

	private void handleShowAddDialog() {
		this.pnlDetail.setMode(DynDaoMode.ADD);
		SwingUtility.showPanelInDialog(this.pnlDetail, "ADD_RECORD");
		this.queryTable.reloadData();
	}

	/**
	 * Init UI
	 */
	private void init() {
		initTable();
		setLayout(new BorderLayout());
		setBorder(SwingUtility.createTitledBorder(this.foriegnKeyFieldMeta.getParentTable().getCaption()));
		// add(getButtonsPanel(),getButtonsPanelLocation());
		if (!isShowDaoPanelInDialog()) {
			add(this.pnlDetail, getDaoPanelLocation());
		}

		add(getCenterPanel(), BorderLayout.CENTER);
		this.queryTable.addDaoRecordListener(new RecordActionAdapter() {
			@Override
			public void recordSelected(final String recordId) {
				handleFind(recordId);
			}
		});
		this.pnlDetail.addDynDaoActionListener(new DynDaoActionAdapter() {
			@Override
			public void afterAddRecord(final Record record) {
				reloadTableData();
				DetailOneToManyPanel.this.pnlDetail.requestFocus();
			}

			@Override
			public void afterDeleteRecord(final Record record) {
				reloadTableData();
			}

			@Override
			public void afterResetComponents() {
				// we re-set the value here again because the DynPanel are
				// clearing all
				// the fields from its old values , so we need a way to reset it
				// again
				DetailOneToManyPanel.this.pnlDetail.setComponentValue(DetailOneToManyPanel.this.foriegnKeyFieldMeta.getName(),
						DetailOneToManyPanel.this.masterIdValue);
			}

			@Override
			public void afterUpdateRecord(final Record record) {
				reloadTableData();
			}
		});
		this.btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleDelete();
			}
		});
		this.btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleShowAddDialog();
			}
		});

	}

	/**
	 *
	 */
	private void initTable() {
		this.queryTable.setAllowFiltering(true);
		this.queryTable.setShowFilterButtons(false);
		this.queryTable.setShowSortingPanel(false);
	}

	protected boolean isShowDaoPanelInDialog() {
		return getDaoPanelLocation() == null;
	}

	/**
	 * Reload the table data only
	 */
	public void reloadTableData() {
		final int selectedRecord = this.queryTable.getSelectedRow();
		String staticWhere = getDetailTableMeta().getTableName() + "." + this.foriegnKeyFieldMeta.getName() + "=" + this.masterIdValue;
		if (this.extraWhereCondition != null) {
			staticWhere += " AND " + this.extraWhereCondition;
		}
		this.queryTable.getModel().setStaticWhere(staticWhere);
		this.queryTable.setQuery(getDetailTableMeta().getReportSql());
		this.btnDelete.setVisible(this.queryTable.getModel().getRowCount() > 0 && getDetailTableMeta().isAllowDelete());
		this.queryTable.setSelectedRow(selectedRecord);
	}

	@Override
	public void resetComponents() throws JKDataAccessException {
		setMasterIdValue(null);
		// queryTable.setQuery("");
		// pnlDetail.resetComponents();
	}

	/**
	 * @param extraWhereCondition
	 *            the extraWhereCondition to set
	 */
	public void setExtraWhereCondition(final String extraWhereCondition) {
		this.extraWhereCondition = extraWhereCondition;
	}

	public void setFieldValue(final String name, final Object value) {
		getPnlDetail().setComponentValue(name, value);
	}

	/*
	 * This method should be called only from two places : 1- From master panel
	 * when master to command this panel show the detail info of that record 2-
	 * from resetComponent by passing null parameter , any other call should be
	 * very careful
	 */
	@Override
	public void setMasterIdValue(final Object masterIdValue) {
		if (masterIdValue == null || masterIdValue.toString().trim().equals("")) {
			this.masterIdValue = null;
			this.queryTable.setQuery("");
			this.pnlDetail.setMode(DynDaoMode.ADD);
			// is very important to call the following statement since setMode
			// will enable the fields
			// automatically
			setEnabled(false);
		} else {
			this.masterIdValue = masterIdValue;
			reloadTableData();
			this.pnlDetail.setComponentValue(this.foriegnKeyFieldMeta.getName(), masterIdValue);
			// this call wil insure that the buttons will be anabled
			setEnabled(true);
			// pnlDetail.setEnabled(true);
			// btnDelete.setEnabled(true);
			// btnAdd.setEnabled(true);
			// this will enable the mode if the table meta is allowed to add
			this.pnlDetail.setMode(DynDaoMode.ADD);

		}
	}

	/*
	 *
	 */
	@Override
	public void setMode(final DynDaoMode mode) {
		this.pnlDetail.setMode(mode);
	}

}
