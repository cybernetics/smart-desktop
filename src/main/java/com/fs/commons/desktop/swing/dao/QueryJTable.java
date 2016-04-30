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

	JKRadioButton btnDesc = new JKRadioButton(Lables.get("DESCENDING"));

	// private boolean renderersSet;

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

	// /////////////////////////////////////////////////////////////////////////////
	public QueryJTable(String sql, String title) {
		this(sql, title, true);
	}

	public QueryJTable(String sql) {
		this(sql, "", true);
	}

	/**
	 * 
	 * @param tableMeta
	 */
	public QueryJTable(TableMeta tableMeta) {
		this(tableMeta, tableMeta.getTableId(), true);
	}

	public QueryJTable(TableMeta tableMeta, String title, boolean showFilters) {
		this(title, tableMeta.getDataSource(), tableMeta.getReportSql(), showFilters);
		setShowIdColunm(!tableMeta.isAutoIncrementId());
		this.pageRowCount = tableMeta.getPageRowCount();
		int[] filters = tableMeta.getFilters();
		for (int i = 0; i < filters.length; i++) {
			showFilterPanel(filters[i], isShowFilterButtons());
		}

	}

	/**
	 * 
	 * @return
	 */
	private boolean isShowFilterButtons() {
		return showFilterButtons;
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
	public QueryJTable(String sql, String title, boolean allowFiltering) {
		this(new QueryTableModel(sql), title, allowFiltering);
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
	public QueryJTable(QueryTableModel model, String title, boolean allowFiltering) {
		table.setModel(model);
		this.model = model;
		setTitle(title);
		init();
		refreshComponents(); // to avoid requesting the query again since it
		table.setPreferredScrollableViewportSize(new Dimension(600, 250));
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());

		setAllowFiltering(allowFiltering);
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		if (title != null && !title.equals("")) {
			setBorder(SwingUtility.createTitledBorder(title));
		}
	}

	public QueryJTable(String title, DataSource resource, String sql, boolean allowFiltering) {
		this(new QueryTableModel(resource, sql), title, allowFiltering);
	}

	public QueryJTable(TableMeta tableMeta, boolean allowFiltering) {
		this(tableMeta, tableMeta.getTableId(), allowFiltering);
	}

	/**
	 * 
	 * @param sql
	 *            String
	 */
	public void setQuery(String sql) {
		setQuery(sql, model.getOrderByColunmIndex());
	}

	/**
	 * 
	 */
	private void init() {
		// setPreferredSize(new Dimension(500,300));
		setLayout(new BorderLayout());
		JKPanel pnl = new JKPanel();
		pnl.setLayout(new BorderLayout());
		// calcColunmWidthes();
		pane = new JKScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		setBackground(SwingUtility.getDefaultBackgroundColor());

		table.setPreferredScrollableViewportSize(getPreferredSize());

		Color c = new Color(128, 128, 252);

		pane.getVerticalScrollBar().setBackground(c);
		pane.getHorizontalScrollBar().setBackground(c);
		// if (allowFiltering) {//to handle the show filter panel with no
		// buttons
		pnl.add(filtersBox, BorderLayout.NORTH);
		// }
		pnl.add(pane, BorderLayout.CENTER);

		// south components
		// JKPanel pnl2=new JKPanel();
		pnlSouth.setLayout(new BoxLayout(pnlSouth, BoxLayout.Y_AXIS));
		pnlSouth.add(getSouthLablesPanel());
		pnlSouth.add(getSouthButtonsPanel());
		pnlSouth.add(getPagingPanel());
		pnl.add(pnlSouth, BorderLayout.SOUTH);

		pane.setFocusable(false);
		// txtCount.setFocusable(false);
		pnl.setBorder(BorderFactory.createEtchedBorder());
		add(pnl, BorderLayout.CENTER);
		table.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				handleKeyPress(e);
			}

		});
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (e.getClickCount() == 2) {
						fireRecordSelectedEvent(model.getRecordIdAsInteger(table.getSelectedRow()));
					}
				}
			}
		});
		table.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				System.out.println("component shown");
			}
		});
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleHeaderMouseClicked(e);
			};
		});
		btnExportToExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExcelUtil.buildExcelSheet(model);
			}
		});
		btnSelectPrintFields.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleShowFields();
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handlePrint();
			}
		});

		// txtLimit.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyPressed(KeyEvent e) {
		// handleLimitChanged(e);
		// }
		// });
		btnFirstPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleGoFirstPage();
			}
		});
		btnNextPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleGotoNextPage();
			}
		});
		btnPreviousePage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleGotoPreviousePage();
			}
		});
		btnLastPage.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleGotoLastPage();
			}
		});
		btnAsc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSortAsc();
			}
		});
		btnDesc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model.setOrderDirection(OrderDirection.DESCENDING);
				reloadData();
			}
		});
		txtCount.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
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

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGotoPreviousePage() {
		try {
			model.moveToPreviousePage();
			refreshComponents();
		} catch (PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGotoNextPage() {
		try {
			model.moveToNextPage();
			refreshComponents();
		} catch (PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGotoLastPage() {
		try {
			model.moveToLastPage();
			refreshComponents();
		} catch (PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void handleGoFirstPage() {
		try {
			model.moveToFirstPage();
			refreshComponents();
		} catch (PagingException e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////

	private JKPanel getSouthButtonsPanel() {
		if (pnlSouthButtons == null) {
			pnlSouthButtons = new JKPanel();
			pnlSouthButtons.add(getPrintPanel());
			pnlSouthButtons.add(getExcelPanel());
		}
		return pnlSouthButtons;
	}

	/**
	 * 
	 * @return
	 */
	protected JKPanel getSouthLablesPanel() {
		if (pnlSouthLables == null) {
			pnlSouthLables = new JKPanel();
			pnlSouthLables.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			// txtCount.setEditable(false);
			txtCount.setHorizontalAlignment(SwingConstants.CENTER);
			pnlRecordsCount = new JKLabledComponent("RECORDS_COUNT", txtCount);
			pnlSouthLables.add(pnlRecordsCount);
		}
		return pnlSouthLables;
	}

	/**
	 * 
	 */
	public void reloadData() {
		// the below validation is for performance issue
		if (!model.getSql().equals("")) {
			final int row = table.getSelectedRow();
			model.loadData();
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

	// private void checkShowPaging() {
	// pnlPaging.setVisible(allowPaging && getModel().getRowCount() > 0 &&
	// getModel().getRowCount() == getModel().getLimit());
	// }

	public void checkEmptySelection() throws ValidationException {
		if (getSelectedIdAsInteger() == -1) {
			requestFocus();
			throw new ValidationException("PLEASE_SELECT_RECORD");
		}
	}

	/**
	 * should be called after the data is loaded
	 */
	public void refreshComponents() {
		btnExportToExcel.setShortcut("ctrl X", "Ctrl X");
		txtCount.setText(model.getRowCount() + "");
		// txtCount.setEditable(false);
		btnExportToExcel.setEnabled(model.getRowCount() > 0);
		btnPrint.setEnabled(model.getRowCount() > 0);
		btnSelectPrintFields.setEnabled(model.getRowCount() > 0);

		// txtAllRowsCount.setValue(model.getAllRowsCount());
		txtPagesCount.setValue((model.getCurrentPage() + 1) + "/" + model.getPagesCount());
		txtAllRowsCount.setValue(model.getAllRowsCount());
		pnlPaging.setVisible(model.getPagesCount() > 1);

		btnNextPage.setEnabled(model.getCurrentPage() < model.getPagesCount() - 1);
		btnPreviousePage.setEnabled(model.getCurrentPage() > 0);
		btnLastPage.setEnabled(model.getCurrentPage() < model.getPagesCount() - 1);
		btnFirstPage.setEnabled(model.getCurrentPage() > 0);
		// txtLimit.setValue(model.getPageRowsCount());
		// if (!renderersSet) {
		// table.setDefaultRenderer(String.class, new
		// TableRenderers.CustomDataRenderer());
		// renderersSet = true;
		// }
		// resetPane();
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

	/**
	 * @deprecated
	 * @return String[]
	 */
	public String[] getSelectedIds() {
		int[] rows = table.getSelectedRows();
		String ids[] = new String[rows.length];
		for (int i = 0; i < rows.length; i++) {
			ids[i] = model.getRecordId(rows[i]).toString();
		}
		return ids;
	}

	/**
	 * 
	 * @return String[]
	 */
	public int[] getSelectedIdsAsInteger() {
		int[] rows = table.getSelectedRows();
		int ids[] = new int[rows.length];
		for (int i = 0; i < rows.length; i++) {
			ids[i] = model.getRecordIdAsInteger(rows[i]);
		}
		return ids;
	}

	/**
	 * 
	 * @return String
	 */
	public Object getSelectedId() {
		int row = table.getSelectedRow();
		if (row != -1) {
			return model.getRecordId(row);
		}
		return null;
	}

	/**
	 * 
	 * @param recordId
	 *            int
	 */
	public void setSelectedRowByRecordId(final int recordId) {
		setSelectedRowByRecordId(recordId, true);
	}

	/**
	 * 
	 * @return int
	 */
	public int getSelectedIdAsInteger() {
		int selectedRow = getSelectedRow();
		if (selectedRow == -1) {
			return -1;
		}
		return model.getRecordIdAsInteger(selectedRow);
	}

	/**
	 * 
	 * @param enable
	 *            boolean
	 */
	public void allowMultipleSelections(boolean enable) {
		table.setAlowMutipleSelection(enable);
	}

	public JKTable getTable() {
		return table;
	}

	public QueryTableModel getModel() {
		return model;
	}

	// /**
	// * setTableColunmsRenderer
	// */
	// private void setTableColunmsRenderer() {
	// table.setRenderers();
	// }

	/**
	 * 
	 * @param panel
	 *            TableFilterPanel
	 */
	public void filterUpdated(TableFilterPanel panel) {
		model.setExtraSQLCondition(panel.getFilterColunmName(), panel.getConditionString());
		reloadData();
		table.requestFocus();
	}

	/**
	 * buildFilterPanels
	 * 
	 * @return Box
	 */
	private Box buildFilterPanels() {
		filters.clear();
		filtersBox.removeAll();
		if (allowFiltering) {
			filtersBox.setBorder(SwingUtility.createTitledBorder(""));
			// filters = new TableFilterPanel[model.getColumnCount()];
			for (int i = 0; i < model.getColumnCount(); i++) {
				TableFilterPanel filter = new TableFilterPanel(model, model.getActualColumnName(i), this);
				filtersBox.add(filter);
				filters.put(model.getColumnName(i), filter);
			}
			// if (allowFiltering) {
			filtersBox.add(createOrderPanel());
		}
		filtersBox.revalidate();
		// }
		return filtersBox;
	}

	/**
	 * 
	 * @param colunmIndex
	 *            int
	 */
	public void showFilterPanel(final int colunmIndex) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				showFilterPanel(colunmIndex, showFilterButtons);
			}
		});
	}

	/**
	 * 
	 * @param colunmIndex
	 */
	public void showFilterPanel(String filterName, boolean hideButtons) {
		if (allowFiltering) {
			int index = model.getColunmIndex(filterName);
			if (index != -1) {
				showFilterPanel(index, hideButtons);
			}
		}
	}

	/**
	 * 
	 * @return JKPanel
	 */
	private JKPanel createOrderPanel() {
		if (pnlSorting == null) {
			pnlSorting = new JKPanel(new FlowLayout(FlowLayout.LEADING));
			pnlSorting.setVisible(false);
			JKPanel pnl1 = new JKPanel();
			pnl1.setBorder(BorderFactory.createEtchedBorder());
			JKLabel lblSortDir = new JKLabel(Lables.get("ORDER"));
			lblSortDir.setIcon("sort_az.png");

			pnl1.add(lblSortDir);
			// SwingUtility.setBoldFont(lblSortDir);

			btnAsc.setFocusable(false);
			btnDesc.setFocusable(false);
			pnl1.add(btnAsc);
			pnl1.add(btnDesc);

			ButtonGroup group = new ButtonGroup();

			group.add(btnAsc);
			group.add(btnDesc);

			btnAsc.setSelected(true);

			pnlSorting.add(pnl1);

		}
		return pnlSorting;
	}

	/**
	 * @return
	 */
	public boolean isShowSortingPanel() {
		return pnlSorting.isVisible();
	}

	/**
	 * @param showSortingPanel
	 *            the showSortingPanel to set
	 */
	public void setShowSortingPanel(boolean showSortingPanel) {
		if (pnlSorting != null) {
			pnlSorting.setVisible(showSortingPanel);
		}
	}

	@Override
	public void requestFocus() {
		super.requestFocus();
		if (isAllowFiltering()) {
			filtersBox.requestFocus();
			return;
		}
		// for (int i = 0; i < filters.length; i++) {
		// if (filters[i].isVisible()) {
		// filters[i].requestFocus();
		// // transferFocus();
		// return;
		// }
		// }
		table.requestFocus();
	}

	/**
	 * @param listener
	 *            DaoRecordListener
	 */
	public void addRecordListener(RecordSelectionListener listener) {
		listeners.add(listener);
	}

	/**
	 * @param RecordId
	 *            String
	 */
	protected void fireRecordSelectedEvent(Object recordId) {
		for (int i = 0; i < listeners.size(); i++) {
			int value = -1;
			if (recordId != null) {
				value = new Integer(recordId.toString());
			}

			listeners.get(i).recordSelected(value);
		}
	}

	/**
	 * 
	 * @param comp
	 *            JComponent
	 */
	public void addComponentToLablesPanel(JComponent comp) {
		pnlSouthLables.add(comp);
		pnlSouthLables.validate();
		pnlSouthLables.repaint();
	}

	/**
	 * 
	 * @param btn
	 */
	public void addButtonToSouthButtonsPanel(JKButton btn) {
		pnlSouthButtons.add(btn);
		pnlSouthButtons.validate();
		pnlSouthButtons.repaint();
	}

	/**
	 * 
	 * @param colunmIndex
	 *            colIndex
	 * @param hideButtons
	 *            boolean
	 */
	public void showFilterPanel(int colunmIndex, boolean showButtons) {
		if (allowFiltering) {
			if (colunmIndex != -1) {
				TableFilterPanel tableFilterPanel = filters.get(model.getColumnName(colunmIndex));
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
	 * @param show
	 *            boolean
	 */
	public void setShowRecordsCount(boolean show) {
		// txtCount.setVisible(show);
		pnlRecordsCount.setVisible(show);
	}

	/**
	 * 
	 * @return JKPanel
	 */
	JKPanel getPrintPanel() {
		if (pnlPrint == null) {
			pnlPrint = new JKPanel();
			pnlPrint.add(btnPrint);
			btnPrint.setIcon("fileprint.png");
			pnlPrint.add(btnSelectPrintFields);
			btnSelectPrintFields.setIcon("select.png");
			// pnlPrint.add(btnExportToExcel);
			// btnExportToExcel.setIcon("excel_commons_mod_icon.gif");
			setAllowPrinting(allowPrinting); // to set it visible or invisiable
		}
		return pnlPrint;
	}

	/**
	 * 
	 * @return
	 */
	protected JKPanel getExcelPanel() {
		if (pnlExcel == null) {
			pnlExcel = new JKPanel();
			pnlExcel.add(btnExportToExcel);
			setAllowExcelExport(allowExcelExport);
			btnExportToExcel.setIcon("excel_icon.gif");
		}
		return pnlExcel;
	}

	/**
	 * 
	 * @param allow
	 *            boolean
	 */
	public void setAllowPrinting(boolean allow) {
		this.allowPrinting = allow;
		btnPrint.setEnabled(allow);
		pnlPrint.setVisible(allow);
	}

	/**
	 * 
	 */
	void handlePrint() {
		dynamicReportTitle = SwingUtility.showInputDialog("ENTER_REPORT_TITLE");
		PrintUtil.printQueryModel(model, dynamicReportTitle);
	}

	/**
	 * 
	 * @param show
	 *            boolean
	 */
	public void setShowIdColunm(boolean show) {
		model.setShowIdColunm(show);
		buildFilterPanels();
		model.fireTableStructureChanged();

		// reloadData();
	}

	/**
	 * 
	 * 
	 */
	public void clearTable() {
		table.resetRecords();
	}

	/**
	 * @return the pane
	 */
	public JScrollPane getPane() {
		return this.pane;
	}

	public boolean isAllowExcelExport() {
		return allowExcelExport;
	}

	/**
	 * 
	 * @param allowExcelExport
	 */
	public void setAllowExcelExport(boolean allowExcelExport) {
		this.allowExcelExport = allowExcelExport;
		pnlExcel.setVisible(allowExcelExport);
	}

	/**
	 * 
	 * @param show
	 */
	public void setShowSouthButtonsPanel(boolean show) {
		pnlSouthButtons.setVisible(show);

	}

	/**
	 * 
	 * @return
	 */
	public int getSelectedRow() {
		return table.getSelectedRow();
	}

	/**
	 * 
	 * @param index
	 */
	public void setSelectedRow(final int index) {
		table.setSelectedRow(index);
	}

	public boolean isAllowFiltering() {
		return allowFiltering;
	}

	public void setAllowFiltering(boolean allowFiltering) {
		this.allowFiltering = allowFiltering;
		buildFilterPanels();
	}

	public String getSelectedIdsAsIntegerAsCSV() {
		int ids[] = getSelectedIdsAsInteger();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			buf.append(ids[i]);
			if (i < ids.length - 1) {
				buf.append(",");
			}
		}
		return buf.toString();
	}

	public void setShowFilterButtons(boolean showFilterButtons) {
		this.showFilterButtons = showFilterButtons;
	}

	/**
	 * 
	 * @param columnName
	 * @param columnType
	 * @param displaySize
	 */
	public void addColunm(String columnName, int columnType, int displaySize) {
		invalidate();
		repaint();
	}

	/**
	 * 
	 * @param sql
	 * @param orderbyColunmIndex
	 */
	public void setQuery(String sql, int orderByColunmIndex) {
		if (isVisible()) {
			String staticSql = model.getStaticWhere();
			DataSource manager = model.getReourceManager();
			int pagesRowsCount = model.getPageRowsCount();
			String oldSql = model.getSql();
			boolean showIdColunm = model.getShowIdColunm();

			model = new QueryTableModel(manager, sql, orderByColunmIndex);
			model.setPageRowsCount(pagesRowsCount);
			model.setShowIdColunm(showIdColunm);
			model.setStaticWhere(staticSql);

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
			Collection<TableFilterPanel> values = filters.values();
			for (TableFilterPanel tableFilterPanel : values) {
				model.setExtraSQLCondition(tableFilterPanel.getFilterColunmName(), tableFilterPanel.getConditionString());
			}
			// for (int i = 0; i < filters.length; i++) {

			// }
			table.setModel(model);
			reloadData();
			// TODO : add event for the model change listener
		}
	}

	public void showFilterPanels(int[] filters) {
		for (int filter : filters) {
			showFilterPanel(filter);
		}
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
			return this.getModel().getRecordIdAsInteger(getModel().getRowCount() - 1);
		}
		return -1;
	}

	@Override
	public int getNextRecord(int recordId) {
		int row = getModel().getRowIndexForRecordId(recordId);
		if (row + 1 == table.getRowCount()) {
			return -1;
		}
		return getModel().getRecordIdAsInteger(row + 1);
	}

	@Override
	public int getPreviouseRecord(int recordId) {
		int row = getModel().getRowIndexForRecordId(recordId);
		if (row == 0) {
			return -1;
		}
		return getModel().getRecordIdAsInteger(row - 1);
	}

	@Override
	public void setCurrentRecord(int recordId) {
		if (recordId == -1) {
			setSelectedRow(-1);
		}
		setSelectedRowByRecordId(recordId);
	}

	public void setMasterTable() {
		setAllowExcelExport(true);
		setAllowFiltering(true);
		setAllowPrinting(true);
	}

	private JKPanel getPagingPanel() {
		btnFirstPage.setIcon(SwingUtility.isLeftOrientation() ? "first_button_commons_icon.gif" : "last_button_commons_icon.gif");
		btnLastPage.setIcon(SwingUtility.isLeftOrientation() ? "last_button_commons_icon.gif" : "first_button_commons_icon.gif");
		btnNextPage.setIcon(SwingUtility.isLeftOrientation() ? "next_button_commons_icon.gif" : "previous_button_commons_icon.gif");
		btnPreviousePage.setIcon(SwingUtility.isLeftOrientation() ? "previous_button_commons_icon.gif" : "next_button_commons_icon.gif");
		// pnlPaging.add(txtLimit);

		//
		txtAllRowsCount.setHorizontalAlignment(JKTextField.CENTER);
		txtPagesCount.setHorizontalAlignment(JKTextField.CENTER);
		pnlPaging.add(btnFirstPage);
		pnlPaging.add(btnPreviousePage);
		pnlPaging.add(txtAllRowsCount);
		pnlPaging.add(txtPagesCount);
		pnlPaging.add(btnNextPage);
		pnlPaging.add(btnLastPage);
		// txtLimit.setHorizontalAlignment(SwingConstants.CENTER);
		return pnlPaging;
	}

	// private void handleLimitChanged(KeyEvent e) {
	// if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	// model.setPageRowsCount(txtLimit.getTextAsInteger());
	// }
	// }

	public boolean isAllowPaging() {
		return allowPaging;
	}

	public void checkSelectMoreThanOne() throws ValidationException {
		checkEmptySelection();
		String[] selectedIds = getSelectedIds();
		if (selectedIds.length > 1) {
			requestFocus();
			throw new ValidationException("PLEASE_SELECT_ONE_RECORD");
		}

	}

	public void setColumnPrefereddWidth(int row, int col) {
		table.setColumnPrefereddWidth(row, col);
	}

	public void setColumnNumberFormat(int col, String format) {
		table.setColumnNumberFormat(col, format);
	}

	public String getValueAtAsString(int row, int col) {
		return table.getValueAtAsString(row, col);
	}

	public double getValueAtAsDouble(int i, int j) {
		return table.getValueAtAsDouble(i, j);
	}

	public int getValueAtAsInteger(int i, int j) {
		return table.getValueAtAsInteger(i, j);
	}

	public void addCellFocusListener(CellFocusListener cellFocusListener) {
		table.addCellFocusListener(cellFocusListener);
	}

	public double getColunmSum(int col) {
		return table.getColunmSum(col);
	}

	public int getRowCount() {
		return table.getRowCount();
	}

	public Object getValueAt(int row, int col) {
		return table.getValueAt(row, col);
	}

	public Vector<FSTableRecord> getDeletedRecords() {
		return table.getDeletedRecords();
	}

	public Vector<FSTableRecord> getModifiedRecords() {
		return table.getModifiedRecords();
	}

	public void addRow() {
		table.addRow();
	}

	public void setEditable(boolean editable) {
		table.setEditable(editable);
	}

	public void setEditable(int col, boolean editable) {
		table.setEditable(col, editable);
	}

	public void setColunmRenderer(int col, BindingComponent comp) {
		table.setColunmRenderer(col, comp);
	}

	public void setColunmEditor(int col, BindingComponent comp) {
		table.setColunmEditor(col, comp);
	}

	public boolean isDataModified() {
		return table.isDataModified();
	}

	public java.util.Date getValueAtAsDate(int row, int col) {
		return table.getValueAtAsDate(row, col);
	}

	public Date getValueAtAsSqlDate(int row, int col) {
		return table.getValueAtAsSqlDate(row, col);
	}

	public Vector<FSTableRecord> getRecords() {
		return table.getRecords();
	}

	public TableModel getTableModel() {
		return getTable().getModel();
	}

	public void setRequiredColumn(int col, boolean required) {
		table.setRequiredColumn(col, required);
	}

	public void setColumnName(int col, String name) {
		table.setColumnName(col, name);
	}

	// /////////////////////////////////////////////////////////
	private void addEmptyRowIfNeeded() {
		if (table.getRowCount() == 0 && table.isEditable()) {
			addRow();
		}
	}

	public boolean isEditable() {
		return table.isEditable();
	}

	public void setValueAt(Object aValue, int row, int column) {
		table.setValueAt(aValue, row, column);
	}

	@Override
	public synchronized void addMouseListener(MouseListener l) {
		if (table != null) {
			// if table==null this indicate that event has been set from super
			// class before
			// calling the constructor of this class
			table.addMouseListener(l);
		} else {
			super.addMouseListener(l);
		}
	}

	@Override
	public synchronized void addKeyListener(KeyListener l) {
		if (table != null) {
			table.addKeyListener(l);
		} else {
			// if table==null this indicate that event has been set from super
			// class before
			// calling the constructor of this class
			super.addKeyListener(l);
		}

	}

	@Deprecated
	/**
	 * replaced with 	addRecordListener(new RecordSelectionListener() {
	 */
	public void addDaoRecordListener(final RecordActionListener recordActionListener) {
		addRecordListener(new RecordSelectionListener() {
			@Override
			public void recordSelected(int recordId) {
				recordActionListener.recordSelected(recordId + "");
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	public int getColumnCount() {
		return table.getColumnCount();
	}

	// ////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getData() {
		return table.getData();
	}

	// ////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getDeletedRows() {
		return table.getDeletedRows();
	}

	// ////////////////////////////////////////////////////////////////////////
	private void handleShowFields() {
		PnlQueryFields pnl = new PnlQueryFields(model);
		SwingUtility.showPanelInDialog(pnl, "PRINT_FIELDS");
		reloadData();
	}

	// ////////////////////////////////////////////////////////////////////////
	private void handleKeyPress(KeyEvent e) {
		// System.out.println("Control : "+e.isControlDown());
		// System.out.println("Char : "+e.getKeyCode());
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (table.getSelectedRow() != -1) {
				fireRecordSelectedEvent(model.getRecordIdAsInteger(table.getSelectedRow()));
			}
		} else if (e.getKeyCode() == KeyEvent.VK_TAB) {
			table.transferFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_F11) {
			// copy the selected record id to clipboard
			if (table.getSelectedRow() != -1) {
				GeneralUtility.copyToClipboard(model.getRecordId(table.getSelectedRow()) + "");
			}
		} else if (e.getKeyCode() == KeyEvent.VK_F12) {
			handleCopyQuery();
		} else if (e.getKeyCode() == KeyEvent.VK_F9) {
			model.setShowIdColunm(true);
			/** @todo refelct it on the filters panel */
			reloadData();
		} else if (e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_C) {
			// copy the selected record id to clipboard
			if (table.getSelectedRow() != -1) {
				GeneralUtility.copyToClipboard(model.getValueAt(table.getSelectedRow(), 0).toString());
				// to oevrride the default control c
				e.consume();
			}
		} else if (e.getKeyCode() == KeyEvent.VK_F5) {
			reloadData();
		}
	}

	private void handleCopyQuery() {
		GeneralUtility.copyToClipboard(model.getSql());
	}

	// //////////////////////////////////////////////////////////////////////////
	public void setVisible(int col, boolean visible) {
		table.setVisible(col, visible);
	}

	// //////////////////////////////////////////////////////////////////////////
	public Object getValueAt(int row, int col, boolean includeInvisibleColumns) {
		return table.getValueAt(row, col, includeInvisibleColumns);
	}

	public static void main(String[] args) {
		QueryJTable t = new QueryJTable("SELECT * FROM GEN_NATIONAL_NUMBERS");
		t.setAllowFiltering(true);
		SwingUtility.testPanel(t);
	}

	public void deleteRow(int row) {
		table.deleteRow(row);
	}

	public void setAllowDelete(boolean allowDelete) {
		table.setAllowDelete(allowDelete);
	}

	public boolean isAllowDelete() {
		return table.isAllowDelete();
	}

	public void stopEditing() {
		table.stopEditing();
	}

	public void setPagRowsCount(int count) {
		model.setPageRowsCount(count);
	}

	public void setSqlFileName(String fileName) {
		setQuery(GeneralUtility.getSqlFile(fileName));
	}

	public void setColumnDateFormat(int col, String format) {
		table.setColumnDateFormat(col, format);
	}

	public void setAllowAddNew(boolean allowAddNew) {
		table.setAllowAddNew(allowAddNew);
	}

	public boolean isAllowAddNew() {
		return table.isAllowAddNew();
	}

	public void setAllowPaging(boolean allow) {
		if (allow) {
			model.setPageRowsCount(-1);// read the default from the DataSource
		} else {
			model.setPageRowsCount(0);
		}
	}

	private void handleSortColumn(int colIndex) {
		// to get the actual column index
		model.setOrderByColunmIndex(model.getActualColumnIndexFromVisible(colIndex));
		reloadData();
	}

	/**
	 * 
	 */
	private void toggleOrderBy() {
		if (btnAsc.isSelected()) {
			btnDesc.doClick();
		} else {
			btnAsc.doClick();
		}
	}

	/**
	 * 
	 */
	private void handleRowCountChanged() {
		int count = txtCount.getTextAsInteger();
		setPagRowsCount(count);
		reloadData();
	}

	/**
	 * 
	 */
	private void handleSortAsc() {
		model.setOrderDirection(OrderDirection.ASCENDING);
		reloadData();
	}

	/**
	 * 
	 * @param e
	 */
	private void handleHeaderMouseClicked(MouseEvent e) {
		if (getRowCount() == 0) {
			handleCopyQuery();
			return;
		}
		int colIndex = table.getTableHeader().columnAtPoint(e.getPoint());
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
			showFilterPanel(colIndex);
		} else if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
			int oldOrderByColunmIndex = model.getOrderByColunmIndex();
			if (oldOrderByColunmIndex != -1 && model.getVisibleColumnIndexFromActual(oldOrderByColunmIndex) == colIndex) {
				toggleOrderBy();
			} else {
				handleSortColumn(colIndex);
			}
			// TableColumn tableColumn =
			// table.getColumnModel().getColumn(colIndex);
		}
	}

	public Object getRecordId(int row) {
		return model.getRecordId(row);
	}

	public int getRecordIdAsInteger(int row) {
		return ConversionUtil.toInteger(getRecordId(row));
	}

	public int[] getAllRecordIds() {
		return getModel().getAllRecordIds();
	}

	public void clearTableListeners() {
		listeners.clear();
	}

	public void setSelectedRowByRecordId(final int recordId, boolean fireRecordSelected) {
		final int rowIndex = model.getRowIndexForRecordId(recordId);
		table.setSelectedRow(rowIndex);
		if (fireRecordSelected) {
//			SwingUtilities.invokeLater(new Runnable() {
//
//				@Override
//				public void run() {
					fireRecordSelectedEvent(recordId);
//				}
//			});
		}
	}
}