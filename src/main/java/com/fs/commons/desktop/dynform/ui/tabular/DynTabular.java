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
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
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
import com.jk.exceptions.handler.JKExceptionUtil;

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

	public DynTabular(final String tableName) throws JKRecordNotFoundException, TableMetaNotFoundException, JKDataAccessException {
		this(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	/**
	 *
	 * @param meta
	 * @throws JKRecordNotFoundException
	 * @throws JKDataAccessException
	 */
	public DynTabular(final TableMeta meta) throws JKRecordNotFoundException, JKDataAccessException {
		this.meta = meta;
		init();
	}

	public void addFSTableColumn(final FSTableColumn col) {
		this.tbl.addFSTableColumn(col);
	}

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

	/**
	 *
	 */
	public void createEmptyRecords(final int numberOfRecords) {
		for (int i = 0; i < numberOfRecords; i++) {
			this.model.addRecord();
		}
	}

	/**
	 *
	 */
	private void createRecord() {
		// int count =
		// SwingUtility.showIntegerInput("PLEASE_ENTER_NUMBER_OF_RECORDS");
		// if (count > 0) {
		this.model.addRecord();
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
	 *
	 */
	private void deleteSelectedRecords() {
		final int[] selectedRow = this.tbl.getSelectedRows();
		for (int i = selectedRow.length - 1; i >= 0; i--) {
			final int row = selectedRow[i];
			this.model.deleteRow(row);
			this.tbl.setSelectedRow(row);
		}

	}

	public void fireTableCellUpdated(final int row, final int col) {
		this.tbl.fireTableCellUpdated(row, col);
	}

	public void fireTableColumnDataChanged(final int col) {
		this.tbl.fireTableColumnDataChanged(col);
	}

	public void fireTableDataChanged() {
		this.tbl.fireTableDataChanged();
	}

	public void fireTableStructureChanged() {
		this.tbl.fireTableStructureChanged();
	}

	public List<Record> getAllDaoRecords() throws TableMetaNotFoundException, JKDataAccessException {
		return this.model.getAllDaoRecords();
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
	 * @return
	 */
	public Vector<FSTableRecord> getAllRecords() {
		return this.model.getRecords();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> getButtonsPanel() {
		if (this.pnlButtons == null) {
			this.pnlButtons = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));

			this.pnlButtons.setLayout(new BoxLayout(this.pnlButtons, BoxLayout.PAGE_AXIS));

			this.btnAddRecord.setShortcut("control S", "Ctrl S");
			this.btnAddRecord.setFocusable(false);
			this.btnDeleteRecord.setFocusable(false);
			this.btnDeleteRecord.setShortcut("control D", "Ctrl D");
			this.pnlButtons.add(this.btnAddRecord);
			this.pnlButtons.add(this.btnDeleteRecord);
			this.pnlButtons.add(this.btnReloadTabular);

			this.btnAddRecord.setIcon("add_something_smart_org_icon.gif");
			this.btnDeleteRecord.setIcon("delete_something_smart_org_icon.gif");

			// JKPanel<?> pnl2 = new JKPanel<Object>(new
			// FlowLayout(FlowLayout.TRAILING));
			this.pnlButtons.add(this.btnSaveTabular);

			// JKPanel<?> pnl3 = new JKPanel<Object>(new java.awt.GridLayout(1,
			// 2));
			// pnl3.add(pnl1);
			// pnl3.add(pnl2);
		}
		return this.pnlButtons;
	}

	/**
	 *
	 * @param column
	 * @return
	 */
	public double getColunmSum(final int column) {
		return this.model.getColunmSum(column);
	}

	public FSTableModel getModel() {
		return this.model;
	}

	public DynMetaModel getModelAsMetaModel() {
		// TODO
		return null;
	}

	public FSTableRecord getRecord(final int row) {
		return getModel().getRecord(row);
	}

	public Vector<FSTableRecord> getRecords() {
		return this.tbl.getRecords();
	}

	public int getRowCount() {
		return this.tbl.getRowCount();
	}

	private Component getSumPanel() {
		this.pnlSum = new JKPanel<Object>();
		return this.pnlSum;
	}

	/**
	 * @return the tbl
	 */
	public JKTable getTable() {
		return this.tbl;
	}

	public double getValueAtAsDouble(final int row, final int col) {
		return this.tbl.getValueAtAsDouble(row, col);
	}

	public int getValueAtAsInteger(final int row, final int col) {
		return this.tbl.getValueAtAsInteger(row, col);
	}

	/**
	 * @param e
	 */
	private void handleKeyListener(final KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_DELETE) {
			this.btnDeleteRecord.doClick();
		}
		if (e.getKeyCode() == KeyEvent.VK_INSERT) {
			this.btnAddRecord.doClick();
		}
	}

	/**
	 *
	 */
	protected void handleSaveTabular() {
		final boolean reload = true;
		try {
			this.tbl.stopEditing();
			validateData();
			// TODO : add save action
			// model.getDao().saveRecords(getAllRecords());
			SwingUtility.showSuccessDialog("DATA_UPDATED_SUCC");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		} finally {
			if (reload) {
				reloadData(false);
			}
		}
	}

	/**
	 *
	 */
	protected void handleTableDataChanged() {
		final Enumeration<Integer> keys = this.components.keys();
		while (keys.hasMoreElements()) {
			final Integer col = keys.nextElement();
			this.components.get(col).setText(this.model.getColunmSum(col) + "");
		}
	}

	public void hideAllSum() {
		this.components.clear();
		this.pnlSum.removeAll();
		this.pnlSum.invalidate();
		this.pnlSum.repaint();
	}

	/**
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 *
	 */
	private void init() throws TableMetaNotFoundException, JKDataAccessException {
		setFocusable(false);
		initTable();
		setBorder(SwingUtility.createTitledBorder(""));
		setLayout(new BorderLayout());
		add(new JKScrollPane(this.tbl), BorderLayout.CENTER);
		add(getButtonsPanel(), BorderLayout.LINE_END);
		add(getSumPanel(), BorderLayout.SOUTH);
		this.btnSaveTabular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSaveTabular();
			}
		});
		this.btnReloadTabular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				reloadData(true);
			}
		});
		this.btnAddRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				createRecord();
			}
		});
		this.btnDeleteRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				deleteSelectedRecords();
			}
		});
		this.model.addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(final TableModelEvent e) {
				handleTableDataChanged();
			}
		});
	}

	private void initTable() throws TableMetaNotFoundException, JKDataAccessException {
		this.model = new TableMetaModel(this.meta);
		this.tbl.setModel(this.model);
		// tbl.putClientProperty("JTable.autoStartsEdit", Boolean.TRUE);
	}

	/**
	 * @return the allowCreate
	 */
	public boolean isAllowCreate() {
		return this.allowCreate;
	}

	/**
	 * @return the allowDelete
	 */
	public boolean isAllowDelete() {
		return this.allowDelete;
	}

	/**
	 * @return the allowReload
	 */
	public boolean isAllowReload() {
		return this.allowReload;
	}

	/**
	 * @return the allowSave
	 */
	public boolean isAllowSave() {
		return this.allowSave;
	}

	/**
	 * @param b
	 *
	 */
	private void reloadData(final boolean showMessage) {
		try {
			// model.reload();
			if (showMessage) {
				// SwingUtility.showSuccessDialog("TABULAR_DATA_RELOADED_SUCC");
			}
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	public void reloadRecords() throws JKRecordNotFoundException, JKDataAccessException {
		// model.reload();
	}

	/**
	 * @param allowCreate
	 *            the allowCreate to set
	 */
	public void setAllowCreate(final boolean allowCreate) {
		this.allowCreate = allowCreate;
		this.btnAddRecord.setVisible(allowCreate);
	}

	/**
	 * @param allowDelete
	 *            the allowDelete to set
	 */
	public void setAllowDelete(final boolean allowDelete) {
		this.allowDelete = allowDelete;
		this.btnDeleteRecord.setVisible(allowDelete);
	}

	/**
	 * @param allowReload
	 *            the allowReload to set
	 */
	public void setAllowReload(final boolean allowReload) {
		this.allowReload = allowReload;
		this.btnReloadTabular.setVisible(allowReload);
	}

	/**
	 * @param allowSave
	 *            the allowSave to set
	 */
	public void setAllowSave(final boolean allowSave) {
		this.allowSave = allowSave;
		this.btnSaveTabular.setVisible(allowSave);
	}

	public void setColumnValue(final int row, final int col, final Object value, final boolean visibleIndex) {
		this.tbl.setColumnValue(row, col, value, visibleIndex);
	}

	public void setEditableCell(final int row, final int col, final boolean enable) {
		this.tbl.setEditable(row, col, enable);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		this.tbl.setEnabled(enabled);
		this.btnAddRecord.setVisible(isAllowCreate() && enabled);
		this.btnDeleteRecord.setVisible(isAllowDelete() && enabled);
		this.btnReloadTabular.setVisible(isAllowReload() && enabled);
		this.btnSaveTabular.setVisible(isAllowSave() && enabled);
	}

	/**
	 *
	 * @param string
	 * @param trxId
	 * @throws JKDataAccessException
	 * @throws JKRecordNotFoundException
	 */
	public void setFilterValue(final String fieldName, final String trxId) throws JKRecordNotFoundException, JKDataAccessException {
		this.model.setFilterValue(fieldName, trxId);
		// model.reload();
	}

	/**
	 * @param showSum
	 *            the showSum to set
	 */
	public void setShowSum(final int colunmIndex) {
		// if (model.isNumericClumn(colunmIndex)) {
		final JKTextField txt = new JKTextField(12, false);
		txt.setFocusable(false);
		final String name = this.model.getColumnName(colunmIndex);
		this.components.put(colunmIndex, txt);
		final JKLabel lbl = new JKLabel(name, false);
		this.pnlSum.add(new JKLabledComponent(lbl, txt));
		// }
	}

	public void setValueAt(final Object value, final int row, final int col) {
		this.tbl.setValueAt(value, row, col);
	}

	// /**
	// *
	// * @param b
	// */
	// public void setShowButtons(boolean show) {
	// btnSaveTabular.setVisible(show);
	// btnReloadTabular.setVisible(show);
	// }

	public void stopEditing() {
		this.tbl.stopEditing();
	}

	/**
	 * @throws ValidationException
	 *
	 */
	public void validateData() throws ValidationException {
		final Vector<FSTableRecord> records = this.model.getRecords();
		for (int i = 0; i < records.size(); i++) {
			final FSTableRecord record = records.get(i);
			if (record.isModified()) {
				try {
					final Record rec = this.meta.createEmptyRecord();
					rec.setValues(record.toHash());
					this.meta.validateData(rec);
				} catch (final ValidationException e) {
					this.tbl.setSelectedRow(i);
					this.tbl.editCellAt(i, 0);
					throw e;
				}
			}
		}
	}
}
