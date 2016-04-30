/**
 * 
 */
package com.fs.commons.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;

import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKMenuItem;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

/**
 * @author u087
 * 
 */
public class ReportManagerPanel extends JKPanel<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ReportUIPanel mainPanel;

	/**
	 * 
	 */
	public ReportManagerPanel() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout());
		ArrayList<Report> reports = ReportManager.getReports();
		JKPanel<?> pnlMenu = new JKPanel<Object>();
		pnlMenu.setBorder(SwingUtility.createTitledBorder("AVAILABLE_REPORTS"));
		pnlMenu.setPreferredSize(new Dimension(180, 800));
		// pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
		for (int i = 0; i < reports.size(); i++) {
			final Report report = reports.get(i);
			if (report.isVisible()) {
				JKMenuItem btn = new JKMenuItem(report.getTitle());
				// btn.setPreferredSize(new Dimension(400, 30));
				pnlMenu.add(btn);
				pnlMenu.add(Box.createVerticalStrut(2));
				btn.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						handleShowReport(report);
					}
				});
			}
		}
		add(pnlMenu, BorderLayout.LINE_START);

	}

	/**
	 * @param report
	 */
	protected void handleShowReport(Report report) {
		try {
			ReportUIPanel pnl = new ReportUIPanel(report);
			if (mainPanel != null) {
				remove(mainPanel);
			}
			mainPanel = pnl;
			add(mainPanel, BorderLayout.CENTER);
			validate();
			repaint();
		} catch (TableMetaNotFoundException e) {
			ExceptionUtil.handleException(e);
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}
}
