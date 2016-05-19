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
package com.fs.commons.desktop;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp2.FSTextField;
import com.jk.exceptions.handler.JKExceptionUtil;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

public class JKReportViewer extends JasperViewer {

	private static final long serialVersionUID = 1L;
	JKButton btnPrint = new JKButton("PRINT");
	private final JasperPrint jasperPrint;
	private final FSTextField txtCount = new FSTextField(2);

	////////////////////////////////////////////////////////////////
	public JKReportViewer(final JasperPrint jasperPrint, final boolean isExitOnClose) {
		super(jasperPrint, isExitOnClose);
		this.jasperPrint = jasperPrint;
		init();
	}

	////////////////////////////////////////////////////////////////
	private JKPanel getButtonsPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(this.btnPrint);
		pnl.add(this.txtCount);
		this.txtCount.setNumbersOnly(true);
		this.txtCount.setMaxLength(1);
		this.txtCount.setValue(1);
		this.btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handlePrint();
			}

		});
		return pnl;
	}

	////////////////////////////////////////////////////////////////
	private void handlePrint() {
		if (this.jasperPrint != null) {
			try {
				int count = this.txtCount.getTextAsInteger();
				if (count <= 0) {
					count = 1;
				}
				for (int i = 0; i < count; i++) {
					JasperPrintManager.printReport(this.jasperPrint, false);
				}
				dispose();
			} catch (final JRException e1) {
				JKExceptionUtil.handle(e1);
			}
		}
	}

	////////////////////////////////////////////////////////////////
	private void init() {
		setTitle("FS-Viewer");
		setExtendedState(MAXIMIZED_BOTH);

		setLocationRelativeTo(SwingUtility.getDefaultMainFrame());
		add(getButtonsPanel(), BorderLayout.SOUTH);
	}

	////////////////////////////////////////////////////////////////
	public void setPrintCount(final int i) {
		this.txtCount.setValue(i);
	}

	////////////////////////////////////////////////////////////////
	@Override
	public void setVisible(final boolean b) {
		super.setVisible(b);
		this.btnPrint.requestFocusInWindow();
	}
}
