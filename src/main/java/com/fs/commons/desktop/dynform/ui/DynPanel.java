/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      11/11/2008     Jamil Shreet    -Add methods : callBeforeDeleteEventOnTriggers(), callAfterDeleteEventOnTriggers().
 * 										   -Modify method : handleDeleteEvent().
 * 1.2      11/11/2008     Jamil Shreet    -Modify method : handleDeleteEvent().
 */

package com.fs.commons.desktop.dynform.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.constraints.exceptions.DuplicateDataException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldGroup;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swin.ConversionUtil;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKScrollable;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DataPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class DynPanel extends DataPanel {
	private static final long serialVersionUID = 1L;

	private TableMeta tableMeta;

	BindingComponent compId;

	Hashtable<String, BindingComponent> components = new Hashtable<String, BindingComponent>();
	// private FieldEventHandler fieldEventHandler;
	// private Hashtable<BindingComponent, FieldEventHandler>
	// componentToFieldEventHandler = new Hashtable<BindingComponent,
	// FieldEventHandler>();

	DynamicDao dao;

	private Record currentRecord;

	ArrayList<DynDaoActionListener> listeners = new ArrayList<DynDaoActionListener>();

	// used to customized visible fields list
	// setting this array will override the default behavior
	// which uses the field.isVisible properties
	String[] visibleFieldNames;
	// if set to bigger than 0 , it will override the table-meta value
	int uIColunmCount;

	/**
	 * 
	 */
	public DynPanel() {
	}

	/**
	 * @param tableMeta
	 * @param visibleFieldNames
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynPanel(TableMeta tableMeta, String[] visibleFieldNames) throws TableMetaNotFoundException, DaoException {
		this(tableMeta, false);
		this.visibleFieldNames = visibleFieldNames;
		init();
	}

	/**
	 * @param tableMeta
	 * @param visibleFieldNames
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynPanel(TableMeta tableMeta, int uIColunmCount) throws TableMetaNotFoundException, DaoException {
		this(tableMeta, false);
		this.uIColunmCount = uIColunmCount;
		init();
	}

	/**
	 * @param tableMeta
	 * @param visibleFieldNames
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynPanel(TableMeta tableMeta, String[] visibleFieldNames, int uIColunmCount) throws TableMetaNotFoundException, DaoException {
		this(tableMeta, false);
		this.visibleFieldNames = visibleFieldNames;
		this.uIColunmCount = uIColunmCount;
		init();
	}

	/**
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynPanel(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException {
		this(tableMeta, true);
	}

	/**
	 * 
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 */
	public DynPanel(TableMeta tableMeta, boolean init) throws TableMetaNotFoundException, DaoException {
		this.tableMeta = tableMeta;
		dao = DaoFactory.createDynamicDao(tableMeta);

		compId = ComponentFactory.createComponent(tableMeta.getIdField(), false);
		Vector<FieldMeta> fields = tableMeta.getFieldList();
		for (int i = 0; i < fields.size(); i++) {
			FieldMeta field = fields.get(i);
			BindingComponent comp = ComponentFactory.createComponent(field, false);
			// regFieldEventHandler(comp, new FieldEventHandler(field));
			// addEventHandlers(comp);
			comp.setName(field.getName());
			components.put(field.getName(), comp);// add for local refernece
			if (field.getFilteredBy() != null) {
				attachFilters(field);
			}
		}
		addComponents();
		if (init) {
			init();
		}
	}

	/**
	 * 
	 * @param tableMetaName
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 */
	public DynPanel(String tableMetaName) throws TableMetaNotFoundException, DaoException {
		this(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	/**
	 * 
	 * @param field
	 * @throws DaoException
	 */
	private void attachFilters(FieldMeta field) throws DaoException {
		FieldMeta filteredBy = getTableMeta().getField(field.getFilteredBy());
		if(filteredBy==null){
			System.err.println("Filtered by contains invalid field name : "+field.getName()+" in table :"+getTableMeta().getTableId());
			return ;
		}
		final BindingComponent comp1 = components.get(filteredBy.getName());
		final BindingComponent comp2 = components.get(field.getName());

		comp2.filterValues(comp1);// to set initial value
		comp1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					comp2.filterValues(comp1);
				} catch (DaoException e1) {
					ExceptionUtil.handleException(e1);
				}
			}
		});

	}

	//
	// /**
	// *
	// * @param comp
	// */
	// private void addEventHandlers(final BindingComponent comp) {
	// final FieldEventHandler handler = getFieldEventHandler(comp);
	// if (handler == null) {
	// return;
	// }
	// comp.addFocusListener(new FocusAdapter() {
	// @Override
	// public void focusGained(FocusEvent e) {
	// handler.fireFocusGained(comp);
	// }
	//
	// @Override
	// public void focusLost(FocusEvent e) {
	// handler.fireFocusLost(comp);
	// }
	//
	// });
	// if (comp instanceof JCheckBox || comp instanceof JRadioButton) {
	// ((JToggleButton) comp).addActionListener(new ActionListener() {
	//
	// @Override
	// public void actionPerformed(ActionEvent actionEvent) {
	// AbstractButton abstractButton = (AbstractButton) actionEvent.getSource();
	// boolean selected = abstractButton.getModel().isSelected();
	// if (selected) {
	// handler.onSelected(comp);
	// } else {
	// handler.onUnSelected(comp);
	// }
	// }
	// });
	// }
	// }

	/**
	 * 
	 * @param tableMeta
	 */
	private void init() {
		int defaultRows = getDefaultUIRowsCount();
		// int count = tableMeta.getVisibleFieldsCount();
		// int col = (count / defaultRows + 1);
		// int rowCount = count < defaultRows ? count : defaultRows;

		// setLayout(new GridLayout(rowCount, col, 5, 5));
		// test
		int rowsCounter = 0;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JKPanel colunm = new JKPanel();
		colunm.setLayout(new BoxLayout(colunm, BoxLayout.Y_AXIS));
		// \test
		// ArrayList<FieldMeta> fields;
		// setBorder(SwingUtility.createTitledBorder(tableMeta.getCaption()));
		if (tableMeta.getIdField().isVisible() && !tableMeta.getIdField().isAutoIncrement()) {
			colunm.add(new JKLabledComponent(tableMeta.getIdField().getCaption(), compId));
			rowsCounter++;
		}
		// fields = tableMeta.getFieldList();
		// int counter = 0;
		Vector<FieldGroup> groups = tableMeta.getGroups();
		for (FieldGroup group : groups) {
			if (group.getFields().size() > 0) {
				int maxRowsCount = group.getRowCount() != 0 ? group.getRowCount() : defaultRows;
				JKPanel pnlGroup = new JKPanel();
				pnlGroup.setBorder(BorderFactory.createTitledBorder(group.getName()));
				for (FieldMeta field : group.getFields()) {
					if (isVisible(field)) {
						String fieldCaption = getFieldCaption(field);
						BindingComponent comp = components.get(field.getName());
						if (comp instanceof JKScrollable) {
							colunm.add(new JKLabel(fieldCaption));
							JKScrollPane pane = new JKScrollPane((Component) comp);
							pane.setPreferredSize(new Dimension(field.getVisibleWidth(), field.getVisibleHeight()));
							// comp.setPreferredSize(new Dimension(300,200));
							colunm.add(pane);
							rowsCounter += 2;// for the label
						} else {
							colunm.add(new JKLabledComponent(fieldCaption, comp));
							rowsCounter++;
						}
						// colunm.add(new JKSeparator());
					}
					if (rowsCounter == maxRowsCount) {
						JKPanel temp = new JKPanel();
						temp.add(colunm);
						pnlGroup.add(temp);
						colunm = new JKPanel();
						colunm.setLayout(new BoxLayout(colunm, BoxLayout.Y_AXIS));
						rowsCounter = 0;
					}
				}
				if (rowsCounter != 0) {
					JKPanel temp = new JKPanel();
					temp.add(colunm);
					pnlGroup.add(temp);
				}
				add(pnlGroup);
			}
		}
	}

	/**
	 * @param field
	 * @return
	 */
	private String getFieldCaption(FieldMeta field) {
		String caption = Lables.get(field.getCaption());
		if (field.isRequired()) {
			caption = caption + " *";
		}
		return caption;
	}

	/**
	 * if visible field names set , only fields in this array will be shown ,
	 * otherwise , the field visible will be used
	 * 
	 * @param field
	 * @return
	 */
	private boolean isVisible(FieldMeta field) {
		if (visibleFieldNames != null) {
			for (int i = 0; i < visibleFieldNames.length; i++) {
				if (field.getName().equals(visibleFieldNames[i])) {
					return true;
				}
			}
			return false;
		}
		return field.isVisible();
	}

	/**
	 * 
	 * @return
	 */
	private int getDefaultUIRowsCount() {
		if (uIColunmCount > 0) {
			return uIColunmCount;
		}
		return tableMeta.getDefaultUIRowCount();
	}

	/**
	 * 
	 */
	@Override
	public void validateAddData(boolean validateId) throws ValidationException, DaoException {
		Record record = viewToRecord();
		// if (validateId) {
		// SwingValidator.checkEmpty((UIComponent) compId);
		// }
		// for (int i = 0; i < record.getFieldsCount(); i++) {
		// Field field = record.getField(i);
		// JComponent comp = components.get(field.getMeta().getName());
		// if (field.getMeta().isRequired()) {
		// SwingValidator.checkEmpty((UIComponent) comp);
		// }
		// }
		// // business validation
		// if (isAddMode() && validateId) {
		// if (dao.isIdExists(record.getIdValue())) {
		// throw new ValidationException(record.getIdValue() + " " +
		// Lables.get("ALREADY_EXISTS"), (UIComponent) getIdField());
		// }
		// }
		try {
			tableMeta.validateData(record);
		} catch (ValidationException ex) {
			if (ex.getField() != null) {
				components.get(ex.getField().getMeta().getName()).requestFocus();
			}
			throw ex;
		}
	}

	// /**
	// */
	// private boolean isAddMode() {
	// return currentRecord == null;
	// }

	@Override
	public void validateUpdateData() throws ValidationException, DaoException {
		validateAddData(false);
	}

	/**
	 * 
	 */
	public String handleAddEvent() throws DaoException {
		Record record = viewToRecord();
		fireBeforeAddRecord(record);
		try {
			String id = addRecord(record);
			if (record.getIdValue() == null && getIdField().isVisible()) {
				// in case of not autoinrement , we should depends on this value
				id = getIdValue();
			}
			record.setIdValue(id);
			fireAfterAddRecord(record);
			return id;
		} catch (DuplicateDataException e) {
			fireOnException(record, e);
			throw e;
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
			throw e;
		}
	}

	/**
	 * @param record
	 * @return
	 * @throws DaoException
	 */
	protected String addRecord(Record record) throws DaoException {
		record.setGui(GeneralUtility.toXml(this));
		return dao.insertRecord(record);
	}

	/**
	 * @1.1
	 * @1.2
	 */
	public void handleDeleteEvent() throws DaoException {
		fireBeforeDeleteRecord(currentRecord);
		Record record = currentRecord;
		deleteRecord(record);
		fireAfterDeleteRecord(currentRecord);

	}

	/**
	 * @param record
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	protected void deleteRecord(Record record) throws RecordNotFoundException, DaoException {
		record.setGui(GeneralUtility.toXml(this));
		dao.deleteRecord(record);
	}

	/**
	 * 
	 */
	public void handleFindEvent(Object id) throws DaoException {
		handleFindEvent(id, true);
	}

	/**
	 * @param id
	 * @throws DaoException
	 */
	protected void handleFindEvent(Object id, boolean fireEvents) throws DaoException {
		setIdValue(id);
		try {
			Record record = findRecord(id);
			this.currentRecord = record;
			recordToView(record, true);
			if (fireEvents) {
				fireRecordFound(record);
			}
		} catch (DaoException e) {
			fireRecordNotFound(id, e);
			throw e;
		}
	}

	/**
	 * @param id
	 * @return
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	protected Record findRecord(Object id) throws RecordNotFoundException, DaoException {
		return dao.findRecord(id);
	}

	/**
	 * @return the currentRecord
	 */
	public Record getCurrentRecord() {
		return currentRecord;
	}

	/**
	 * 
	 */
	public void handleSaveEvent() throws DaoException {
		Record record = viewToRecord();
		fireBeforeUpdateRecord(record);
		updateRecord(record);
		fireAfterUpdateRecord(record);
	}

	/**
	 * @param record
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	protected void updateRecord(Record record) throws RecordNotFoundException, DaoException {
		if (System.getProperty("security.audit.gui.enabled", "true").toLowerCase().equals("false")) {
			record.setGui(GeneralUtility.toXml(this));
		}
		dao.updateRecord(record);
	}

	/**
	 * 
	 * @param record
	 * @param ex
	 */
	void fireOnException(Record record, DaoException ex) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onDaoException(record, ex);

		}
	}

	/**
	 * 
	 * @param record
	 */
	void fireRecordFound(Record record) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onRecordFound((record));
		}
	}

	/**
	 * 
	 * @param recordId
	 * @param ex
	 */
	void fireRecordNotFound(Object recordId, DaoException ex) {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).onRecordNotFound(recordId, ex);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireBeforeAddRecord(Record record) throws DaoException {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).beforeAddRecord(record);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireAfterAddRecord(Record record) throws DaoException {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			DynDaoActionListener listsner = listeners.get(i);
			listsner.afterAddRecord(record);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireAfterDeleteRecord(Record record) throws DaoException {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).afterDeleteRecord(record);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireBeforeUpdateRecord(Record record) throws DaoException {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).beforeUpdateRecord(record);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireAfterUpdateRecord(Record record) throws DaoException {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).afterUpdateRecord(record);
		}
	}

	/**
	 * 
	 * @param record
	 * @throws DaoException
	 */
	void fireBeforeDeleteRecord(Record record) throws DaoException {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).beforeDeleteRecord(record);

		}
	}

	/**
	 * 
	 */
	void fireAfterClose() {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).afterClosePanel();

		}
	}

	/**
	 * 
	 */
	void fireBeforeClose() {
		for (int i = listeners.size() - 1; i >= 0; i--) {
			listeners.get(i).beforeClosePanel();

		}
	}

	/**
	 * 
	 * @return
	 */
	public Record viewToRecord() {
		Record record = tableMeta.createEmptyRecord();
		record.setModified(true);
		record.getIdField().setValue(((BindingComponent) compId).getValue());
		for (int i = 0; i < record.getFieldsCount(); i++) {
			Field field = record.getField(i);
			BindingComponent comp = (BindingComponent) components.get(field.getMeta().getName());
			field.setValue(comp.getValue());
		}
		return record;

	}

	/**
	 * 
	 * @param record
	 */
	private void recordToView(Record record, boolean update) {
		((BindingComponent) getIdField()).setValue(record.getIdValue());
		for (int i = 0; i < record.getFieldsCount(); i++) {
			Field field = record.getField(i);
			BindingComponent comp = (BindingComponent) components.get(field.getMeta().getName());
			comp.setValue(field.getValueObject());
		}
	}

	@Override
	public boolean enableDataFields(boolean enable) {
		super.enableDataFields(enable);
		if (enable) {
			Record record = tableMeta.createEmptyRecord();
			for (int i = 0; i < record.getFieldsCount(); i++) {
				Field field = record.getField(i);
				BindingComponent comp = components.get(field.getMeta().getName());
				if (!field.getMeta().isEnabled()) {
					comp.setEnabled(false);
				} else if (currentRecord != null) {// edit mode
					comp.setEnabled(field.getMeta().isAllowUpdate());
				}
			}
		}
		return true;
	}

	@Override
	public void resetComponents() throws DaoException {
		// will be used to set the current values
		Record latestRecord = viewToRecord();
		fireBeforeResetComponents();

		super.resetComponents();

		currentRecord = null;
		fireAfterResetComponents();

		setLastValues(latestRecord.getFields());
	}

	/**
	 * @param fields2
	 * 
	 */
	private void setLastValues(List<Field> fields) {
		if (fields != null) {
			for (Field field : fields) {
				if (field.getMeta().isKeepLastValue()) {
					setComponentValue(field.getFieldName(), field.getValueObject());
				}
			}
		}
	}

	/**
	 * 
	 */
	void fireBeforeResetComponents() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeResetComponents(viewToRecord());

		}
	}

	/**
	 * 
	 */
	void fireAfterResetComponents() {
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterResetComponents();

		}
	}

	@Override
	public void addComponents() {
		addComponent(compId, tableMeta.getIdField().getName());
		Set<String> keys = components.keySet();
		for (Object object : keys) {
			addComponent(components.get(object), (String) object);
		}
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public BindingComponent getFieldComponent(String fieldName) {
		return components.get(fieldName);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DynDaoActionListener> getDynDaoActionListener() {
		return listeners;
	}

	/**
	 * 
	 * @param listener
	 */
	public void addDynDaoActionListener(DynDaoActionListener listener) {
		listeners.add(listener);
	}

	/**
	 * By default the close button has nothing to do , it would be decided by
	 * outer listener
	 */
	public void close() {
		fireBeforeClose();
		fireAfterClose();
	}

	/**
	 * 
	 * @return
	 */
	public DynamicDao getDao() {
		return dao;
	}

	//
	// /**
	// *
	// * @param compName
	// * @return
	// */
	// public JComponent getFieldComponenet(String compName) {
	// return components.get(compName);
	// }

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
	public ArrayList<DynDaoActionListener> getListeners() {
		return listeners;
	}

	/**
	 * 
	 */
	public void reFind() {
	}

	public void setComponentValue(String componentName, Object value) {
		((BindingComponent) getFieldComponent(componentName)).setValue(value);
	}

	public void handleFindEventByField(String fieldName, Object value) throws DaoException {
		ArrayList<Record> records = dao.findByFieldValue(fieldName, value);
		if (records.size() > 0) {
			handleFindEvent(records.get(0).getIdValue());
		}

	}

	//
	// public FieldEventHandler getFieldEventHandler() {
	// return fieldEventHandler;
	// }
	//
	// public void setFieldEventHandler(FieldEventHandler fieldEventHandler) {
	// this.fieldEventHandler = fieldEventHandler;
	// }
	//
	// private FieldEventHandler getFieldEventHandler(BindingComponent comp) {
	// return componentToFieldEventHandler.get(comp);
	// }
	//
	// private void regFieldEventHandler(BindingComponent comp,
	// FieldEventHandler fieldEventHandler) {
	// this.componentToFieldEventHandler.put(comp, fieldEventHandler);
	// }

	public double getComponentValueAsDouble(String fieldName) {
		return ConversionUtil.toDouble(getFieldComponent(fieldName).getValue());
	}

	public int getComponentValueAsInteger(String fieldName) {
		return ConversionUtil.toInteger(getFieldComponent(fieldName).getValue());
	}

	public Date getComponentValueAsDate(String fieldName) {
		return ConversionUtil.toDate(getFieldComponent(fieldName).getValue());
	}

	public String getComponentValueAsString(String fieldName) {
		return ConversionUtil.toString(getFieldComponent(fieldName).getValue());
	}

}
