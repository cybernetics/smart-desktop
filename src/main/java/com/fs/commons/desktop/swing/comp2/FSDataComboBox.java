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

import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.dao.DaoComboBox;

public class FSDataComboBox extends DaoComboBox {

	/**
	 *
	 */
	private static final long serialVersionUID = -4062882799656103407L;

	public FSDataComboBox() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(final ForiegnKeyFieldMeta meta) {
		super(meta);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(final String sql) {
		super(sql);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(final String sql, final boolean transferFocusOnEnter) {
		super(sql, transferFocusOnEnter);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(final String sql, final int defauleSelectedIndex) {
		super(sql, defauleSelectedIndex);
		// TODO Auto-generated constructor stub
	}

	public FSDataComboBox(final TableMeta tableMeta) {
		super(tableMeta);
		// TODO Auto-generated constructor stub
	}

}
