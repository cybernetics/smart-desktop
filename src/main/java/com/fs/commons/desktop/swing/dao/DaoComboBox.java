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
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DefaultDao;
import com.fs.commons.dao.IdValueRecord;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.desktop.swing.comp.FSComboBoxListCellRenderer;
import com.fs.commons.desktop.swing.comp.JKComboBox;
import com.fs.commons.desktop.swing.dialogs.QueryDialog;
import com.fs.commons.util.ExceptionUtil;

/**
 * TODO : refactor this class : 1- Duplicate codes in constructors 2- sql code
 * need to be refactord
 * 
 * @author user
 * 
 */
public class DaoComboBox extends JKComboBox implements DaoComponent {
	private static final long serialVersionUID = 1L;
	private String sql;
	Object defaultValue;

	private int defauleSelectedIndex = -1;
	private String originalSql;
	private boolean autoShowPopup = false;
	private transient DataSource datasource;
	private List<DaoComboBox> notifiers = new ArrayList<DaoComboBox>();
	private TableMeta tableMeta;

	/**
	 * @param sql
	 *            String
	 * @throws DaoException
	 */
	public DaoComboBox(String sql) {
		this(sql, true);
	}

	/**
	 * @return the autoShowPopup
	 */
	public boolean isAutoShowPopup() {
		return autoShowPopup;
	}

	/**
	 * @param autoShowPopup
	 *            the autoShowPopup to set
	 */
	public void setAutoShowPopup(boolean autoShowPopup) {
		this.autoShowPopup = autoShowPopup;
	}

	public DaoComboBox(String sql, boolean transferFocusOnEnter) {
		setTransferFocusOnEnter(transferFocusOnEnter);
		setSql(sql);
		init();
		// loadData();
	}

	public DaoComboBox(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		setDataSource(tableMeta.getDataSource());
		setTransferFocusOnEnter(true);
		setSql(tableMeta.getListSql());
		init();
	}

	/**
	 * 
	 */
	public DaoComboBox() {
		init();
	}

	/*
	 * 
	 */
	public DaoComboBox(String sql, int defauleSelectedIndex) {
		this.defauleSelectedIndex = defauleSelectedIndex;
		setSql(sql);
		init();
	}

	public DaoComboBox(ForiegnKeyFieldMeta meta) {
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
	public DaoComboBox(ForiegnKeyFieldMeta meta, boolean load) {
		TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(meta.getParentTable().getDataSource(), meta.getReferenceTable());
		setDataSource(tableMeta.getDataSource());
		setTransferFocusOnEnter(true);
		setSql(tableMeta.getListSql(), load);
		init();
	}

	/**
	 * 
	 */
	void init() {
		setRenderer(new FSComboBoxListCellRenderer());
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
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
			public void focusGained(FocusEvent e) {
				if (autoShowPopup) {
					showPopup();
				}
			}
		});
	}

	@Override
	public void filterValues(BindingComponent component) throws DaoException {
		filterValues(component, component.getName(), false);
	}

	public void filterValues(final BindingComponent component, final String fieldName, boolean addActionListener) {
		if (fieldName == null) {
			throw new IllegalStateException(
					"Unable to call filterValues on BindingComponent with name equals null, where name shold be database column name");
		}
		String value = component.getValue() == null ? "-1" : component.getValue().toString();
		filterValue(fieldName, value);
		if (addActionListener) {
			component.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					filterValues(component, fieldName, false);
				}
			});
		}
	}

	public void filterValue(String name, String value) {
		setSql(addWhere(String.valueOf(getSql()), name, value));
		setValue(null);

	}

	// FIXME need to cover all cases
	// Mohammad Sakejha work :)
	private String addWhere(String sql, String name, String value) {
		sql = sql.toLowerCase();
		if (sql.contains("order")) {
			String[] split = sql.split("order by");
			String firstPart = split[0];
			if (!firstPart.toLowerCase().contains("where")) {
				sql = firstPart + " WHERE " + name + " = " + value + " order by " + split[1];
			} else {
				String[] whereSQl = firstPart.split("where");
				String afterWhere = whereSQl[1];
				HashMap<String, String> conditions = new HashMap<String, String>();
				if (afterWhere.contains("and")) {
					String[] split2 = afterWhere.split("and");
					for (String condition : split2) {
						String[] split3 = condition.split("=");
						conditions.put(split3[0].trim(), split3[1].trim());
					}
				} else {
					String[] split3 = afterWhere.split("=");
					conditions.put(split3[0].trim(), split3[1].trim());
				}
				conditions.put(name, value);

				whereSQl[0] += "WHERE ";
				for (String condition : conditions.keySet()) {
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

	/**
	 * 
	 */
	protected void handleFindValue() {
		// TODO : fix me , to hande\le the null pointer exception
		Object value = QueryDialog.showQueryDialog(originalSql, "SEARCH", 0);
		if (value != null) {
			String id = value.toString();
			if (id != null) {
				setValue(id);
			}
		}
	}

	/**
	 * @throws DaoException
	 * @throws DaoException
	 * 
	 */
	private void loadData() throws DaoException {
		if (originalSql != null && !originalSql.equals("")) {
			DefaultDao dao = new DefaultDao(getDataSource());
			List v = dao.createRecordsFromSQL(sql);
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
	 * @throws DaoException
	 * @throws DaoException
	 * 
	 */
	public void reloadData(boolean callReset) {
		try {
			removeAllItems();
			if (callReset) {
				reset();
			} else {
				loadData();
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
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
	 * 
	 * @param id
	 *            int
	 */
	public void setSelectedIndexForId(Object id) {
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

	/**
	 * 
	 * @param id
	 *            String
	 * @return Object
	 */
	public Object removeItemWithId(String id) {
		int index = getIndexForId(id);
		Object o = null;
		if (index != -1) {
			o = getItemAt(index);
			removeItemAt(index);
		}
		return o;
	}

	/**
	 * 
	 * @param id
	 *            int
	 */
	public int getIndexForId(String id) {
		for (int i = 0; i < getItemCount(); i++) {
			if (((IdValueRecord) getItemAt(i)).getId().equals(id)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param combo
	 *            JComboBox
	 * @param valueRecords
	 *            ArrayList
	 */
	public static void populateCombo(JComboBox combo, Vector<IdValueRecord> valueRecords) {
		combo.removeAllItems();
		for (int i = 0; i < valueRecords.size(); i++) {
			combo.addItem(valueRecords.get(i));
		}
	}

	@Override
	public Object getValue() {
		if (getSelectedIndex() != -1) {
			return getSelectedIdValue();
		}
		return null;
	}

	@Override
	public void setValue(Object value) {
		Object oldValue = getValue();
		if (value != null) {
			setSelectedIndexForId(value.toString());
		} else {
			setSelectedIndex(-1);
		}
		fsWrapper.fireValueChangeListener(oldValue, value);
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return this.sql;
	}

	public void setSql(String sql) {
		setSql(sql, true);
	}

	// /////////////////////////////////////////////////////////////////////////
	public void setSql(String sql, boolean reload) {
		try {
			sql = sql.toLowerCase();
			originalSql = new String(sql);
			// sometimes we would call non select statements like show tables;
			if (sql.startsWith("select") && sql.indexOf("order by") == -1) {
				sql += " order by 2";
			}
			this.sql = sql;
			if (reload) {
				reloadData();
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}

	/**
	 * 
	 * @param defaultValue
	 * @throws DaoException
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
		reloadData();
	}

	@Override
	public void reset() {
		reloadData(false);
		if (getItemCount() > 0) {
			if (defaultValue != null) {
				setSelectedIndexForId(defaultValue);
			} else {
				setSelectedIndex(defauleSelectedIndex);
			}
		}
	}

	@Override
	public DataSource getDataSource() {
		return datasource;
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.datasource = datasource;
	}

	public void addNotifier(DaoComboBox notifier) {
		this.notifiers.add(notifier);
	}

	public void setRequired(boolean required) {
		// TODO Auto-generated method stub

	}

	public void setQuery(String sql) {
		setSql(sql);
	}

	public void forceReload() {
		AbstractDao.removeListCache(getSql());
		reloadData();
	}

}
