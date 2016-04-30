package com.fs.commons.desktop.swing.dao;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.IdValueRecord;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.comp.FSAbstractComponent;
import com.fs.commons.desktop.swing.comp.FSComboBoxListCellRenderer;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class DaoJList extends JList implements BindingComponent<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sql;

	DefaultListModel model = new DefaultListModel();
	private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private boolean transfer;


	/**
	 * 
	 * @param sql
	 *            String
	 * @throws DaoException
	 */
	public DaoJList(String sql) throws DaoException {
		setModel(model);
		this.sql = sql;
		init();
		loadData();
	}

	/**
	 * 
	 */
	void init() {
		setCellRenderer(new FSComboBoxListCellRenderer());
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					transferFocus();
				}
			}
		});

	}

	/**
	 * @throws DaoException
	 * 
	 */
	public void loadData() throws DaoException {
		List v = DaoUtil.createRecordsFromSQL(sql);
		for (int i = 0; i < v.size(); i++) {
			model.addElement(v.get(i));
		}
	}

	/**
	 * @throws DaoException
	 * 
	 */
	public void reloadData() throws DaoException {
		model.removeAllElements();
		loadData();
		setSelectedIndex(-1);
	}

	/**
	 * 
	 * @return String
	 */
	public String getSelectedIdValue() {
		if (getSelectedIndex() != -1) {
			return ((IdValueRecord) getSelectedValue()).getId().toString();
		}
		return null;
	}

	/**
	 * 
	 * @return int
	 */
	public int getSelectedIdValueAsInteger() {
		return Integer.parseInt(getSelectedIdValue());
	}

	/**
	 * 
	 * @param id
	 *            int
	 */
	public void setSelectedIndexForId(String id) {
		for (int i = 0; i < model.capacity(); i++) {
			if (((IdValueRecord) model.getElementAt(i)).getId().equals(id)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	public String getValue() {
		return getSelectedIdValue();
	}

	public void setValue(String value) {
		setSelectedIndexForId(value);
		
	}

	@Override
	public String getDefaultValue() {
		return null;
	}

	@Override
	public void setDefaultValue(String t) {
	}

	@Override
	public void reset() {		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addValidator(Validator validator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void filterValues(BindingComponent comp1) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDataSource(DataSource manager) {
		fsWrapper.setDataSource(manager);
	}

	@Override
	public DataSource getDataSource() {
		return fsWrapper.getDataSource();
	}

	@Override
	public void addValueChangeListener(ValueChangeListener addValueChangeListener) {
		fsWrapper.addValueChangeListsner(addValueChangeListener);
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAutoTransferFocus(boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public boolean isAutoTransferFocus() {
		return transfer;
	}
}
