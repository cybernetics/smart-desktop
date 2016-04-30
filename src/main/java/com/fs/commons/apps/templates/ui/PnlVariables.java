package com.fs.commons.apps.templates.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.GeneralUtility;

public class PnlVariables extends DynMasterDetailPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// ////////////////////////////////////////////////////////////////////////
	public PnlVariables() throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		super(AbstractTableMetaFactory.getTableMeta("conf_vars"));
		init();
		getTableNameCombo().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleTableNameChanged();
			}
		});
	}

	// /////////////////////////////////////////////////////////////////////////////
	private JComboBox<?> getTableNameCombo() {
		return ((JComboBox<?>) getMasterPanel().getFieldComponent("table_name"));
	}

	private DaoComboBox getFieldNameCombo() {
		return (DaoComboBox) (getMasterPanel().getFieldComponent("field_name"));
	}

	// /////////////////////////////////////////////////////////////////////////////
	protected void handleTableNameChanged() {
		Object selectedItem = getTableNameCombo().getSelectedItem();
		DaoComboBox fields = getFieldNameCombo();
		if (selectedItem == null) {
			// fields.removeAll();
		} else {
			String sql = GeneralUtility.getSqlFile("db_table_col_names.sql");
			sql = DaoUtil.compileSql(sql, new String[] { selectedItem.toString(), fields.getDataSource().getDatabaseName() });
			fields.setSql(sql);
		}
	}

}
