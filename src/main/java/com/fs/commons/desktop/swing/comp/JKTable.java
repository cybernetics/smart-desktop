package com.fs.commons.desktop.swing.comp;

import java.awt.Component;
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

public class JKTable extends JTable {
	private final TableFocusListener tableFocusListener = new TableFocusListener();
	private static final long serialVersionUID = 1L;
	// ArrayList<Integer> editableColumns = new ArrayList<Integer>();
	Vector<CellFocusListener> focusListsner = new Vector<CellFocusListener>();
	// Vector<FSTableColumn> fsTableColunms;
	// private Vector<FSTableColumn> visibleColumns;
	private boolean confirmDelete = true;
	private int lastSelectedRow;
	private int lastSelectedColunm;
	// private boolean focused;
	private boolean allowAddNew;

	// private FSTableModel model;

	// ////////////////////////////////////////////////////////////////////////////
	public JKTable() {
		this(new FSTableModel());
	}

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
	public JKTable(TableModel dm) {
		super(dm);
		init();
	}

	// //
	// ////////////////////////////////////////////////////////////////////////////
	// public JKTable(Vector rowData, Vector columnNames) {
	// super(rowData, columnNames);
	// init();
	// }

	// ////////////////////////////////////////////////////////////////////////////
	private void init() {
		initTableHeader();		

//		putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
//		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
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
			public void focusGained(FocusEvent e) {
				handleFocusGained();
			}

			@Override
			public void focusLost(FocusEvent e) {
				handleFocusLost();
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				handleKeyReleased(e);
			}
		});
		getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
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
//		getTableHeader().setPreferredSize(new Dimension(0,30));
		getTableHeader().setBackground(Colors.MAIN_PANEL_BG);
	}

	// @Override
	// public void setModel(TableModel model) {
	// super.setModel(model);
	// }

	// ///////////////////////////////////////////////////////////
	public FSTableModel getFsModel() {
		if (getModel() instanceof FSTableModel) {
			return (FSTableModel) getModel();
		}
		throw new IllegalStateException(getModel().getClass().getName() + " is not instanceof FSTableModel");
	}

	// // ///////////////////////////////////////////////////////////
	protected void handleFocusLost() {
		checkCellFocusChanged();
		// focused = false;
		// stopEditing();
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public Component prepareRenderer(final TableCellRenderer renderer, final int row, final int column) {
		Component comp = super.prepareRenderer(renderer, row, column);
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

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public TableCellRenderer getCellRenderer(int row, int column) {
		TableCellRenderer renderer = getFsModel().getCellRenderer(column);
		if (renderer != null) {
			return renderer;
		}
		// System.out.println(getColumnClass(column));
		return super.getCellRenderer(row, column);
	}

	// ////////////////////////////////////////////////////////////////////////////
	@Override
	public TableCellEditor getCellEditor(int row, int column) {
		TableCellEditor editor = getFsModel().getCellEditor(column);
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

	@Override
	public Component prepareEditor(TableCellEditor editor, int row, int column) {
		final Component field = super.prepareEditor(editor, row, column);
		fixWidth(column, field);
		FocusListener[] focusListeners = field.getFocusListeners();
		boolean listenerRegistred = false;
		for (FocusListener focusListener : focusListeners) {
			if (focusListener instanceof TableFocusListener) {
				listenerRegistred = true;
			}
		}
		if (!listenerRegistred) {
			field.addFocusListener(tableFocusListener);
		}
		return field;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return getFsModel().isEditable(row, column);
		// return getFsModel().isEditable(column);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
	}

	@Override
	public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
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

	/**
	 * 
	 */
	public void stopEditing() {
		if (getCellEditor() != null) {
			getCellEditor().stopCellEditing();
		}
	}

	/*
	 * 
	 */
	public void setAlowMutipleSelection(boolean enable) {
		getSelectionModel().setSelectionMode(enable ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION : ListSelectionModel.SINGLE_SELECTION);

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

	// /**
	// *
	// */
	// protected void handleTabPressed() {
	// stopEditing();
	// transferFocus();
	// }

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

	/**
	 * Move selection to first row , this method called by
	 * transferFocusToNextColunm which calculate the next ,
	 * 
	 * @param row
	 * @param column
	 */
	protected void moveSelectionToRow(int row, int column) {
		setSelectionRow(row, column);
	}

	/**
	 * 
	 * @param row
	 * @param column
	 */
	private void setSelectionRow(final int row, final int column) {
		SwingUtilities.invokeLater(new Runnable() {
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

	/**
	 * @param column
	 */
	private void setSelectedColunm(int column) {
		setColumnSelectionInterval(column, column);
	}

	@Override
	public boolean editCellAt(int row, int column) {
		boolean editCellAt = super.editCellAt(row, column);
		if (editCellAt) {
			// getEditorComponent().requestFocus();
		}
		return editCellAt;
	}

	public void setColumnPrefereddWidth(int col, int width) {
		getFsModel().setPreferredWidth(col, width);
	}

	public void setColumnNumberFormat(int col, String pattern) {
		DecimalFormat format = new DecimalFormat(pattern);
		setColumnFormat(col, format);

	}

	private void setColumnFormat(int col, Format format) {
		getFsModel().setFormatter(col, format);
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

	// ///////////////////////////////////////////////////////////////////
	public double getValueAtAsDouble(int i, int col) {
		Object valueAt = getValueAtAsString(i, col);
		if (valueAt == null) {
			return 0;
		}
		return new Double(valueAt.toString());
	}

	// ///////////////////////////////////////////////////////////////////
	public int getValueAtAsInteger(int row, int col) {
		Object valueAt = getValueAt(row, col);
		if (valueAt == null) {
			return 0;
		}
		if (valueAt instanceof Boolean) {
			return ((Boolean) valueAt) == true ? 1 : 0;
		}
		return new Integer(valueAt.toString());
	}

	public String getValueAtAsString(int row, int column) {
		Object valueAt = getValueAt(row, column);
		if (valueAt == null || valueAt.toString().trim().equals("")) {
			return null;
		}
		return valueAt.toString().trim();
	}

	// ///////////////////////////////////////////////////////////////////
	public Date getValueAtAsDate(int row, int column) {
		Object valueAt = getValueAt(row, column);
		if (valueAt == null) {
			return null;
		}
		if (valueAt instanceof Date) {
			return (Date) valueAt;
		}
		try {
			return DateTimeUtil.parseShortDate(valueAt.toString());
		} catch (ParseException e) {
			return null;
		}
	}

	// ///////////////////////////////////////////////////////////////////
	public java.sql.Date getValueAtAsSqlDate(int row, int col) {
		Date date = getValueAtAsDate(row, col);
		if (date == null) {
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	// ///////////////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getDeletedRecords() {
		return getFsModel().getDeletedRecords();
	}

	// ///////////////////////////////////////////////////////////////////
	public void setColumnName(int col, String name) {
		getColumnModel().getColumn(col).setHeaderValue(name);
	}

	// ///////////////////////////////////////////////////////////////////
	public void addCellFocusListener(CellFocusListener cellFocusListener) {
		focusListsner.add(cellFocusListener);
	}

	// ///////////////////////////////////////////////////////////////////
	public double getColunmSum(int col) {
		double sum = 0;
		for (int i = 0; i < getRowCount(); i++) {
			sum += getValueAtAsDouble(i, col);
		}
		return sum;

	}

	// ///////////////////////////////////////////////////////////////////
	public void addRow() {
		addRows(1);
	}

	// ///////////////////////////////////////////////////////////////////
	private void setSelectedColumn(int col) {
		setColumnSelectionInterval(col, col);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setEditable(boolean editable) {
		getFsModel().setEditable(editable);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setEditable(int column, boolean editable) {
		getFsModel().setEditable(column, editable);
	}

	// ///////////////////////////////////////////////////////////////////
	public void setColunmRenderer(int col, BindingComponent component) {
		FSBindingComponentRenderer cellRenderer = new FSBindingComponentRenderer(component);
		getFsModel().setRenderer(col, cellRenderer);
		// getTableHeader().getColumnModel().getColumn(col).setCellRenderer(cellRenderer);
	}

	// ///////////////////////////////////////////////////////////////////
	public boolean setColunmEditor(int colunm, BindingComponent comp) {
		FSBindingComponentEditor cellEditor = new FSBindingComponentEditor(comp);
		getFsModel().setEditor(colunm, cellEditor);
		BindingComponent copy = (BindingComponent) GeneralUtility.copy(comp);
		if (copy != null) {
			setColunmRenderer(colunm, copy);
			return true;
		}
		return false;
		// getTableHeader().getColumnModel().getColumn(colunm).setCellEditor(cellEditor2);
	}

	// ///////////////////////////////////////////////////////////////////
	public boolean isDataModified() {
		return getFsModel().isDataModified();
	}

	// ///////////////////////////////////////////////////////////////
	public Vector<FSTableRecord> getRecords() {
		return getFsModel().getRecords();
	}

	// ///////////////////////////////////////////////////////////////
	public void setRequiredColumn(int col, boolean required) {
		getFsModel().setRequired(col, required);
	}

	// ///////////////////////////////////////////////////////////////
	private void fireCellFocusGained(final int row, final int col) {
		for (final CellFocusListener f : focusListsner) {
			f.focusGained(new CellFocusEvent(JKTable.this, row, col));
		}
	}

	// ///////////////////////////////////////////////////////////////
	private void fireCellFocusLost(final int row, final int col) {
		for (final CellFocusListener f : focusListsner) {
			f.focusLost(new CellFocusEvent(JKTable.this, row, col));
		}
	}

	// ///////////////////////////////////////////////////////////////
	private void checkCellFocusChanged() {
		int selectedRow = getSelectedRow();
		int selectedColunm = getSelectedColumn();
		if (selectedRow != lastSelectedRow || selectedColunm != lastSelectedColunm) {
			lastSelectedRow = getSelectedRow();
			int focusLostColumn = lastSelectedColunm;
			lastSelectedColunm = getSelectedColumn();
			// if we put the above statment after fireCellFocusLost call , focus
			// lost will be called inifinitly
			fireCellFocusLost(lastSelectedRow, focusLostColumn);
			if (lastSelectedColunm == -1 || lastSelectedColunm == -1) {
				return;
			}
			fireCellFocusGained(lastSelectedRow, lastSelectedColunm);
			// if (selectedColunm != -1 && selectedRow != -1) {
			// if (isCellEditable(selectedColunm, selectedColunm)) {
			// editCellAt(selectedRow, selectedColunm);
			// }
			// }
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////
	private void fixWidth(final int column, Component comp) {
		if (getColumnCount() <= column) {
			// TODO : check me
			return;
		}
		int userColunmWidth = getFsModel().getPrefferedWidth(column);
		TableColumn tableColumn = getColumnModel().getColumn(column);
		if (userColunmWidth > 0 && userColunmWidth > tableColumn.getPreferredWidth()) {
			tableColumn.setPreferredWidth(userColunmWidth);
		} else {
			comp.setPreferredSize(null);// to ignore value set by the caller
			int compWidth = comp.getPreferredSize().width+10 ;
			if (compWidth > tableColumn.getPreferredWidth()) {
				// we add one for the fraction loss purpose
				tableColumn.setPreferredWidth(compWidth);
			}
		}
	}

	// // ///////////////////////////////////////////////////////////////
	private void handleKeyReleased(KeyEvent e) {
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

	// ///////////////////////////////////////////////////////////////
	public boolean isEditable() {
		return getFsModel().isEditable();
	}

	// ///////////////////////////////////////////////////////////////
	private boolean isAddNewRow() {
		if (getRowCount() > 0) {
			boolean validData = getFsModel().isAllDataValid();
			return validData;
		}
		return isAllowAddNew();
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleMouseClicked() {
		checkCellFocusChanged();
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleDeleteRow() {
		if (isAllowDelete()) {
			TableModel model = getModel();
			if (model instanceof FSTableModel) {
				FSTableModel defaultModel = (FSTableModel) model;
				int[] selectedRows = getSelectedRows();
				if (!isConfirmDelete()
						|| (isConfirmDelete() && SwingUtility.showConfirmationDialog("YOU_ARE_ABOUT_TO_DELETE_THIS_ROW,ARE_YOU_SURE?"))) {
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

	// ///////////////////////////////////////////////////////////////
	public boolean isAllowDelete() {
		return getFsModel().isAllowDelete();
	}

	// ///////////////////////////////////////////////////////////////
	public void setAllowDelete(boolean allowDelete) {
		getFsModel().setAllowDelete(allowDelete);
	}

	// ///////////////////////////////////////////////////////////////
	public void deleteRow(int row) {
		getFsModel().deleteRow(row);
	}

	// ///////////////////////////////////////////////////////////////
	public boolean isConfirmDelete() {
		return confirmDelete;
	}

	// ///////////////////////////////////////////////////////////////
	protected void handleInsertRow() {
		if (isEditable()) {
			if (isAddNewRow()) {
				TableModel model = getModel();
				if (model instanceof FSTableModel) {
					FSTableModel defaultModel = (FSTableModel) model;
					int selectedRow = getSelectedRow();
					defaultModel.insertRecord(selectedRow);
					setSelectedRow(selectedRow);
					setSelectedColumn(0);
				}
			}
		}
	}

	public boolean isEditable(int column) {
		return getFsModel().isEditable(column);
	}

	// ///////////////////////////////////////////////////////////////////
	private void setDefaultEditors() {
		FSTextField txtInteger = new FSTextField();
		FSTextField txtAmount = new FSTextField(new FloatDocument());
		FSTextField txt = new FSTextField();

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
		FSDefaultTableRenderer fsDefaultTableRenderer = new FSDefaultTableRenderer();
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
	public void setVisible(int col, boolean visible) {
		getFsModel().setVisible(col, visible);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getData() {
		return getFsModel().getRecordsAsDataVector();
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public Vector<Vector> getDeletedRows() {
		return getFsModel().getDeletedRecordsAsDataVector();
	}

	public void resetRecords() {
		getFsModel().resetRecords();
	}

	public Object getValueAt(int row, int col, boolean includeVisibleColumns) {
		if (includeVisibleColumns) {
			return getFsModel().getRecord(row).getColumnValue(col);
		}
		return getValueAt(row, col);
	}

	private void handleFocusGained() {
		// focused = true;
		checkCellFocusChanged();
		// if (getSelectedRow() == -1 && getRowCount() > 0) {
		// setRowSelectionInterval(0, 0);
		// changeSelection(0, 0, false, false);
		// }
	}

	// ///////////////////////////////////////////////////////////////////////////////////////

	class TableFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			// focused = false;
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					stopEditing();
				}
			});
		}

		@Override
		public void focusGained(FocusEvent e) {
			// focused = true;
		}
	}

	public void setColumnDateFormat(int col, String format) {
		getFsModel().setFormatter(col, new SimpleDateFormat(format));
	}

	public boolean isAllowAddNew() {
		return allowAddNew;
	}

	public void setAllowAddNew(boolean allowAddNew) {
		this.allowAddNew = allowAddNew;
	}

	public void fireTableColumnDataChanged(int col) {
		getFsModel().fireTableColumnDataChanged(col);
	}

	public void fireTableCellUpdated(int row, int col) {
		getFsModel().fireTableCellUpdated(row, col);
	}

	public void setColumnValue(int row, int col, Object value) {
		setColumnValue(row, col, value, true);
	}

	public void setColumnValue(int row, int col, Object value, boolean visibleIndex) {
		getFsModel().setColumnValue(row, col, value, visibleIndex);
	}

	/**
	 * set editable in the cell level
	 * 
	 * @param row
	 * @param col
	 * @param enable
	 */
	public void setEditable(int row, int col, boolean enable) {
		getFsModel().setEditable(row, col, enable);
	}

	public void fireTableDataChanged() {
		getFsModel().fireTableDataChanged();
	}

	public void addFSTableColumn(FSTableColumn col) {
		getFsModel().addFSTableColumn(col);
	}

	public void clearRecords() {
		getFsModel().clearRecords();
	}

	public void addRows(int size) {
		for (int i = 0; i < size; i++) {
			TableModel model = getModel();
			if (model instanceof FSTableModel) {
				FSTableModel defaultModel = (FSTableModel) model;
				defaultModel.addRecord();
				setSelectedRow(model.getRowCount() - 1);
				setSelectedColumn(0);
			}
		}

	}

	public void setModel(TableModel model) {
		if (!(model instanceof FSTableModel)) {
			throw new IllegalStateException("FSTable only accept FSTableModel");
		}
		super.setModel(model);
		handleTableStuctorChanged();
	}

	public void fireTableStructureChanged() {
		getFsModel().fireTableStructureChanged();
	}

	/**
	 * 
	 */
	protected void handleTableStuctorChanged() {
		FSTableModel model = getFsModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			TableColumn column = getColumnModel().getColumn(i);
			column.setMinWidth(SwingUtility.getTextWidth(model.getColumnName(i),true)+15);
			// column.setPreferredWidth(column.getWidth()+20);
		}
	}

	public FSTableRecord getRecord(int row) {
		return getFsModel().getRecord(row);
	}

	public Vector<FSTableRecord> getModifiedRecords() {
		Vector<FSTableRecord> modeifiedRecords = new Vector<FSTableRecord>();
		Vector<FSTableRecord> records = getRecords();
		for (FSTableRecord fsTableRecord : records) {
			if(fsTableRecord.getStatus().equals(RecordStatus.MODIFIED)){
				modeifiedRecords.add(fsTableRecord);
			}
		}
		return modeifiedRecords;
	}


}
