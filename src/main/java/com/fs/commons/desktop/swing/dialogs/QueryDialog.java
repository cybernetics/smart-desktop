package com.fs.commons.desktop.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.event.RecordActionAdapter;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.desktop.swing.frames.ApplicationFrame;
import com.fs.commons.util.GeneralUtility;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class QueryDialog extends JKDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JKButton btnOK = new JKButton("OK");

	JKButton btnCancel = new JKButton("CANCEL");

	private QueryJTable queryTable;

	private final int filterIndex;

	private Object result = null;

	private Object results[] = new Object[0];

	private final boolean multipleSelecion;

	/**
	 * 
	 * @param sql
	 *            String
	 * @param title
	 *            title
	 * @param filterIndex
	 *            int
	 * @return int
	 */
	public static Object showQueryDialog(String sql, String title, int filterIndex) {
		QueryDialog dlg = new QueryDialog(SwingUtility.getDefaultMainFrame(), sql, title, filterIndex, true);
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.result;
	}

	/**
	 * 
	 * @param tableMeta
	 * @return
	 */
	public static Object showQueryDialog(TableMeta tableMeta) {
		int filterIndex =0;
		if(tableMeta.getFilters().length>0){
			filterIndex= tableMeta.getFilters()[0];
		}
		QueryDialog dlg = new QueryDialog(SwingUtility.getDefaultMainFrame(), tableMeta.getReportSql(), tableMeta.getTableName(), filterIndex, false,tableMeta.getDataSource());
		dlg.queryTable.setShowIdColunm(!tableMeta.getIdField().isAutoIncrement());
		int[] filters = tableMeta.getFilters();
		for (int i = 0; i < filters.length; i++) {
			dlg.queryTable.showFilterPanel(filters[i], false);
		}
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.result;
	}

	/**
	 * 
	 * @param sql
	 *            String
	 * @param title
	 *            title
	 * @param filterIndex
	 *            int
	 * @param multipleSelecion
	 *            boolean
	 * @return int
	 */
	public static Object[] showQueryDialog(String sql, String title, int filterIndex, boolean multipleSelecion) {
		QueryDialog dlg = new QueryDialog(SwingUtility.getDefaultMainFrame(), sql, title, filterIndex, multipleSelecion);
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.results;
	}

	/**
	 * 
	 * @param dialog
	 *            Dialog
	 * @param sql
	 *            title
	 * @param title
	 *            String
	 * @param filterIndex
	 *            int
	 * @return int
	 */
	public static Object showQueryDialog(Dialog dialog, String sql, String title, int filterIndex) {
		QueryDialog dlg = new QueryDialog(dialog, sql, title, filterIndex, false);
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.result;
	}

	/**
	 * 
	 * @param sql
	 *            String
	 * @param title
	 *            String
	 * @param filterIndex
	 *            int
	 */
	// private QueryDialog(String sql, String title, int filterIndex) {
	// this(MainFrame.getInstance(), sql, title, filterIndex);
	// }
	/**
	 * 
	 * @param frame
	 *            Frame
	 * @param sql
	 *            String
	 * @param title
	 *            String
	 * @param filterIndex
	 *            int
	 * @param multipleSelecion
	 *            boolean
	 */
	public QueryDialog(Dialog frame, String sql, String title, int filterIndex, boolean multipleSelecion) {
		super(frame);
		this.multipleSelecion = multipleSelecion;
		this.filterIndex = filterIndex;
		initQueryDialog();
	}

	/**
	 * 
	 * @param frame
	 *            Frame
	 * @param sql
	 *            String
	 * @param title
	 *            String
	 * @param filterIndex
	 *            int : -1 will not show any filter
	 * @param multipleSelecion
	 *            boolean
	 * @param connectionManager 
	 */
	public QueryDialog(Frame frame, String sql, String title, int filterIndex, boolean multipleSelecion, DataSource connectionManager) {
		super(frame);
		queryTable = new QueryJTable(title,connectionManager,sql, true);
		this.multipleSelecion = multipleSelecion;
		this.filterIndex = filterIndex;
		initQueryDialog();
	}

	public QueryDialog(JKFrame defaultMainFrame, String sql, String title, int filterIndex, boolean multipleSelecion) {
		this(defaultMainFrame,sql,title,filterIndex,multipleSelecion,DataSourceFactory.getDefaultDataSource());
	}

	/**
	 * 
	 */
	protected void initQueryDialog() {
//		super.initDialog();
		setPreferredSize(new Dimension(ApplicationFrame.DEFAULT_FRAME_WIDTH, 600));
		
		queryTable.allowMultipleSelections(multipleSelecion);
		queryTable.showFilterPanel(filterIndex);
//		setModal(true);
		
		JKPanel<?> pnlButtons = new JKMainPanel();
		pnlButtons.add(btnOK);
		btnOK.setIcon(new ImageIcon(GeneralUtility.getIconURL("button_ok.png")));
		btnOK.setShortcut("F1", "F1");
		pnlButtons.add(btnCancel);
		btnCancel.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		btnCancel.setShortcut("F2", "F2");
		JKMainPanel conatiner=new JKMainPanel(new BorderLayout());
		
		conatiner.add(queryTable, BorderLayout.CENTER);
		conatiner.add(pnlButtons, BorderLayout.SOUTH);
		
		add(conatiner);
		initDialog();
		queryTable.addDaoRecordListener(new RecordActionAdapter() {
			@Override
			public void recordSelected(String recordId) {
				btnOK.doClick();
			}
		});
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object res = queryTable.getSelectedId();
				if (res == null) {
					SwingUtility.showUserErrorDialog("PLEASE_SELECT_VALUE_FROM_LIST");
				} else {
					result = res;
					results = queryTable.getSelectedIds();
					setVisible(false);
				}
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				result = null;
				results = null;
				dispose();
			}
		});
	}

	/**
	 * 
	 * @return int
	 */
	public Object getResult() {
		return result;
	}

	public Object[] getResults() {
		return results;
	}

	public QueryJTable getQueryTable() {
		return queryTable;
	}

	public void setSql(String sql) {
		queryTable.setQuery(sql);
	}

	public void setAllowExcelExport(boolean allowExcelExport) {
		queryTable.setAllowExcelExport(allowExcelExport);
	}

	/**
	 * 
	 * @param args
	 *            String[]
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// System.out.println(QueryDialog.showQueryDialog(Utility.getSqlFile("class_courses.sql"),
		// "COURSES", 1));
	}

}
