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
package com.fs.commons.apps.templates.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.GeneralUtility;

public class PnlVariables extends DynMasterDetailPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// ////////////////////////////////////////////////////////////////////////
	public PnlVariables() throws TableMetaNotFoundException, JKDataAccessException, UIOPanelCreationException {
		super(AbstractTableMetaFactory.getTableMeta("conf_vars"));
		init();
		getTableNameCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleTableNameChanged();
			}
		});
	}

	private DaoComboBox getFieldNameCombo() {
		return (DaoComboBox) getMasterPanel().getFieldComponent("field_name");
	}

	// /////////////////////////////////////////////////////////////////////////////
	private JComboBox<?> getTableNameCombo() {
		return (JComboBox<?>) getMasterPanel().getFieldComponent("table_name");
	}

	// /////////////////////////////////////////////////////////////////////////////
	protected void handleTableNameChanged() {
		final Object selectedItem = getTableNameCombo().getSelectedItem();
		final DaoComboBox fields = getFieldNameCombo();
		if (selectedItem == null) {
			// fields.removeAll();
		} else {
			String sql = GeneralUtility.getSqlFile("db_table_col_names.sql");
			sql = DaoUtil.compileSql(sql, new String[] { selectedItem.toString(), fields.getDataSource().getDatabaseName() });
			fields.setSql(sql);
		}
	}

}
