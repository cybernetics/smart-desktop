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

	private static final long serialVersionUID = 1L;

	public enum DynDaoMode {
		ADD, // Add Record
		VIEW, // View the record with disabled fields
		EDIT, // View the record with enabled fields
		UPDATE, // View the record and allow update
		DELETE, // View the record and allow delete
		FIND, DUPLICATE
		// allow find
	};

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

	public JKPanel getPnlButtonsContainer() {
		return pnlButtonsContainer;
	}

	/**
	 * 
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynDaoPanel(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException {
		this(new DynPanel(tableMeta));
	}

	/**
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 */
	public DynDaoPanel(DynPanel dynPanel) throws TableMetaNotFoundException, DaoException {
		this.tableMeta = dynPanel.getTableMeta();
		pnlDao = dynPanel;
		pnlDao.addDynDaoActionListener(this);
		init();
		setMode(DynDaoMode.ADD);
		setTraversePolicy(null);
	}

	public DynDaoPanel(String tableName) throws TableMetaNotFoundException, DaoException {
		this(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	/**
	 */
	private void init() {
		JKPanel container = new JKPanel(new BorderLayout());
		container.add(pnlDao, BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		container.setBorder(SwingUtility.createTitledBorder(""));
		add(container, BorderLayout.CENTER);
		btnImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleImport();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAdd();
			}
		});
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleFind();
			}
		});
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleClose();
			}
		});
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleClear();

			}
		});
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleEdit();
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});
		btnDuplicate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleDuplicate();
			}
		});
		btnDelete.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				handleDelete(true);
			}
		});
		btnCancelEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleCancelEdit();
			}
		});
		btnNextRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMoveNextRecord();
			}
		});
		btnPreviouseRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMovePreviouseRecord();
			}
		});
		btnLastRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMoveLastRecord();
			}
		});
		btnFirstRecord.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleMoveFirstRecord();
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handlePrint();
			}
		});
		btnHistory.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleShowHistory();
			}
		});
	}

	/**
	 * 
	 */
	protected void handleShowHistory() {
		PnlAuditHistory.showHistory(getIdValueAsInteger(), tableMeta.getTableName());
	}

	protected void handlePrint() {
		PrintUtilities.printComponent(pnlDao);
	}

	/**
	 * 
	 */
	protected void handleMoveNextRecord() {
		int id = getIdValueAsInteger();
		int nextRecord = traversePolicy.getNextRecord(id);
		moveToRecord(nextRecord);
	}

	/**
	 * 
	 */
	protected void handleMovePreviouseRecord() {
		int id = getIdValueAsInteger();
		int nextRecord = traversePolicy.getPreviouseRecord(id);
		moveToRecord(nextRecord);
	}

	/**
	 * 
	 */
	protected void handleMoveLastRecord() {
		int nextRecord = traversePolicy.getLastRecord();
		moveToRecord(nextRecord);
	}

	/**
	 * 
	 */
	protected void handleMoveFirstRecord() {
		int nextRecord = traversePolicy.getFirstRecord();
		moveToRecord(nextRecord);
	}

	/**
	 * @param recordId
	 */
	private void moveToRecord(int recordId) {
		if (recordId != -1) {
			try {
				handleFindRecord(recordId);
				traversePolicy.setCurrentRecord(getIdValueAsInteger());
				checkPnlTraverseEnability();
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	/**
	 * 
	 */
	private void checkPnlTraverseEnability() {
		if (pnlTraverse.isVisible()) {
			int currentId = getIdValueAsInteger();
			btnFirstRecord.setEnabled(traversePolicy.getFirstRecord() != currentId);
			btnLastRecord.setEnabled(traversePolicy.getLastRecord() != currentId);
			btnNextRecord.setEnabled(btnLastRecord.isEnabled());
			btnPreviouseRecord.setEnabled(btnFirstRecord.isEnabled());
		}
	}

	/**
	 * 
	 */
	protected void handleDuplicate() {
		duplicatedRecord = getRecord();
		setMode(DynDaoMode.DUPLICATE);
		ArrayList<Field> fields = duplicatedRecord.getFields();
		for (Field field : fields) {
			if (field.getValue() != null) {
				setComponentValue(field.getFieldName(), field.getValueObject());
			}
		}
	}

	/**
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		pnlButtonsContainer = new JKPanel();
		pnlButtonsContainer.setLayout(new BoxLayout(pnlButtonsContainer, BoxLayout.Y_AXIS));

		pnlEmpty = new JKPanel();
		pnlButtonsContainer.add(pnlEmpty);
		pnlActionButtons = new JKPanel();
		btnAdd.setShortcut("F1", "F1");
		btnPrint.setShortcut("alt P", "Alt P");
		btnEdit.setShortcut("F2", "F2");
		btnSave.setShortcut("F3", "F3");
		btnDuplicate.setShortcut("F4", "F4");
		btnDelete.setShortcut("F9", "F9");
		btnCancelEdit.setShortcut("F8", "F8");
		btnClose.setShortcut("F6", "F6");
		btnClear.setShortcut("F7", "F7");
		btnFind.setShortcut("alt F", "Alt F");
		btnHistory.setShortcut("alt H", "Alt H");

		btnEdit.setIcon("edit_smart_org_icon.gif");
		btnDuplicate.setIcon("dublicate_commons_model_icon.gif");
		btnDelete.setIcon("delete_commons_system_icons.gif");
		btnClear.setIcon("clean_commons_model_icon.gif");
		btnAdd.setIcon("add_commons_system_icon.gif");
		btnPrint.setIcon("fileprint.png");
		btnHistory.setIcon("fileprint.png");
		btnCancelEdit.setIcon("back_commons_system_icon.gif");
		btnSave.setIcon("save_commons_model_icon.gif");
		btnClose.setIcon("close.png");
		btnFind.setIcon("find_commons_system_icon.gif");

		btnFirstRecord.setIcon(SwingUtility.isLeftOrientation() ? "first_button_commons_icon.gif" : "last_button_commons_icon.gif");
		btnLastRecord.setIcon(SwingUtility.isLeftOrientation() ? "last_button_commons_icon.gif" : "first_button_commons_icon.gif");
		btnNextRecord.setIcon(SwingUtility.isLeftOrientation() ? "next_button_commons_icon.gif" : "previous_button_commons_icon.gif");
		btnPreviouseRecord.setIcon(SwingUtility.isLeftOrientation() ? "previous_button_commons_icon.gif" : "next_button_commons_icon.gif");

		pnlActionButtons.add(btnAdd);
		pnlActionButtons.add(btnFind);
		pnlActionButtons.add(btnImport);
		pnlActionButtons.add(btnEdit);
		pnlActionButtons.add(btnSave);
		pnlActionButtons.add(btnDuplicate);
		pnlActionButtons.add(btnDelete);
		pnlActionButtons.add(btnClear);
		pnlActionButtons.add(btnCancelEdit);
		pnlActionButtons.add(btnPrint);
		pnlActionButtons.add(btnHistory);
		pnlActionButtons.add(btnClose);

		JKPanel pnlTravrse = getTraversePanel();

		pnlButtonsContainer.add(pnlActionButtons);
		pnlButtonsContainer.add(pnlTravrse);
		return pnlButtonsContainer;
	}

	/**
	 * @return
	 */
	private JKPanel getTraversePanel() {
		pnlTraverse = new JKPanel();
		pnlTraverse.setBorder(SwingUtility.createTitledBorder("RECORDS"));
		pnlTraverse.add(btnFirstRecord);
		pnlTraverse.add(btnPreviouseRecord);
		pnlTraverse.add(btnNextRecord);
		pnlTraverse.add(btnLastRecord);
		return pnlTraverse;
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
		if (mode == DynDaoMode.VIEW && !allowEdit && allowFind) {
			mode = DynDaoMode.FIND;
		}
		if (mode == DynDaoMode.ADD) {
			duplicatedRecord = null;
		}
		if (mode == DynDaoMode.ADD || mode == DynDaoMode.DUPLICATE) {
			pnlDao.enableAllComponents(isAllowAdd());
		} else if (mode == DynDaoMode.VIEW) {
			pnlDao.enableAllComponents(false);
			btnEdit.requestFocus();
		} else if (mode == DynDaoMode.FIND) {
			pnlDao.enableAllComponents(false);
		} else if (mode == DynDaoMode.EDIT) {
			pnlDao.enableDataFields(tableMeta.isAllowUpdate());
		} else {
			System.err.print("Unhandled mode : " + mode.toString());
		}

		btnCancelEdit.setVisible(mode == DynDaoMode.EDIT);
		btnAdd.setVisible(tableMeta.isAllowAdd() && (mode == DynDaoMode.ADD || mode == DynDaoMode.DUPLICATE));
		btnFind.setVisible(allowFind && (mode == DynDaoMode.ADD || mode == DynDaoMode.FIND));
		btnEdit.setVisible(allowEdit && mode == DynDaoMode.VIEW && (tableMeta.isAllowUpdate() || tableMeta.isAllowDelete()));

		btnDelete.setVisible(isAllowDelete() && (mode == DynDaoMode.DELETE || mode == DynDaoMode.EDIT));
		btnSave.setVisible(isAllowUpdate() && (mode == DynDaoMode.UPDATE || mode == DynDaoMode.EDIT));
		btnClear.setVisible(isAllowAdd() && (allowClear && (mode == DynDaoMode.VIEW || mode == DynDaoMode.EDIT)));
		btnClose.setVisible(allowClose);
		btnDuplicate.setVisible(isAllowAdd() && isAllowDuplicate() && btnEdit.isVisible());
		pnlTraverse.setVisible(traversePolicy != null && btnEdit.isVisible());
		btnImport.setVisible(allowImport && btnAdd.isVisible());
		btnPrint.setVisible(!btnAdd.isVisible() && isAllowPrint());
		btnHistory.setVisible(!btnAdd.isVisible() && isAllowHistory());
		checkPnlTraverseEnability();
		this.mode = mode;

		pack();
		fireAfterSetMode(mode);
	}

	private boolean isAllowDuplicate() {
		return allowDuplicate && !tableMeta.isSingleRecord();
	}

	private boolean isAllowUpdate() {
		return tableMeta.isAllowUpdate();
	}

	private boolean isAllowDelete() {
		return tableMeta.isAllowDelete();
	}

	private boolean isAllowAdd() {
		return allowAdd && tableMeta.isAllowAdd();
	}

	/**
	 * @param allowCopy
	 *            the allowCopy to set
	 */
	public void setAllowDuplicate(boolean allowCopy) {
		this.allowDuplicate = allowCopy;
	}

	/**
	 * 
	 * 
	 */
	public void handleDelete(boolean confirm) {
		if (!confirm || (confirm && SwingUtility.showConfirmationDialog("CONFIRM_DELETE_RECORD"))) {
			try {
				pnlDao.handleDeleteEvent();
			} catch (DaoException ex) {
				SwingUtility.showDatabaseErrorDialog(ex.getMessage(), ex);
			}
		}
	}

	/**
	 * 
	 * 
	 */
	protected void handleSave() {
		try {
			pnlDao.validateUpdateData();
			pnlDao.handleSaveEvent();
		} catch (ValidationException e) {
			ExceptionUtil.handleException(e);
		} catch (RecordNotFoundException ex) {
			ExceptionUtil.handleException(ex);
		} catch (DaoException ex) {
			ExceptionUtil.handleException(ex);
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
	 * 
	 * 
	 */
	protected void handleClear() {
		setMode(DynDaoMode.ADD);
	}

	/**
	 * 
	 * @param mode
	 */
	private void fireAfterSetMode(DynDaoMode mode) {
		ArrayList<DynDaoActionListener> list = pnlDao.getListeners();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).afterSetMode(mode);
		}
	}

	/**
	 */
	private void fireBeforeSetMode(DynDaoMode mode) {
		ArrayList<DynDaoActionListener> list = pnlDao.getListeners();
		for (int i = 0; i < list.size(); i++) {
			list.get(i).beforeSetMode(mode);
		}
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
	 * 
	 */
	private void handleAdd() {
		try {
			pnlDao.validateAddData(!tableMeta.getIdField().isAutoIncrement());
			String newId = pnlDao.handleAddEvent();
			if (newId != null) {
				setNewRecordId(newId);
			}
		} catch (ValidationException ex) {
			ExceptionUtil.handleException(ex);
		} catch (DaoException ex) {
			ExceptionUtil.handleException(ex);
		}
	}

	/**
	 * 
	 * @param newId
	 */
	public void setNewRecordId(String newId) {
		this.newId = newId;

	}

	/**
	 * Called by the find button event
	 * 
	 */
	private void handleFind() {
		try {
			Object id = QueryDialog.showQueryDialog(tableMeta);
			if (id != null) {
				handleFindRecord(id);
			}
		} catch (RecordNotFoundException ex) {
			SwingUtility.showMessageDialog(ex.getMessage(), ex);
			pnlDao.requestFocus();
		} catch (DaoException ex) {
			SwingUtility.showDatabaseErrorDialog(ex.getMessage(), ex);
		}

	}

	/**
	 * 
	 */
	public void handleFindRecord(Object id) throws DaoException {
		handleFind(id.toString(), true);
	}

	/**
	 * Created to fix the recursion issue at refined method
	 * 
	 * @param recordId
	 * @throws DaoException
	 */
	public void handleFind(String recordId, boolean setViewMode) throws DaoException {
		pnlDao.handleFindEvent(recordId);
		if (setViewMode) {
			setMode(DynDaoMode.VIEW);
		}
	}

	/**
	 * 
	 */
	public void handleClose() {
		pnlDao.close();// wait for call back to execute the real close on
	}

	/**
	 * 
	 * @param allowFind
	 */
	public void setAllowFind(boolean allowFind) {
		this.allowFind = allowFind;
		setMode(mode);// just to refresh the components
	}

	@Override
	public void resetComponents() throws DaoException {
		pnlDao.resetComponents();

	}

	@Override
	public void requestFocus() {
		if (mode == DynDaoMode.VIEW) {// DynPanel will be disabled
			btnEdit.requestFocus();
		} else {
			pnlDao.requestFocus();
		}
	}

	/**
	 * 
	 * @param showClose
	 */
	public void setAllowClose(boolean showClose) {
		this.allowClose = showClose;
		if (btnClose.isVisible()) {
			btnClose.setVisible(showClose);
		}
	}

	/**
	 * 
	 * @param showClear
	 */
	public void setAllowClear(boolean showClear) {
		this.allowClear = showClear;
	}

	/**
	 * 
	 * @param showAdd
	 */
	public void setAllowAdd(boolean showAdd) {
		this.allowAdd = showAdd;

	}

	/**
	 * 
	 * @param showEdit
	 */
	public void setAllowEdit(boolean showEdit) {
		this.allowEdit = showEdit;

	}

	/**
	 * 
	 */
	public void addDynDaoActionListener(DynDaoActionListener listener) {
		pnlDao.addDynDaoActionListener(listener);
	}

	/**
	 * 
	 */
	public DynDaoMode getMode() {
		return mode;
	}

	/**
	 * 
	 */
	public Record getRecord() {
		return pnlDao.viewToRecord();
	}

	/**
	 * 
	 * @return
	 */
	public String getIdValue() {
		return pnlDao.getIdValue();
	}

	/**
	 * @return the pnlDao
	 */
	public DynPanel getPnlDao() {
		return this.pnlDao;
	}

	/**
	 * @param pnlDao
	 *            the pnlDao to set
	 */
	public void setPnlDao(DynPanel pnlDao) {
		this.pnlDao = pnlDao;
	}

	/**
	 * 
	 */
	public Object getValue() {
		return getIdValue();
	}

	/**
	 * 
	 */
	public void setValue(Object value) {
		try {
			handleFindRecord(value.toString());
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public TableMeta getTableMeta() {
		return tableMeta;
	}

	/**
	 * 
	 * @return
	 */
	public int getIdValueAsInteger() {
		return getPnlDao().getIdFieldValueAsInteger();
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
			pnlDao.handleFindEvent(getIdValue(), false);
			// pnlDao.findRecord(getIdValue());
		} else {
			throw new IllegalStateException("Refind in non view mode is not allowed , Current mode is :" + getMode());
		}
	}

	/**
	 * 
	 * @param componentName
	 * @param masterIdValue
	 */
	public void setComponentValue(String componentName, Object value) {
		pnlDao.setComponentValue(componentName, value);
	}

	// /////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////
	/**
	 * 
	 */
	@Override
	public void afterSetMode(DynDaoMode mode) {
		if (mode == DynDaoMode.EDIT) {
			pnlDao.requestFocus();
		}
	}

	/**
	 * Override the DaoActionListner
	 */
	@Override
	public void afterAddRecord(Record record) throws DaoException {
		// SwingUtility.showSuccessDialog("SUCC_RECORD_ADDED");
		if (showRecordAfterAdd) {
			handleFindRecord(record.getIdValue());
		} else {
			setMode(DynDaoMode.ADD);
		}
	}

	/**
	 * 
	 */
	@Override
	public void afterDeleteRecord(Record record) throws DaoException {
		setMode(DynDaoMode.ADD);
	}

	@Override
	public void afterUpdateRecord(Record record) throws DaoException {
		pnlDao.handleFindEvent(record.getIdValue());
	}

	@Override
	public void onRecordFound(Record record) {
		setMode(DynDaoMode.VIEW);
	}

	@Override
	public void beforeAddRecord(Record record) {
	}

	@Override
	public void beforeClosePanel() {
	}

	/**
	 * 
	 */
	@Override
	public void afterClosePanel() {
		setVisible(false);
	}

	@Override
	public void beforeDeleteRecord(Record record) throws DaoException {
	}

	@Override
	public void beforeUpdateRecord(Record record) throws DaoException {
	}

	@Override
	public void onDaoException(Record recod, DaoException ex) {
		ExceptionUtil.handleException(ex);
	}

	@Override
	public void onRecordNotFound(Object recordId, DaoException e) {
	}

	/**
	 * 
	 */
	@Override
	public void afterResetComponents() {
	}

	@Override
	public void beforeResetComponents(Record record) {
	}

	/**
	 * 
	 */
	@Override
	public void beforeSetMode(DynDaoMode mode) {
		if (mode == DynDaoMode.ADD || mode == DynDaoMode.DUPLICATE) {
			try {
				resetComponents();
				pnlDao.requestFocus();
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	// ////////////////////////////
	// Delegate methods
	/**
	 * 
	 * @param compName
	 * @return
	 */
	public BindingComponent getFieldComponent(String compName) {
		return pnlDao.getFieldComponent(compName);
	}

	public Object getComponentValue(String compName) {
		return ((BindingComponent) getFieldComponent(compName)).getValue();
	}

	public int getComponentValueAsInteger(String compName) {
		Object obj = getComponentValue(compName);
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
		return pnlDao.getDao();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (enabled) {
			// this call to fix the conflict may caused by the the setEnabled
			// call which will
			// enable every without any respect the to the current mode
			pnlDao.enableDataFields(true);
		}
	}

	/**
	 * @return the traversePolicy
	 */
	public RecordTraversePolicy getTraversePolicy() {
		return traversePolicy;
	}

	/**
	 * @param traversePolicy
	 *            the traversePolicy to set
	 */
	public void setTraversePolicy(RecordTraversePolicy traversePolicy) {
		this.traversePolicy = traversePolicy;
	}

	/**
	 * @param data
	 * @throws DaoException
	 */
	public void importRecord(String[] data) throws DaoException {
		Record emptyRecord = getDao().createEmptyRecord(true);
		ArrayList<Field> fields = emptyRecord.getFields();
		int index = 0;
		for (Field field : fields) {
			if (index >= data.length) {
				break;
			}
			if (field.getMeta().isVisible() && field.getMeta().isEnabled()) {
				field.setValue(data[index++]);
			}
		}
		String recordId = getDao().insertRecord(emptyRecord);
		handleFindRecord(recordId);
	}

	/**
	 * @return the allowImport
	 */
	public boolean isAllowImport() {
		return allowImport;
	}

	/**
	 * @param allowImport
	 *            the allowImport to set
	 */
	public void setAllowImport(boolean allowImport) {
		this.allowImport = allowImport;
	}

	private void handleImport() {
		String record = SwingUtility.showInputDialog("ENTER_RECORD");
		if (record != null) {
			try {
				importRecord(record.split(";"));
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}
	}

	/**
	 * 
	 */
	private void handleCancelEdit() {
		// setMode(DynDaoMode.VIEW);
		try {
			handleFindRecord(getRecord().getIdValue());
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}

	public JKPanel getEmptyPanel() {
		return pnlEmpty;
	}

	public void setShowButtons(boolean show) {
		pnlButtonsContainer.setVisible(show);
	}

	public void addButton(JButton btn) {
		pnlActionButtons.add(btn);
	}

	public Record getDuplicatedRecord() {
		return duplicatedRecord;
	}

	public void setAllowPrint(boolean allowPrint) {
		this.allowPrint = allowPrint;
	}

	public void setAllowHistory(boolean allowHostory) {
		this.allowHistory = allowHostory;
	}

	public boolean isAllowHistory() {
		return allowHistory;
	}

	public boolean isAllowPrint() {
		return allowPrint;
	}

	public boolean isShowRecordAfterAdd() {
		return showRecordAfterAdd;
	}

	public void setShowRecordAfterAdd(boolean showRecordAfterAdd) {
		this.showRecordAfterAdd = showRecordAfterAdd;
	}

}
