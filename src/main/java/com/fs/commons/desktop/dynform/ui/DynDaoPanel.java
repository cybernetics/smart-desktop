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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.commons.desktop.swing.dialogs.QueryDialog;
import com.fs.commons.desktop.swing.printing.PrintUtilities;
import com.fs.commons.util.ExceptionUtil;

/**
 * contains DynPanel with Add,Edit,update,Delete buttons
 *
 * @author u087
 *
 */
public class DynDaoPanel extends JKMainPanel implements DynDaoActionListener, BindingComponent {

	public enum DynDaoMode {
		ADD, // Add Record
		VIEW, // View the record with disabled fields
		EDIT, // View the record with enabled fields
		UPDATE, // View the record and allow update
		DELETE, // View the record and allow delete
		FIND, DUPLICATE
		// allow find
	}

	private static final long serialVersionUID = 1L;;

	DynDaoMode mode;

	RecordTraversePolicy traversePolicy;

	final TableMeta tableMeta;

	String newId;

	boolean allowFind = false;

	boolean allowClose = true;

	boolean allowClear = true;

	boolean allowAdd = true;

	boolean allowEdit = true;

	boolean allowDuplicate = false;

	boolean allowImport = false;

	JKButton btnAdd = new JKButton("ADD_RECORD");

	JKButton btnClose = new JKButton("CLOSE");

	JKButton btnEdit = new JKButton("EDIT");

	JKButton btnSave = new JKButton("SAVE");
	JKButton btnDuplicate = new JKButton("DUPLICTE");

	JKButton btnDelete = new JKButton("DELETE");

	JKButton btnClear = new JKButton("CLEAR");

	JKButton btnCancelEdit = new JKButton("CANCEL_EDIT");

	JKButton btnFind = new JKButton("FIND");

	JKButton btnImport = new JKButton("IMPORT");

	JKButton btnPrint = new JKButton("PRINT");
	JKButton btnHistory = new JKButton("HISTORY");

	JKPanel pnlTraverse;

	JKButton btnFirstRecord = new JKButton("FIRST_RECORD", "alt F");
	JKButton btnNextRecord = new JKButton("NEXT_RECORD", "alt N");
	JKButton btnPreviouseRecord = new JKButton("PREVIOUSE_RECORD", "alt P");
	JKButton btnLastRecord = new JKButton("LAST_RECORD", "alt L");

	DynPanel pnlDao;

	private JKPanel pnlEmpty;

	private JKPanel pnlButtonsContainer;

	private JKPanel pnlActionButtons;

	private Record duplicatedRecord;

	private boolean allowHistory;

	private boolean allowPrint;

	private boolean showRecordAfterAdd = false;

	/**
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 */
	public DynDaoPanel(final DynPanel dynPanel) throws TableMetaNotFoundException, DaoException {
		this.tableMeta = dynPanel.getTableMeta();
		this.pnlDao = dynPanel;
		this.pnlDao.addDynDaoActionListener(this);
		init();
		setMode(DynDaoMode.ADD);
		setTraversePolicy(null);
	}

	public DynDaoPanel(final String tableName) throws TableMetaNotFoundException, DaoException {
		this(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	/**
	 *
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynDaoPanel(final TableMeta tableMeta) throws TableMetaNotFoundException, DaoException {
		this(new DynPanel(tableMeta));
	}

	public void addButton(final JButton btn) {
		this.pnlActionButtons.add(btn);
	}

	/**
	 *
	 */
	public void addDynDaoActionListener(final DynDaoActionListener listener) {
		this.pnlDao.addDynDaoActionListener(listener);
	}

	/**
	 * Override the DaoActionListner
	 */
	@Override
	public void afterAddRecord(final Record record) throws DaoException {
		// SwingUtility.showSuccessDialog("SUCC_RECORD_ADDED");
		if (this.showRecordAfterAdd) {
			handleFindRecord(record.getIdValue());
		} else {
			setMode(DynDaoMode.ADD);
		}
	}

	/**
	 *
	 */
	@Override
	public void afterClosePanel() {
		setVisible(false);
	}

	/**
	 *
	 */
	@Override
	public void afterDeleteRecord(final Record record) throws DaoException {
		setMode(DynDaoMode.ADD);
	}

	/**
	 *
	 */
	@Override
	public void afterResetComponents() {
	}

	// /////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////
	/**
	 *
	 */
	@Override
	public void afterSetMode(final DynDaoMode mode) {
		if (mode == DynDaoMode.EDIT) {
			this.pnlDao.requestFocus();
		}
	}

	@Override
	public void afterUpdateRecord(final Record record) throws DaoException {
		this.pnlDao.handleFindEvent(record.getIdValue());
	}

	@Override
	public void beforeAddRecord(final Record record) {
	}

	@Override
	public void beforeClosePanel() {
	}

	@Override
	public void beforeDeleteRecord(final Record record) throws DaoException {
	}

	@Override
	public void beforeResetComponents(final Record record) {
	}

	/**
	 *
	 */
	@Override
	public void beforeSetMode(final DynDaoMode mode) {
		if (mode == DynDaoMode.ADD || mode == DynDaoMode.DUPLICATE) {
			try {
				resetComponents();
				this.pnlDao.requestFocus();
			} catch (final DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	@Override
	public void beforeUpdateRecord(final Record record) throws DaoException {
	}

	/**
	 *
	 */
	private void checkPnlTraverseEnability() {
		if (this.pnlTraverse.isVisible()) {
			final int currentId = getIdValueAsInteger();
			this.btnFirstRecord.setEnabled(this.traversePolicy.getFirstRecord() != currentId);
			this.btnLastRecord.setEnabled(this.traversePolicy.getLastRecord() != currentId);
			this.btnNextRecord.setEnabled(this.btnLastRecord.isEnabled());
			this.btnPreviouseRecord.setEnabled(this.btnFirstRecord.isEnabled());
		}
	}

	/**
	 *
	 * @param mode
	 */
	private void fireAfterSetMode(final DynDaoMode mode) {
		final ArrayList<DynDaoActionListener> list = this.pnlDao.getListeners();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).afterSetMode(mode);
		}
	}

	/**
	 */
	private void fireBeforeSetMode(final DynDaoMode mode) {
		final ArrayList<DynDaoActionListener> list = this.pnlDao.getListeners();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).beforeSetMode(mode);
		}
	}

	/**
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		this.pnlButtonsContainer = new JKPanel();
		this.pnlButtonsContainer.setLayout(new BoxLayout(this.pnlButtonsContainer, BoxLayout.Y_AXIS));

		this.pnlEmpty = new JKPanel();
		this.pnlButtonsContainer.add(this.pnlEmpty);
		this.pnlActionButtons = new JKPanel();
		this.btnAdd.setShortcut("F1", "F1");
		this.btnPrint.setShortcut("alt P", "Alt P");
		this.btnEdit.setShortcut("F2", "F2");
		this.btnSave.setShortcut("F3", "F3");
		this.btnDuplicate.setShortcut("F4", "F4");
		this.btnDelete.setShortcut("F9", "F9");
		this.btnCancelEdit.setShortcut("F8", "F8");
		this.btnClose.setShortcut("F6", "F6");
		this.btnClear.setShortcut("F7", "F7");
		this.btnFind.setShortcut("alt F", "Alt F");
		this.btnHistory.setShortcut("alt H", "Alt H");

		this.btnEdit.setIcon("edit_smart_org_icon.gif");
		this.btnDuplicate.setIcon("dublicate_commons_model_icon.gif");
		this.btnDelete.setIcon("delete_commons_system_icons.gif");
		this.btnClear.setIcon("clean_commons_model_icon.gif");
		this.btnAdd.setIcon("add_commons_system_icon.gif");
		this.btnPrint.setIcon("fileprint.png");
		this.btnHistory.setIcon("fileprint.png");
		this.btnCancelEdit.setIcon("back_commons_system_icon.gif");
		this.btnSave.setIcon("save_commons_model_icon.gif");
		this.btnClose.setIcon("close.png");
		this.btnFind.setIcon("find_commons_system_icon.gif");

		this.btnFirstRecord.setIcon(SwingUtility.isLeftOrientation() ? "first_button_commons_icon.gif" : "last_button_commons_icon.gif");
		this.btnLastRecord.setIcon(SwingUtility.isLeftOrientation() ? "last_button_commons_icon.gif" : "first_button_commons_icon.gif");
		this.btnNextRecord.setIcon(SwingUtility.isLeftOrientation() ? "next_button_commons_icon.gif" : "previous_button_commons_icon.gif");
		this.btnPreviouseRecord.setIcon(SwingUtility.isLeftOrientation() ? "previous_button_commons_icon.gif" : "next_button_commons_icon.gif");

		this.pnlActionButtons.add(this.btnAdd);
		this.pnlActionButtons.add(this.btnFind);
		this.pnlActionButtons.add(this.btnImport);
		this.pnlActionButtons.add(this.btnEdit);
		this.pnlActionButtons.add(this.btnSave);
		this.pnlActionButtons.add(this.btnDuplicate);
		this.pnlActionButtons.add(this.btnDelete);
		this.pnlActionButtons.add(this.btnClear);
		this.pnlActionButtons.add(this.btnCancelEdit);
		this.pnlActionButtons.add(this.btnPrint);
		this.pnlActionButtons.add(this.btnHistory);
		this.pnlActionButtons.add(this.btnClose);

		final JKPanel pnlTravrse = getTraversePanel();

		this.pnlButtonsContainer.add(this.pnlActionButtons);
		this.pnlButtonsContainer.add(pnlTravrse);
		return this.pnlButtonsContainer;
	}

	public Object getComponentValue(final String compName) {
		return getFieldComponent(compName).getValue();
	}

	public int getComponentValueAsInteger(final String compName) {
		final Object obj = getComponentValue(compName);
		if (obj == null || obj.toString().trim().equals("")) {
			return -1;
		}
		return Integer.parseInt(obj.toString());
	}

	/**
	 *
	 * @return
	 */
	public DynamicDao getDao() {
		return this.pnlDao.getDao();
	}

	public Record getDuplicatedRecord() {
		return this.duplicatedRecord;
	}

	public JKPanel getEmptyPanel() {
		return this.pnlEmpty;
	}

	// ////////////////////////////
	// Delegate methods
	/**
	 *
	 * @param compName
	 * @return
	 */
	public BindingComponent getFieldComponent(final String compName) {
		return this.pnlDao.getFieldComponent(compName);
	}

	/**
	 *
	 * @return
	 */
	public String getIdValue() {
		return this.pnlDao.getIdValue();
	}

	/**
	 *
	 * @return
	 */
	public int getIdValueAsInteger() {
		return getPnlDao().getIdFieldValueAsInteger();
	}

	/**
	 *
	 */
	public DynDaoMode getMode() {
		return this.mode;
	}

	public JKPanel getPnlButtonsContainer() {
		return this.pnlButtonsContainer;
	}

	/**
	 * @return the pnlDao
	 */
	public DynPanel getPnlDao() {
		return this.pnlDao;
	}

	/**
	 *
	 */
	public Record getRecord() {
		return this.pnlDao.viewToRecord();
	}

	/**
	 *
	 * @return
	 */
	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	/**
	 * @return
	 */
	private JKPanel getTraversePanel() {
		this.pnlTraverse = new JKPanel();
		this.pnlTraverse.setBorder(SwingUtility.createTitledBorder("RECORDS"));
		this.pnlTraverse.add(this.btnFirstRecord);
		this.pnlTraverse.add(this.btnPreviouseRecord);
		this.pnlTraverse.add(this.btnNextRecord);
		this.pnlTraverse.add(this.btnLastRecord);
		return this.pnlTraverse;
	}

	/**
	 * @return the traversePolicy
	 */
	public RecordTraversePolicy getTraversePolicy() {
		return this.traversePolicy;
	}

	/**
	 *
	 */
	@Override
	public Object getValue() {
		return getIdValue();
	}

	/**
	 *
	 */
	private void handleAdd() {
		try {
			this.pnlDao.validateAddData(!this.tableMeta.getIdField().isAutoIncrement());
			final String newId = this.pnlDao.handleAddEvent();
			if (newId != null) {
				setNewRecordId(newId);
			}
		} catch (final ValidationException ex) {
			ExceptionUtil.handleException(ex);
		} catch (final DaoException ex) {
			ExceptionUtil.handleException(ex);
		}
	}

	/**
	 *
	 */
	private void handleCancelEdit() {
		// setMode(DynDaoMode.VIEW);
		try {
			handleFindRecord(getRecord().getIdValue());
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 *
	 *
	 */
	protected void handleClear() {
		setMode(DynDaoMode.ADD);
	}

	/**
	 *
	 */
	public void handleClose() {
		this.pnlDao.close();// wait for call back to execute the real close on
	}

	/**
	 *
	 *
	 */
	public void handleDelete(final boolean confirm) {
		if (!confirm || confirm && SwingUtility.showConfirmationDialog("CONFIRM_DELETE_RECORD")) {
			try {
				this.pnlDao.handleDeleteEvent();
			} catch (final DaoException ex) {
				SwingUtility.showDatabaseErrorDialog(ex.getMessage(), ex);
			}
		}
	}

	/**
	 *
	 */
	protected void handleDuplicate() {
		this.duplicatedRecord = getRecord();
		setMode(DynDaoMode.DUPLICATE);
		final ArrayList<Field> fields = this.duplicatedRecord.getFields();
		for (final Field field : fields) {
			if (field.getValue() != null) {
				setComponentValue(field.getFieldName(), field.getValueObject());
			}
		}
	}

	/**
	 *
	 * @throws DaoException
	 */
	protected void handleEdit() {
		setMode(DynDaoMode.EDIT);
	}

	/**
	 * Called by the find button event
	 *
	 */
	private void handleFind() {
		try {
			final Object id = QueryDialog.showQueryDialog(this.tableMeta);
			if (id != null) {
				handleFindRecord(id);
			}
		} catch (final RecordNotFoundException ex) {
			SwingUtility.showMessageDialog(ex.getMessage(), ex);
			this.pnlDao.requestFocus();
		} catch (final DaoException ex) {
			SwingUtility.showDatabaseErrorDialog(ex.getMessage(), ex);
		}

	}

	/**
	 * Created to fix the recursion issue at refined method
	 *
	 * @param recordId
	 * @throws DaoException
	 */
	public void handleFind(final String recordId, final boolean setViewMode) throws DaoException {
		this.pnlDao.handleFindEvent(recordId);
		if (setViewMode) {
			setMode(DynDaoMode.VIEW);
		}
	}

	/**
	 *
	 */
	public void handleFindRecord(final Object id) throws DaoException {
		handleFind(id.toString(), true);
	}

	private void handleImport() {
		final String record = SwingUtility.showInputDialog("ENTER_RECORD");
		if (record != null) {
			try {
				importRecord(record.split(";"));
			} catch (final DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	/**
	 *
	 */
	protected void handleMoveFirstRecord() {
		final int nextRecord = this.traversePolicy.getFirstRecord();
		moveToRecord(nextRecord);
	}

	/**
	 *
	 */
	protected void handleMoveLastRecord() {
		final int nextRecord = this.traversePolicy.getLastRecord();
		moveToRecord(nextRecord);
	}

	/**
	 *
	 */
	protected void handleMoveNextRecord() {
		final int id = getIdValueAsInteger();
		final int nextRecord = this.traversePolicy.getNextRecord(id);
		moveToRecord(nextRecord);
	}

	/**
	 *
	 */
	protected void handleMovePreviouseRecord() {
		final int id = getIdValueAsInteger();
		final int nextRecord = this.traversePolicy.getPreviouseRecord(id);
		moveToRecord(nextRecord);
	}

	protected void handlePrint() {
		PrintUtilities.printComponent(this.pnlDao);
	}

	/**
	 *
	 *
	 */
	protected void handleSave() {
		try {
			this.pnlDao.validateUpdateData();
			this.pnlDao.handleSaveEvent();
		} catch (final ValidationException e) {
			ExceptionUtil.handleException(e);
		} catch (final RecordNotFoundException ex) {
			ExceptionUtil.handleException(ex);
		} catch (final DaoException ex) {
			ExceptionUtil.handleException(ex);
		}
	}

	/**
	 *
	 */
	protected void handleShowHistory() {
		PnlAuditHistory.showHistory(getIdValueAsInteger(), this.tableMeta.getTableName());
	}

	/**
	 * @param data
	 * @throws DaoException
	 */
	public void importRecord(final String[] data) throws DaoException {
		final Record emptyRecord = getDao().createEmptyRecord(true);
		final ArrayList<Field> fields = emptyRecord.getFields();
		int index = 0;
		for (final Field field : fields) {
			if (index >= data.length) {
				break;
			}
			if (field.getMeta().isVisible() && field.getMeta().isEnabled()) {
				field.setValue(data[index++]);
			}
		}
		final String recordId = getDao().insertRecord(emptyRecord);
		handleFindRecord(recordId);
	}

	/**
	 */
	private void init() {
		final JKPanel container = new JKPanel(new BorderLayout());
		container.add(this.pnlDao, BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		container.setBorder(SwingUtility.createTitledBorder(""));
		add(container, BorderLayout.CENTER);
		this.btnImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleImport();
			}
		});
		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAdd();
			}
		});
		this.btnFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleFind();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClose();
			}
		});
		this.btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClear();

			}
		});
		this.btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleEdit();
			}
		});
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.btnDuplicate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleDuplicate();
			}
		});
		this.btnDelete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleDelete(true);
			}
		});
		this.btnCancelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCancelEdit();
			}
		});
		this.btnNextRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleMoveNextRecord();
			}
		});
		this.btnPreviouseRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleMovePreviouseRecord();
			}
		});
		this.btnLastRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleMoveLastRecord();
			}
		});
		this.btnFirstRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleMoveFirstRecord();
			}
		});
		this.btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handlePrint();
			}
		});
		this.btnHistory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleShowHistory();
			}
		});
	}

	private boolean isAllowAdd() {
		return this.allowAdd && this.tableMeta.isAllowAdd();
	}

	private boolean isAllowDelete() {
		return this.tableMeta.isAllowDelete();
	}

	private boolean isAllowDuplicate() {
		return this.allowDuplicate && !this.tableMeta.isSingleRecord();
	}

	public boolean isAllowHistory() {
		return this.allowHistory;
	}

	/**
	 * @return the allowImport
	 */
	public boolean isAllowImport() {
		return this.allowImport;
	}

	public boolean isAllowPrint() {
		return this.allowPrint;
	}

	private boolean isAllowUpdate() {
		return this.tableMeta.isAllowUpdate();
	}

	public boolean isShowRecordAfterAdd() {
		return this.showRecordAfterAdd;
	}

	/**
	 * @param recordId
	 */
	private void moveToRecord(final int recordId) {
		if (recordId != -1) {
			try {
				handleFindRecord(recordId);
				this.traversePolicy.setCurrentRecord(getIdValueAsInteger());
				checkPnlTraverseEnability();
			} catch (final DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	@Override
	public void onDaoException(final Record recod, final DaoException ex) {
		ExceptionUtil.handleException(ex);
	}

	@Override
	public void onRecordFound(final Record record) {
		setMode(DynDaoMode.VIEW);
	}

	@Override
	public void onRecordNotFound(final Object recordId, final DaoException e) {
	}

	/**
	 *
	 *
	 */
	private void pack() {
		if (getRootPane() != null && getRootPane().getParent() instanceof JKDialog) {
			((JKDialog) getRootPane().getParent()).pack();
		}
	}

	/**
	 * USED TO CALL THE FIND AGAIN , THE IDEA FROM THIS METHOD IS TO RE-POPULATE
	 * THE DATA ESPECIALY IF THE USER UPDTED THE CONTENTS OF THE COMPONENTS
	 * (COMBO BOX FOR EXAM) FROM CUSTOMIZED PANEL , like PnlClassCourses in
	 * smart-school
	 *
	 * @throws DaoException
	 */
	public void reFind() throws DaoException {
		if (getMode() == DynDaoMode.VIEW) {
			this.pnlDao.handleFindEvent(getIdValue(), false);
			// pnlDao.findRecord(getIdValue());
		} else {
			throw new IllegalStateException("Refind in non view mode is not allowed , Current mode is :" + getMode());
		}
	}

	@Override
	public void requestFocus() {
		if (this.mode == DynDaoMode.VIEW) {// DynPanel will be disabled
			this.btnEdit.requestFocus();
		} else {
			this.pnlDao.requestFocus();
		}
	}

	@Override
	public void resetComponents() throws DaoException {
		this.pnlDao.resetComponents();

	}

	/**
	 *
	 * @param showAdd
	 */
	public void setAllowAdd(final boolean showAdd) {
		this.allowAdd = showAdd;

	}

	/**
	 *
	 * @param showClear
	 */
	public void setAllowClear(final boolean showClear) {
		this.allowClear = showClear;
	}

	/**
	 *
	 * @param showClose
	 */
	public void setAllowClose(final boolean showClose) {
		this.allowClose = showClose;
		if (this.btnClose.isVisible()) {
			this.btnClose.setVisible(showClose);
		}
	}

	/**
	 * @param allowCopy
	 *            the allowCopy to set
	 */
	public void setAllowDuplicate(final boolean allowCopy) {
		this.allowDuplicate = allowCopy;
	}

	/**
	 *
	 * @param showEdit
	 */
	public void setAllowEdit(final boolean showEdit) {
		this.allowEdit = showEdit;

	}

	/**
	 *
	 * @param allowFind
	 */
	public void setAllowFind(final boolean allowFind) {
		this.allowFind = allowFind;
		setMode(this.mode);// just to refresh the components
	}

	public void setAllowHistory(final boolean allowHostory) {
		this.allowHistory = allowHostory;
	}

	/**
	 * @param allowImport
	 *            the allowImport to set
	 */
	public void setAllowImport(final boolean allowImport) {
		this.allowImport = allowImport;
	}

	public void setAllowPrint(final boolean allowPrint) {
		this.allowPrint = allowPrint;
	}

	/**
	 *
	 * @param componentName
	 * @param masterIdValue
	 */
	public void setComponentValue(final String componentName, final Object value) {
		this.pnlDao.setComponentValue(componentName, value);
	}

	@Override
	public void setEnabled(final boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			// this call to fix the conflict may caused by the the setEnabled
			// call which will
			// enable every without any respect the to the current mode
			this.pnlDao.enableDataFields(true);
		}
	}

	/**
	 *
	 * @param mode
	 * @throws DaoException
	 */
	public void setMode(DynDaoMode mode) {
		fireBeforeSetMode(mode);

		// SwingUtility.enableContainer(pnlDao, mode == DynDaoMode.VIEW);
		// used for filtering in the report , to avoid hiding
		// the find button when record already found
		if (mode == DynDaoMode.VIEW && !this.allowEdit && this.allowFind) {
			mode = DynDaoMode.FIND;
		}
		if (mode == DynDaoMode.ADD) {
			this.duplicatedRecord = null;
		}
		if (mode == DynDaoMode.ADD || mode == DynDaoMode.DUPLICATE) {
			this.pnlDao.enableAllComponents(isAllowAdd());
		} else if (mode == DynDaoMode.VIEW) {
			this.pnlDao.enableAllComponents(false);
			this.btnEdit.requestFocus();
		} else if (mode == DynDaoMode.FIND) {
			this.pnlDao.enableAllComponents(false);
		} else if (mode == DynDaoMode.EDIT) {
			this.pnlDao.enableDataFields(this.tableMeta.isAllowUpdate());
		} else {
			System.err.print("Unhandled mode : " + mode.toString());
		}

		this.btnCancelEdit.setVisible(mode == DynDaoMode.EDIT);
		this.btnAdd.setVisible(this.tableMeta.isAllowAdd() && (mode == DynDaoMode.ADD || mode == DynDaoMode.DUPLICATE));
		this.btnFind.setVisible(this.allowFind && (mode == DynDaoMode.ADD || mode == DynDaoMode.FIND));
		this.btnEdit.setVisible(this.allowEdit && mode == DynDaoMode.VIEW && (this.tableMeta.isAllowUpdate() || this.tableMeta.isAllowDelete()));

		this.btnDelete.setVisible(isAllowDelete() && (mode == DynDaoMode.DELETE || mode == DynDaoMode.EDIT));
		this.btnSave.setVisible(isAllowUpdate() && (mode == DynDaoMode.UPDATE || mode == DynDaoMode.EDIT));
		this.btnClear.setVisible(isAllowAdd() && this.allowClear && (mode == DynDaoMode.VIEW || mode == DynDaoMode.EDIT));
		this.btnClose.setVisible(this.allowClose);
		this.btnDuplicate.setVisible(isAllowAdd() && isAllowDuplicate() && this.btnEdit.isVisible());
		this.pnlTraverse.setVisible(this.traversePolicy != null && this.btnEdit.isVisible());
		this.btnImport.setVisible(this.allowImport && this.btnAdd.isVisible());
		this.btnPrint.setVisible(!this.btnAdd.isVisible() && isAllowPrint());
		this.btnHistory.setVisible(!this.btnAdd.isVisible() && isAllowHistory());
		checkPnlTraverseEnability();
		this.mode = mode;

		pack();
		fireAfterSetMode(mode);
	}

	/**
	 *
	 * @param newId
	 */
	public void setNewRecordId(final String newId) {
		this.newId = newId;

	}

	/**
	 * @param pnlDao
	 *            the pnlDao to set
	 */
	public void setPnlDao(final DynPanel pnlDao) {
		this.pnlDao = pnlDao;
	}

	public void setShowButtons(final boolean show) {
		this.pnlButtonsContainer.setVisible(show);
	}

	public void setShowRecordAfterAdd(final boolean showRecordAfterAdd) {
		this.showRecordAfterAdd = showRecordAfterAdd;
	}

	/**
	 * @param traversePolicy
	 *            the traversePolicy to set
	 */
	public void setTraversePolicy(final RecordTraversePolicy traversePolicy) {
		this.traversePolicy = traversePolicy;
	}

	/**
	 *
	 */
	@Override
	public void setValue(final Object value) {
		try {
			handleFindRecord(value.toString());
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

}
