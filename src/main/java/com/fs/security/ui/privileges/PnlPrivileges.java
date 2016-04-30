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

public class PnlPrivileges extends JKPanel<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DaoComboBox cmbRole = ListsBuilder.buildRoleComboBox();
	JKButton btnPrint = new JKButton("PRINT");
	QueryJTable tblPrivileges;
	 
	public PnlPrivileges() {
		tblPrivileges = new QueryJTable(getSql(), "USER");
		init();
	}

	public void init() {
		btnPrint.setEnabled(false);
		btnPrint.setShowProgress(true);
		URL iconURL = GeneralUtility.getIconURL("selcted_print.png");
		if(iconURL != null){
			btnPrint.setIcon(new ImageIcon(iconURL));
		}
		
		setLayout(new BorderLayout());
		JPanel pnlNorth = getNorthPanel();
		JPanel pnlCenter = getCenterPanel();
		JPanel pnlSouth = new JKPanel<Object>();
		pnlSouth.add(btnPrint, BorderLayout.SOUTH);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);

		cmbRole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadTableData();
				 
			}
		});

		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handelPrintUserPrivilege();
			}
		});
	}

	private JPanel getCenterPanel() {

		JKPanel<?> pnlCenter = new JKPanel<Object>(new BorderLayout());
		pnlCenter.add(tblPrivileges, BorderLayout.CENTER);
		return pnlCenter;
	}

	public String getSql(){
		return GeneralUtility.getSqlFile("role_privileges.sql");
	}
	public String getSql2(){
		return GeneralUtility.getSqlFile("role_privileges2.sql");
	}
	
	public void loadTableData() {
		btnPrint.setEnabled(true);
		if (cmbRole.getSelectedIdValue() == null) {
			tblPrivileges.setQuery(getSql2());
		}else{
			tblPrivileges.setQuery(getSql().replace("?", cmbRole.getSelectedIdValue()));
		}
	}

	private JPanel getNorthPanel() {
		JKPanel<?> pnlNorth = new JKPanel<Object>(new BorderLayout());
		JPanel pnl = new JKPanel<Object>();
		pnl.add(new JKLabledComponent("USER_NAME", 80, cmbRole));
		pnlNorth.add(pnl, BorderLayout.CENTER);
		return pnlNorth;
	}

	protected void handelPrintUserPrivilege() {
		Report report = ReportManager.getReport(getGroupReportName());
		Hashtable<String, Object> params = new Hashtable<String, Object>();
		params.put("role_id", cmbRole.getSelectedIdValue());
		try {
			ReportsUtil.printReport(report, params);
		} catch (ReportException e) {
			e.printStackTrace();
		}
	}

	protected String getGroupReportName() {
		return "sec_privilege";
	}

}
