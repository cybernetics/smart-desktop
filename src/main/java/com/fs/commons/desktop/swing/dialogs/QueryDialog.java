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

	/**
	 *
	 * @param args
	 *            String[]
	 * @throws IOException
	 */
	public static void main(final String[] args) throws IOException {
		// System.out.println(QueryDialog.showQueryDialog(Utility.getSqlFile("class_courses.sql"),
		// "COURSES", 1));
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
	public static Object showQueryDialog(final Dialog dialog, final String sql, final String title, final int filterIndex) {
		final QueryDialog dlg = new QueryDialog(dialog, sql, title, filterIndex, false);
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
	 * @return int
	 */
	public static Object showQueryDialog(final String sql, final String title, final int filterIndex) {
		final QueryDialog dlg = new QueryDialog(SwingUtility.getDefaultMainFrame(), sql, title, filterIndex, true);
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
	public static Object[] showQueryDialog(final String sql, final String title, final int filterIndex, final boolean multipleSelecion) {
		final QueryDialog dlg = new QueryDialog(SwingUtility.getDefaultMainFrame(), sql, title, filterIndex, multipleSelecion);
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.results;
	}

	/**
	 *
	 * @param tableMeta
	 * @return
	 */
	public static Object showQueryDialog(final TableMeta tableMeta) {
		int filterIndex = 0;
		if (tableMeta.getFilters().length > 0) {
			filterIndex = tableMeta.getFilters()[0];
		}
		final QueryDialog dlg = new QueryDialog(SwingUtility.getDefaultMainFrame(), tableMeta.getReportSql(), tableMeta.getTableName(), filterIndex,
				false, tableMeta.getDataSource());
		dlg.queryTable.setShowIdColunm(!tableMeta.getIdField().isAutoIncrement());
		final int[] filters = tableMeta.getFilters();
		for (final int filter : filters) {
			dlg.queryTable.showFilterPanel(filter, false);
		}
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.result;
	}

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
	public QueryDialog(final Dialog frame, final String sql, final String title, final int filterIndex, final boolean multipleSelecion) {
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
	public QueryDialog(final Frame frame, final String sql, final String title, final int filterIndex, final boolean multipleSelecion,
			final DataSource connectionManager) {
		super(frame);
		this.queryTable = new QueryJTable(title, connectionManager, sql, true);
		this.multipleSelecion = multipleSelecion;
		this.filterIndex = filterIndex;
		initQueryDialog();
	}

	public QueryDialog(final JKFrame defaultMainFrame, final String sql, final String title, final int filterIndex, final boolean multipleSelecion) {
		this(defaultMainFrame, sql, title, filterIndex, multipleSelecion, DataSourceFactory.getDefaultDataSource());
	}

	public QueryJTable getQueryTable() {
		return this.queryTable;
	}

	/**
	 *
	 * @return int
	 */
	public Object getResult() {
		return this.result;
	}

	public Object[] getResults() {
		return this.results;
	}

	/**
	 *
	 */
	protected void initQueryDialog() {
		// super.initDialog();
		setPreferredSize(new Dimension(ApplicationFrame.DEFAULT_FRAME_WIDTH, 600));

		this.queryTable.allowMultipleSelections(this.multipleSelecion);
		this.queryTable.showFilterPanel(this.filterIndex);
		// setModal(true);

		final JKPanel<?> pnlButtons = new JKMainPanel();
		pnlButtons.add(this.btnOK);
		this.btnOK.setIcon(new ImageIcon(GeneralUtility.getIconURL("button_ok.png")));
		this.btnOK.setShortcut("F1", "F1");
		pnlButtons.add(this.btnCancel);
		this.btnCancel.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		this.btnCancel.setShortcut("F2", "F2");
		final JKMainPanel conatiner = new JKMainPanel(new BorderLayout());

		conatiner.add(this.queryTable, BorderLayout.CENTER);
		conatiner.add(pnlButtons, BorderLayout.SOUTH);

		add(conatiner);
		initDialog();
		this.queryTable.addDaoRecordListener(new RecordActionAdapter() {
			@Override
			public void recordSelected(final String recordId) {
				QueryDialog.this.btnOK.doClick();
			}
		});
		this.btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final Object res = QueryDialog.this.queryTable.getSelectedId();
				if (res == null) {
					SwingUtility.showUserErrorDialog("PLEASE_SELECT_VALUE_FROM_LIST");
				} else {
					QueryDialog.this.result = res;
					QueryDialog.this.results = QueryDialog.this.queryTable.getSelectedIds();
					setVisible(false);
				}
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				QueryDialog.this.result = null;
				QueryDialog.this.results = null;
				dispose();
			}
		});
	}

	public void setAllowExcelExport(final boolean allowExcelExport) {
		this.queryTable.setAllowExcelExport(allowExcelExport);
	}

	public void setSql(final String sql) {
		this.queryTable.setQuery(sql);
	}

}
