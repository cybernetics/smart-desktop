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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.paging.DataBasePager;
import com.fs.commons.dao.paging.DataPager;
import com.fs.commons.dao.paging.PagingException;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;
import com.fs.commons.locale.Lables;
import com.jk.exceptions.handler.ExceptionUtil;

public class QueryTableModel extends FSTableModel implements DataPager {
	enum OrderDirection {
		ASCENDING, DESCENDING;
		/**
		 *
		 * @return String
		 */
		@Override
		public String toString() {
			return this == ASCENDING ? "ASC" : "DESC";
		}
	}

	private static final long serialVersionUID = 1L;

	public static final String EMPETY_STRING = null;

	boolean showIdColunm = false;

	// Vector<Vector<Object>> data; // will hold String[] objects . . .

	// boolean colunmsVisibility[]; // indicates weather the colunms are visible

	// or not

	// it is usefull in the advanced printing

	String sql;

	// key is column index , value is the condition
	// e.g. Key= 1 , value = "like '%Test' "
	Hashtable<Integer, String> extraSqlConditions = new Hashtable<Integer, String>();

	int orderByColunmIndex = 0;

	OrderDirection orderDirection = OrderDirection.ASCENDING;

	// ResultSetMetaData rsMetaData;

	// private CachedRowSetImpl crs;

	private String groupBy = "";

	int counter = 0;

	String staticWhere;

	// private ConnectionManager connectionManager =
	// ConnectionManagerFactory.getDefaultConnectionManager();

	private JKDataSource dataSource;

	private String orderByStatement = "";

	private final int limit = -1;
	private final int idColumnIndex = 0;

	private final DataBasePager pager = new DataBasePager();
	private boolean queryChanged;

	// private boolean modified;
	// Hashtable<Integer, Boolean> modifiedRows = new Hashtable<Integer,
	// Boolean>();

	public QueryTableModel() {
		this("", false);
	}

	/**
	 *
	 * @param connectionManager
	 */
	public QueryTableModel(final JKDataSource connectionManager) {
		this();
		setDataSource(connectionManager);
	}

	/**
	 * @param manager
	 * @param sql
	 */
	public QueryTableModel(final JKDataSource reourceManager, final String sql) {
		this(reourceManager, sql, 0);
	}

	/**
	 * @param manager
	 * @param sql2
	 * @param orderByColunmIndex2
	 */
	public QueryTableModel(final JKDataSource manager, final String sql, final int orderByColunmIndex) {
		setReourceManager(manager);
		setOrderByColunmIndex(orderByColunmIndex);
		setSql(sql);
		// initDB();
		loadData();
	}

	/**
	 *
	 * @param sql
	 *            String
	 */
	public QueryTableModel(final String sql) {
		this(sql, false);
	}

	/**
	 * @param sql
	 * @param showIdColunm
	 */
	public QueryTableModel(final String sql, final boolean showIdColunm) {
		setSql(sql);
		this.showIdColunm = showIdColunm;
		// data = new Vector<Vector<Object>>();
		// initDB();
		loadData();
	}

	public int[] getAllRecordIds() {
		final int ids[] = new int[getRowCount()];
		for (int i = 0; i < getRowCount(); i++) {
			ids[i] = getRecordIdAsInteger(i);
		}
		return ids;
	}

	@Override
	public int getAllRowsCount() {
		return this.pager.getAllRowsCount();
	}

	/**
	 *
	 * @param i
	 *            int
	 * @return String
	 */
	// @Override
	// public String getColumnName(int i) {
	// return Lables.get(getActualColumnName(i), true);
	// // try {
	// // return Lables.get(rsMetaData.getColumnName(i + (showIdColunm ? 1
	// // : 2)));
	// // } catch (SQLException ex) {
	// // ex.printStackTrace();
	// // return null;
	// // }
	// }

	/**
	 *
	 * @param i
	 * @param noLabel
	 * @return
	 */
	// public String getActualColumnName(int i) {
	// try {
	// return rsMetaData.getColumnName(i + (showIdColunm ? 1 : 2));
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// return null;
	// }
	// }

	// @Override
	// public Class<?> getColumnClass(int columnIndex) {
	// try {
	// String columnClassName = rsMetaData.getColumnClassName(columnIndex +
	// (showIdColunm ? 1 : 2));
	// //
	// System.out.println("Coluinm name : "+getColumnName(columnIndex)+" class =
	// "+
	// // columnClassName);
	// Class<?> clas = Class.forName(columnClassName);
	// if (clas.isInstance(BigDecimal.class)) {
	// return Double.class;
	// }
	// return clas;
	// } catch (Exception e) {
	// ExceptionUtil.handle(e);
	// return null;
	// }
	// }

	@Override
	public int getCurrentPage() {
		return this.pager.getCurrentPage();

	}

	/**
	 * @return the connectionManager
	 */
	public JKDataSource getDataSource() {
		if (this.dataSource != null) {
			return this.dataSource;
		}
		return JKDataSourceFactory.getDefaultDataSource();
	}

	// /**
	// *
	// * @return int
	// */
	// public int getColumnCount() {
	// if (rsMetaData == null) {
	// return 0;
	// }
	// try {
	// return rsMetaData.getColumnCount() + (showIdColunm ? 0 : -1);
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// return -1;
	// }
	// }

	// /////////////////////////////////////////////////////////////////////////////
	public int getOrderByColunmIndex() {
		return this.orderByColunmIndex;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public OrderDirection getOrderDirection() {
		return this.orderDirection;
	}

	@Override
	public int getPageRowsCount() {
		return this.pager.getPageRowsCount();
	}

	@Override
	public int getPagesCount() {
		return this.pager.getPagesCount();
	}

	/**
	 *
	 * @param row
	 *            int
	 * @return java.lang.String
	 */
	public Object getRecordId(final int row) {
		return getRecord(row).getColumnValue(this.idColumnIndex);
	}

	public int getRecordIdAsInteger(final int row) {
		final Object recordId = getRecordId(row);
		return recordId != null ? Integer.parseInt(recordId.toString()) : 0;
	}

	public JKDataSource getReourceManager() {
		return this.dataSource;
	}

	// /////////////////////////////////////////////////////////////////////
	public int getRowIndexForRecordId(final Object id) {
		if (id != null) {
			int counter = 0;
			for (final FSTableRecord rec : getRecords()) {
				final Object o = rec.getColumnValue(this.idColumnIndex);
				// TODO : be carefull from this method , because it the object
				// tyoe maybe different types of
				// numeric classes
				if (o != null && o.equals(id)) {
					return counter;
				}
				counter++;
			}
		}
		return -1;
	}

	// // /////////////////////////////////////////////////////////////////////
	// private String getQueryLimit() {
	// if (getLimit() > 0) {
	// return "LIMIT " + getLimit();
	// }
	// return "";
	// }

	//
	// ///////////////////////////////////////////////////////////////////////
	// public int getColunmType(int colunm) {
	// try {
	// return rsMetaData.getColumnType(colunm + (showIdColunm ? 1 : 2));
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// return -1;
	// }
	// }

	// /**
	// *
	// * @return String
	// * @param colunm
	// * int
	// */
	// public String getColunmDatabaseName(int colunm) {
	// try {
	// return rsMetaData.getColumnName(colunm + (showIdColunm ? 1 : 2));
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// return "";
	// }
	// }

	/**
	 *
	 * @return boolean
	 */
	public boolean getShowIdColunm() {
		return this.showIdColunm;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public String getSql() {
		if (this.sql == null || this.sql.equals("")) {
			return "";
		}
		String sqlStr = this.sql;
		// String groupBy="";//optional
		final int indexOfWhere = sqlStr.toLowerCase().indexOf("where");
		if (indexOfWhere != -1) {
			sqlStr = sqlStr.substring(0, indexOfWhere);
		}
		// int gruopByIndex=sqlStr.toLowerCase().indexOf("group by");
		// if(gruopByIndex!=-1){
		// groupBy=sqlStr.substring(gruopByIndex);
		// }
		sqlStr += getWhereClause();
		sqlStr += " " + this.groupBy;
		if (this.orderByColunmIndex != -1) {
			sqlStr += " ORDER BY " + (this.orderByColunmIndex + 1);
			sqlStr += " " + getOrderDirection().toString();
		} else {
			sqlStr += " " + this.orderByStatement;
		}
		// sqlStr += " " + getQueryLimit();
		// System.out.println(sqlStr);
		return sqlStr;
	}

	/**
	 * @return the staticWhere
	 */
	public String getStaticWhere() {
		return this.staticWhere;
	}

	// ////////////////////////////////////////////////////////////////////////
	public int getValueRowIndex(final String value, final int colunm) {
		final Vector<FSTableRecord> records = getRecords();
		for (int i = 0; i < records.size(); i++) {
			final FSTableRecord record = records.get(i);
			final Object o = record.getColumnValue(colunm);
			if (o.equals(value)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *
	 * @return String
	 */
	public String getWhereClause() {
		final StringBuffer where = new StringBuffer(" 1=1 ");
		if (getStaticWhere() != null) {
			where.append(" AND " + getStaticWhere());
		}
		final Set keys = this.extraSqlConditions.keySet();
		final Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			final Object item = iter.next();
			if (!this.extraSqlConditions.get(item).equals("")) {
				where.append(" AND " + this.extraSqlConditions.get(item));
			}
		}
		// return where.toString();
		int indexOfWhere;
		if ((indexOfWhere = this.sql.toLowerCase().indexOf("where")) == -1) {
			return " WHERE " + where.toString();
		}
		final StringBuffer buf = new StringBuffer(" " + this.sql.substring(indexOfWhere));
		buf.append(" AND " + where.toString());
		// remove the old where clause
		// sql=sql.substring(0,indexOfWhere);
		return buf.toString();

	}

	// /////////////////////////////////////////////////////////////////////
	public boolean isIdExists(final Object id) {
		return getRowIndexForRecordId(id) != -1;
	}

	// //////////////////////////////////////////////////////////////////////////
	// public int getIntegerColunmSum(int col) {
	// return (int) getColunmSum(col);
	// }

	// /**
	// *
	// * @param col
	// * int
	// * @return float
	// */
	// public double getColunmSum(int col) {
	// double sum = 0;
	// for (int i = 0; i < data.size(); i++) {
	// if (!getValueAt(i, col).equals(EMPETY_STRING)) { // if not zero
	// sum += Float.parseFloat(getValueAt(i, col).toString());
	// }
	// }
	// return sum;
	// }

	// /**
	// *
	// * @param col
	// * @return
	// */
	// public double getColunmSum(String colunmName) {
	// return getColunmSum(getColunmIndex(colunmName));
	// }

	// /**
	// *
	// * @return boolean[]
	// */
	// public boolean[] getColunmsVisilbleArray() {
	// return colunmsVisibility;
	// }

	// public void clearAll() {
	// data.clear();
	// fireTableDataChanged();
	// }

	/**
	 */
	public void loadData() {
		// System.out.println("Reloading Data..."+counter++);
		final String sql = getSql();
		// System.out.println(sql);
		if (sql != null && !sql.equals("")) {
			// System.out.println("Load data :"+counter++);
			// Connection connection = null;
			// DateTimeUtil.printCurrentTime(1);
			// ResultSet rs = null;
			// Statement statement = null;
			// DataSource connectionManager = getDataSource();
			try {
				// connection = connectionManager.getQueryConnection();
				// DateTimeUtil.printCurrentTime(2);
				// statement = connection.createStatement();
				// DateTimeUtil.printCurrentTime(3);
				this.pager.setQuery(sql);

				// rs=pager.getResultSet();
				// rs = statement.executeQuery(sql);
				// DateTimeUtil.printCurrentTime(4);
				// crs = new CachedRowSetImpl();
				// crs.populate(rs);
				// rsMetaData = (ResultSetMetaData) crs.getMetaData();
				refreshData();
				// table.
			} catch (final Exception e) {
				ExceptionUtil.handle(new JKDataAccessException("Exception occured while trying to execute the following query : \n" + sql, e));
			} finally {
				// connectionManager.close(rs);
				// connectionManager.close(statement);
				// connectionManager.close(connection);
			}

		}
	}

	@Override
	public void moveToFirstPage() throws PagingException {
		this.pager.moveToFirstPage();
		try {
			refreshData();
		} catch (final Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public void moveToLastPage() throws PagingException {
		this.pager.moveToLastPage();
		try {
			refreshData();
		} catch (final Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public void moveToNextPage() throws PagingException {
		this.pager.moveToNextPage();
		try {
			refreshData();
		} catch (final Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public void moveToPage(final int page) throws PagingException {
		this.pager.moveToPage(page);
		try {
			refreshData();
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	// @Override
	// public void setValueAt(Object value, int rowIndex, int columnIndex) {
	// System.out.println("trying to set value : " + value);
	// modified = true;
	// int column = columnIndex + (showIdColunm ? 0 : 1);
	// if (rowIndex >= getRowCount()) {
	// return;// simply ignore the value
	// }
	// modifiedRows.put(rowIndex, true);
	// data.elementAt(rowIndex).set(column, value);
	// // fireTableCellUpdated(rowIndex, column);
	// // the above doesn't always notify the cell if it is not editable,as
	// // temp work around , we will try this
	// fireTableRowsUpdated(rowIndex, rowIndex);
	// }

	@Override
	public void moveToPreviousePage() throws PagingException {
		this.pager.moveToPreviousePage();
		try {
			refreshData();
		} catch (final Exception e) {
			throw new PagingException(e);
		}
	}

	// public void setQueryLimit(int limit) {
	// this.limit = limit;
	// }

	// ///////////////////////////////////////////////////////////////////////
	private void refreshData() throws SQLException, ClassNotFoundException {
		resetRecords();
		final CachedRowSet rs = this.pager.getResultSet();
		int colCount;
		if (rs != null) {
			if (this.queryChanged) {
				final ResultSetMetaData rsMetaData = rs.getMetaData();
				setTablesColumnFromMetaData(rsMetaData);
				colCount = rsMetaData.getColumnCount();
			} else {
				colCount = getColumnCount();
			}

			// Workaround for oracle rownum in the DataBasePager
			// TODO : handle the paging in oracle in betterway
			final int colunmIndex = getColunmIndex("R");
			if (colunmIndex >= 0) {
				final FSTableColumn tableColumn = getTableColumn(colunmIndex);
				tableColumn.setVisible(false);
			}

			while (rs.next()) {
				final FSTableRecord record = createEmptyRecord();
				for (int i = 0; i < colCount; i++) {
					record.setColumnValue(i, rs.getObject(i + 1));
				}
				addRecord(record);
			}
		}

		fireTableChanged(null); // notify everyone that we have a new
	}

	/**
	 * @param connectionManager
	 *            the connectionManager to set
	 */
	public void setDataSource(final JKDataSource connectionManager) {
		this.dataSource = connectionManager;
	}

	/**
	 *
	 * @param colunmIndex
	 *            String
	 * @param condition
	 *            String
	 */
	public void setExtraSQLCondition(final String colunmName, final String condition) {
		this.extraSqlConditions.put(getColunmIndex(colunmName), condition);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public void setOrderByColunmIndex(final int orderByColunmIndex) {
		this.orderByColunmIndex = orderByColunmIndex;

	}

	// /////////////////////////////////////////////////////////////////////////////
	public void setOrderDirection(final OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	@Override
	public void setPageRowsCount(final int pageRowsCount) {
		this.pager.setPageRowsCount(pageRowsCount);
		// loadData();
	}

	public void setReourceManager(final JKDataSource reourceManager) {
		this.dataSource = reourceManager;
	}

	/**
	 *
	 * @param showIdColunm
	 *            boolean
	 */
	public void setShowIdColunm(final boolean showIdColunm) {
		this.showIdColunm = showIdColunm;
		if (getColumnCount() > 0) {
			getTableColumn(0, false).setVisible(showIdColunm);
			refreshVisibility();
		}
	}

	/**
	 * setSql
	 *
	 * @param sql
	 *            String
	 */
	public void setSql(String sql) {
		// this.sql=sql;
		// if(orderByColunmIndex!=-1 &&
		// sql.toUpperCase().indexOf("ORDER BY")!=-1){
		// throw new
		// RuntimeException("ORDER by is not allowed in QueryTableModel , " +
		// "please use setOrderByColunmIndex\n"+sql
		// +" , or set orderByColunmIndex to -1");
		// }
		this.queryChanged = true;
		final int orderByIndex = sql.toUpperCase().lastIndexOf("ORDER BY");
		if (orderByIndex != -1) {
			this.orderByColunmIndex = -1;
			this.orderByStatement = sql.substring(orderByIndex);
			sql = sql.substring(0, orderByIndex);
		}

		final int indexOfGroupBy = sql.toLowerCase().indexOf("group by");
		if (indexOfGroupBy != -1) {
			this.groupBy = sql.substring(indexOfGroupBy);
			this.sql = sql.substring(0, indexOfGroupBy);
		} else {
			this.sql = sql;
		}
	}

	/**
	 * @param staticWhere
	 *            the staticWhere to set
	 */
	public void setStaticWhere(final String staticWhere) {
		this.staticWhere = staticWhere;
	}

	// ///////////////////////////////////////////////////////////////////////
	protected void setTablesColumnFromMetaData(final ResultSetMetaData rsMetaData) throws SQLException, ClassNotFoundException {
		for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
			final int sqlIndex = i + 1;
			FSTableColumn col;
			if (!isValidTableColumnIndex(i)) {
				col = new FSTableColumn();
				addFSTableColumn(col);
			} else {
				col = getTableColumn(i, false);
			}
			col.setName(rsMetaData.getColumnName(sqlIndex));
			col.setHumanName(Lables.get(col.getName()));
			col.setColumnClassName(rsMetaData.getColumnClassName(sqlIndex));
			// col.setRequired(rsMetaData.isNullable(sqlIndex) ==
			// rsMetaData.columnNullable);
			col.setColumnType(rsMetaData.getColumnType(sqlIndex));
			col.setColumnTypeName(rsMetaData.getColumnTypeName(sqlIndex));
		}
		setShowIdColunm(this.showIdColunm);
	}

}
