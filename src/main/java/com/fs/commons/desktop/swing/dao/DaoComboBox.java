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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.IdValueRecord;
import com.fs.commons.dao.JKAbstractPlainDataAccess;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKDefaultDataAccess;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.desktop.swing.comp.FSComboBoxListCellRenderer;
import com.fs.commons.desktop.swing.comp.JKComboBox;
import com.fs.commons.desktop.swing.dialogs.QueryDialog;
import com.jk.exceptions.handler.ExceptionUtil;

/**
 * TODO : refactor this class : 1- Duplicate codes in constructors 2- sql code
 * need to be refactord
 *
 * @author user
 *
 */
public class DaoComboBox extends JKComboBox implements DaoComponent {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param combo
	 *            JComboBox
	 * @param valueRecords
	 *            ArrayList
	 */
	public static void populateCombo(final JComboBox combo, final Vector<IdValueRecord> valueRecords) {
		combo.removeAllItems();
		for (int i = 0; i < valueRecords.size(); i++) {
			combo.addItem(valueRecords.get(i));
		}
	}

	private String sql;

	Object defaultValue;
	private int defauleSelectedIndex = -1;
	private String originalSql;
	private boolean autoShowPopup = false;
	private transient JKDataSource datasource;
	private final List<DaoComboBox> notifiers = new ArrayList<DaoComboBox>();

	private TableMeta tableMeta;

	/**
	 *
	 */
	public DaoComboBox() {
		init();
	}

	public DaoComboBox(final ForiegnKeyFieldMeta meta) {
		this(AbstractTableMetaFactory.getTableMeta(meta.getParentTable().getDataSource(), meta.getReferenceTable()));
		setName(meta.getName());
	}

	/**
	 * added for performance tuning for fieldBy fields to avoid loading all the
	 * data during construction
	 *
	 * @param meta
	 * @param load
	 */
	public DaoComboBox(final ForiegnKeyFieldMeta meta, final boolean load) {
		final TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(meta.getParentTable().getDataSource(), meta.getReferenceTable());
		setDataSource(tableMeta.getDataSource());
		setTransferFocusOnEnter(true);
		setSql(tableMeta.getListSql(), load);
		init();
	}

	/**
	 * @param sql
	 *            String
	 * @throws JKDataAccessException
	 */
	public DaoComboBox(final String sql) {
		this(sql, true);
	}

	public DaoComboBox(final String sql, final boolean transferFocusOnEnter) {
		setTransferFocusOnEnter(transferFocusOnEnter);
		setSql(sql);
		init();
		// loadData();
	}

	/*
	 *
	 */
	public DaoComboBox(final String sql, final int defauleSelectedIndex) {
		this.defauleSelectedIndex = defauleSelectedIndex;
		setSql(sql);
		init();
	}

	public DaoComboBox(final TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		setDataSource(tableMeta.getDataSource());
		setTransferFocusOnEnter(true);
		setSql(tableMeta.getListSql());
		init();
	}

	public void addNotifier(final DaoComboBox notifier) {
		this.notifiers.add(notifier);
	}

	// FIXME need to cover all cases
	// Mohammad Sakejha work :)
	private String addWhere(String sql, final String name, final String value) {
		sql = sql.toLowerCase();
		if (sql.contains("order")) {
			final String[] split = sql.split("order by");
			final String firstPart = split[0];
			if (!firstPart.toLowerCase().contains("where")) {
				sql = firstPart + " WHERE " + name + " = " + value + " order by " + split[1];
			} else {
				final String[] whereSQl = firstPart.split("where");
				final String afterWhere = whereSQl[1];
				final HashMap<String, String> conditions = new HashMap<String, String>();
				if (afterWhere.contains("and")) {
					final String[] split2 = afterWhere.split("and");
					for (final String condition : split2) {
						final String[] split3 = condition.split("=");
						conditions.put(split3[0].trim(), split3[1].trim());
					}
				} else {
					final String[] split3 = afterWhere.split("=");
					conditions.put(split3[0].trim(), split3[1].trim());
				}
				conditions.put(name, value);

				whereSQl[0] += "WHERE ";
				for (final String condition : conditions.keySet()) {
					whereSQl[0] += condition + " = " + conditions.get(condition) + " AND";
				}
				if (whereSQl[0].length() - 3 > 0) {
					whereSQl[0] = whereSQl[0].substring(0, whereSQl[0].length() - 3);
				}
				return whereSQl[0] + "ORDER BY" + split[1];
			}
		}

		return sql;
	}

	public void filterValue(final String name, final String value) {
		setSql(addWhere(String.valueOf(getSql()), name, value));
		setValue(null);

	}

	@Override
	public void filterValues(final BindingComponent component) throws JKDataAccessException {
		filterValues(component, component.getName(), false);
	}

	public void filterValues(final BindingComponent component, final String fieldName, final boolean addActionListener) {
		if (fieldName == null) {
			throw new IllegalStateException(
					"Unable to call filterValues on BindingComponent with name equals null, where name shold be database column name");
		}
		final String value = component.getValue() == null ? "-1" : component.getValue().toString();
		filterValue(fieldName, value);
		if (addActionListener) {
			component.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(final ActionEvent e) {
					filterValues(component, fieldName, false);
				}
			});
		}
	}

	public void forceReload() {
		JKAbstractPlainDataAccess.removeListCache(getSql());
		reloadData();
	}

	@Override
	public JKDataSource getDataSource() {
		return this.datasource;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 *
	 * @param id
	 *            int
	 */
	public int getIndexForId(final String id) {
		for (int i = 0; i < getItemCount(); i++) {
			if (((IdValueRecord) getItemAt(i)).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *
	 * @return String
	 */
	public String getSelectedIdValue() {
		if (getSelectedIndex() != -1) {
			return ((IdValueRecord) getSelectedItem()).getId().toString();
		}
		return null;
	}

	/**
	 *
	 * @return int
	 */
	public int getSelectedIdValueAsInteger() {
		return Integer.parseInt(getSelectedIdValue() == null ? "-1" : getSelectedIdValue());
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return this.sql;
	}

	@Override
	public Object getValue() {
		if (getSelectedIndex() != -1) {
			return getSelectedIdValue();
		}
		return null;
	}

	/**
	 *
	 */
	protected void handleFindValue() {
		// TODO : fix me , to hande\le the null pointer exception
		final Object value = QueryDialog.showQueryDialog(this.originalSql, "SEARCH", 0);
		if (value != null) {
			final String id = value.toString();
			if (id != null) {
				setValue(id);
			}
		}
	}

	/**
	 *
	 */
	void init() {
		setRenderer(new FSComboBoxListCellRenderer());
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F5) {
					forceReload();
				}
				if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
					handleFindValue();
				}

			}
		});
		addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(final FocusEvent e) {
				if (DaoComboBox.this.autoShowPopup) {
					showPopup();
				}
			}
		});
	}

	/**
	 * @return the autoShowPopup
	 */
	public boolean isAutoShowPopup() {
		return this.autoShowPopup;
	}

	/**
	 * @throws JKDataAccessException
	 * @throws JKDataAccessException
	 *
	 */
	private void loadData() throws JKDataAccessException {
		if (this.originalSql != null && !this.originalSql.equals("")) {
			final JKDefaultDataAccess dao = new JKDefaultDataAccess(getDataSource());
			final List v = dao.createRecordsFromSQL(this.sql);
			// the below config will help imporoving the performance by limit
			// the size of the combo box to be 30 by default
			// int maxSize =
			// Integer.parseInt(System.getProperty("list-max-items", "30"));
			// int size = Math.min(v.size(), maxSize);
			for (int i = 0; i < v.size(); i++) {
				if (v.get(i) != null) {
					addItem(v.get(i));
				}
			}
		}
	}

	public void reloadData() {
		reloadData(true);
	}

	/**
	 * @param b
	 * @throws JKDataAccessException
	 * @throws JKDataAccessException
	 *
	 */
	public void reloadData(final boolean callReset) {
		try {
			removeAllItems();
			if (callReset) {
				reset();
			} else {
				loadData();
			}
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 * @param id
	 *            String
	 * @return Object
	 */
	public Object removeItemWithId(final String id) {
		final int index = getIndexForId(id);
		Object o = null;
		if (index != -1) {
			o = getItemAt(index);
			removeItemAt(index);
		}
		return o;
	}

	@Override
	public void reset() {
		reloadData(false);
		if (getItemCount() > 0) {
			if (this.defaultValue != null) {
				setSelectedIndexForId(this.defaultValue);
			} else {
				setSelectedIndex(this.defauleSelectedIndex);
			}
		}
	}

	/**
	 * @param autoShowPopup
	 *            the autoShowPopup to set
	 */
	public void setAutoShowPopup(final boolean autoShowPopup) {
		this.autoShowPopup = autoShowPopup;
	}

	@Override
	public void setDataSource(final JKDataSource datasource) {
		this.datasource = datasource;
	}

	/**
	 *
	 * @param defaultValue
	 * @throws JKDataAccessException
	 */
	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;
		reloadData();
	}

	public void setQuery(final String sql) {
		setSql(sql);
	}

	@Override
	public void setRequired(final boolean required) {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * @param id
	 *            int
	 */
	public void setSelectedIndexForId(final Object id) {
		int i = 0;
		if (id != null) {
			for (i = 0; i < getItemCount(); i++) {
				if (((IdValueRecord) getItemAt(i)).getId().equals(id.toString())) {
					setSelectedIndex(i);
					break;
				}
			}
		}
		if (id == null || i == getItemCount()) {
			super.setSelectedIndex(-1);
		}

	}

	public void setSql(final String sql) {
		setSql(sql, true);
	}

	// /////////////////////////////////////////////////////////////////////////
	public void setSql(String sql, final boolean reload) {
		try {
			sql = sql.toLowerCase();
			this.originalSql = new String(sql);
			// sometimes we would call non select statements like show tables;
			if (sql.startsWith("select") && sql.indexOf("order by") == -1) {
				sql += " order by 2";
			}
			this.sql = sql;
			if (reload) {
				reloadData();
			}
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	public void setValue(final Object value) {
		final Object oldValue = getValue();
		if (value != null) {
			setSelectedIndexForId(value.toString());
		} else {
			setSelectedIndex(-1);
		}
		this.fsWrapper.fireValueChangeListener(oldValue, value);
	}

}
