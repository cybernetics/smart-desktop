/**
 * 
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

import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.event.RecordActionAdapter;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.dynform.ui.masterdetail.DetailPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

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
	 * @return the extraWhereCondition
	 */
	public String getExtraWhereCondition() {
		return extraWhereCondition;
	}

	/**
	 * @param extraWhereCondition
	 *            the extraWhereCondition to set
	 */
	public void setExtraWhereCondition(String extraWhereCondition) {
		this.extraWhereCondition = extraWhereCondition;
	}

	/**
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * 
	 */
	public DetailOneToManyPanel(ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws TableMetaNotFoundException, DaoException {
		// fix me
		this.masterTableMeta = foriegnKeyFieldMeta.getReferenceTableMeta();
		this.foriegnKeyFieldMeta = foriegnKeyFieldMeta;
		this.foriegnKeyFieldMeta.setAllowUpdate(false);
		this.foriegnKeyFieldMeta.setEnabled(false);
		queryTable.getModel().setDataSource(foriegnKeyFieldMeta.getParentTable().getDataSource());
		// queryTable.setShowRecordsCount(true);
		pnlDetail = createDetailPanel();
		init();
		resetComponents();
	}

	public DetailOneToManyPanel(String detailTableName, String foriegKeyFieldName) throws TableMetaNotFoundException, DaoException {
		this((ForiegnKeyFieldMeta) AbstractTableMetaFactory.getTableMeta(detailTableName).getField(foriegKeyFieldName));
	}

	/**
	 * p
	 * 
	 * @return
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 */
	protected DynDaoPanel createDetailPanel() throws TableMetaNotFoundException, DaoException {
		DynDaoPanel pnlDetail = new DynDaoPanel(getDetailTableMeta());
		pnlDetail.setAllowClose(false);
		pnlDetail.setAllowDuplicate(true);
		return pnlDetail;
	}

	public void addDaoListener(DynDaoActionListener listener) {
		pnlDetail.addDynDaoActionListener(listener);
	}

	/**
	 * Init UI
	 */
	private void init() {
		initTable();
		setLayout(new BorderLayout());
		setBorder(SwingUtility.createTitledBorder(foriegnKeyFieldMeta.getParentTable().getCaption()));
		// add(getButtonsPanel(),getButtonsPanelLocation());
		if (!isShowDaoPanelInDialog()) {
			add(pnlDetail, getDaoPanelLocation());
		}

		add(getCenterPanel(), BorderLayout.CENTER);
		queryTable.addDaoRecordListener(new RecordActionAdapter() {
			@Override
			public void recordSelected(String recordId) {
				handleFind(recordId);
			}
		});
		pnlDetail.addDynDaoActionListener(new DynDaoActionAdapter() {
			@Override
			public void afterResetComponents() {
				// we re-set the value here again because the DynPanel are
				// clearing all
				// the fields from its old values , so we need a way to reset it
				// again
				pnlDetail.setComponentValue(foriegnKeyFieldMeta.getName(), masterIdValue);
			}

			@Override
			public void afterAddRecord(Record record) {
				reloadTableData();
				pnlDetail.requestFocus();
			}

			@Override
			public void afterUpdateRecord(Record record) {
				reloadTableData();
			}

			@Override
			public void afterDeleteRecord(Record record) {
				reloadTableData();
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleDelete();
			}
		});
		btnAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleShowAddDialog();
			}
		});

	}

	/**
	 * 
	 */
	protected void handleImport() {
		JFileChooser fs = new JFileChooser();
		fs.showOpenDialog(null);
		File file = fs.getSelectedFile();
		if (file != null && !file.isFile()) {
			try {
				GeneralUtility.readFile(file);
			} catch (IOException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	private Component getCenterPanel() {
		JKPanel pnl = new JKPanel(new BorderLayout());
		queryTable.setShowRecordsCount(true);
		queryTable.setPreferredSize(new Dimension(700, 300));
		pnl.add(getButtonsPanel(), getButtonsPanelLocation());
		pnl.add(queryTable, BorderLayout.CENTER);
		return pnl;
	}

	/**
	 * 
	 */
	protected void handleDelete() {
		int[] ids = queryTable.getSelectedIdsAsInteger();
		if (ids.length == 0) {
			SwingUtility.showUserErrorDialog("PLEASE_SELECT_RECORDS_FROM_TABLE");
		}
		if (SwingUtility.showConfirmationDialog("YOU_ARE_ABOUT_TO_DELETE_ALL_SELECTED_RECORDS,ARE_YOU_SURE?")) {
			for (Integer id : ids) {
				try {
					pnlDetail.handleFindRecord(id);
					pnlDetail.handleDelete(false);
				} catch (DaoException e) {
					ExceptionUtil.handleException(e);
				}
			}
		}
		reloadTableData();
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		JKPanel pnl = new JKPanel(new FlowLayout(FlowLayout.LEADING));
		if (isShowDaoPanelInDialog()) {
			pnl.add(btnAdd);
		}
		pnl.add(btnDelete);
		btnDelete.setShowProgress(true);
		btnDelete.setIcon("db_remove.png");
		btnAdd.setIcon("add_commons_system_icon.gif");
		return pnl;
	}

	protected boolean isShowDaoPanelInDialog() {
		return getDaoPanelLocation() == null;
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

	/**
	 * 
	 * @return
	 */
	protected String getDaoPanelLocation() {
		return BorderLayout.NORTH;
	}

	/**
	 * 
	 */
	private void initTable() {
		queryTable.setAllowFiltering(true);
		queryTable.setShowFilterButtons(false);
		queryTable.setShowSortingPanel(false);
	}

	/*
	 * This method should be called only from two places : 1- From master panel
	 * when master to command this panel show the detail info of that record 2-
	 * from resetComponent by passing null parameter , any other call should be
	 * very careful
	 */
	public void setMasterIdValue(Object masterIdValue) {
		if (masterIdValue == null || masterIdValue.toString().trim().equals("")) {
			this.masterIdValue = null;
			this.queryTable.setQuery("");
			pnlDetail.setMode(DynDaoMode.ADD);
			// is very important to call the following statement since setMode
			// will enable the fields
			// automatically
			setEnabled(false);
		} else {
			this.masterIdValue = masterIdValue;
			reloadTableData();
			pnlDetail.setComponentValue(foriegnKeyFieldMeta.getName(), masterIdValue);
			// this call wil insure that the buttons will be anabled
			setEnabled(true);
			// pnlDetail.setEnabled(true);
			// btnDelete.setEnabled(true);
			// btnAdd.setEnabled(true);
			// this will enable the mode if the table meta is allowed to add
			pnlDetail.setMode(DynDaoMode.ADD);

		}
	}

	/**
	 * Reload the table data only
	 */
	public void reloadTableData() {
		int selectedRecord = queryTable.getSelectedRow();
		String staticWhere = getDetailTableMeta().getTableName() + "." + foriegnKeyFieldMeta.getName() + "=" + masterIdValue;
		if (extraWhereCondition != null) {
			staticWhere += " AND " + extraWhereCondition;
		}
		queryTable.getModel().setStaticWhere(staticWhere);
		queryTable.setQuery(getDetailTableMeta().getReportSql());
		btnDelete.setVisible(queryTable.getModel().getRowCount() > 0 && getDetailTableMeta().isAllowDelete());
		queryTable.setSelectedRow(selectedRecord);
	}

	/**
	 * @param recordId
	 */
	public void handleFind(Object recordId) {
		try {
			pnlDetail.handleFindRecord(recordId);
			if (isShowDaoPanelInDialog()) {
				SwingUtility.showPanelInDialog(pnlDetail, "EDIT_RECORD");
			}
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	@Override
	public void resetComponents() throws DaoException {
		setMasterIdValue(null);
		// queryTable.setQuery("");
		// pnlDetail.resetComponents();
	}

	/**
	 * 
	 */
	public void addDynDaoActionListener(DynDaoActionListener listener) {
		pnlDetail.addDynDaoActionListener(listener);
	}

	/*
	 * 
	 */
	public void setMode(DynDaoMode mode) {
		pnlDetail.setMode(mode);
	}

	/**
	 * 
	 * @return
	 */
	public ForiegnKeyFieldMeta getForiegnKeyFieldMeta() {
		return foriegnKeyFieldMeta;
	}

	/**
	 * 
	 * @return
	 */
	public TableMeta getMasterTableMeta() {
		return masterTableMeta;
	}

	/**
	 * 
	 * @return
	 */
	public TableMeta getDetailTableMeta() {
		return foriegnKeyFieldMeta.getParentTable();
	}

	/**
	 * 
	 * @return
	 */
	public DynDaoPanel getPnlDetail() {
		return pnlDetail;
	}

	/**
	 * 
	 * @return
	 */
	public Object getMasterIdValue() {
		return masterIdValue;
	}

	/**
	 * 
	 * @return
	 */
	public QueryJTable getQueryTable() {
		return queryTable;
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

	private void handleShowAddDialog() {
		pnlDetail.setMode(DynDaoMode.ADD);
		SwingUtility.showPanelInDialog(pnlDetail, "ADD_RECORD");
		this.queryTable.reloadData();
	}

	public void setFieldValue(String name, Object value) {
		getPnlDetail().setComponentValue(name,value);
	}

}
