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
package com.fs.commons.desktop.swing.comp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.documents.FloatDocument;
import com.fs.commons.desktop.swing.comp.editors.FSBindingComponentEditor;
import com.fs.commons.desktop.swing.comp.listeners.CellFocusEvent;
import com.fs.commons.desktop.swing.comp.listeners.CellFocusListener;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord.RecordStatus;
import com.fs.commons.desktop.swing.comp.renderers.FSBindingComponentRenderer;
import com.fs.commons.desktop.swing.comp.renderers.FSDefaultTableRenderer;
import com.fs.commons.desktop.swing.comp2.FSCheckBox;
import com.fs.commons.desktop.swing.comp2.FSDate;
import com.fs.commons.desktop.swing.comp2.FSTextField;
import com.fs.commons.util.DateTimeUtil;
import com.fs.commons.util.GeneralUtility;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;

public class JKTable extends JTable {
	private static JKLogger logger = JKLoggerFactory.getLogger(JKTable.class);

	class TableFocusListener extends FocusAdapter {
		@Override
		public void focusGained(final FocusEvent e) {
			// focused = true;
		}

		@Override
		public void focusLost(final FocusEvent e) {
			// focused = false;
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					stopEditing();
				}
			});
		}
	}

	private static final long serialVersionUID = 1L;
	private final TableFocusListener tableFocusListener = new TableFocusListener();
	// ArrayList<Integer> editableColumns = new ArrayList<Integer>();
	Vector<CellFocusListener> focusListsner = new Vector<CellFocusListener>();
	// Vector<FSTableColumn> fsTableColunms;
	// private Vector<FSTableColumn> visibleColumns;
	private final boolean confirmDelete = true;
	private int lastSelectedRow;
	private int lastSelectedColunm;

	// private FSTableModel model;

	// private boolean focused;
	private boolean allowAddNew;

	// //
	// ////////////////////////////////////////////////////////////////////////////
	// public JKTable(int numRows, int numColumns) {
	// super(numRows, numColumns);
	// init();
	// }

	// //
	// ////////////////////////////////////////////////////////////////////////////
	// public JKTable(Object[][] rowData, Object[] columnNames) {
	// super(rowData, columnNames);
	// init();
	// }
	//
	// //
	// ////////////////////////////////////////////////////////////////////////////
	// public JKTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm)
	// {
	// super(dm, cm, sm);
	// init();
	// }
	//
	// //
	// ////////////////////////////////////////////////////////////////////////////
	// public JKTable(TableModel dm, TableColumnModel cm) {
	// super(dm, cm);
	// init();
	// }

	// ////////////////////////////////////////////////////////////////////////////
	public JKTable() {
		this(new FSTableModel());
	}

	// //
	// ////////////////////////////////////////////////////////////////////////////
	// public JKTable(Vector rowData, Vector columnNames) {
	// super(rowData, columnNames);
	// init();
	// }

	// ////////////////////////////////////////////////////////////////////////////
	public JKTable(final TableModel dm) {
		super(dm);
		init();
	}

	// ///////////////////////////////////////////////////////////////////
	public void addCellFocusListener(final CellFocusListener cellFocusListener) {
		this.focusListsner.add(cellFocusListener);
	}

	// @Override
	// public void setModel(TableModel model) {
	// super.setModel(model);
	// }

	public void addFSTableColumn(final FSTableColumn col) {
		getFsModel().addFSTableColumn(col);
	}

	// ///////////////////////////////////////////////////////////////////
	public void addRow() {
		addRows(1);
	}

	public void addRows(final int size) {
		for (int i = 0; i < size; i++) {
			final TableModel model = getModel();
			if (model instanceof FSTableModel) {
				final FSTableModel defaultModel = (FSTableModel) model;
				defaultModel.addRecord();
				setSelectedRow(model.getRowCount() - 1);
				setSelectedColumn(0);
			}
		}

	}

	// //
	// ////////////////////////////////////////////////////////////////////////////
	// @Override
	// public void requestFocus() {
	// if (getRowCount() > 0) {
	// super.requestFocus();
	// } else {
	// transferFocus();
	// }
	// }

	// ///////////////////////////////////////////////////////////////
	private void checkCellFocusChanged() {
		final int selectedRow = getSelectedRow();
		final int selectedColunm = getSelectedColumn();
		if (selectedRow != this.lastSelectedRow || selectedColunm != this.lastSelectedColunm) {
			this.lastSelectedRow = getSelectedRow();
			final int focusLostColumn = this.lastSelectedColunm;
			this.lastSelectedColunm = getSelectedColumn();
			// if we put the above statment after fireCellFocusLost call , focus
			// lost will be called inifinitly
			fireCellFocusLost(this.lastSelectedRow, focusLostColumn);
			if (this.lastSelectedColunm == -1 || this.lastSelectedColunm == -1) {
				return;
			}
			fireCellFocusGained(this.lastSelectedRow, this.lastSelectedColunm);
			// if (selectedColunm != -1 && selectedRow != -1) {
			// if (isCellEditable(selectedColunm, selectedColunm)) {
			// editCellAt(selectedRow, selectedColunm);
			// }
			// }
		}
	}

	public void clearRecords() {
		getFsModel().clearRecords();
	}

	// ///////////////////////////////////////////////////////////////
	public void deleteRow(final int row) {
		getFsModel().deleteRow(row);
	}

	@Override
	public boolean editCellAt(final int row, final int column) {
		final boolean editCellAt = super.editCellAt(row, column);
		if (editCellAt) {
			// getEditorComponent().requestFocus();
		}
		return editCellAt;
	}

	// ///////////////////////////////////////////////////////////////
	private void fireCellFocusGained(final int row, final int col) {
		for (final CellFocusListener f : this.focusListsner) {
			f.focusGained(new CellFocusEvent(JKTable.this, row, col));
		}
	}

	// ///////////////////////////////////////////////////////////////
	private void fireCellFocusLost(final int row, final int col) {
		for (final CellFocusListener f : this.focusListsner) {
			f.focusLost(new CellFocusEvent(JKTable.this, row, col));
		}
	}

	public void fireTableCellUpdated(final int row, final int col) {
		getFsModel().fireTableCellUpdated(row, col);
	}

	public void fireTableColumnDataChanged(final int col) {
		getFsModel().fireTableColumnDataChanged(col);
	}

	public void fireTableDataChanged() {
		getFsModel().fireTableDataChanged();
	}

	// /**
	// *
	// */
	// protected void handleTabPressed() {
	// stopEditing();
	// transferFocus();
	// }

	public void fireTableStructureChanged() {
		getFsModel().fireTableStructureChanged();
	}

	// ////////////////////////////////////////////////////////////////////////////////
	private void fixWidth(final int column, final Component comp) {
		if (getColumnCount() <= column) {
			// TODO : check me
			return;
		}
		final int userColunmWidth = getFsModel().getPrefferedWidth(column);
		final int compWidth = comp.getPreferredSize().width;
		final TableColumn tableColumn = getColumnModel().getColumn(column);

		logger.debug("Fix width Column : ", column, getColumnName(column));
		logger.debug("User width ", userColunmWidth, "compWidth:", compWidth);
		logger.debug("Col reference:", tableColumn.toString(), " Col profered width : ", tableColumn.getPreferredWidth(), " , width: ",
				tableColumn.getWidth());
		if (userColunmWidth > tableColumn.getPreferredWidth()) {
			logger.debug("respect user width");
			tableColumn.setPreferredWidth(userColunmWidth);
		} else {
			comp.setPreferredSize(null);// to ignore value set by the caller
			if (compWidth > tableColumn.getPreferredWidth()) {
				// we add one for the fraction loss purpose
				logger.debug("approve component width", compWidth);
				tableColumn.setPreferredWidth(compWidth);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public TableCellEditor getCellEditor(final int row, final int column) {
		final TableCellEditor editor = getFsModel().getCellEditor(column);
		if (editor != null) {
			return editor;
		}
		return super.getCellEditor(row, column);

		// TableCellEditor editor;
		// if (getModel().getColumnClass(column) == Integer.class) {
		// editor = new DefaultCellEditor(new JKTextField(new
		// NumberDocument()));
		// }
		// if (getModel().getColumnClass(column) == Float.class) {
		// editor = new DefaultCellEditor(new JKTextField(new FloatDocument()));
		// } else {
		// editor = super.getCellEditor(row, column);
		// }
		// return editor;
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public TableCellRenderer getCellRenderer(final int row, final int column) {
		final TableCellRenderer renderer = getFsModel().getCellRenderer(column);
		if (renderer != null) {
			return renderer;
		}
		// System.out.println(getColumnClass(column));
		return super.getCellRenderer(row, column);
	}

	// ///////////////////////////////////////////////////////////////////
	public double getColunmSum(final int col) {
		double sum = 0;
		for (int i = 0; i < getRowCount(); i++) {
			sum += getValueAtAsDouble(i, col);
		}
		return sum;

	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getData() {
		return getFsModel().getRecordsAsDataVector();
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getDeletedRecords() {
		return getFsModel().getDeletedRecords();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getDeletedRows() {
		return getFsModel().getDeletedRecordsAsDataVector();
	}

	// // ///////////////////////////////////////////////////////////////////
	// private Format getColumnFormat(int col) {
	// Format format = getFsModel().getFormatter(col);
	// if (format != null) {
	// return format;
	// }
	// // make this method smart
	// return null;
	// }

	// ///////////////////////////////////////////////////////////
	public FSTableModel getFsModel() {
		if (getModel() instanceof FSTableModel) {
			return (FSTableModel) getModel();
		}
		throw new IllegalStateException(getModel().getClass().getName() + " is not instanceof FSTableModel");
	}

	public Vector<FSTableRecord> getModifiedRecords() {
		final Vector<FSTableRecord> modeifiedRecords = new Vector<FSTableRecord>();
		final Vector<FSTableRecord> records = getRecords();
		for (final FSTableRecord fsTableRecord : records) {
			if (fsTableRecord.getStatus().equals(RecordStatus.MODIFIED)) {
				modeifiedRecords.add(fsTableRecord);
			}
		}
		return modeifiedRecords;
	}

	public FSTableRecord getRecord(final int row) {
		return getFsModel().getRecord(row);
	}

	// ///////////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getRecords() {
		return getFsModel().getRecords();
	}

	public Object getValueAt(final int row, final int col, final boolean includeVisibleColumns) {
		if (includeVisibleColumns) {
			return getFsModel().getRecord(row).getColumnValue(col);
		}
		return getValueAt(row, col);
	}

	// ///////////////////////////////////////////////////////////////////
	public Date getValueAtAsDate(final int row, final int column) {
		final Object valueAt = getValueAt(row, column);
		if (valueAt == null) {
			return null;
		}
		if (valueAt instanceof Date) {
			return (Date) valueAt;
		}
		try {
			return DateTimeUtil.parseShortDate(valueAt.toString());
		} catch (final ParseException e) {
			return null;
		}
	}

	// ///////////////////////////////////////////////////////////////////
	public double getValueAtAsDouble(final int i, final int col) {
		final Object valueAt = getValueAtAsString(i, col);
		if (valueAt == null) {
			return 0;
		}
		return new Double(valueAt.toString());
	}

	// ///////////////////////////////////////////////////////////////////
	public int getValueAtAsInteger(final int row, final int col) {
		final Object valueAt = getValueAt(row, col);
		if (valueAt == null) {
			return 0;
		}
		if (valueAt instanceof Boolean) {
			return (Boolean) valueAt == true ? 1 : 0;
		}
		return new Integer(valueAt.toString());
	}

	// ///////////////////////////////////////////////////////////////////
	public java.sql.Date getValueAtAsSqlDate(final int row, final int col) {
		final Date date = getValueAtAsDate(row, col);
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	public String getValueAtAsString(final int row, final int column) {
		final Object valueAt = getValueAt(row, column);
		if (valueAt == null || valueAt.toString().trim().equals("")) {
			return null;
		}
		return valueAt.toString().trim();
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleDeleteRow() {
		if (isAllowDelete()) {
			final TableModel model = getModel();
			if (model instanceof FSTableModel) {
				final FSTableModel defaultModel = (FSTableModel) model;
				final int[] selectedRows = getSelectedRows();
				if (!isConfirmDelete()
						|| isConfirmDelete() && SwingUtility.showConfirmationDialog("YOU_ARE_ABOUT_TO_DELETE_THIS_ROW,ARE_YOU_SURE?")) {
					defaultModel.deleteRows(selectedRows);
					// for (int selectedRow : selectedRows) {
					// if (selectedRow >= 0) {
					// defaultModel.deleteRow(selectedRow);
					// setSelectedRow(selectedRow);
					// setSelectedColumn(0);
					// // if (defaultModel.getRowCount() == 1 &&
					// // isEditable())
					// // {
					// // addRow();
					// // }
					// }
					// }
				}
			}
		}
	}

	private void handleFocusGained() {
		// focused = true;
		checkCellFocusChanged();
		// if (getSelectedRow() == -1 && getRowCount() > 0) {
		// setRowSelectionInterval(0, 0);
		// changeSelection(0, 0, false, false);
		// }
	}

	// // ///////////////////////////////////////////////////////////
	protected void handleFocusLost() {
		checkCellFocusChanged();
		// focused = false;
		// stopEditing();
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleInsertRow() {
		if (isEditable()) {
			if (isAddNewRow()) {
				final TableModel model = getModel();
				if (model instanceof FSTableModel) {
					final FSTableModel defaultModel = (FSTableModel) model;
					final int selectedRow = getSelectedRow();
					defaultModel.insertRecord(selectedRow);
					setSelectedRow(selectedRow);
					setSelectedColumn(0);
				}
			}
		}
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleKeyDownPressed() {
		if (isEditable()) {
			// if last row
			if (getSelectedRow() == getModel().getRowCount() - 1) {
				if (isAddNewRow()) {
					addRow();
				}
			}
		}
	}

	// // ///////////////////////////////////////////////////////////////
	private void handleKeyReleased(final KeyEvent e) {
		// int keyCode = e.getKeyCode();
		// if (keyCode == KeyEvent.VK_DOWN) {
		// handleKeyDownPressed();
		// }
		// if (keyCode == KeyEvent.VK_DELETE && e.isControlDown()) {
		// handleDeleteRow();
		// }
		// if (keyCode == KeyEvent.VK_INSERT) {
		// handleInsertRow();
		// }
		checkCellFocusChanged();
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleMouseClicked() {
		checkCellFocusChanged();
	}

	/**
	 *
	 */
	protected void handleTableStuctorChanged() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				logger.debug("handleTableStuctorChanged");
				final FSTableModel model = getFsModel();
				for (int i = 0; i < model.getColumnCount(); i++) {
					final TableColumn column = getColumnModel().getColumn(i);
					int textWidth = SwingUtility.getTextWidth(model.getColumnName(i), true)+10;
					column.setMinWidth(textWidth);
					column.setPreferredWidth(textWidth);
				}
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////////
	private void init() {
		initTableHeader();

		// putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
		// putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		// setSurrendersFocusOnKeystroke(true);
		setAutoResizeMode(AUTO_RESIZE_OFF);
		// setFillsViewportHeight(true);
		setOpaque(false);
		setRowHeight(25);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setDefaultEditors();
		setDefaultRenderers();

		addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(final FocusEvent e) {
				handleFocusGained();
			}

			@Override
			public void focusLost(final FocusEvent e) {
				handleFocusLost();
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				handleKeyReleased(e);
			}
		});
		getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(final TableModelEvent e) {
				if (e.getType() == TableModelEvent.HEADER_ROW) {
					handleTableStuctorChanged();
				}
			}
		});
		// addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// handleMouseClicked();
		// }
		// });
		// addFocusListener(new FocusListener() {
		//
		// @Override
		// public void focusLost(FocusEvent e) {
		// System.out.println("table focus lost");
		// }
		//
		// @Override
		// public void focusGained(FocusEvent e) {
		// System.out.println("table focus gained");
		// }
		// });

		// Action actions = new AbstractAction() {
		// public void actionPerformed(ActionEvent ae) {
		// // This action will get fired on Enter Key
		// }
		// };

		// getInputMap(JInternalFrame.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
		// 0), "SOME_ACTION");
		// getInputMap(JInternalFrame.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
		// 0), "SOME_ACTION");
		// getActionMap().put("SOME_ACTION", actions);

	}

	private void initTableHeader() {
		getTableHeader().setDefaultRenderer(new FSTableHeaderRendere());
		getTableHeader().setPreferredSize(new Dimension(100, 30));
		getTableHeader().setBackground(Colors.MAIN_PANEL_BG);
	}

	// ///////////////////////////////////////////////////////////////
	private boolean isAddNewRow() {
		if (getRowCount() > 0) {
			final boolean validData = getFsModel().isAllDataValid();
			return validData;
		}
		return isAllowAddNew();
	}

	public boolean isAllowAddNew() {
		return this.allowAddNew;
	}

	// ///////////////////////////////////////////////////////////////
	public boolean isAllowDelete() {
		return getFsModel().isAllowDelete();
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return getFsModel().isEditable(row, column);
		// return getFsModel().isEditable(column);
	}

	// ///////////////////////////////////////////////////////////////
	public boolean isConfirmDelete() {
		return this.confirmDelete;
	}

	// ///////////////////////////////////////////////////////////////////
	public boolean isDataModified() {
		return getFsModel().isDataModified();
	}

	// ///////////////////////////////////////////////////////////////
	public boolean isEditable() {
		return getFsModel().isEditable();
	}

	public boolean isEditable(final int column) {
		return getFsModel().isEditable(column);
	}

	/**
	 * Move selection to first row , this method called by
	 * transferFocusToNextColunm which calculate the next ,
	 *
	 * @param row
	 * @param column
	 */
	protected void moveSelectionToRow(final int row, final int column) {
		setSelectionRow(row, column);
	}

	@Override
	public Component prepareEditor(final TableCellEditor editor, final int row, final int column) {
		final Component field = super.prepareEditor(editor, row, column);
		fixWidth(column, field);
		final FocusListener[] focusListeners = field.getFocusListeners();
		boolean listenerRegistred = false;
		for (final FocusListener focusListener : focusListeners) {
			if (focusListener instanceof TableFocusListener) {
				listenerRegistred = true;
			}
		}
		if (!listenerRegistred) {
			field.addFocusListener(this.tableFocusListener);
		}
		return field;
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
		final Component comp = super.prepareRenderer(renderer, row, column);
		fixWidth(column, comp);
		// if (comp instanceof JComponent) {
		// JComponent component = (JComponent) comp;
		// if (isEditable(column)) {
		// component.setBorder(BorderFactory.createLoweredBevelBorder());
		// } else {
		// component.setBorder(null);
		// }
		// }
		checkCellFocusChanged();
		return comp;
	}

	@Override
	public boolean processKeyBinding(final KeyStroke ks, final KeyEvent e, final int condition, final boolean pressed) {
		if (!SwingUtility.isLeftOrientation()) {// Switch right and left arrows
												// for arabic support
			if (ks == KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0)) {
				return super.processKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), e, condition, pressed);
			}
			if (ks == KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0)) {
				return super.processKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), e, condition, pressed);
			}
		}

		// if (ks == KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0)) {
		// if (isEditable() && isAddNewRow()) {
		// handleKeyDownPressed();
		// return false;
		// }
		// return
		// super.processKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
		// e, condition, pressed);
		// }
		// if (ks == KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0)) {
		// return
		// super.processKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0),
		// e, condition, pressed);
		// }
		// if (ks == KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0) &&
		// e.isControlDown()) {
		// handleDeleteRow();
		// return false;
		// }
		return super.processKeyBinding(ks, e, condition, pressed);
	}

	public void resetRecords() {
		getFsModel().resetRecords();
	}

	public void setAllowAddNew(final boolean allowAddNew) {
		this.allowAddNew = allowAddNew;
	}

	// ///////////////////////////////////////////////////////////////
	public void setAllowDelete(final boolean allowDelete) {
		getFsModel().setAllowDelete(allowDelete);
	}

	/*
	 *
	 */
	public void setAlowMutipleSelection(final boolean enable) {
		getSelectionModel().setSelectionMode(enable ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);

	}

	public void setColumnDateFormat(final int col, final String format) {
		getFsModel().setFormatter(col, new SimpleDateFormat(format));
	}

	private void setColumnFormat(final int col, final Format format) {
		getFsModel().setFormatter(col, format);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setColumnName(final int col, final String name) {
		getColumnModel().getColumn(col).setHeaderValue(name);
	}

	public void setColumnNumberFormat(final int col, final String pattern) {
		final DecimalFormat format = new DecimalFormat(pattern);
		setColumnFormat(col, format);

	}

	public void setColumnPrefereddWidth(final int col, final int width) {
		getFsModel().setPreferredWidth(col, width);
	}

	public void setColumnValue(final int row, final int col, final Object value) {
		setColumnValue(row, col, value, true);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	public void setColumnValue(final int row, final int col, final Object value, final boolean visibleIndex) {
		getFsModel().setColumnValue(row, col, value, visibleIndex);
	}

	// ///////////////////////////////////////////////////////////////////
	public boolean setColunmEditor(final int colunm, final BindingComponent comp) {
		final FSBindingComponentEditor cellEditor = new FSBindingComponentEditor(comp);
		getFsModel().setEditor(colunm, cellEditor);
		final BindingComponent copy = (BindingComponent) GeneralUtility.copy(comp);
		if (copy != null) {
			setColunmRenderer(colunm, copy);
			return true;
		}
		return false;
		// getTableHeader().getColumnModel().getColumn(colunm).setCellEditor(cellEditor2);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setColunmRenderer(final int col, final BindingComponent component) {
		final FSBindingComponentRenderer cellRenderer = new FSBindingComponentRenderer(component);
		getFsModel().setRenderer(col, cellRenderer);
		// getTableHeader().getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
	}

	// ///////////////////////////////////////////////////////////////////
	private void setDefaultEditors() {
		final FSTextField txtInteger = new FSTextField();
		final FSTextField txtAmount = new FSTextField(new FloatDocument());
		final FSTextField txt = new FSTextField();

		setDefaultEditor(Date.class, new FSBindingComponentEditor(new FSDate()));
		setDefaultEditor(Boolean.class, new FSBindingComponentEditor(new FSCheckBox()));
		txtInteger.setNumbersOnly(true);
		setDefaultEditor(Integer.class, new FSBindingComponentEditor(txtInteger));

		setDefaultEditor(Double.class, new FSBindingComponentEditor(txtAmount));
		setDefaultEditor(BigDecimal.class, new FSBindingComponentEditor(txtAmount));
		setDefaultEditor(String.class, new FSBindingComponentEditor(txt));
	}

	// ///////////////////////////////////////////////////////////////////
	protected void setDefaultRenderers() {
		final FSDefaultTableRenderer fsDefaultTableRenderer = new FSDefaultTableRenderer();
		setDefaultRenderer(Object.class, fsDefaultTableRenderer);
		setDefaultRenderer(Number.class, fsDefaultTableRenderer);
		setDefaultRenderer(Float.class, fsDefaultTableRenderer);
		setDefaultRenderer(Double.class, fsDefaultTableRenderer);
		setDefaultRenderer(Date.class, fsDefaultTableRenderer);
		setDefaultRenderer(Icon.class, fsDefaultTableRenderer);
		setDefaultRenderer(ImageIcon.class, fsDefaultTableRenderer);
		setDefaultRenderer(Boolean.class, fsDefaultTableRenderer);
		setDefaultRenderer(BigDecimal.class, fsDefaultTableRenderer);
		setDefaultRenderer(String.class, fsDefaultTableRenderer);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setEditable(final boolean editable) {
		getFsModel().setEditable(editable);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setEditable(final int column, final boolean editable) {
		getFsModel().setEditable(column, editable);
	}

	/**
	 * set editable in the cell level
	 *
	 * @param row
	 * @param col
	 * @param enable
	 */
	public void setEditable(final int row, final int col, final boolean enable) {
		getFsModel().setEditable(row, col, enable);
	}

	@Override
	public void setModel(final TableModel model) {
		logger.debug("setModel :", model != null ? model.getClass().getName() : "NULL");
		if (!(model instanceof FSTableModel)) {
			throw new IllegalStateException("FSTable only accept FSTableModel");
		}
		super.setModel(model);
		handleTableStuctorChanged();
	}

	// ///////////////////////////////////////////////////////////////
	public void setRequiredColumn(final int col, final boolean required) {
		getFsModel().setRequired(col, required);
	}

	// ///////////////////////////////////////////////////////////////////
	private void setSelectedColumn(final int col) {
		setColumnSelectionInterval(col, col);
	}

	/**
	 * @param column
	 */
	private void setSelectedColunm(final int column) {
		setColumnSelectionInterval(column, column);
	}

	/**
	 *
	 * @param index
	 */
	public void setSelectedRow(final int index) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				int newIndex = index;
				if (index >= getModel().getRowCount() || index == -1) {
					if (getModel().getRowCount() > 0) {
						newIndex = 0;
					}
					// do nothing
				} else {
				}
				if (newIndex >= 0 && getRowCount() > 0 && getColumnCount() > 0) {
					setRowSelectionInterval(newIndex, newIndex);
					scrollRectToVisible(getCellRect(newIndex, 0, true));
					// requestFocusInWindow();
				}
			}
		});
	}

	/**
	 *
	 * @param row
	 * @param column
	 */
	private void setSelectionRow(final int row, final int column) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (row >= getModel().getRowCount() || row == -1) {
					if (getModel().getRowCount() > 0) {
						setRowSelectionInterval(0, 0);
					}
					// do nothing
				} else {
					setRowSelectionInterval(row, row);
				}
				// TODO : Check if the commenting the following line has any
				// side effect
				// scrollRectToVisible(getCellRect(row, 0, true));
				setSelectedColunm(column);
				// TODO : Check if the commenting the following line has any
				// side effect
				// requestFocusInWindow();
			}
		});
	}

	// ///////////////////////////////////////////////////////////////////
	public void setVisible(final int col, final boolean visible) {
		getFsModel().setVisible(col, visible);
	}

	/**
	 *
	 */
	public void stopEditing() {
		if (getCellEditor() != null) {
			getCellEditor().stopCellEditing();
		}
	}

	@Override
	public void tableChanged(final TableModelEvent e) {
		super.tableChanged(e);
	}

	/**
	 *
	 */
	public void transferFocusToNextColunm() {
		int row = getSelectedRow();
		int column = getSelectedColumn() + 1;
		if (column == getColumnCount()) {
			column = 0;
			if (++row == getRowCount()) {
				row = 0;
			}
		}
		// if(isCellEditable(row, column)){
		// editCellAt(row, column);
		// }
		moveSelectionToRow(row, column);
	}

}
