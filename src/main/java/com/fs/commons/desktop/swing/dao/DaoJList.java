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
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.desktop.swing.comp.FSAbstractComponent;
import com.fs.commons.desktop.swing.comp.FSComboBoxListCellRenderer;
import com.fs.commons.desktop.swing.comp.listeners.ValueChangeListener;
import com.fs.commons.desktop.validation.Validator;

public class DaoJList extends JList implements BindingComponent<String> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final String sql;

	DefaultListModel model = new DefaultListModel();
	private final FSAbstractComponent fsWrapper = new FSAbstractComponent(this);

	private boolean transfer;

	/**
	 *
	 * @param sql
	 *            String
	 * @throws JKDataAccessException
	 */
	public DaoJList(final String sql) throws JKDataAccessException {
		setModel(this.model);
		this.sql = sql;
		init();
		loadData();
	}

	@Override
	public void addActionListener(final ActionListener actionListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValidator(final Validator validator) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addValueChangeListener(final ValueChangeListener addValueChangeListener) {
		this.fsWrapper.addValueChangeListsner(addValueChangeListener);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public void filterValues(final BindingComponent comp1) throws JKDataAccessException {
		// TODO Auto-generated method stub

	}

	@Override
	public JKDataSource getDataSource() {
		return this.fsWrapper.getDataSource();
	}

	@Override
	public String getDefaultValue() {
		return null;
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

	@Override
	public String getValue() {
		return getSelectedIdValue();
	}

	/**
	 *
	 */
	void init() {
		setCellRenderer(new FSComboBoxListCellRenderer());
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					transferFocus();
				}
			}
		});

	}

	@Override
	public boolean isAutoTransferFocus() {
		return this.transfer;
	}

	/**
	 * @throws JKDataAccessException
	 *
	 */
	public void loadData() throws JKDataAccessException {
		final List v = DaoUtil.createRecordsFromSQL(this.sql);
		for (int i = 0; i < v.size(); i++) {
			this.model.addElement(v.get(i));
		}
	}

	/**
	 * @throws JKDataAccessException
	 *
	 */
	public void reloadData() throws JKDataAccessException {
		this.model.removeAllElements();
		loadData();
		setSelectedIndex(-1);
	}

	@Override
	public void reset() {
	}

	@Override
	public void setAutoTransferFocus(final boolean transfer) {
		this.transfer = transfer;
	}

	@Override
	public void setDataSource(final JKDataSource manager) {
		this.fsWrapper.setDataSource(manager);
	}

	@Override
	public void setDefaultValue(final String t) {
	}

	/**
	 *
	 * @param id
	 *            int
	 */
	public void setSelectedIndexForId(final String id) {
		for (int i = 0; i < this.model.capacity(); i++) {
			if (((IdValueRecord) this.model.getElementAt(i)).getId().equals(id)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	@Override
	public void setValue(final String value) {
		setSelectedIndexForId(value);

	}

	@Override
	public void validateValue() throws ValidationException {
		// TODO Auto-generated method stub

	}
}
