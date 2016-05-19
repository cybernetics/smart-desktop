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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.IdValueRecord;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKRadioButton;
import com.jk.exceptions.handler.ExceptionUtil;

public class JKRadioGroup extends JKPanel<Object> {
	/**
	 *
	 */
	private static final long serialVersionUID = -8984271145765244275L;
	private String sql;
	ArrayList<JKRadioButton> radios = new ArrayList<JKRadioButton>();
	private ButtonGroup group;
	private List<IdValueRecord> records;

	public JKRadioGroup(final Object[] values) {
		setRecords(IdValueRecord.createList(values));
	}

	public JKRadioGroup(final Object[] values, final String title) {
		this(values);
		setTitle(title);
	}

	// private JKRadioGroup(int[]ids, Object values[]) {
	// setRecords(IdValueRecord.createList(ids,values));
	// }

	/**
	 *
	 * @param sql
	 * @param string
	 */
	private JKRadioGroup(final String sql, final String title) {
		setSql(sql);
		setTitle(title);
	}

	/**
	 *
	 * @param meta
	 */
	public JKRadioGroup(final TableMeta meta) {
		this(meta.getListSql(), meta.getCaption());
	}

	/**
	 * @param listener
	 */
	@Override
	public void addActionListener(final ActionListener listener) {
		for (final JKRadioButton btn : this.radios) {
			btn.addActionListener(listener);
		}
	}

	public int getItemsCount() {
		return this.radios.size();
	}

	public int getSelectedIndex() {
		for (int i = 0; i < this.radios.size(); i++) {
			final JKRadioButton rd = this.radios.get(i);
			if (rd.isSelected()) {
				return i;
			}
		}
		return -1;
	}

	public Object getSelectedItem() {
		return this.records.get(getSelectedIndex()).getValue();
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return this.sql;
	}

	@Override
	public Integer getValue() {
		final int index = getSelectedIndex();
		if (index == -1) {
			return -1;
		}
		return this.records.get(index).getIdAsInteger();
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new java.awt.GridLayout(1, this.radios.size()));
		for (final JKRadioButton rd : this.radios) {
			add(rd);
		}
		if (this.radios.size() > 0) {
			this.radios.get(0).setSelected(true);
		}
	}

	/**
	 * @param vector
	 *
	 */
	private void setRecords(final List<IdValueRecord> records) {
		this.records = records;
		this.group = new ButtonGroup();
		for (final IdValueRecord idValueRecord : records) {
			final JKRadioButton button = new JKRadioButton(idValueRecord.getValue().toString());
			this.group.add(button);
			this.radios.add(button);
		}
		removeAll();
		init();
		repaint();
	}

	public void setSelectedItem(final int index) {
		this.radios.get(index).setSelected(true);
	}

	/**
	 * @param sql
	 *            the sql to set
	 * @throws JKDataAccessException
	 */
	private void setSql(final String sql) {
		this.sql = sql;
		this.radios.clear();
		try {
			setRecords(DaoUtil.createRecordsFromSQL(sql));
		} catch (final JKDataAccessException e) {
			ExceptionUtil.handle(e);
		}

	}

	@Override
	public void setTitle(final String title) {
		setBorder(SwingUtility.createTitledBorder(title));
	}

	@Override
	public void setValue(Object value) {
		if (value == null) {
			value = -1;
		}
		for (int i = 0; i < this.records.size(); i++) {
			final IdValueRecord idValueRecord = this.records.get(i);
			final JKRadioButton radioButton = this.radios.get(i);
			if (idValueRecord.getIdAsInteger() == Integer.parseInt(value.toString())) {
				radioButton.setSelected(true);
				break;
			} else {
				radioButton.setSelected(false);
			}
		}
	}

	// @Override
	// public void requestFocus() {
	// SwingUtilities.invokeLater(new Runnable(){
	//
	// @Override
	// public void run() {
	// radios.get(getSelectedIndex()).requestFocus();
	// }
	// });
	//
	// }
}
