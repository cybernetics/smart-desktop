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
package com.fs.commons.reports;

import java.util.Properties;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.FieldPanelWithFilter;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.dao.DaoComboBox;

/**
 * @author u087
 *
 */
public class ReportUIComponentFactory {
	/**
	 *
	 * @param param
	 * @return
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 */
	public static BindingComponent createComponenet(final Paramter param) throws TableMetaNotFoundException, JKDataAccessException {
		final Properties prop = param.getProperties();
		if (prop.getProperty("table-meta") != null) {
			final String tableMetaName = prop.getProperty("table-meta");
			final TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(tableMetaName);
			final String viewMode = prop.getProperty("view-mode");
			if (viewMode != null && viewMode.equals("filter")) {
				return new FieldPanelWithFilter(tableMeta);
			}
			if (viewMode != null && viewMode.equals("table-meta-panel")) {
				final DynDaoPanel pnl = new DynDaoPanel(tableMeta);
				pnl.setAllowClear(false);
				pnl.setAllowClose(false);
				pnl.setAllowEdit(false);
				pnl.setMode(DynDaoMode.FIND);
				return pnl;
			}
			return new DaoComboBox(tableMeta);
		}
		return new JKTextField(20);
	}
}
