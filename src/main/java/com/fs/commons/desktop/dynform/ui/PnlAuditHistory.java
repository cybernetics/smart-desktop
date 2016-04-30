package com.fs.commons.desktop.dynform.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JComponent;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp2.FSDataTable;
import com.fs.commons.desktop.swing.listener.RecordSelectionListener;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.Audit;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class PnlAuditHistory extends JKMainPanel {
	private static final String AUDITS_SQL_FILE = GeneralUtility.getSqlFile("sec_audits.sql") + " WHERE record_id=? AND record_name='?'";
	private final Object recordId;
	private final String tableName;
	final FSDataTable tbl = new FSDataTable();

	// //////////////////////////////////////////////////////////////////////
	public static void showHistory(int id, String tableName) {
		PnlAuditHistory pnl = new PnlAuditHistory(id, tableName);
		SwingUtility.showPanelInDialog(pnl, Lables.get(tableName) + " " + Lables.get("HISTORY"));
	}

	// /////////////////////////////////////////////////////////////////////
	public PnlAuditHistory(Object recordId, String tableName) {
		this.recordId = recordId;
		this.tableName = tableName;
		init();
	}

	// /////////////////////////////////////////////////////////////////////
	private void init() {
		setLayout(new BorderLayout());
		add(tbl);
		String sql = AUDITS_SQL_FILE;
		sql = DaoUtil.compileSql(sql, recordId, tableName);
		tbl.setQuery(sql);
		tbl.setColumnDateFormat(1, "yyyy/MM/dd hh:mm");
		tbl.addRecordListener(new RecordSelectionListener() {
			@Override
			public void recordSelected(int recordId) {
				handleAuditSelected();
			}
		});
	}

	// ////////////////////////////////////////////////////////////////////////
	private void handleAuditSelected() {
		try {
			int id = tbl.getSelectedIdAsInteger();
			AbstractDao dao = DaoFactory.createDao();
			Audit audit = dao.findAudit(id);
			if (audit.getGui() != null) {
				SwingUtility.showEncodedComponent(audit.getGui(), Lables.get(audit.getTableName(), true));
			}else{
				SwingUtility.showSuccessDialog(audit.getAuditText());
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
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

	// ///////////////////////////////////////////////////////////////////////////
	public static void enableContainer(Container container, boolean enable) {
		int count = container.getComponentCount();
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

}
