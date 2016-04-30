package com.fs.commons.desktop.swing.comp2;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.desktop.swing.dao.QueryTableModel;

public class FSDataTable extends QueryJTable {

	public FSDataTable(QueryTableModel model, String title, boolean allowFiltering) {
		super(model, title, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(String title, DataSource resource, String sql, boolean allowFiltering) {
		super(title, resource, sql, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(String sql, String title, boolean allowFiltering) {
		super(sql, title, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(String sql, String title) {
		super(sql, title);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(TableMeta tableMeta, boolean allowFiltering) {
		super(tableMeta, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(TableMeta tableMeta, String title, boolean showFilters) {
		super(tableMeta, title, showFilters);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(TableMeta tableMeta) {
		super(tableMeta);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(String sql) {
		super(sql);
		// TODO Auto-generated constructor stub
	}



}
