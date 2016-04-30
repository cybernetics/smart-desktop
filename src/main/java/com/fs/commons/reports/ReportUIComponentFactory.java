/**
 * 
 */
package com.fs.commons.reports;

import java.util.Properties;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
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
	 * @throws DaoException
	 */
	public static BindingComponent createComponenet(Paramter param) throws TableMetaNotFoundException, DaoException {
		Properties prop = param.getProperties();
		if (prop.getProperty("table-meta") != null) {
			String tableMetaName = prop.getProperty("table-meta");
			TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(tableMetaName);
			String viewMode = prop.getProperty("view-mode");
			if (viewMode != null && viewMode.equals("filter")) {
				return new FieldPanelWithFilter(tableMeta);
			}
			if (viewMode != null && viewMode.equals("table-meta-panel")) {
				DynDaoPanel pnl = new DynDaoPanel(tableMeta);
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
