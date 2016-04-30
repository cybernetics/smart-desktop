package com.fs.commons.desktop.dynform.ui.tabular;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKTable;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

public class DynTabular extends JKMainPanel {

	private static final long serialVersionUID = 1L;
	TableMetaModel model;
	JKTable tbl = new JKTable();// new DynTable();
	private boolean allowDelete = true;
	private boolean allowCreate = true;
	private boolean allowReload = true;
	private boolean allowSave = true;
	// private DynMetaModel model;
	private final TableMeta meta;

	JKButton btnAddRecord = new JKButton("");
	JKButton btnDeleteRecord = new JKButton("");
	JKButton btnSaveTabular = new JKButton("", "alt S");
	JKButton btnReloadTabular = new JKButton("", "alt R");
	Hashtable<Integer, JKTextField> components = new Hashtable<Integer, JKTextField>();
	JKPanel<?> pnlSum = new JKPanel<Object>();
	private JKPanel<Object> pnlButtons;

	/**
	 * 
	 * @param meta
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	public DynTabular(TableMeta meta) throws RecordNotFoundException, DaoException {
		this.meta = meta;
		init();
	}

	public DynTabular(String tableName) throws RecordNotFoundException, TableMetaNotFoundException, DaoException {
		this(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	/**
	 * 
	 */
	public void createEmptyRecords(int numberOfRecords) {
		for (int i = 0; i < numberOfRecords; i++) {
			model.addRecord();
		}
	}

	/**
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * 
	 */
	private void init() throws TableMetaNotFoundException, DaoException {
		setFocusable(false);
		initTable();
		setBorder(SwingUtility.createTitledBorder(""));
		setLayout(new BorderLayout());
		add(new JKScrollPane(tbl), BorderLayout.CENTER);
		add(getButtonsPanel(), BorderLayout.LINE_END);
		add(getSumPanel(), BorderLayout.SOUTH);
		btnSaveTabular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleSaveTabular();
			}
		});
		btnReloadTabular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				reloadData(true);
			}
		});
		btnAddRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				createRecord();
			}
		});
		btnDeleteRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSelectedRecords();
			}
		});
		model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				handleTableDataChanged();
			}
		});
	}

	private void initTable() throws TableMetaNotFoundException, DaoException {
		model = new TableMetaModel(meta);
		tbl.setModel(model);
//		tbl.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
	}

	private Component getSumPanel() {
		pnlSum = new JKPanel<Object>();
		return pnlSum;
	}

	/**
	 * 
	 */
	protected void handleSaveTabular() {
		boolean reload = true;
		try {
			tbl.stopEditing();
			validateData();
			// TODO : add save action
			// model.getDao().saveRecords(getAllRecords());
			SwingUtility.showSuccessDialog("DATA_UPDATED_SUCC");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		} finally {
			if (reload) {
				reloadData(false);
			}
		}
	}

	/**
	 * @return
	 */
	public Vector<FSTableRecord> getAllRecords() {
		return model.getRecords();
	}
	
	public List<Record> getAllDaoRecords() throws TableMetaNotFoundException, DaoException {
		return model.getAllDaoRecords();
	}

	/**
	 * @throws ValidationException
	 * 
	 */
	public void validateData() throws ValidationException {
		Vector<FSTableRecord> records = model.getRecords();
		for (int i = 0; i < records.size(); i++) {
			FSTableRecord record = records.get(i);
			if (record.isModified()) {
				try {
					Record rec = meta.createEmptyRecord();
					rec.setValues(record.toHash());
					meta.validateData(rec);
				} catch (ValidationException e) {
					tbl.setSelectedRow(i);
					tbl.editCellAt(i, 0);
					throw e;
				}
			}
		}
	}

	/**
	 * @param b
	 * 
	 */
	private void reloadData(boolean showMessage) {
		try {
			// model.reload();
			if (showMessage) {
				// SwingUtility.showSuccessDialog("TABULAR_DATA_RELOADED_SUCC");
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getButtonsPanel() {
		if (pnlButtons == null) {
			pnlButtons = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));

			pnlButtons.setLayout(new BoxLayout(pnlButtons, BoxLayout.PAGE_AXIS));

			btnAddRecord.setShortcut("control S", "Ctrl S");
			btnAddRecord.setFocusable(false);
			btnDeleteRecord.setFocusable(false);
			btnDeleteRecord.setShortcut("control D", "Ctrl D");
			pnlButtons.add(btnAddRecord);
			pnlButtons.add(btnDeleteRecord);
			pnlButtons.add(btnReloadTabular);

			btnAddRecord.setIcon("add_something_smart_org_icon.gif");
			btnDeleteRecord.setIcon("delete_something_smart_org_icon.gif");

			// JKPanel<?> pnl2 = new JKPanel<Object>(new
			// FlowLayout(FlowLayout.TRAILING));
			pnlButtons.add(btnSaveTabular);

			// JKPanel<?> pnl3 = new JKPanel<Object>(new java.awt.GridLayout(1,
			// 2));
			// pnl3.add(pnl1);
			// pnl3.add(pnl2);
		}
		return pnlButtons;
	}

	// /**
	// * @throws RecordNotFoundException
	// * @throws DaoException
	// */
	// private void initTable() throws RecordNotFoundException, DaoException {
	// //model = new DynMetaModel(this.meta, false);
	// //tbl.setModel(model);
	// // tbl.setAlowMutipleSelection(false);
	// ArrayList<FieldMeta> visibleFields = meta.getVisibleFields();
	// for (int i = 0; i < visibleFields.size(); i++) {
	// }
	// tbl.addKeyListener(new KeyAdapter() {
	// @Override
	// public void keyPressed(KeyEvent e) {
	// handleKeyListener(e);
	// }
	// });
	// }

	/**
	 * @return the tbl
	 */
	public JKTable getTable() {
		return tbl;
	}

	/**
	 * @param e
	 */
	private void handleKeyListener(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			btnDeleteRecord.doClick();
		}
		if (e.getKeyCode() == KeyEvent.VK_INSERT) {
			btnAddRecord.doClick();
		}
	}

	/**
	 * 
	 */
	private void deleteSelectedRecords() {
		int[] selectedRow = tbl.getSelectedRows();
		for (int i = selectedRow.length - 1; i >= 0; i--) {
			int row = selectedRow[i];
			model.deleteRow(row);
			tbl.setSelectedRow(row);
		}

	}

	/**
	 * 
	 */
	private void createRecord() {
		// int count =
		// SwingUtility.showIntegerInput("PLEASE_ENTER_NUMBER_OF_RECORDS");
		// if (count > 0) {
		model.addRecord();
		// SwingUtilities.invokeLater(new Runnable() {
		// @Override
		// public void run() {
		// tbl.setSelectedRow(model.getRowCount() - 1);
		// tbl.editCellAt(model.getRowCount() - 1, 0);
		// }
		// });
		// }
	}

	/**
	 * @return the allowDelete
	 */
	public boolean isAllowDelete() {
		return allowDelete;
	}

	/**
	 * @param allowDelete
	 *            the allowDelete to set
	 */
	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
		btnDeleteRecord.setVisible(allowDelete);
	}

	/**
	 * @return the allowCreate
	 */
	public boolean isAllowCreate() {
		return allowCreate;
	}

	/**
	 * @param allowCreate
	 *            the allowCreate to set
	 */
	public void setAllowCreate(boolean allowCreate) {
		this.allowCreate = allowCreate;
		btnAddRecord.setVisible(allowCreate);
	}

	/**
	 * @return the allowReload
	 */
	public boolean isAllowReload() {
		return allowReload;
	}

	/**
	 * @param allowReload
	 *            the allowReload to set
	 */
	public void setAllowReload(boolean allowReload) {
		this.allowReload = allowReload;
		btnReloadTabular.setVisible(allowReload);
	}

	/**
	 * @return the allowSave
	 */
	public boolean isAllowSave() {
		return allowSave;
	}

	/**
	 * @param allowSave
	 *            the allowSave to set
	 */
	public void setAllowSave(boolean allowSave) {
		this.allowSave = allowSave;
		btnSaveTabular.setVisible(allowSave);
	}

	/**
	 * 
	 * @param string
	 * @param trxId
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public void setFilterValue(String fieldName, String trxId) throws RecordNotFoundException, DaoException {
		 model.setFilterValue(fieldName, trxId);
		 //model.reload();
	}

	public void stopEditing() {
		tbl.stopEditing();
	}

	/**
	 * @param showSum
	 *            the showSum to set
	 */
	public void setShowSum(int colunmIndex) {
		// if (model.isNumericClumn(colunmIndex)) {
		JKTextField txt = new JKTextField(12, false);
		txt.setFocusable(false);
		String name = model.getColumnName(colunmIndex);
		components.put(colunmIndex, txt);
		JKLabel lbl = new JKLabel(name, false);
		pnlSum.add(new JKLabledComponent(lbl, txt));
		// }
	}

	/**
	 * 
	 */
	protected void handleTableDataChanged() {
		Enumeration<Integer> keys = components.keys();
		while (keys.hasMoreElements()) {
			Integer col = keys.nextElement();
			components.get(col).setText(model.getColunmSum(col) + "");
		}
	}

	/**
	 * 
	 * @param column
	 * @return
	 */
	public double getColunmSum(int column) {
		return model.getColunmSum(column);
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tbl.setEnabled(enabled);
		btnAddRecord.setVisible(isAllowCreate() && enabled);
		btnDeleteRecord.setVisible(isAllowDelete() && enabled);
		btnReloadTabular.setVisible(isAllowReload() && enabled);
		btnSaveTabular.setVisible(isAllowSave() && enabled);
	}

	public FSTableModel getModel() {
		return model;
	}
	
	public DynMetaModel getModelAsMetaModel() {
		//TODO
		return null;
	}

	public void reloadRecords() throws RecordNotFoundException, DaoException {
		// model.reload();
	}

	public void hideAllSum() {
		components.clear();
		pnlSum.removeAll();
		pnlSum.invalidate();
		pnlSum.repaint();
	}

	public FSTableRecord getRecord(int row) {
		return getModel().getRecord(row);
	}

	public void setValueAt(Object value, int row, int col) {
		tbl.setValueAt(value, row, col);
	}

	public Vector<FSTableRecord> getRecords() {
		return tbl.getRecords();
	}

	public int getRowCount() {
		return tbl.getRowCount();
	}

	public void fireTableColumnDataChanged(int col) {
		tbl.fireTableColumnDataChanged(col);
	}

	public void fireTableCellUpdated(int row, int col) {
		tbl.fireTableCellUpdated(row, col);
	}

	public void setColumnValue(int row, int col, Object value, boolean visibleIndex) {
		tbl.setColumnValue(row, col, value, visibleIndex);
	}

	public int getValueAtAsInteger(int row, int col) {
		return tbl.getValueAtAsInteger(row, col);
	}

	public double getValueAtAsDouble(int row, int col) {
		return tbl.getValueAtAsDouble(row, col);
	}

	public void setEditableCell(int row, int col, boolean enable) {
		tbl.setEditable(row, col, enable);
	}

	public void fireTableDataChanged() {
		tbl.fireTableDataChanged();
	}

	public void addFSTableColumn(FSTableColumn col) {
		tbl.addFSTableColumn(col);
	}

	// /**
	// *
	// * @param b
	// */
	// public void setShowButtons(boolean show) {
	// btnSaveTabular.setVisible(show);
	// btnReloadTabular.setVisible(show);
	// }

	// /**
	// *
	// * @author user
	// *
	// */
	// class DynTable extends JKTable {
	// private static final long serialVersionUID = 1L;
	//
	// @Override
	// public TableCellEditor getCellEditor(int row, int column) {
	// return new DynCellFactory.DynFieldEditor(model.getVisibleField(column));
	// }
	//
	// public TableCellRenderer getCellRenderer(int row, int column) {
	// return new DynCellFactory.DynFieldEditor(model.getVisibleField(column));
	// };
	//
	// protected void moveSelectionToRow(int row, int column) {
	// if (row == 0 && column == 0) {
	// // createRecord();
	// } else {
	// super.moveSelectionToRow(row, column);
	// }
	// };
	// }
	//
	// public Record getRecord(int row) {
	// return getModel().getRecord(row);
	// }
	//
	public void clearRecords() {
		getModel().clearRecords();
	}

	public void fireTableStructureChanged() {
		tbl.fireTableStructureChanged();
	}
}
