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
package com.fs.commons.desktop.swing.comp2;

import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.desktop.swing.dao.QueryTableModel;

public class FSDataTable extends QueryJTable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7487455150366506480L;

	public FSDataTable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final QueryTableModel model, final String title, final boolean allowFiltering) {
		super(model, title, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final String sql) {
		super(sql);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final String title, final JKDataSource resource, final String sql, final boolean allowFiltering) {
		super(title, resource, sql, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final String sql, final String title) {
		super(sql, title);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final String sql, final String title, final boolean allowFiltering) {
		super(sql, title, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final TableMeta tableMeta) {
		super(tableMeta);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final TableMeta tableMeta, final boolean allowFiltering) {
		super(tableMeta, allowFiltering);
		// TODO Auto-generated constructor stub
	}

	public FSDataTable(final TableMeta tableMeta, final String title, final boolean showFilters) {
		super(tableMeta, title, showFilters);
		// TODO Auto-generated constructor stub
	}

}
