package com.fs.commons.desktop;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp2.FSTextField;
import com.fs.commons.util.ExceptionUtil;

public class JKReportViewer extends JasperViewer {

	private static final long serialVersionUID = 1L;
	JKButton btnPrint = new JKButton("PRINT");
	private JasperPrint jasperPrint;
	private FSTextField txtCount = new FSTextField(2);

	////////////////////////////////////////////////////////////////
	public JKReportViewer(JasperPrint jasperPrint, boolean isExitOnClose) {
		super(jasperPrint, isExitOnClose);
		this.jasperPrint = jasperPrint;
		init();
	}

	////////////////////////////////////////////////////////////////
	private void init() {
		setTitle("FS-Viewer");
		setExtendedState(MAXIMIZED_BOTH);

		setLocationRelativeTo(SwingUtility.getDefaultMainFrame());
		add(getButtonsPanel(), BorderLayout.SOUTH);
	}

	////////////////////////////////////////////////////////////////
	private JKPanel getButtonsPanel() {
		JKPanel pnl = new JKPanel();
		pnl.add(btnPrint);
		pnl.add(txtCount);
		txtCount.setNumbersOnly(true);
		txtCount.setMaxLength(1);
		txtCount.setValue(1);
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handlePrint();
			}

		});
		return pnl;
	}

	////////////////////////////////////////////////////////////////
	@Override
	public void setVisible(boolean b) {
		super.setVisible(b);
		btnPrint.requestFocusInWindow();
	}

	////////////////////////////////////////////////////////////////
	private void handlePrint() {
		if (jasperPrint != null) {
			try {
				int count = txtCount.getTextAsInteger();
				if (count <= 0) {
					count = 1;
				}
				for (int i = 0; i < count; i++) {
					JasperPrintManager.printReport(jasperPrint, false);
				}
				dispose();
			} catch (JRException e1) {
				ExceptionUtil.handleException(e1);
			}
		}
	}

	////////////////////////////////////////////////////////////////
	public void setPrintCount(int i) {
		txtCount.setValue(i);
	}
}
