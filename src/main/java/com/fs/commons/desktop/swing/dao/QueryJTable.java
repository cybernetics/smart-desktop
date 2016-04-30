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
package com.fs.commons.desktop.swing.dao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.TableModel;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.event.RecordActionListener;
import com.fs.commons.dao.paging.PagingException;
import com.fs.commons.desktop.dynform.ui.RecordTraversePolicy;
import com.fs.commons.desktop.swin.ConversionUtil;
import com.fs.commons.desktop.swing.PrintUtil;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKRadioButton;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKTable;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.listeners.CellFocusListener;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryTableModel.OrderDirection;
import com.fs.commons.desktop.swing.listener.RecordSelectionListener;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExcelUtil;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

//////////////////////////////////////////////////////////////////////////////
public class QueryJTable extends JKPanel implements FilterListener, RecordTraversePolicy {

	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		final QueryJTable t = new QueryJTable("SELECT * FROM GEN_NATIONAL_NUMBERS");
		t.setAllowFiltering(true);
		SwingUtility.testPanel(t);
	}

	QueryTableModel model;

	JKTable table = new JKTable();
	// contains the record count and the user customised lables
	JKPanel pnlSouthLables;
	JKPanel pnlRecordsCount;

	JKTextField txtCount = new JKTextField(5);

	// TableFilterPanel[] filters;
	Hashtable<String, TableFilterPanel> filters = new Hashtable<String, TableFilterPanel>();

	private boolean allowFiltering = true;

	JKRadioButton btnAsc = new JKRadioButton(Lables.get("ASC"));

	// private boolean renderersSet;

	JKRadioButton btnDesc = new JKRadioButton(Lables.get("DESCENDING"));

	Vector<RecordSelectionListener> listeners = new Vector<RecordSelectionListener>();

	JKPanel pnlSouth = new JKPanel();
	// print components
	JKPanel pnlPrint;

	JKPanel pnlExcel;

	JKButton btnPrint = new JKButton("PRINT");

	JKButton btnSelectPrintFields = new JKButton("SELECT_PRINT_FIELDS");

	private boolean allowPrinting;

	private boolean allowExcelExport = false;

	private final JKButton btnExportToExcel = new JKButton("EXCEL_EXPORT");

	Box filtersBox = Box.createVerticalBox();

	private JKScrollPane pane;

	private int pageRowCount = 25;

	private JKPanel pnlSorting;

	private JKPanel pnlSouthButtons;

	private boolean showFilterButtons = true;
	// ////////////////////////
	// limit support
	JKPanel pnlPaging = new JKPanel();
	JKTextField txtAllRowsCount = new JKTextField(5, false);
	JKTextField txtPagesCount = new JKTextField(5, false);
	// JKTextField txtLimit = new JKTextField(new NumberDocument(4), 5);
	JKButton btnFirstPage = new JKButton("", "alt F");
	JKButton btnNextPage = new JKButton("", "alt N");
	JKButton btnPreviousePage = new JKButton("", "alt P");

	JKButton btnLastPage = new JKButton("", "alt L");

	private boolean allowPaging;

	private String dynamicReportTitle;

	public QueryJTable() {
		this("", "");
	}

	/**
	 *
	 * @param model
	 *            QueryTableModel
	 * @param title
	 *            String
	 * @param allowFiltering
	 *            boolean
	 */
	public QueryJTable(final QueryTableModel model, final String title, final boolean allowFiltering) {
		this.table.setModel(model);
		this.model = model;
		setTitle(title);
		init();
		refreshComponents(); // to avoid requesting the query again since it
		this.table.setPreferredScrollableViewportSize(new Dimension(600, 250));
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());

		setAllowFiltering(allowFiltering);
	}

	public QueryJTable(final String sql) {
		this(sql, "", true);
	}

	public QueryJTable(final String title, final DataSource resource, final String sql, final boolean allowFiltering) {
		this(new QueryTableModel(resource, sql), title, allowFiltering);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public QueryJTable(final String sql, final String title) {
		this(sql, title, true);
	}

	/**
	 *
	 * @param sql
	 *            String
	 * @param title
	 *            String
	 * @param allowFiltering
	 *            boolean
	 */
	public QueryJTable(final String sql, final String title, final boolean allowFiltering) {
		this(new QueryTableModel(sql), title, allowFiltering);
	}

	/**
	 *
	 * @param tableMeta
	 */
	public QueryJTable(final TableMeta tableMeta) {
		this(tableMeta, tableMeta.getTableId(), true);
	}

	public QueryJTable(final TableMeta tableMeta, final boolean allowFiltering) {
		this(tableMeta, tableMeta.getTableId(), allowFiltering);
	}

	public QueryJTable(final TableMeta tableMeta, final String title, final boolean showFilters) {
		this(title, tableMeta.getDataSource(), tableMeta.getReportSql(), showFilters);
		setShowIdColunm(!tableMeta.isAutoIncrementId());
		this.pageRowCount = tableMeta.getPageRowCount();
		final int[] filters = tableMeta.getFilters();
		for (final int filter : filters) {
			showFilterPanel(filter, isShowFilterButtons());
		}

	}

	/**
	 *
	 * @param btn
	 */
	public void addButtonToSouthButtonsPanel(final JKButton btn) {
		this.pnlSouthButtons.add(btn);
		this.pnlSouthButtons.validate();
		this.pnlSouthButtons.repaint();
	}

	public void addCellFocusListener(final CellFocusListener cellFocusListener) {
		this.table.addCellFocusListener(cellFocusListener);
	}

	/**
	 *
	 * @param columnName
	 * @param columnType
	 * @param displaySize
	 */
	public void addColunm(final String columnName, final int columnType, final int displaySize) {
		invalidate();
		repaint();
	}

	/**
	 *
	 * @param comp
	 *            JComponent
	 */
	public void addComponentToLablesPanel(final JComponent comp) {
		this.pnlSouthLables.add(comp);
		this.pnlSouthLables.validate();
		this.pnlSouthLables.repaint();
	}

	@Deprecated
	/**
	 * replaced with addRecordListener(new RecordSelectionListener() {
	 */
	public void addDaoRecordListener(final RecordActionListener recordActionListener) {
		addRecordListener(new RecordSelectionListener() {
			@Override
			public void recordSelected(final int recordId) {
				recordActionListener.recordSelected(recordId + "");
			}
		});
	}

	// /////////////////////////////////////////////////////////
	private void addEmptyRowIfNeeded() {
		if (this.table.getRowCount() == 0 && this.table.isEditable()) {
			addRow();
		}
	}

	@Override
	public synchronized void addKeyListener(final KeyListener l) {
		if (this.table != null) {
			this.table.addKeyListener(l);
		} else {
			// if table==null this indicate that event has been set from super
			// class before
			// calling the constructor of this class
			super.addKeyListener(l);
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////

	@Override
	public synchronized void addMouseListener(final MouseListener l) {
		if (this.table != null) {
			// if table==null this indicate that event has been set from super
			// class before
			// calling the constructor of this class
			this.table.addMouseListener(l);
		} else {
			super.addMouseListener(l);
		}
	}

	/**
	 * @param listener
	 *            DaoRecordListener
	 */
	public void addRecordListener(final RecordSelectionListener listener) {
		this.listeners.add(listener);
	}

	public void addRow() {
		this.table.addRow();
	}

	// private void checkShowPaging() {
	// pnlPaging.setVisible(allowPaging && getModel().getRowCount() > 0 &&
	// getModel().getRowCount() == getModel().getLimit());
	// }

	/**
	 *
	 * @param enable
	 *            boolean
	 */
	public void allowMultipleSelections(final boolean enable) {
		this.table.setAlowMutipleSelection(enable);
	}

	/**
	 * buildFilterPanels
	 *
	 * @return Box
	 */
	private Box buildFilterPanels() {
		this.filters.clear();
		this.filtersBox.removeAll();
		if (this.allowFiltering) {
			this.filtersBox.setBorder(SwingUtility.createTitledBorder(""));
			// filters = new TableFilterPanel[model.getColumnCount()];
			for (int i = 0; i < this.model.getColumnCount(); i++) {
				final TableFilterPanel filter = new TableFilterPanel(this.model, this.model.getActualColumnName(i), this);
				this.filtersBox.add(filter);
				this.filters.put(this.model.getColumnName(i), filter);
			}
			// if (allowFiltering) {
			this.filtersBox.add(createOrderPanel());
		}
		this.filtersBox.revalidate();
		// }
		return this.filtersBox;
	}

	// /**
	// *
	// */
	// public void resetPane() {
	// SwingUtilities.invokeLater(new Runnable(){
	// @Override
	// public void run() {
	// pane.getHorizontalScrollBar().setValue(0);
	// }
	// });
	// }

	public void checkEmptySelection() throws ValidationException {
		if (getSelectedIdAsInteger() == -1) {
			requestFocus();
			throw new ValidationException("PLEASE_SELECT_RECORD");
		}
	}

	public void checkSelectMoreThanOne() throws ValidationException {
		checkEmptySelection();
		final String[] selectedIds = getSelectedIds();
		if (selectedIds.length > 1) {
			requestFocus();
			throw new ValidationException("PLEASE_SELECT_ONE_RECORD");
		}

	}

	/**
	 *
	 *
	 */
	public void clearTable() {
		this.table.resetRecords();
	}

	public void clearTableListeners() {
		this.listeners.clear();
	}

	/**
	 *
	 * @return JKPanel
	 */
	private JKPanel createOrderPanel() {
		if (this.pnlSorting == null) {
			this.pnlSorting = new JKPanel(new FlowLayout(FlowLayout.LEADING));
			this.pnlSorting.setVisible(false);
			final JKPanel pnl1 = new JKPanel();
			pnl1.setBorder(BorderFactory.createEtchedBorder());
			final JKLabel lblSortDir = new JKLabel(Lables.get("ORDER"));
			lblSortDir.setIcon("sort_az.png");

			pnl1.add(lblSortDir);
			// SwingUtility.setBoldFont(lblSortDir);

			this.btnAsc.setFocusable(false);
			this.btnDesc.setFocusable(false);
			pnl1.add(this.btnAsc);
			pnl1.add(this.btnDesc);

			final ButtonGroup group = new ButtonGroup();

			group.add(this.btnAsc);
			group.add(this.btnDesc);

			this.btnAsc.setSelected(true);

			this.pnlSorting.add(pnl1);

		}
		return this.pnlSorting;
	}

	public void deleteRow(final int row) {
		this.table.deleteRow(row);
	}

	/**
	 *
	 * @param panel
	 *            TableFilterPanel
	 */
	@Override
	public void filterUpdated(final TableFilterPanel panel) {
		this.model.setExtraSQLCondition(panel.getFilterColunmName(), panel.getConditionString());
		reloadData();
		this.table.requestFocus();
	}

	/**
	 * @param RecordId
	 *            String
	 */
	protected void fireRecordSelectedEvent(final Object recordId) {
		for (int i = 0; i < this.listeners.size(); i++) {
			int value = -1;
			if (recordId != null) {
				value = new Integer(recordId.toString());
			}

			this.listeners.get(i).recordSelected(value);
		}
	}

	// /**
	// * setTableColunmsRenderer
	// */
	// private void setTableColunmsRenderer() {
	// table.setRenderers();
	// }

	public int[] getAllRecordIds() {
		return getModel().getAllRecordIds();
	}

	// ////////////////////////////////////////////////////////////////////////
	public int getColumnCount() {
		return this.table.getColumnCount();
	}

	public double getColunmSum(final int col) {
		return this.table.getColunmSum(col);
	}

	// ////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getData() {
		return this.table.getData();
	}

	public Vector<FSTableRecord> getDeletedRecords() {
		return this.table.getDeletedRecords();
	}

	// ////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getDeletedRows() {
		return this.table.getDeletedRows();
	}

	/**
	 *
	 * @return
	 */
	protected JKPanel getExcelPanel() {
		if (this.pnlExcel == null) {
			this.pnlExcel = new JKPanel();
			this.pnlExcel.add(this.btnExportToExcel);
			setAllowExcelExport(this.allowExcelExport);
			this.btnExportToExcel.setIcon("excel_icon.gif");
		}
		return this.pnlExcel;
	}

	// //////////////////////////////////////////////////////
	// implemented from RecordTraversePolicy
	// //////////////////////////////////////////////////////
	@Override
	public int getFirstRecord() {
		if (getTable().getRowCount() > 0) {
			return getModel().getRecordIdAsInteger(0);
		}
		return -1;
	}

	@Override
	public int getLastRecord() {
		if (getTable().getRowCount() > 0) {
			return getModel().getRecordIdAsInteger(getModel().getRowCount() - 1);
		}
		return -1;
	}

	public QueryTableModel getModel() {
		return this.model;
	}

	public Vector<FSTableRecord> getModifiedRecords() {
		return this.table.getModifiedRecords();
	}

	@Override
	public int getNextRecord(final int recordId) {
		final int row = getModel().getRowIndexForRecordId(recordId);
		if (row + 1 == this.table.getRowCount()) {
			return -1;
		}
		return getModel().getRecordIdAsInteger(row + 1);
	}

	private JKPanel getPagingPanel() {
		this.btnFirstPage.setIcon(SwingUtility.isLeftOrientation() ? "first_button_commons_icon.gif" : "last_button_commons_icon.gif");
		this.btnLastPage.setIcon(SwingUtility.isLeftOrientation() ? "last_button_commons_icon.gif" : "first_button_commons_icon.gif");
		this.btnNextPage.setIcon(SwingUtility.isLeftOrientation() ? "next_button_commons_icon.gif" : "previous_button_commons_icon.gif");
		this.btnPreviousePage.setIcon(SwingUtility.isLeftOrientation() ? "previous_button_commons_icon.gif" : "next_button_commons_icon.gif");
		// pnlPaging.add(txtLimit);

		//
		this.txtAllRowsCount.setHorizontalAlignment(JKTextField.CENTER);
		this.txtPagesCount.setHorizontalAlignment(JKTextField.CENTER);
		this.pnlPaging.add(this.btnFirstPage);
		this.pnlPaging.add(this.btnPreviousePage);
		this.pnlPaging.add(this.txtAllRowsCount);
		this.pnlPaging.add(this.txtPagesCount);
		this.pnlPaging.add(this.btnNextPage);
		this.pnlPaging.add(this.btnLastPage);
		// txtLimit.setHorizontalAlignment(SwingConstants.CENTER);
		return this.pnlPaging;
	}

	/**
	 * @return the pane
	 */
	public JScrollPane getPane() {
		return this.pane;
	}

	@Override
	public int getPreviouseRecord(final int recordId) {
		final int row = getModel().getRowIndexForRecordId(recordId);
		if (row == 0) {
			return -1;
		}
		return getModel().getRecordIdAsInteger(row - 1);
	}

	/**
	 *
	 * @return JKPanel
	 */
	JKPanel getPrintPanel() {
		if (this.pnlPrint == null) {
			this.pnlPrint = new JKPanel();
			this.pnlPrint.add(this.btnPrint);
			this.btnPrint.setIcon("fileprint.png");
			this.pnlPrint.add(this.btnSelectPrintFields);
			this.btnSelectPrintFields.setIcon("select.png");
			// pnlPrint.add(btnExportToExcel);
			// btnExportToExcel.setIcon("excel_commons_mod_icon.gif");
			setAllowPrinting(this.allowPrinting); // to set it visible or
													// invisiable
		}
		return this.pnlPrint;
	}

	public Object getRecordId(final int row) {
		return this.model.getRecordId(row);
	}

	public int getRecordIdAsInteger(final int row) {
		return ConversionUtil.toInteger(getRecordId(row));
	}

	public Vector<FSTableRecord> getRecords() {
		return this.table.getRecords();
	}

	public int getRowCount() {
		return this.table.getRowCount();
	}

	/**
	 *
	 * @return String
	 */
	public Object getSelectedId() {
		final int row = this.table.getSelectedRow();
		if (row != -1) {
			return this.model.getRecordId(row);
		}
		return null;
	}

	/**
	 *
	 * @return int
	 */
	public int getSelectedIdAsInteger() {
		final int selectedRow = getSelectedRow();
		if (selectedRow == -1) {
			return -1;
		}
		return this.model.getRecordIdAsInteger(selectedRow);
	}

	/**
	 * @deprecated
	 * @return String[]
	 */
	@Deprecated
	public String[] getSelectedIds() {
		final int[] rows = this.table.getSelectedRows();
		final String ids[] = new String[rows.length];
		for (int i = 0; i < rows.length; i++) {
			ids[i] = this.model.getRecordId(rows[i]).toString();
		}
		return ids;
	}

	/**
	 *
	 * @return String[]
	 */
	public int[] getSelectedIdsAsInteger() {
		final int[] rows = this.table.getSelectedRows();
		final int ids[] = new int[rows.length];
		for (int i = 0; i < rows.length; i++) {
			ids[i] = this.model.getRecordIdAsInteger(rows[i]);
		}
		return ids;
	}

	public String getSelectedIdsAsIntegerAsCSV() {
		final int ids[] = getSelectedIdsAsInteger();
		final StringBuffer buf = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			buf.append(ids[i]);
			if (i < ids.length - 1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	/**
	 *
	 * @return
	 */
	public int getSelectedRow() {
		return this.table.getSelectedRow();
	}

	private JKPanel getSouthButtonsPanel() {
		if (this.pnlSouthButtons == null) {
			this.pnlSouthButtons = new JKPanel();
			this.pnlSouthButtons.add(getPrintPanel());
			this.pnlSouthButtons.add(getExcelPanel());
		}
		return this.pnlSouthButtons;
	}

	/**
	 *
	 * @return
	 */
	protected JKPanel getSouthLablesPanel() {
		if (this.pnlSouthLables == null) {
			this.pnlSouthLables = new JKPanel();
			this.pnlSouthLables.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			// txtCount.setEditable(false);
			this.txtCount.setHorizontalAlignment(SwingConstants.CENTER);
			this.pnlRecordsCount = new JKLabledComponent("RECORDS_COUNT", this.txtCount);
			this.pnlSouthLables.add(this.pnlRecordsCount);
		}
		return this.pnlSouthLables;
	}

	public JKTable getTable() {
		return this.table;
	}

	public TableModel getTableModel() {
		return getTable().getModel();
	}

	public Object getValueAt(final int row, final int col) {
		return this.table.getValueAt(row, col);
	}

	// //////////////////////////////////////////////////////////////////////////
	public Object getValueAt(final int row, final int col, final boolean includeInvisibleColumns) {
		return this.table.getValueAt(row, col, includeInvisibleColumns);
	}

	public java.util.Date getValueAtAsDate(final int row, final int col) {
		return this.table.getValueAtAsDate(row, col);
	}

	public double getValueAtAsDouble(final int i, final int j) {
		return this.table.getValueAtAsDouble(i, j);
	}

	public int getValueAtAsInteger(final int i, final int j) {
		return this.table.getValueAtAsInteger(i, j);
	}

	public Date getValueAtAsSqlDate(final int row, final int col) {
		return this.table.getValueAtAsSqlDate(row, col);
	}

	public String getValueAtAsString(final int row, final int col) {
		return this.table.getValueAtAsString(row, col);
	}

	private void handleCopyQuery() {
		GeneralUtility.copyToClipboard(this.model.getSql());
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGoFirstPage() {
		try {
			this.model.moveToFirstPage();
			refreshComponents();
		} catch (final PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGotoLastPage() {
		try {
			this.model.moveToLastPage();
			refreshComponents();
		} catch (final PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// private void handleLimitChanged(KeyEvent e) {
	// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	// model.setPageRowsCount(txtLimit.getTextAsInteger());
	// }
	// }

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGotoNextPage() {
		try {
			this.model.moveToNextPage();
			refreshComponents();
		} catch (final PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGotoPreviousePage() {
		try {
			this.model.moveToPreviousePage();
			refreshComponents();
		} catch (final PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 *
	 * @param e
	 */
	private void handleHeaderMouseClicked(final MouseEvent e) {
		if (getRowCount() == 0) {
			handleCopyQuery();
			return;
		}
		final int colIndex = this.table.getTableHeader().columnAtPoint(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			showFilterPanel(colIndex);
		} else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
			final int oldOrderByColunmIndex = this.model.getOrderByColunmIndex();
			if (oldOrderByColunmIndex != -1 && this.model.getVisibleColumnIndexFromActual(oldOrderByColunmIndex) == colIndex) {
				toggleOrderBy();
			} else {
				handleSortColumn(colIndex);
			}
			// TableColumn tableColumn =
			// table.getColumnModel().getColumn(colIndex);
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	private void handleKeyPress(final KeyEvent e) {
		// System.out.println("Control : "+e.isControlDown());
		// System.out.println("Char : "+e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (this.table.getSelectedRow() != -1) {
				fireRecordSelectedEvent(this.model.getRecordIdAsInteger(this.table.getSelectedRow()));
			}
		} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
			this.table.transferFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_F11) {
			// copy the selected record id to clipboard
			if (this.table.getSelectedRow() != -1) {
				GeneralUtility.copyToClipboard(this.model.getRecordId(this.table.getSelectedRow()) + "");
			}
		} else if (e.getKeyCode() == KeyEvent.VK_F12) {
			handleCopyQuery();
		} else if (e.getKeyCode() == KeyEvent.VK_F9) {
			this.model.setShowIdColunm(true);
			/** @todo refelct it on the filters panel */
			reloadData();
		} else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_C) {
			// copy the selected record id to clipboard
			if (this.table.getSelectedRow() != -1) {
				GeneralUtility.copyToClipboard(this.model.getValueAt(this.table.getSelectedRow(), 0).toString());
				// to oevrride the default control c
				e.consume();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_F5) {
			reloadData();
		}
	}

	/**
	 *
	 */
	void handlePrint() {
		this.dynamicReportTitle = SwingUtility.showInputDialog("ENTER_REPORT_TITLE");
		PrintUtil.printQueryModel(this.model, this.dynamicReportTitle);
	}

	/**
	 *
	 */
	private void handleRowCountChanged() {
		final int count = this.txtCount.getTextAsInteger();
		setPagRowsCount(count);
		reloadData();
	}

	// ////////////////////////////////////////////////////////////////////////
	private void handleShowFields() {
		final PnlQueryFields pnl = new PnlQueryFields(this.model);
		SwingUtility.showPanelInDialog(pnl, "PRINT_FIELDS");
		reloadData();
	}

	/**
	 *
	 */
	private void handleSortAsc() {
		this.model.setOrderDirection(OrderDirection.ASCENDING);
		reloadData();
	}

	private void handleSortColumn(final int colIndex) {
		// to get the actual column index
		this.model.setOrderByColunmIndex(this.model.getActualColumnIndexFromVisible(colIndex));
		reloadData();
	}

	/**
	 *
	 */
	private void init() {
		// setPreferredSize(new Dimension(500,300));
		setLayout(new BorderLayout());
		final JKPanel pnl = new JKPanel();
		pnl.setLayout(new BorderLayout());
		// calcColunmWidthes();
		this.pane = new JKScrollPane(this.table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setBackground(SwingUtility.getDefaultBackgroundColor());

		this.table.setPreferredScrollableViewportSize(getPreferredSize());

		final Color c = new Color(128, 128, 252);

		this.pane.getVerticalScrollBar().setBackground(c);
		this.pane.getHorizontalScrollBar().setBackground(c);
		// if (allowFiltering) {//to handle the show filter panel with no
		// buttons
		pnl.add(this.filtersBox, BorderLayout.NORTH);
		// }
		pnl.add(this.pane, BorderLayout.CENTER);

		// south components
		// JKPanel pnl2=new JKPanel();
		this.pnlSouth.setLayout(new BoxLayout(this.pnlSouth, BoxLayout.Y_AXIS));
		this.pnlSouth.add(getSouthLablesPanel());
		this.pnlSouth.add(getSouthButtonsPanel());
		this.pnlSouth.add(getPagingPanel());
		pnl.add(this.pnlSouth, BorderLayout.SOUTH);

		this.pane.setFocusable(false);
		// txtCount.setFocusable(false);
		pnl.setBorder(BorderFactory.createEtchedBorder());
		add(pnl, BorderLayout.CENTER);
		this.table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				handleKeyPress(e);
			}

		});
		this.table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						fireRecordSelectedEvent(QueryJTable.this.model.getRecordIdAsInteger(QueryJTable.this.table.getSelectedRow()));
					}
				}
			}
		});
		this.table.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(final ComponentEvent e) {
				System.out.println("component shown");
			}
		});
		this.table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				handleHeaderMouseClicked(e);
			};
		});
		this.btnExportToExcel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				ExcelUtil.buildExcelSheet(QueryJTable.this.model);
			}
		});
		this.btnSelectPrintFields.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleShowFields();
			}
		});
		this.btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handlePrint();
			}
		});

		// txtLimit.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyPressed(KeyEvent e) {
		// handleLimitChanged(e);
		// }
		// });
		this.btnFirstPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGoFirstPage();
			}
		});
		this.btnNextPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGotoNextPage();
			}
		});
		this.btnPreviousePage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGotoPreviousePage();
			}
		});
		this.btnLastPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGotoLastPage();
			}
		});
		this.btnAsc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSortAsc();
			}
		});
		this.btnDesc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				QueryJTable.this.model.setOrderDirection(OrderDirection.DESCENDING);
				reloadData();
			}
		});
		this.txtCount.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(final FocusEvent e) {
				handleRowCountChanged();
			}
		});
		// model.addTableModelListener(new TableModelListener() {
		//
		// @Override
		// public void tableChanged(TableModelEvent e) {
		// refreshComponents();
		// }
		// });
	}

	public boolean isAllowAddNew() {
		return this.table.isAllowAddNew();
	}

	public boolean isAllowDelete() {
		return this.table.isAllowDelete();
	}

	public boolean isAllowExcelExport() {
		return this.allowExcelExport;
	}

	public boolean isAllowFiltering() {
		return this.allowFiltering;
	}

	public boolean isAllowPaging() {
		return this.allowPaging;
	}

	public boolean isDataModified() {
		return this.table.isDataModified();
	}

	public boolean isEditable() {
		return this.table.isEditable();
	}

	/**
	 *
	 * @return
	 */
	private boolean isShowFilterButtons() {
		return this.showFilterButtons;
	}

	/**
	 * @return
	 */
	public boolean isShowSortingPanel() {
		return this.pnlSorting.isVisible();
	}

	/**
	 * should be called after the data is loaded
	 */
	@Override
	public void refreshComponents() {
		this.btnExportToExcel.setShortcut("ctrl X", "Ctrl X");
		this.txtCount.setText(this.model.getRowCount() + "");
		// txtCount.setEditable(false);
		this.btnExportToExcel.setEnabled(this.model.getRowCount() > 0);
		this.btnPrint.setEnabled(this.model.getRowCount() > 0);
		this.btnSelectPrintFields.setEnabled(this.model.getRowCount() > 0);

		// txtAllRowsCount.setValue(model.getAllRowsCount());
		this.txtPagesCount.setValue(this.model.getCurrentPage() + 1 + "/" + this.model.getPagesCount());
		this.txtAllRowsCount.setValue(this.model.getAllRowsCount());
		this.pnlPaging.setVisible(this.model.getPagesCount() > 1);

		this.btnNextPage.setEnabled(this.model.getCurrentPage() < this.model.getPagesCount() - 1);
		this.btnPreviousePage.setEnabled(this.model.getCurrentPage() > 0);
		this.btnLastPage.setEnabled(this.model.getCurrentPage() < this.model.getPagesCount() - 1);
		this.btnFirstPage.setEnabled(this.model.getCurrentPage() > 0);
		// txtLimit.setValue(model.getPageRowsCount());
		// if (!renderersSet) {
		// table.setDefaultRenderer(String.class, new
		// TableRenderers.CustomDataRenderer());
		// renderersSet = true;
		// }
		// resetPane();
	}

	/**
	 *
	 */
	public void reloadData() {
		// the below validation is for performance issue
		if (!this.model.getSql().equals("")) {
			final int row = this.table.getSelectedRow();
			this.model.loadData();
			refreshComponents();
			addEmptyRowIfNeeded();

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					setSelectedRow(row);
				}
			});
		}

		// table.setPreferredScrollableViewportSize(new
		// Dimension(table.getWidth(), 400));
		// if (model.getRowCount() < pageRowCount) {
		// table.setPreferredScrollableViewportSize(table.getPreferredSize());
		// } else {
		// Dimension dim = table.getPreferredSize();
		// dim.setSize(dim.getWidth(), pageRowCount * 20);
		// table.setPreferredScrollableViewportSize(dim);
		// }

	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		if (isAllowFiltering()) {
			this.filtersBox.requestFocus();
			return;
		}
		// for (int i = 0; i < filters.length; i++) {
		// if (filters[i].isVisible()) {
		// filters[i].requestFocus();
		// // transferFocus();
		// return;
		// }
		// }
		this.table.requestFocus();
	}

	public void setAllowAddNew(final boolean allowAddNew) {
		this.table.setAllowAddNew(allowAddNew);
	}

	public void setAllowDelete(final boolean allowDelete) {
		this.table.setAllowDelete(allowDelete);
	}

	/**
	 *
	 * @param allowExcelExport
	 */
	public void setAllowExcelExport(final boolean allowExcelExport) {
		this.allowExcelExport = allowExcelExport;
		this.pnlExcel.setVisible(allowExcelExport);
	}

	public void setAllowFiltering(final boolean allowFiltering) {
		this.allowFiltering = allowFiltering;
		buildFilterPanels();
	}

	public void setAllowPaging(final boolean allow) {
		if (allow) {
			this.model.setPageRowsCount(-1);// read the default from the
											// DataSource
		} else {
			this.model.setPageRowsCount(0);
		}
	}

	/**
	 *
	 * @param allow
	 *            boolean
	 */
	public void setAllowPrinting(final boolean allow) {
		this.allowPrinting = allow;
		this.btnPrint.setEnabled(allow);
		this.pnlPrint.setVisible(allow);
	}

	public void setColumnDateFormat(final int col, final String format) {
		this.table.setColumnDateFormat(col, format);
	}

	public void setColumnName(final int col, final String name) {
		this.table.setColumnName(col, name);
	}

	public void setColumnNumberFormat(final int col, final String format) {
		this.table.setColumnNumberFormat(col, format);
	}

	public void setColumnPrefereddWidth(final int row, final int col) {
		this.table.setColumnPrefereddWidth(row, col);
	}

	public void setColunmEditor(final int col, final BindingComponent comp) {
		this.table.setColunmEditor(col, comp);
	}

	public void setColunmRenderer(final int col, final BindingComponent comp) {
		this.table.setColunmRenderer(col, comp);
	}

	@Override
	public void setCurrentRecord(final int recordId) {
		if (recordId == -1) {
			setSelectedRow(-1);
		}
		setSelectedRowByRecordId(recordId);
	}

	public void setEditable(final boolean editable) {
		this.table.setEditable(editable);
	}

	public void setEditable(final int col, final boolean editable) {
		this.table.setEditable(col, editable);
	}

	public void setMasterTable() {
		setAllowExcelExport(true);
		setAllowFiltering(true);
		setAllowPrinting(true);
	}

	public void setPagRowsCount(final int count) {
		this.model.setPageRowsCount(count);
	}

	/**
	 *
	 * @param sql
	 *            String
	 */
	public void setQuery(final String sql) {
		setQuery(sql, this.model.getOrderByColunmIndex());
	}

	/**
	 *
	 * @param sql
	 * @param orderbyColunmIndex
	 */
	public void setQuery(final String sql, final int orderByColunmIndex) {
		if (isVisible()) {
			final String staticSql = this.model.getStaticWhere();
			final DataSource manager = this.model.getReourceManager();
			final int pagesRowsCount = this.model.getPageRowsCount();
			final String oldSql = this.model.getSql();
			final boolean showIdColunm = this.model.getShowIdColunm();

			this.model = new QueryTableModel(manager, sql, orderByColunmIndex);
			this.model.setPageRowsCount(pagesRowsCount);
			this.model.setShowIdColunm(showIdColunm);
			this.model.setStaticWhere(staticSql);

			// when the model sql is empty string , then no fiter panels will be
			// created
			// so when updateing the sql the filter panels remains zero length
			// array
			// which will
			// cause null pointer exception when user selects filter colunm
			// Note : appeared at DynCrossDaoPanel
			if (oldSql.equals("") && !sql.equals("")) {
				buildFilterPanels();
			}
			// copy the old filter values
			final Collection<TableFilterPanel> values = this.filters.values();
			for (final TableFilterPanel tableFilterPanel : values) {
				this.model.setExtraSQLCondition(tableFilterPanel.getFilterColunmName(), tableFilterPanel.getConditionString());
			}
			// for (int i = 0; i < filters.length; i++) {

			// }
			this.table.setModel(this.model);
			reloadData();
			// TODO : add event for the model change listener
		}
	}

	public void setRequiredColumn(final int col, final boolean required) {
		this.table.setRequiredColumn(col, required);
	}

	/**
	 *
	 * @param index
	 */
	public void setSelectedRow(final int index) {
		this.table.setSelectedRow(index);
	}

	/**
	 *
	 * @param recordId
	 *            int
	 */
	public void setSelectedRowByRecordId(final int recordId) {
		setSelectedRowByRecordId(recordId, true);
	}

	public void setSelectedRowByRecordId(final int recordId, final boolean fireRecordSelected) {
		final int rowIndex = this.model.getRowIndexForRecordId(recordId);
		this.table.setSelectedRow(rowIndex);
		if (fireRecordSelected) {
			// SwingUtilities.invokeLater(new Runnable() {
			//
			// @Override
			// public void run() {
			fireRecordSelectedEvent(recordId);
			// }
			// });
		}
	}

	public void setShowFilterButtons(final boolean showFilterButtons) {
		this.showFilterButtons = showFilterButtons;
	}

	/**
	 *
	 * @param show
	 *            boolean
	 */
	public void setShowIdColunm(final boolean show) {
		this.model.setShowIdColunm(show);
		buildFilterPanels();
		this.model.fireTableStructureChanged();

		// reloadData();
	}

	/**
	 *
	 * @param show
	 *            boolean
	 */
	public void setShowRecordsCount(final boolean show) {
		// txtCount.setVisible(show);
		this.pnlRecordsCount.setVisible(show);
	}

	/**
	 * @param showSortingPanel
	 *            the showSortingPanel to set
	 */
	public void setShowSortingPanel(final boolean showSortingPanel) {
		if (this.pnlSorting != null) {
			this.pnlSorting.setVisible(showSortingPanel);
		}
	}

	/**
	 *
	 * @param show
	 */
	public void setShowSouthButtonsPanel(final boolean show) {
		this.pnlSouthButtons.setVisible(show);

	}

	public void setSqlFileName(final String fileName) {
		setQuery(GeneralUtility.getSqlFile(fileName));
	}

	/**
	 * @param title
	 */
	@Override
	public void setTitle(final String title) {
		if (title != null && !title.equals("")) {
			setBorder(SwingUtility.createTitledBorder(title));
		}
	}

	public void setValueAt(final Object aValue, final int row, final int column) {
		this.table.setValueAt(aValue, row, column);
	}

	// //////////////////////////////////////////////////////////////////////////
	public void setVisible(final int col, final boolean visible) {
		this.table.setVisible(col, visible);
	}

	/**
	 *
	 * @param colunmIndex
	 *            int
	 */
	public void showFilterPanel(final int colunmIndex) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showFilterPanel(colunmIndex, QueryJTable.this.showFilterButtons);
			}
		});
	}

	/**
	 *
	 * @param colunmIndex
	 *            colIndex
	 * @param hideButtons
	 *            boolean
	 */
	public void showFilterPanel(final int colunmIndex, final boolean showButtons) {
		if (this.allowFiltering) {
			if (colunmIndex != -1) {
				final TableFilterPanel tableFilterPanel = this.filters.get(this.model.getColumnName(colunmIndex));
				if (tableFilterPanel != null) {
					tableFilterPanel.setVisible(true);
					tableFilterPanel.showButtons(showButtons);
				}
				// colunmIndex =
				// model.getActualColumnIndexFromVisible(colunmIndex);
				//
				// if (colunmIndex > -1 && colunmIndex < filters.length) {
				// filters[colunmIndex].setVisible(true);
				// if (hideButtons) {
				// filters[colunmIndex].hideButtons();
				// }
				// // }
				// } else {
				// System.err.println(colunmIndex +
				// " is out of range for Filters array(" + filters.length +
				// ")");
				// GeneralUtility.printStackTrace();
				// }
			}
		}
	}

	/**
	 *
	 * @param colunmIndex
	 */
	public void showFilterPanel(final String filterName, final boolean hideButtons) {
		if (this.allowFiltering) {
			final int index = this.model.getColunmIndex(filterName);
			if (index != -1) {
				showFilterPanel(index, hideButtons);
			}
		}
	}

	public void showFilterPanels(final int[] filters) {
		for (final int filter : filters) {
			showFilterPanel(filter);
		}
	}

	public void stopEditing() {
		this.table.stopEditing();
	}

	/**
	 *
	 */
	private void toggleOrderBy() {
		if (this.btnAsc.isSelected()) {
			this.btnDesc.doClick();
		} else {
			this.btnAsc.doClick();
		}
	}
}