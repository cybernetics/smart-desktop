package com.fs.commons.desktop.swing.dao;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.paging.DataBasePager;
import com.fs.commons.dao.paging.DataPager;
import com.fs.commons.dao.paging.PagingException;
import com.fs.commons.desktop.swing.comp.model.FSTableColumn;
import com.fs.commons.desktop.swing.comp.model.FSTableModel;
import com.fs.commons.desktop.swing.comp.model.FSTableRecord;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;

public class QueryTableModel extends FSTableModel implements DataPager {
	private static final long serialVersionUID = 1L;
	public static final String EMPETY_STRING = null;

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

	private DataSource dataSource;

	private String orderByStatement = "";

	private int limit = -1;
	private int idColumnIndex = 0;

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
	 * @param sql
	 *            String
	 */
	public QueryTableModel(String sql) {
		this(sql, false);
	}

	/**
	 * @param sql
	 * @param showIdColunm
	 */
	public QueryTableModel(String sql, boolean showIdColunm) {
		setSql(sql);
		this.showIdColunm = showIdColunm;
		// data = new Vector<Vector<Object>>();
		// initDB();
		loadData();
	}

	/**
	 * 
	 * @param connectionManager
	 */
	public QueryTableModel(DataSource connectionManager) {
		this();
		setDataSource(connectionManager);
	}

	/**
	 * @param connectionManager
	 *            the connectionManager to set
	 */
	public void setDataSource(DataSource connectionManager) {
		this.dataSource = connectionManager;
	}

	/**
	 * @param manager
	 * @param sql
	 */
	public QueryTableModel(DataSource reourceManager, String sql) {
		this(reourceManager, sql, 0);
	}

	/**
	 * @param manager
	 * @param sql2
	 * @param orderByColunmIndex2
	 */
	public QueryTableModel(DataSource manager, String sql, int orderByColunmIndex) {
		setReourceManager(manager);
		setOrderByColunmIndex(orderByColunmIndex);
		setSql(sql);
		// initDB();
		loadData();
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
		queryChanged = true;
		int orderByIndex = sql.toUpperCase().lastIndexOf("ORDER BY");
		if (orderByIndex != -1) {
			orderByColunmIndex = -1;
			orderByStatement = sql.substring(orderByIndex);
			sql = sql.substring(0, orderByIndex);
		}

		int indexOfGroupBy = sql.toLowerCase().indexOf("group by");
		if (indexOfGroupBy != -1) {
			groupBy = sql.substring(indexOfGroupBy);
			this.sql = sql.substring(0, indexOfGroupBy);
		} else {
			this.sql = sql;
		}
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
	// System.out.println("Coluinm name : "+getColumnName(columnIndex)+" class = "+
	// // columnClassName);
	// Class<?> clas = Class.forName(columnClassName);
	// if (clas.isInstance(BigDecimal.class)) {
	// return Double.class;
	// }
	// return clas;
	// } catch (Exception e) {
	// ExceptionUtil.handleException(e);
	// return null;
	// }
	// }

	/**
	 * 
	 * @param showIdColunm
	 *            boolean
	 */
	public void setShowIdColunm(boolean showIdColunm) {
		this.showIdColunm = showIdColunm;
		if (getColumnCount() > 0) {
			getTableColumn(0, false).setVisible(showIdColunm);
			refreshVisibility();
		}
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean getShowIdColunm() {
		return showIdColunm;
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

	/**
	 */
	public void loadData() {
		// System.out.println("Reloading Data..."+counter++);
		String sql = getSql();
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
				pager.setQuery(sql);

				// rs=pager.getResultSet();
				// rs = statement.executeQuery(sql);
				// DateTimeUtil.printCurrentTime(4);
				// crs = new CachedRowSetImpl();
				// crs.populate(rs);
				// rsMetaData = (ResultSetMetaData) crs.getMetaData();
				refreshData();
				// table.
			} catch (Exception e) {
				ExceptionUtil.handleException(new DaoException("Exception occured while trying to execute the following query : \n" + sql, e));
			} finally {
				// connectionManager.close(rs);
				// connectionManager.close(statement);
				// connectionManager.close(connection);
			}

		}
	}

	// ///////////////////////////////////////////////////////////////////////
	private void refreshData() throws SQLException, ClassNotFoundException {
		resetRecords();
		CachedRowSet rs = pager.getResultSet();
		int colCount;
		if (rs != null) {
			if (queryChanged) {
				ResultSetMetaData rsMetaData = (ResultSetMetaData) rs.getMetaData();
				setTablesColumnFromMetaData(rsMetaData);
				colCount = rsMetaData.getColumnCount();
			} else {
				colCount = getColumnCount();
			}

			// Workaround for oracle rownum in the DataBasePager
			// TODO : handle the paging in oracle in betterway
			int colunmIndex = getColunmIndex("R");
			if (colunmIndex >= 0) {
				FSTableColumn tableColumn = getTableColumn(colunmIndex);
				tableColumn.setVisible(false);
			}

			while (rs.next()) {
				FSTableRecord record = createEmptyRecord();
				for (int i = 0; i < colCount; i++) {
					record.setColumnValue(i, rs.getObject(i + 1));
				}
				addRecord(record);
			}
		}

		fireTableChanged(null); // notify everyone that we have a new
	}

	// ///////////////////////////////////////////////////////////////////////
	protected void setTablesColumnFromMetaData(ResultSetMetaData rsMetaData) throws SQLException, ClassNotFoundException {
		for (int i = 0; i < rsMetaData.getColumnCount(); i++) {
			int sqlIndex = i + 1;
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
		setShowIdColunm(showIdColunm);
	}

	// /////////////////////////////////////////////////////////////////////////////
	public int getOrderByColunmIndex() {
		return orderByColunmIndex;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public OrderDirection getOrderDirection() {
		return orderDirection;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public void setOrderDirection(OrderDirection orderDirection) {
		this.orderDirection = orderDirection;
	}

	// /////////////////////////////////////////////////////////////////////////////
	public void setOrderByColunmIndex(int orderByColunmIndex) {
		this.orderByColunmIndex = orderByColunmIndex;

	}

	// /////////////////////////////////////////////////////////////////////////////
	public String getSql() {
		if (sql == null || sql.equals("")) {
			return "";
		}
		String sqlStr = sql;
		// String groupBy="";//optional
		int indexOfWhere = sqlStr.toLowerCase().indexOf("where");
		if (indexOfWhere != -1) {
			sqlStr = sqlStr.substring(0, indexOfWhere);
		}
		// int gruopByIndex=sqlStr.toLowerCase().indexOf("group by");
		// if(gruopByIndex!=-1){
		// groupBy=sqlStr.substring(gruopByIndex);
		// }
		sqlStr += getWhereClause();
		sqlStr += " " + groupBy;
		if (orderByColunmIndex != -1) {
			sqlStr += " ORDER BY " + (orderByColunmIndex + 1);
			sqlStr += " " + getOrderDirection().toString();
		} else {
			sqlStr += " " + orderByStatement;
		}
		// sqlStr += " " + getQueryLimit();
		// System.out.println(sqlStr);
		return sqlStr;
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
	 * @param colunmIndex
	 *            String
	 * @param condition
	 *            String
	 */
	public void setExtraSQLCondition(String colunmName, String condition) {
		extraSqlConditions.put(getColunmIndex(colunmName), condition);
	}

	/**
	 * 
	 * @return String
	 */
	public String getWhereClause() {
		StringBuffer where = new StringBuffer(" 1=1 ");
		if (getStaticWhere() != null) {
			where.append(" AND " + getStaticWhere());
		}
		Set keys = extraSqlConditions.keySet();
		Iterator iter = keys.iterator();
		while (iter.hasNext()) {
			Object item = iter.next();
			if (!extraSqlConditions.get(item).equals("")) {
				where.append(" AND " + extraSqlConditions.get(item));
			}
		}
		// return where.toString();
		int indexOfWhere;
		if ((indexOfWhere = sql.toLowerCase().indexOf("where")) == -1) {
			return " WHERE " + where.toString();
		}
		StringBuffer buf = new StringBuffer(" " + sql.substring(indexOfWhere));
		buf.append(" AND " + where.toString());
		// remove the old where clause
		// sql=sql.substring(0,indexOfWhere);
		return buf.toString();

	}

	/**
	 * 
	 * @param row
	 *            int
	 * @return java.lang.String
	 */
	public Object getRecordId(int row) {
		return getRecord(row).getColumnValue(idColumnIndex);
	}

	// /////////////////////////////////////////////////////////////////////
	public boolean isIdExists(Object id) {
		return getRowIndexForRecordId(id) != -1;
	}

	// /////////////////////////////////////////////////////////////////////
	public int getRowIndexForRecordId(Object id) {
		if (id != null) {
			int counter = 0;
			for (FSTableRecord rec : getRecords()) {
				Object o = rec.getColumnValue(idColumnIndex);
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

	// ////////////////////////////////////////////////////////////////////////
	public int getValueRowIndex(String value, int colunm) {
		Vector<FSTableRecord> records = getRecords();
		for (int i = 0; i < records.size(); i++) {
			FSTableRecord record = records.get(i);
			Object o = record.getColumnValue(colunm);
			if (o.equals(value)) {
				return i;
			}
		}
		return -1;
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
	 * @return the staticWhere
	 */
	public String getStaticWhere() {
		return this.staticWhere;
	}

	/**
	 * @param staticWhere
	 *            the staticWhere to set
	 */
	public void setStaticWhere(String staticWhere) {
		this.staticWhere = staticWhere;
	}

	public DataSource getReourceManager() {
		return dataSource;
	}

	public void setReourceManager(DataSource reourceManager) {
		this.dataSource = reourceManager;
	}

	public int getRecordIdAsInteger(int row) {
		Object recordId = getRecordId(row);
		return recordId != null ? Integer.parseInt(recordId.toString()) : 0;
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

	/**
	 * @return the connectionManager
	 */
	public DataSource getDataSource() {
		if (dataSource != null) {
			return dataSource;
		}
		return DataSourceFactory.getDefaultDataSource();
	}

	// public void setQueryLimit(int limit) {
	// this.limit = limit;
	// }

	@Override
	public void setPageRowsCount(int pageRowsCount) {
		pager.setPageRowsCount(pageRowsCount);
		// loadData();
	}

	@Override
	public int getPagesCount() {
		return pager.getPagesCount();
	}

	@Override
	public void moveToFirstPage() throws PagingException {
		pager.moveToFirstPage();
		try {
			refreshData();
		} catch (Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public void moveToLastPage() throws PagingException {
		pager.moveToLastPage();
		try {
			refreshData();
		} catch (Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public void moveToNextPage() throws PagingException {
		pager.moveToNextPage();
		try {
			refreshData();
		} catch (Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public void moveToPreviousePage() throws PagingException {
		pager.moveToPreviousePage();
		try {
			refreshData();
		} catch (Exception e) {
			throw new PagingException(e);
		}
	}

	@Override
	public int getCurrentPage() {
		return pager.getCurrentPage();

	}

	@Override
	public int getAllRowsCount() {
		return pager.getAllRowsCount();
	}

	@Override
	public int getPageRowsCount() {
		return pager.getPageRowsCount();
	}

	@Override
	public void moveToPage(int page) throws PagingException {
		pager.moveToPage(page);
		try {
			refreshData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	public int[] getAllRecordIds() {
		int ids[] = new int[getRowCount()];
		for (int i = 0; i < getRowCount(); i++) {
			ids[i] = getRecordIdAsInteger(i);
		}
		return ids;
	}

}
