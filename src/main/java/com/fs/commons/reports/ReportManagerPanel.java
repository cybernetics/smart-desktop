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
package com.fs.commons.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKMenuItem;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.jk.exceptions.handler.JKExceptionUtil;

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
	 * @param report
	 */
	protected void handleShowReport(final JKReport report) {
		try {
			final ReportUIPanel pnl = new ReportUIPanel(report);
			if (this.mainPanel != null) {
				remove(this.mainPanel);
			}
			this.mainPanel = pnl;
			add(this.mainPanel, BorderLayout.CENTER);
			validate();
			repaint();
		} catch (final TableMetaNotFoundException e) {
			JKExceptionUtil.handle(e);
		} catch (final JKDataAccessException e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		final ArrayList<JKReport> reports = JKReportManager.getReports();
		final JKPanel<?> pnlMenu = new JKPanel<Object>();
		pnlMenu.setBorder(SwingUtility.createTitledBorder("AVAILABLE_REPORTS"));
		pnlMenu.setPreferredSize(new Dimension(180, 800));
		// pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));
		for (int i = 0; i < reports.size(); i++) {
			final JKReport report = reports.get(i);
			if (report.isVisible()) {
				final JKMenuItem btn = new JKMenuItem(report.getTitle());
				// btn.setPreferredSize(new Dimension(400, 30));
				pnlMenu.add(btn);
				pnlMenu.add(Box.createVerticalStrut(2));
				btn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						handleShowReport(report);
					}
				});
			}
		}
		add(pnlMenu, BorderLayout.LINE_START);

	}
}
