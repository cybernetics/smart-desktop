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
	private static final long serialVersionUID = 1L;

	// private static final String CONTROL_A = "control A";

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
	public DynMasterDetailCRUDLPanel(String tableName) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	/**
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	public DynMasterDetailCRUDLPanel(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		initPanels(tableMeta);
	}

	/**
	 * 
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	protected void initPanels(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this.tableMeta = tableMeta;
		getTitle();
		table = new QueryJTable(tableMeta);
		table.setAllowPrinting(true);
		pnlMasterDetail = createMasterDetailPanel(tableMeta);
		pnlMasterDetail.setRecordTraversePolicy(table);
		init();
		checkCountExceeded(false);
	}

	/**
	 * @param tableMeta
	 * @return
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	protected AbstractMasterDetail createMasterDetailPanel(TableMeta tableMeta) throws DaoException, UIOPanelCreationException {
		AbstractMasterDetail panel;
		panel = MasterPanelFactory.createMasterPanel(tableMeta);
		panel.addMasterDaoActionListener(new MasterPanelListener());
		panel.addDetailDaoActionListener(new DetailPanelListener());
		return panel;
	}

	/**
	 * 
	 * 
	 */
	protected void init() {
		// SwingUtility.setHotKeyFoButton(btnAdd, CONTROL_A, CONTROL_A);
		btnAdd.setShortcut("F1", "F1");
		// btnAdd.setShortcut("Ctrl A", false);

		btnAdd.setIcon("db_add.png");
		btnClose.setIcon("fileclose.png");
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(getNorthButtonsPanel(), BorderLayout.NORTH);
		add(table, BorderLayout.CENTER);
		add(getSouthPanel(), BorderLayout.SOUTH);

		table.addDaoRecordListener(new RecordActionAdapter() {
			public void recordSelected(String recordId) {
				handleEdit(recordId);
			}
		});
		table.getTable().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_E && e.isControlDown()) {
					handleEditInTabular();
				}
			}
		});

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAdd();
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtility.closePanel(DynMasterDetailCRUDLPanel.this);
			}
		});
		btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleDeleteSelected();
			}
		});
	}

	// ////////////////////////////////////////////////////////////
	protected void handleDeleteSelected() {
		bulkDelete = true;
		try {
			int[] ids = table.getSelectedIdsAsInteger();
			if (SwingUtility.showConfirmationDialog("YOU_ARE_ABOUT_TO_DELETE_THE_SELECTED_RECORDS,ARE_YOU_SURE?")) {
				for (int i : ids) {
					try {
						pnlMasterDetail.handleFind(i);
						pnlMasterDetail.getMasterPanel().getPnlDao().handleDeleteEvent();
					} catch (DaoException e) {
						SwingUtility.showUserErrorDialog(Lables.get("UNABLE_TO_DELETE_RECORD") + " : (" + getSummaryValue() + ")\n" + e.getMessage(),
								false);
					}
				}
			}
		} finally {
			bulkDelete = false;
			table.reloadData();
		}
	}

	/**
	 * @return
	 */
	private String getSummaryValue() {
		try {
			return pnlMasterDetail.getMasterPanel().getRecord().getSummaryValue();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
			// unreachable
			return null;
		}
	}

	// ////////////////////////////////////////////////////////////
	public JKPanel getSouthPanel() {
		if (pnlSouthPanel == null) {
			pnlSouthPanel = new JKPanel(new FlowLayout(FlowLayout.LEADING));
			if (getTableMeta().isAllowDeleteAll()) {
				btnDelete.setShortcut("F3", "F3");
				btnDelete.setIcon("delete_commons_system_icons.gif");
				btnDelete.setShowProgress(true);
				pnlSouthPanel.add(btnDelete);
			}
		}
		return pnlSouthPanel;
	}

	protected void handleEditInTabular() {
		try {
			DynTabular tbl = new DynTabular(getTableMeta());
			tbl.setAllowSave(true);
			tbl.setAllowCreate(true);
			tbl.setAllowDelete(true);

			SwingUtility.getDefaultMainFrame().handleShowPanel(tbl);
			// SwingUtility.showPanelInDialog(tbl, "EDIT");
			// this.queryTable.reloadData();
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}

	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getNorthButtonsPanel() {
		JKPanel pnlButtons = new JKPanel();
		if (tableMeta.isAllowAdd()) {
			pnlButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
			pnlButtons.add(btnAdd);
		}
		return pnlButtons;
	}

	/**
	 * 
	 * @param recordId
	 */
	protected void handleEdit(String recordId) {
		try {
			pnlMasterDetail.handleFind(recordId);
			if (!SwingUtility.isVisibleOnScreen(pnlMasterDetail)) {
				//to avoid re-view the panel
				showDynPanel(true);
			}
		} catch (DaoException e) {
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

	/**
	 * 
	 * 
	 */
	private void handleAdd() {
		try {
			pnlMasterDetail.setMode(DynDaoMode.ADD);
			showDynPanel(true);
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @param show
	 */
	void showDynPanel(boolean show) {
		if (show) {
			// pnlMasterDetail.getMasterPanel().setAllowClear(!isCountExceeded(false));
			SwingUtility.showPanelInDialog(pnlMasterDetail, title);
		} else {
			if (pnlMasterDetail.getRootPane() != null) {
				if (pnlMasterDetail.getRootPane().getParent() instanceof JDialog) {
					((JDialog) pnlMasterDetail.getRootPane().getParent()).dispose();
				}
			}
		}
	}

	/**
	 * 
	 * @param listener
	 */
	public void addMasterDaoActionListener(DynDaoActionListener listener) {
		pnlMasterDetail.addMasterDaoActionListener(listener);
	}

	/**
	 * 
	 * @return
	 */
	public Record getRecord() {
		return pnlMasterDetail.getMasterPanel().getRecord();
	}

	/**
	 * 
	 * 
	 */
	void checkCountExceeded(boolean reloadData) {
		boolean showAdd = !isCountExceeded(reloadData);
		btnAdd.setVisible(showAdd);
		pnlMasterDetail.getMasterPanel().setAllowAdd(showAdd);
	}

	/**
	 * 
	 * @return
	 */
	private boolean isCountExceeded(boolean reloadData) {
		if (reloadData) {
			table.reloadData();
		}
		return tableMeta.getMaxRecordsCount() != 0 && table.getTable().getRowCount() >= tableMeta.getMaxRecordsCount();
	}

	/**
	 * @return the tableMeta
	 */
	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	/**
	 * @param tableMeta
	 *            the tableMeta to set
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * @throws UIOPanelCreationException
	 */
	public void setTableMeta(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		initPanels(tableMeta);
	}

	/**
	 * 
	 */
	private void getTitle() {
		if (SwingUtility.isLeftOrientation()) {
			title = Lables.get(this.tableMeta.getCaption()) + " " + Lables.get("MANAGEMENT");
		} else {
			title = Lables.get("MANAGEMENT") + " " + Lables.get(this.tableMeta.getCaption());
		}
	}

	/**
	 * @return the pnlDao
	 */
	public AbstractMasterDetail getMasterDetailPanel() {
		return this.pnlMasterDetail;
	}

	/*
	 *
	 */
	public QueryJTable getTable() {
		return table;
	}

	@Override
	public void resetComponents() throws DaoException {
		pnlMasterDetail.resetComponents();
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
	public int getIdFieldValueAsInteger() {
		return getMasterDetailPanel().getIdFieldValueAsInteger();
	}

	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	public class MasterPanelListener extends DynDaoActionAdapter {

		@Override
		public void afterAddRecord(Record record) {
			if (isCountExceeded(true)) {
				showDynPanel(false);
			}
			// isCountExceeded method will reload the data , so no need to
			// reload again
			table.setSelectedRowByRecordId(record.getIdValueAsInteger(), false);
		}

		@Override
		public void afterClosePanel() {
			handleClosePanel();
		}

		@Override
		public void afterDeleteRecord(Record record) {
			if (!bulkDelete) {
				showDynPanel(false);
				// this to make the add visible again if was hidden because if
				// max
				// count exceeded rule
				checkCountExceeded(true);
			}
		}

		@Override
		public void onDaoException(Record recod, DaoException ex) {
			// check for record before use , because it maybe null
			if (ex instanceof DuplicateDataException) {
				String recordId = ((DuplicateDataException) ex).getIdValue();
				table.setSelectedRowByRecordId(new Integer(recordId), false);
				pnlMasterDetail.requestFocus();
			}
		}

		@Override
		public void afterUpdateRecord(Record record) {
			table.reloadData();
		}
	}

	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	public class DetailPanelListener extends DynDaoActionAdapter {

		@Override
		public void afterAddRecord(Record record) {
			table.reloadData();
		}

		@Override
		public void afterDeleteRecord(Record record) {
			table.reloadData();
		}

		@Override
		public void afterUpdateRecord(Record record) {
			table.reloadData();
		}

		@Override
		public void beforeAddRecord(Record record) {
			table.reloadData();
		}
	}

}
