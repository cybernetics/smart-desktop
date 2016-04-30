//package com.fs.commons.desktop.swing.comp.model;
//
//import java.awt.BorderLayout;
//import java.util.ArrayList;
//
//import com.fs.commons.desktop.swing.RowData;
//import com.fs.commons.desktop.swing.SwingUtility;
//import com.fs.commons.desktop.swing.comp.JKScrollPane;
//import com.fs.commons.desktop.swing.comp.JKTable;
//import com.fs.commons.desktop.swing.comp.panels.JKPanel;
//import com.fs.commons.locale.Lables;
//import com.fs.commons.logging.Logger;
//
//public class DynamicTableModel extends FSTableModel {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//
//	enum OrderDirection {
//		ASCENDING, DESCENDING;
//		/**
//		 * 
//		 * @return String
//		 */
//		@Override
//		public String toString() {
//			return this == ASCENDING ? "ASC" : "DESC";
//		}
//	}
//
//	ArrayList<RowData> data;
//	// names should include invisble fields
//	ArrayList<Column> columns;
//
//	OrderDirection orderDirection = OrderDirection.ASCENDING;
//
//	// if the any cell data is equal to the next string , it will show empty
//	// text
//	String ignoreString = "-1";
//
//	/**
//	 * 
//	 * @return
//	 */
//	public String getIgnoreString() {
//		return ignoreString;
//	}
//
//	/**
//	 * 
//	 * @param ignoreString
//	 */
//	public void setIgnoreString(String ignoreString) {
//		this.ignoreString = ignoreString;
//	}
//
//	/**
//	 * 
//	 */
//	public DynamicTableModel() {
//		this(new ArrayList<RowData>(), new ArrayList<Column>());
//	}
//
//	/**
//	 * 
//	 * @param data
//	 * @param arrayList
//	 */
//	public DynamicTableModel(ArrayList<RowData> data, ArrayList<Column> columnNames) {
//		this.data = data;
//		columns = columnNames;
//	}
//
//	/**
//	 * 
//	 * @param i
//	 *            int
//	 * @return String
//	 */
//	@Override
//	public String getColumnName(int col) {
//		String columnName = getColumnRealName(col);
//		return Lables.get(columnName);
//	}
//
//	/**
//	 */
//	private String getColumnRealName(int col) {
//		return columns.get(getColumnRealIndex(col)).getColumnName();
//	}
//
//	@Override
//	public Class<?> getColumnClass(int columnIndex) {
//		return columns.get(columnIndex).getColumnType();
//	}
//
//	/**
//	 * 
//	 * @return int
//	 */
//	public int getColumnCount() {
//		return getVisibleColumnCount();
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	public int getColumnRealCount() {
//		return columns.size();
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	private int getVisibleColumnCount() {
//		int count = 0;
//		for (int i = 0; i < columns.size(); i++) {
//			if (columns.get(i).isVisible()) {
//				count++;
//			}
//		}
//		return count;
//	}
//
//	/**
//	 * 
//	 * @return int
//	 */
//	public int getRowCount() {
//		return data.size();
//	}
//
//	/**
//	 * used to get value from visible columns only
//	 * 
//	 * @param row
//	 *            int
//	 * @param col
//	 *            int
//	 * @return Object
//	 */
//	public Object getValueAt(int row, int col) {
//		int columnIndex = getColumnRealIndex(col);
//		Object o = (data.get(row)).get(columnIndex);
//		if (o == null || o.equals("")) {
//			return getEmptyCellValue();
//		}
//		if (o.toString().trim().equals(getIgnoreString())) {
//			return "";
//		}
//		return o;
//	}
//
//	/**
//	 * called to get visible column real index in the data list
//	 * 
//	 * @param col
//	 * @return
//	 */
//	private int getColumnRealIndex(int col) {
//		int visibleIndex = 0;
//		int index = 0;
//		do {
//			if (columns.get(index).isVisible()) {
//				if (visibleIndex++ == col) {
//					return index;
//				}
//			}
//		} while (++index < columns.size());
//		if (visibleIndex == col) {
//			return index - 1;
//		}
//		throw new IllegalStateException("model with " + getColumnCount() + " columns has invalid on column getVisibleColumnRealIndex() " + col);
//	}
//
//	/**
//	 * 
//	 * @return
//	 */
//	protected Object getEmptyCellValue() {
//		return "";
//	}
//
//	/**
//	 * 
//	 * @param colunm
//	 *            int
//	 * @param visible
//	 *            boolean
//	 */
//	public void setColunmVisibility(int colunm, boolean visible) {
//		columns.get(colunm).setVisible(visible);
//	}
//
//	/**
//	 * 
//	 * @return boolean
//	 * @param colunm
//	 *            int
//	 */
//	public boolean isColunmVisible(int colunm) {
//		return columns.get(colunm).isVisible();
//	}
//
//	/**
//	 * 
//	 * @param index
//	 *            int
//	 * @return int
//	 */
//	public int getColunmWidth(int column) {
//		return columns.get(column).getWidth();
//	}
//
//	/**
//	 * 
//	 * @return OrderDirection
//	 */
//	public OrderDirection getOrderDirection() {
//		return orderDirection;
//	}
//
//	/**
//	 * 
//	 * @param orderDirection
//	 *            OrderDirection
//	 */
//	public void setOrderDirection(OrderDirection orderDirection) {
//		this.orderDirection = orderDirection;
//	}
//
//	/**
//	 * col could be any index including hidden columns
//	 * 
//	 * @param row
//	 *            int
//	 * @return java.lang.String
//	 */
//	public Object getCellValue(int row, int col) {
//		return (data.get(row)).get(col);
//	}
//
//	/**
//	 * get the real index name
//	 * 
//	 * @param name
//	 * @return
//	 */
//	public int getColumnRealIndex(String name) {
//		for (int i = 0; i < getColumnCount(); i++) {
//			if (getColumnRealName(i).equals(name)) {
//				return i;
//			}
//		}
//		return -1;
//	}
//
//	// /**
//	// *
//	// * @param col
//	// * @return
//	// */
//	// public int getColumnIntegerSum(int col) {
//	// return (int) getColunmSum(col);
//	// }
//	// /**
//	// *
//	// * @param col
//	// * int
//	// * @return float
//	// */
//	// public float getColunmSum(int col) {
//	// float sum = 0;
//	// for (int i = 0; i < data.size(); i++) {
//	// if (getCellValue(i, col)!=null) {
//	// sum += Float.parseFloat((getValueAt(i, col).toString()));
//	// }
//	// }
//	// return sum;
//	// }
//
//	/**
//	 * 
//	 * @param col
//	 * @return
//	 */
//	public double getColunmSum(String colunmName) {
//		return getColunmSum(getColumnRealIndex(colunmName));
//	}
//
//	/**
//	 * 
//	 */
//	public void clear() {
//		data.clear();
//		fireTableDataChanged();
//	}
//
//	@Override
//	public void setValueAt(Object value, int rowIndex, int columnIndex) {
//		int column = getColumnRealIndex(columnIndex);
//		// the follwing check used to avoid the problem caused by defaulttable model
//		// which always return the values from the JKTextField renders as string
//		Logger.info("Value : "+value+"   instanceof "+value.getClass().getName());
//		if (value != null && !value.equals("") && value instanceof String ){
//			if( getColumnClass(columnIndex) == Integer.class) {		
//				value = Integer.parseInt((String) value);
//			}
//			if(getColumnClass(columnIndex) == Float.class) {
//				value=Float.parseFloat((String)value);
//			}
//		}
//		data.get(rowIndex).set(column, value);
//		fireTableRowsUpdated(rowIndex, rowIndex);
//	}
//
//	/**
//	 * 
//	 * @author jalal
//	 * 
//	 */
//	public static class Column {
//		String columnName;
//		Class<?> columnType;
//		boolean visible;
//		private int width;
//
//		/**
//		 * 
//		 */
//		public Column() {
//		}
//
//		/**
//		 * 
//		 * @param string
//		 * @param class1
//		 */
//		public Column(String name, Class<?> clas) {
//			this(name, clas, true);
//		}
//
//		/**
//		 * 
//		 * @param name
//		 * @param clas
//		 * @param visible
//		 */
//		public Column(String name, Class<?> clas, boolean visible) {
//			this.columnName = name;
//			this.columnType = clas;
//			this.visible = visible;
//		}
//
//		/**
//		 * @return the columnName
//		 */
//		public String getColumnName() {
//			return columnName;
//		}
//
//		public int getWidth() {
//			return width;
//		}
//
//		/**
//		 * @param columnName
//		 *            the columnName to set
//		 */
//		public void setColumnName(String columnName) {
//			this.columnName = columnName;
//		}
//
//		/**
//		 * @return the columnType
//		 */
//		public Class<?> getColumnType() {
//			return columnType;
//		}
//
//		/**
//		 * @param columnType
//		 *            the columnType to set
//		 */
//		public void setColumnType(Class<?> columnType) {
//			this.columnType = columnType;
//		}
//
//		/**
//		 * @return the visible
//		 */
//		public boolean isVisible() {
//			return visible;
//		}
//
//		/**
//		 * @param visible
//		 *            the visible to set
//		 */
//		public void setVisible(boolean visible) {
//			this.visible = visible;
//		}
//
//		/**
//		 * @param width
//		 *            the width to set
//		 */
//		public void setWidth(int width) {
//			this.width = width;
//		}
//	}
//
//	/**
//	 * 
//	 * @param row
//	 */
//	public void addRow(RowData row) {
//		data.add(row);
////		if (row.size() < columns.size()) {
////			for (int i = row.size(); i < columns.size(); i++) {
////				row.add(null,col);
////			}
////		}
//		fireTableDataChanged();
//	}
//
//	// /**
//	// *
//	// * @param rowData
//	// */
//	// public void addRow(RowData rowData){
//	// addRow(rowData.toArrayList());
//	// }
//
//	/**
//	 * 
//	 * @param col1
//	 */
//	public void addColumn(Column col) {
//		columns.add(col);
////		for (int i = 0; i < data.size(); i++) {
////			data.get(i).add(null);
////		}
//		fireTableStructureChanged();
//	}
//
//	/**
//	 * 
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		Column col1 = new Column("Student Id", Integer.class);
//		Column col2 = new Column("Student name", String.class);
//		Column col3 = new Column("Student avg", Float.class, false);
//		Column col4 = new Column("Student avg", Boolean.class, false);
//		DynamicTableModel model = new DynamicTableModel();
//		model.addColumn(col1);
//		model.addColumn(col2);
//		model.addColumn(col3);
//		model.addColumn(col4);
//		ArrayList<Object> row = new ArrayList<Object>();
//		row.add(1);
//		row.add("Jalal");
//		row.add("12.5");
//		model.addRow(row);
//
//		row = new ArrayList<Object>();
//		row.add(2);
//		row.add("Ata");
//		row.add("13.2");
//		model.addRow(row);
//		JKTable tbl = new JKTable(model);
//		JKPanel<?> pnl = new JKPanel<Object>();
//		pnl.setLayout(new BorderLayout());
//		pnl.add(new JKScrollPane(tbl));
//		model.addColumn(new Column("TEST_COLUMN", Integer.class, true));
//		tbl.getFsModel();
//
//		SwingUtility.testPanel(pnl);
//	}
//
//	public void addRow(ArrayList<?> row) {
//		addRow(new DefaultRowData(row));
//	}
//
//	/**
//	 * 
//	 * @param i
//	 * @param j
//	 * @return
//	 */
//	public int findRowIndexByColunmValue(int columnIndex, Object Object) {
//		for (int i = 0; i < data.size(); i++) {
//			if (getCellValue(i, columnIndex).equals(Object)) {
//				return i;
//			}
//		}
//		return -1;
//	}
//
//	/**
//	 * 
//	 */
//	public void removeRow(int row) {
//		data.remove(row);
//		fireTableDataChanged();
//	}
//
//	public void setData(ArrayList<RowData> data) {
//		this.data.clear();
//		for (int i = 0; i < data.size(); i++) {
//			addRow(data.get(i));
//		}
//		fireTableDataChanged();
//	}
//
//	public void setColumns(ArrayList<Column> column) {
//		this.columns = column;
//		fireTableStructureChanged();
//	}
//
//	/**
//	 * 
//	 */
//	public void removeAllColunms() {
//		this.columns.clear();
//		this.data.clear();
//		fireTableStructureChanged();
//	}
//
//	/**
//	 * @return the data
//	 */
//	public ArrayList<RowData> getData() {
//		return data;
//	}
//
//	public RowData getRowData(int row) {
//		return data.get(row);
//	}
//}
//
//class DefaultRowData implements RowData {
//	private ArrayList list;
//
//	public DefaultRowData() {
//		list=new ArrayList<Object>();
//	}
//	public DefaultRowData(ArrayList<?> list) {
//		this.list = list;
//	}
//
//	@Override
//	public Object get(int columnIndex) {
//		return list.get(columnIndex);
//	}
//
//	@Override
//	public void set(int column, Object value) {
//		list.set(column, value);
//	}
//
//	@Override
//	public int size() {
//		return list.size();
//	}
//
//}
