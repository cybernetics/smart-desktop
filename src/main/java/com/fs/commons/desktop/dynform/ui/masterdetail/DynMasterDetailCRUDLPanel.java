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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JDialog;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.constraints.exceptions.DuplicateDataException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.event.RecordActionAdapter;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.MasterPanelFactory;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.dynform.ui.tabular.DynTabular;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;

public class DynMasterDetailCRUDLPanel extends JKMainPanel {
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	public class DetailPanelListener extends DynDaoActionAdapter {

		@Override
		public void afterAddRecord(final Record record) {
			DynMasterDetailCRUDLPanel.this.table.reloadData();
		}

		@Override
		public void afterDeleteRecord(final Record record) {
			DynMasterDetailCRUDLPanel.this.table.reloadData();
		}

		@Override
		public void afterUpdateRecord(final Record record) {
			DynMasterDetailCRUDLPanel.this.table.reloadData();
		}

		@Override
		public void beforeAddRecord(final Record record) {
			DynMasterDetailCRUDLPanel.this.table.reloadData();
		}
	}

	// private static final String CONTROL_A = "control A";

	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	public class MasterPanelListener extends DynDaoActionAdapter {

		@Override
		public void afterAddRecord(final Record record) {
			if (isCountExceeded(true)) {
				showDynPanel(false);
			}
			// isCountExceeded method will reload the data , so no need to
			// reload again
			DynMasterDetailCRUDLPanel.this.table.setSelectedRowByRecordId(record.getIdValueAsInteger(), false);
		}

		@Override
		public void afterClosePanel() {
			handleClosePanel();
		}

		@Override
		public void afterDeleteRecord(final Record record) {
			if (!DynMasterDetailCRUDLPanel.this.bulkDelete) {
				showDynPanel(false);
				// this to make the add visible again if was hidden because if
				// max
				// count exceeded rule
				checkCountExceeded(true);
			}
		}

		@Override
		public void afterUpdateRecord(final Record record) {
			DynMasterDetailCRUDLPanel.this.table.reloadData();
		}

		@Override
		public void onDaoException(final Record recod, final DaoException ex) {
			// check for record before use , because it maybe null
			if (ex instanceof DuplicateDataException) {
				final String recordId = ((DuplicateDataException) ex).getIdValue();
				DynMasterDetailCRUDLPanel.this.table.setSelectedRowByRecordId(new Integer(recordId), false);
				DynMasterDetailCRUDLPanel.this.pnlMasterDetail.requestFocus();
			}
		}
	}

	// ///////////////////////////////////////////////////////////////
	private static final long serialVersionUID = 1L;

	TableMeta tableMeta;

	QueryJTable table;
	AbstractMasterDetail pnlMasterDetail;

	JKButton btnAdd = new JKButton("ADD_RECORD");

	JKButton btnDelete = new JKButton("DELETE_SELECTED");

	JKButton btnClose = new JKButton("CLOSE");

	private String title;

	private boolean bulkDelete;

	private JKPanel pnlSouthPanel;

	// boolean singleRecordOnly;
	/**
	 *
	 */
	public DynMasterDetailCRUDLPanel() {
	}

	/**
	 * @throws UIOPanelCreationException
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 *
	 */
	public DynMasterDetailCRUDLPanel(final String tableName) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	/**
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	public DynMasterDetailCRUDLPanel(final TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		initPanels(tableMeta);
	}

	/**
	 *
	 * @param listener
	 */
	public void addMasterDaoActionListener(final DynDaoActionListener listener) {
		this.pnlMasterDetail.addMasterDaoActionListener(listener);
	}

	/**
	 *
	 *
	 */
	void checkCountExceeded(final boolean reloadData) {
		final boolean showAdd = !isCountExceeded(reloadData);
		this.btnAdd.setVisible(showAdd);
		this.pnlMasterDetail.getMasterPanel().setAllowAdd(showAdd);
	}

	/**
	 * @param tableMeta
	 * @return
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	protected AbstractMasterDetail createMasterDetailPanel(final TableMeta tableMeta) throws DaoException, UIOPanelCreationException {
		AbstractMasterDetail panel;
		panel = MasterPanelFactory.createMasterPanel(tableMeta);
		panel.addMasterDaoActionListener(new MasterPanelListener());
		panel.addDetailDaoActionListener(new DetailPanelListener());
		return panel;
	}

	/**
	 *
	 * @return
	 */
	public int getIdFieldValueAsInteger() {
		return getMasterDetailPanel().getIdFieldValueAsInteger();
	}

	/**
	 * @return the pnlDao
	 */
	public AbstractMasterDetail getMasterDetailPanel() {
		return this.pnlMasterDetail;
	}

	/**
	 *
	 * @return
	 */
	public DynDaoMode getMode() {
		return getMasterDetailPanel().getMode();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getNorthButtonsPanel() {
		final JKPanel pnlButtons = new JKPanel();
		if (this.tableMeta.isAllowAdd()) {
			pnlButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
			pnlButtons.add(this.btnAdd);
		}
		return pnlButtons;
	}

	/**
	 *
	 * @return
	 */
	public Record getRecord() {
		return this.pnlMasterDetail.getMasterPanel().getRecord();
	}

	// ////////////////////////////////////////////////////////////
	public JKPanel getSouthPanel() {
		if (this.pnlSouthPanel == null) {
			this.pnlSouthPanel = new JKPanel(new FlowLayout(FlowLayout.LEADING));
			if (getTableMeta().isAllowDeleteAll()) {
				this.btnDelete.setShortcut("F3", "F3");
				this.btnDelete.setIcon("delete_commons_system_icons.gif");
				this.btnDelete.setShowProgress(true);
				this.pnlSouthPanel.add(this.btnDelete);
			}
		}
		return this.pnlSouthPanel;
	}

	/**
	 * @return
	 */
	private String getSummaryValue() {
		try {
			return this.pnlMasterDetail.getMasterPanel().getRecord().getSummaryValue();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
			// unreachable
			return null;
		}
	}

	/*
	 *
	 */
	public QueryJTable getTable() {
		return this.table;
	}

	/**
	 * @return the tableMeta
	 */
	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	/**
	 *
	 */
	private void getTitle() {
		if (SwingUtility.isLeftOrientation()) {
			this.title = Lables.get(this.tableMeta.getCaption()) + " " + Lables.get("MANAGEMENT");
		} else {
			this.title = Lables.get("MANAGEMENT") + " " + Lables.get(this.tableMeta.getCaption());
		}
	}

	/**
	 *
	 *
	 */
	private void handleAdd() {
		try {
			this.pnlMasterDetail.setMode(DynDaoMode.ADD);
			showDynPanel(true);
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 *
	 *
	 */
	private void handleClosePanel() {
		showDynPanel(false);
	}

	// ////////////////////////////////////////////////////////////
	protected void handleDeleteSelected() {
		this.bulkDelete = true;
		try {
			final int[] ids = this.table.getSelectedIdsAsInteger();
			if (SwingUtility.showConfirmationDialog("YOU_ARE_ABOUT_TO_DELETE_THE_SELECTED_RECORDS,ARE_YOU_SURE?")) {
				for (final int i : ids) {
					try {
						this.pnlMasterDetail.handleFind(i);
						this.pnlMasterDetail.getMasterPanel().getPnlDao().handleDeleteEvent();
					} catch (final DaoException e) {
						SwingUtility.showUserErrorDialog(Lables.get("UNABLE_TO_DELETE_RECORD") + " : (" + getSummaryValue() + ")\n" + e.getMessage(),
								false);
					}
				}
			}
		} finally {
			this.bulkDelete = false;
			this.table.reloadData();
		}
	}

	/**
	 *
	 * @param recordId
	 */
	protected void handleEdit(final String recordId) {
		try {
			this.pnlMasterDetail.handleFind(recordId);
			if (!SwingUtility.isVisibleOnScreen(this.pnlMasterDetail)) {
				// to avoid re-view the panel
				showDynPanel(true);
			}
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}

	}

	protected void handleEditInTabular() {
		try {
			final DynTabular tbl = new DynTabular(getTableMeta());
			tbl.setAllowSave(true);
			tbl.setAllowCreate(true);
			tbl.setAllowDelete(true);

			SwingUtility.getDefaultMainFrame().handleShowPanel(tbl);
			// SwingUtility.showPanelInDialog(tbl, "EDIT");
			// this.queryTable.reloadData();
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}

	}

	/**
	 *
	 *
	 */
	protected void init() {
		// SwingUtility.setHotKeyFoButton(btnAdd, CONTROL_A, CONTROL_A);
		this.btnAdd.setShortcut("F1", "F1");
		// btnAdd.setShortcut("Ctrl A", false);

		this.btnAdd.setIcon("db_add.png");
		this.btnClose.setIcon("fileclose.png");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getNorthButtonsPanel(), BorderLayout.NORTH);
		add(this.table, BorderLayout.CENTER);
		add(getSouthPanel(), BorderLayout.SOUTH);

		this.table.addDaoRecordListener(new RecordActionAdapter() {
			@Override
			public void recordSelected(final String recordId) {
				handleEdit(recordId);
			}
		});
		this.table.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_E && e.isControlDown()) {
					handleEditInTabular();
				}
			}
		});

		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAdd();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SwingUtility.closePanel(DynMasterDetailCRUDLPanel.this);
			}
		});
		this.btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleDeleteSelected();
			}
		});
	}

	/**
	 *
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	protected void initPanels(final TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this.tableMeta = tableMeta;
		getTitle();
		this.table = new QueryJTable(tableMeta);
		this.table.setAllowPrinting(true);
		this.pnlMasterDetail = createMasterDetailPanel(tableMeta);
		this.pnlMasterDetail.setRecordTraversePolicy(this.table);
		init();
		checkCountExceeded(false);
	}

	/**
	 *
	 * @return
	 */
	private boolean isCountExceeded(final boolean reloadData) {
		if (reloadData) {
			this.table.reloadData();
		}
		return this.tableMeta.getMaxRecordsCount() != 0 && this.table.getTable().getRowCount() >= this.tableMeta.getMaxRecordsCount();
	}

	@Override
	public void resetComponents() throws DaoException {
		this.pnlMasterDetail.resetComponents();
	}

	/**
	 * @param tableMeta
	 *            the tableMeta to set
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * @throws UIOPanelCreationException
	 */
	public void setTableMeta(final TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		initPanels(tableMeta);
	}

	/**
	 *
	 * @param show
	 */
	void showDynPanel(final boolean show) {
		if (show) {
			// pnlMasterDetail.getMasterPanel().setAllowClear(!isCountExceeded(false));
			SwingUtility.showPanelInDialog(this.pnlMasterDetail, this.title);
		} else {
			if (this.pnlMasterDetail.getRootPane() != null) {
				if (this.pnlMasterDetail.getRootPane().getParent() instanceof JDialog) {
					((JDialog) this.pnlMasterDetail.getRootPane().getParent()).dispose();
				}
			}
		}
	}

}
