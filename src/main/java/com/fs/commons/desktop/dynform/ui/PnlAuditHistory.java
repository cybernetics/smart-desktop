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
package com.fs.commons.desktop.dynform.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKAbstractPlainDataAccess;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp2.FSDataTable;
import com.fs.commons.desktop.swing.listener.RecordSelectionListener;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.security.JKAudit;

public class PnlAuditHistory extends JKMainPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = -5661428777683046654L;
	private static final String AUDITS_SQL_FILE = GeneralUtility.getSqlFile("sec_audits.sql") + " WHERE record_id=? AND record_name='?'";

	// ///////////////////////////////////////////////////////////////////////////
	public static void enableContainer(final Container container, final boolean enable) {
		final int count = container.getComponentCount();
		Component comp;
		for (int i = 0; i < count; i++) {
			comp = container.getComponent(i);
			if (comp instanceof Container) {
				comp.setEnabled(enable);
				if (!(comp instanceof JKPanel)) {// since its already call this
													// method
					enableContainer((Container) comp, enable);
				}
			} else {
				if (comp instanceof JComponent) {
					comp.setEnabled(enable);
				} else if (comp instanceof BindingComponent) {
					comp.setEnabled(enable);
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public static void showHistory(final int id, final String tableName) {
		final PnlAuditHistory pnl = new PnlAuditHistory(id, tableName);
		SwingUtility.showPanelInDialog(pnl, Lables.get(tableName) + " " + Lables.get("HISTORY"));
	}

	private final Object recordId;

	private final String tableName;

	final FSDataTable tbl = new FSDataTable();

	// /////////////////////////////////////////////////////////////////////
	public PnlAuditHistory(final Object recordId, final String tableName) {
		this.recordId = recordId;
		this.tableName = tableName;
		init();
	}

	// ////////////////////////////////////////////////////////////////////////
	private void handleAuditSelected() {
		try {
			final int id = this.tbl.getSelectedIdAsInteger();
			final JKAbstractPlainDataAccess dao = DaoFactory.createDao();
			final JKAudit audit = dao.findAudit(id);
			if (audit.getGui() != null) {
				SwingUtility.showEncodedComponent(audit.getGui(), Lables.get(audit.getTableName(), true));
			} else {
				SwingUtility.showSuccessDialog(audit.getAuditText());
			}
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	// public static void testComponentSerialization(Object obj) {
	// String xml = GeneralUtility.toXml(obj);
	// // Object object = GeneralUtility.toObject(xml);
	// showEncodedComponent(xml);
	// }
	// public static Window getActiveWindow(){
	// KeyboardFocusManager keyboardFocusManager =
	// KeyboardFocusManager.getCurrentKeyboardFocusManager();
	// rkeyboardFocusManager.getActiveWindow();
	// }

	// /////////////////////////////////////////////////////////////////////
	private void init() {
		setLayout(new BorderLayout());
		add(this.tbl);
		String sql = AUDITS_SQL_FILE;
		sql = DaoUtil.compileSql(sql, this.recordId, this.tableName);
		this.tbl.setQuery(sql);
		this.tbl.setColumnDateFormat(1, "yyyy/MM/dd hh:mm");
		this.tbl.addRecordListener(new RecordSelectionListener() {
			@Override
			public void recordSelected(final int recordId) {
				handleAuditSelected();
			}
		});
	}

}
