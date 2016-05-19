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
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
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
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swin.ConversionUtil;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKScrollable;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DataPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

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
	 *
	 * @param tableMetaName
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 */
	public DynPanel(final String tableMetaName) throws TableMetaNotFoundException, JKDataAccessException {
		this(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

	/**
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 */
	public DynPanel(final TableMeta tableMeta) throws TableMetaNotFoundException, JKDataAccessException {
		this(tableMeta, true);
	}

	/**
	 *
	 * @param tableMeta
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 */
	public DynPanel(final TableMeta tableMeta, final boolean init) throws TableMetaNotFoundException, JKDataAccessException {
		this.tableMeta = tableMeta;
		this.dao = DaoFactory.createDynamicDao(tableMeta);

		this.compId = ComponentFactory.createComponent(tableMeta.getIdField(), false);
		final Vector<FieldMeta> fields = tableMeta.getFieldList();
		for (int i = 0; i < fields.size(); i++) {
			final FieldMeta field = fields.get(i);
			final BindingComponent comp = ComponentFactory.createComponent(field, false);
			// regFieldEventHandler(comp, new FieldEventHandler(field));
			// addEventHandlers(comp);
			comp.setName(field.getName());
			this.components.put(field.getName(), comp);// add for local
														// refernece
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
	 * @param tableMeta
	 * @param visibleFieldNames
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 */
	public DynPanel(final TableMeta tableMeta, final int uIColunmCount) throws TableMetaNotFoundException, JKDataAccessException {
		this(tableMeta, false);
		this.uIColunmCount = uIColunmCount;
		init();
	}

	/**
	 * @param tableMeta
	 * @param visibleFieldNames
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 */
	public DynPanel(final TableMeta tableMeta, final String[] visibleFieldNames) throws TableMetaNotFoundException, JKDataAccessException {
		this(tableMeta, false);
		this.visibleFieldNames = visibleFieldNames;
		init();
	}

	/**
	 * @param tableMeta
	 * @param visibleFieldNames
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 */
	public DynPanel(final TableMeta tableMeta, final String[] visibleFieldNames, final int uIColunmCount)
			throws TableMetaNotFoundException, JKDataAccessException {
		this(tableMeta, false);
		this.visibleFieldNames = visibleFieldNames;
		this.uIColunmCount = uIColunmCount;
		init();
	}

	@Override
	public void addComponents() {
		addComponent(this.compId, this.tableMeta.getIdField().getName());
		final Set<String> keys = this.components.keySet();
		for (final Object object : keys) {
			addComponent(this.components.get(object), (String) object);
		}
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
	 * @param listener
	 */
	public void addDynDaoActionListener(final DynDaoActionListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * @param record
	 * @return
	 * @throws JKDataAccessException
	 */
	protected String addRecord(final Record record) throws JKDataAccessException {
		record.setGui(GeneralUtility.toXml(this));
		return this.dao.insertRecord(record);
	}

	/**
	 *
	 * @param field
	 * @throws JKDataAccessException
	 */
	private void attachFilters(final FieldMeta field) throws JKDataAccessException {
		final FieldMeta filteredBy = getTableMeta().getField(field.getFilteredBy());
		if (filteredBy == null) {
			System.err.println("Filtered by contains invalid field name : " + field.getName() + " in table :" + getTableMeta().getTableId());
			return;
		}
		final BindingComponent comp1 = this.components.get(filteredBy.getName());
		final BindingComponent comp2 = this.components.get(field.getName());

		comp2.filterValues(comp1);// to set initial value
		comp1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					comp2.filterValues(comp1);
				} catch (final JKDataAccessException e1) {
					JKExceptionUtil.handle(e1);
				}
			}
		});

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
	 * @param record
	 * @throws JKRecordNotFoundException
	 * @throws JKDataAccessException
	 */
	protected void deleteRecord(final Record record) throws JKRecordNotFoundException, JKDataAccessException {
		record.setGui(GeneralUtility.toXml(this));
		this.dao.deleteRecord(record);
	}

	// /**
	// */
	// private boolean isAddMode() {
	// return currentRecord == null;
	// }

	@Override
	public boolean enableDataFields(final boolean enable) {
		super.enableDataFields(enable);
		if (enable) {
			final Record record = this.tableMeta.createEmptyRecord();
			for (int i = 0; i < record.getFieldsCount(); i++) {
				final Field field = record.getField(i);
				final BindingComponent comp = this.components.get(field.getMeta().getName());
				if (!field.getMeta().isEnabled()) {
					comp.setEnabled(false);
				} else if (this.currentRecord != null) {// edit mode
					comp.setEnabled(field.getMeta().isAllowUpdate());
				}
			}
		}
		return true;
	}

	/**
	 * @param id
	 * @return
	 * @throws JKRecordNotFoundException
	 * @throws JKDataAccessException
	 */
	protected Record findRecord(final Object id) throws JKRecordNotFoundException, JKDataAccessException {
		return this.dao.findRecord(id);
	}

	/**
	 *
	 * @param record
	 * @throws JKDataAccessException
	 */
	void fireAfterAddRecord(final Record record) throws JKDataAccessException {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			final DynDaoActionListener listsner = this.listeners.get(i);
			listsner.afterAddRecord(record);
		}
	}

	/**
	 *
	 */
	void fireAfterClose() {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).afterClosePanel();

		}
	}

	/**
	 *
	 * @param record
	 * @throws JKDataAccessException
	 */
	void fireAfterDeleteRecord(final Record record) throws JKDataAccessException {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).afterDeleteRecord(record);
		}
	}

	/**
	 *
	 */
	void fireAfterResetComponents() {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).afterResetComponents();

		}
	}

	/**
	 *
	 * @param record
	 * @throws JKDataAccessException
	 */
	void fireAfterUpdateRecord(final Record record) throws JKDataAccessException {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).afterUpdateRecord(record);
		}
	}

	/**
	 *
	 * @param record
	 * @throws JKDataAccessException
	 */
	void fireBeforeAddRecord(final Record record) throws JKDataAccessException {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).beforeAddRecord(record);
		}
	}

	/**
	 *
	 */
	void fireBeforeClose() {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).beforeClosePanel();

		}
	}

	/**
	 *
	 * @param record
	 * @throws JKDataAccessException
	 */
	void fireBeforeDeleteRecord(final Record record) throws JKDataAccessException {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).beforeDeleteRecord(record);

		}
	}

	/**
	 *
	 */
	void fireBeforeResetComponents() {
		for (int i = 0; i < this.listeners.size(); i++) {
			this.listeners.get(i).beforeResetComponents(viewToRecord());

		}
	}

	/**
	 *
	 * @param record
	 * @throws JKDataAccessException
	 */
	void fireBeforeUpdateRecord(final Record record) throws JKDataAccessException {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).beforeUpdateRecord(record);
		}
	}

	/**
	 *
	 * @param record
	 * @param ex
	 */
	void fireOnException(final Record record, final JKDataAccessException ex) {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).onDaoException(record, ex);

		}
	}

	/**
	 *
	 * @param record
	 */
	void fireRecordFound(final Record record) {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).onRecordFound(record);
		}
	}

	/**
	 *
	 * @param recordId
	 * @param ex
	 */
	void fireRecordNotFound(final Object recordId, final JKDataAccessException ex) {
		for (int i = this.listeners.size() - 1; i >= 0; i--) {
			this.listeners.get(i).onRecordNotFound(recordId, ex);
		}
	}

	public Date getComponentValueAsDate(final String fieldName) {
		return ConversionUtil.toDate(getFieldComponent(fieldName).getValue());
	}

	public double getComponentValueAsDouble(final String fieldName) {
		return ConversionUtil.toDouble(getFieldComponent(fieldName).getValue());
	}

	public int getComponentValueAsInteger(final String fieldName) {
		return ConversionUtil.toInteger(getFieldComponent(fieldName).getValue());
	}

	public String getComponentValueAsString(final String fieldName) {
		return ConversionUtil.toString(getFieldComponent(fieldName).getValue());
	}

	/**
	 * @return the currentRecord
	 */
	public Record getCurrentRecord() {
		return this.currentRecord;
	}

	/**
	 *
	 * @return
	 */
	public DynamicDao getDao() {
		return this.dao;
	}

	/**
	 *
	 * @return
	 */
	private int getDefaultUIRowsCount() {
		if (this.uIColunmCount > 0) {
			return this.uIColunmCount;
		}
		return this.tableMeta.getDefaultUIRowCount();
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<DynDaoActionListener> getDynDaoActionListener() {
		return this.listeners;
	}

	/**
	 * @param field
	 * @return
	 */
	private String getFieldCaption(final FieldMeta field) {
		String caption = Lables.get(field.getCaption());
		if (field.isRequired()) {
			caption = caption + " *";
		}
		return caption;
	}

	/**
	 *
	 * @param fieldName
	 * @return
	 */
	public BindingComponent getFieldComponent(final String fieldName) {
		return this.components.get(fieldName);
	}

	/**
	 *
	 * @return
	 */
	public ArrayList<DynDaoActionListener> getListeners() {
		return this.listeners;
	}

	/**
	 *
	 * @return
	 */
	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	/**
	 *
	 */
	@Override
	public String handleAddEvent() throws JKDataAccessException {
		final Record record = viewToRecord();
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
		} catch (final DuplicateDataException e) {
			fireOnException(record, e);
			throw e;
		} catch (final JKDataAccessException e) {
			JKExceptionUtil.handle(e);
			throw e;
		}
	}

	/**
	 * @1.1 @1.2
	 */
	@Override
	public void handleDeleteEvent() throws JKDataAccessException {
		fireBeforeDeleteRecord(this.currentRecord);
		final Record record = this.currentRecord;
		deleteRecord(record);
		fireAfterDeleteRecord(this.currentRecord);

	}

	/**
	 *
	 */
	@Override
	public void handleFindEvent(final Object id) throws JKDataAccessException {
		handleFindEvent(id, true);
	}

	/**
	 * @param id
	 * @throws JKDataAccessException
	 */
	protected void handleFindEvent(final Object id, final boolean fireEvents) throws JKDataAccessException {
		setIdValue(id);
		try {
			final Record record = findRecord(id);
			this.currentRecord = record;
			recordToView(record, true);
			if (fireEvents) {
				fireRecordFound(record);
			}
		} catch (final JKDataAccessException e) {
			fireRecordNotFound(id, e);
			throw e;
		}
	}

	public void handleFindEventByField(final String fieldName, final Object value) throws JKDataAccessException {
		final ArrayList<Record> records = this.dao.findByFieldValue(fieldName, value);
		if (records.size() > 0) {
			handleFindEvent(records.get(0).getIdValue());
		}

	}

	/**
	 *
	 */
	@Override
	public void handleSaveEvent() throws JKDataAccessException {
		final Record record = viewToRecord();
		fireBeforeUpdateRecord(record);
		updateRecord(record);
		fireAfterUpdateRecord(record);
	}

	/**
	 *
	 * @param tableMeta
	 */
	private void init() {
		final int defaultRows = getDefaultUIRowsCount();
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
		if (this.tableMeta.getIdField().isVisible() && !this.tableMeta.getIdField().isAutoIncrement()) {
			colunm.add(new JKLabledComponent(this.tableMeta.getIdField().getCaption(), this.compId));
			rowsCounter++;
		}
		// fields = tableMeta.getFieldList();
		// int counter = 0;
		final Vector<FieldGroup> groups = this.tableMeta.getGroups();
		for (final FieldGroup group : groups) {
			if (group.getFields().size() > 0) {
				final int maxRowsCount = group.getRowCount() != 0 ? group.getRowCount() : defaultRows;
				final JKPanel pnlGroup = new JKPanel();
				pnlGroup.setBorder(BorderFactory.createTitledBorder(group.getName()));
				for (final FieldMeta field : group.getFields()) {
					if (isVisible(field)) {
						final String fieldCaption = getFieldCaption(field);
						final BindingComponent comp = this.components.get(field.getName());
						if (comp instanceof JKScrollable) {
							colunm.add(new JKLabel(fieldCaption));
							final JKScrollPane pane = new JKScrollPane((Component) comp);
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
						final JKPanel temp = new JKPanel();
						temp.add(colunm);
						pnlGroup.add(temp);
						colunm = new JKPanel();
						colunm.setLayout(new BoxLayout(colunm, BoxLayout.Y_AXIS));
						rowsCounter = 0;
					}
				}
				if (rowsCounter != 0) {
					final JKPanel temp = new JKPanel();
					temp.add(colunm);
					pnlGroup.add(temp);
				}
				add(pnlGroup);
			}
		}
	}

	/**
	 * if visible field names set , only fields in this array will be shown ,
	 * otherwise , the field visible will be used
	 *
	 * @param field
	 * @return
	 */
	private boolean isVisible(final FieldMeta field) {
		if (this.visibleFieldNames != null) {
			for (final String visibleFieldName : this.visibleFieldNames) {
				if (field.getName().equals(visibleFieldName)) {
					return true;
				}
			}
			return false;
		}
		return field.isVisible();
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
	 * @param record
	 */
	private void recordToView(final Record record, final boolean update) {
		getIdField().setValue(record.getIdValue());
		for (int i = 0; i < record.getFieldsCount(); i++) {
			final Field field = record.getField(i);
			final BindingComponent comp = this.components.get(field.getMeta().getName());
			comp.setValue(field.getValueObject());
		}
	}

	/**
	 *
	 */
	public void reFind() {
	}

	@Override
	public void resetComponents() throws JKDataAccessException {
		// will be used to set the current values
		final Record latestRecord = viewToRecord();
		fireBeforeResetComponents();

		super.resetComponents();

		this.currentRecord = null;
		fireAfterResetComponents();

		setLastValues(latestRecord.getFields());
	}

	public void setComponentValue(final String componentName, final Object value) {
		getFieldComponent(componentName).setValue(value);
	}

	/**
	 * @param fields2
	 *
	 */
	private void setLastValues(final List<Field> fields) {
		if (fields != null) {
			for (final Field field : fields) {
				if (field.getMeta().isKeepLastValue()) {
					setComponentValue(field.getFieldName(), field.getValueObject());
				}
			}
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

	/**
	 * @param record
	 * @throws JKRecordNotFoundException
	 * @throws JKDataAccessException
	 */
	protected void updateRecord(final Record record) throws JKRecordNotFoundException, JKDataAccessException {
		if (System.getProperty("security.audit.gui.enabled", "true").toLowerCase().equals("false")) {
			record.setGui(GeneralUtility.toXml(this));
		}
		this.dao.updateRecord(record);
	}

	/**
	 *
	 */
	@Override
	public void validateAddData(final boolean validateId) throws ValidationException, JKDataAccessException {
		final Record record = viewToRecord();
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
			this.tableMeta.validateData(record);
		} catch (final ValidationException ex) {
			if (ex.getField() != null) {
				this.components.get(ex.getField().getMeta().getName()).requestFocus();
			}
			throw ex;
		}
	}

	@Override
	public void validateUpdateData() throws ValidationException, JKDataAccessException {
		validateAddData(false);
	}

	/**
	 *
	 * @return
	 */
	public Record viewToRecord() {
		final Record record = this.tableMeta.createEmptyRecord();
		record.setModified(true);
		record.getIdField().setValue(this.compId.getValue());
		for (int i = 0; i < record.getFieldsCount(); i++) {
			final Field field = record.getField(i);
			final BindingComponent comp = this.components.get(field.getMeta().getName());
			field.setValue(comp.getValue());
		}
		return record;

	}

}
