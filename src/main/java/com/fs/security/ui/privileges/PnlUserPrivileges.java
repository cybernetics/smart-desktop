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
import com.fs.commons.reports.Report;
import com.fs.commons.reports.ReportException;
import com.fs.commons.reports.ReportManager;
import com.fs.commons.reports.ReportsUtil;
import com.fs.commons.util.GeneralUtility;
import com.fs.security.util.ListsBuilder;

public class PnlUserPrivileges extends JKPanel<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	DaoComboBox cmbUser = ListsBuilder.buildUserComboBox();
	JKButton btnPrint = new JKButton("PRINT");
	QueryJTable tblUser;

	public PnlUserPrivileges() {
		this.tblUser = new QueryJTable(getSql2(), "USER");
		init();
	}

	private JPanel getCenterPanel() {

		final JKPanel<?> pnlCenter = new JKPanel<Object>(new BorderLayout());
		pnlCenter.add(this.tblUser, BorderLayout.CENTER);
		return pnlCenter;
	}

	protected String getGroupReportName() {
		return "sec_user_role_privilege";
	}

	private JPanel getNorthPanel() {
		final JKPanel<?> pnlNorth = new JKPanel<Object>(new BorderLayout());
		final JPanel pnl = new JKPanel<Object>();
		pnl.add(new JKLabledComponent("USER_NAME", 80, this.cmbUser));
		pnlNorth.add(pnl, BorderLayout.CENTER);
		return pnlNorth;
	}

	public String getSql() {
		return GeneralUtility.getSqlFile("user_role_privileges.sql");
	}

	public String getSql2() {
		return GeneralUtility.getSqlFile("user_role_privileges2.sql");
	}

	protected void handelPrintUserPrivilege() {
		final Report report = ReportManager.getReport(getGroupReportName());
		final Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("user_id", this.cmbUser.getSelectedIdValue());
		try {
			ReportsUtil.printReport(report, params);
		} catch (final ReportException e) {
			e.printStackTrace();
		}
	}

	public void init() {
		this.btnPrint.setEnabled(false);
		this.btnPrint.setShowProgress(true);
		final URL iconURL = GeneralUtility.getIconURL("selcted_print.png");
		if (iconURL != null) {
			this.btnPrint.setIcon(new ImageIcon(iconURL));
		}

		setLayout(new BorderLayout());
		final JPanel pnlNorth = getNorthPanel();
		final JPanel pnlCenter = getCenterPanel();
		final JPanel pnlSouth = new JKPanel<Object>();
		pnlSouth.add(this.btnPrint, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);

		this.cmbUser.addActionListener(new ActionListener() {
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
		if (this.cmbUser.getSelectedIdValue() == null) {
			// cmbUser.setSelectedIndex(0);
			this.tblUser.setQuery(getSql2());
		} else {
			this.tblUser.setQuery(getSql().replace("?", this.cmbUser.getSelectedIdValue()));
		}
	}

}
