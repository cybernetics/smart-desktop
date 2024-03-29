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
package com.fs.security.ui.privileges;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.reports.JKReport;
import com.fs.commons.reports.JKReportManager;
import com.fs.commons.reports.JKReportsUtil;
import com.fs.commons.reports.ReportException;
import com.fs.commons.util.GeneralUtility;
import com.fs.security.util.ListsBuilder;
import com.jk.exceptions.handler.JKExceptionUtil;

public class PnlPrivileges extends JKPanel<Object> {

	private static final long serialVersionUID = 1L;
	DaoComboBox cmbRole = ListsBuilder.buildRoleComboBox();
	JKButton btnPrint = new JKButton("PRINT");
	QueryJTable tblPrivileges;

	public PnlPrivileges() {
		this.tblPrivileges = new QueryJTable(getSql(), "USER");
		init();
	}

	private JPanel getCenterPanel() {

		final JKPanel<?> pnlCenter = new JKPanel<Object>(new BorderLayout());
		pnlCenter.add(this.tblPrivileges, BorderLayout.CENTER);
		return pnlCenter;
	}

	protected String getGroupReportName() {
		return "sec_privilege";
	}

	private JPanel getNorthPanel() {
		final JKPanel<?> pnlNorth = new JKPanel<Object>(new BorderLayout());
		final JPanel pnl = new JKPanel<Object>();
		pnl.add(new JKLabledComponent("USER_NAME", 80, this.cmbRole));
		pnlNorth.add(pnl, BorderLayout.CENTER);
		return pnlNorth;
	}

	public String getSql() {
		return GeneralUtility.getSqlFile("sec_role_privileges.sql");
	}

	public String getSql2() {
		return GeneralUtility.getSqlFile("sec_role_privileges2.sql");
	}

	protected void handelPrintUserPrivilege() {
		final JKReport report = JKReportManager.getReport(getGroupReportName());
		final Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("role_id", this.cmbRole.getSelectedIdValue());
		try {
			JKReportsUtil.printReport(report, params);
		} catch (final ReportException e) {
			JKExceptionUtil.handle(e);
		}
	}

	public void init() {
		this.btnPrint.setEnabled(false);
		this.btnPrint.setShowProgress(true);
		this.btnPrint.setIcon("selcted_print.png");
		setLayout(new BorderLayout());
		final JPanel pnlNorth = getNorthPanel();
		final JPanel pnlCenter = getCenterPanel();
		final JPanel pnlSouth = new JKPanel<Object>();
		pnlSouth.add(this.btnPrint, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);

		this.cmbRole.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				loadTableData();

			}
		});

		this.btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handelPrintUserPrivilege();
			}
		});
	}

	public void loadTableData() {
		this.btnPrint.setEnabled(true);
		if (this.cmbRole.getSelectedIdValue() == null) {
			this.tblPrivileges.setQuery(getSql2());
		} else {
			this.tblPrivileges.setQuery(getSql().replace("?", this.cmbRole.getSelectedIdValue()));
		}
	}

}
