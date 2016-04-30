/**
 * 
 */
package com.fs.commons.reports;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

/**
 * @author u087
 * 
 */
public class ReportUIPanel extends JKPanel<Object> implements UIPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Report report;

	private Hashtable<String, BindingComponent> paramters;

	JKButton btnPrint = new JKButton("PRINT");

	JKButton btnClear = new JKButton("CLEAR");

	JKButton btnClose = new JKButton("CLOSE");

	/**
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * 
	 */
	public ReportUIPanel(Report report) throws TableMetaNotFoundException, DaoException {
		this.report = report;
		init();
	}

	/**
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * 
	 */
	private void init() throws TableMetaNotFoundException, DaoException {
		JKPanel<?> pnl = new JKPanel<Object>();
		if (report.getParamtersCount() > 1) {
			// to avoid nested borders especially with DynDaoPanel
			pnl.setBorder(SwingUtility.createTitledBorder(""));
		}
		pnl.setLayout(new BorderLayout());
		JKPanel<?> pnlCenter = new JKPanel<Object>();
		pnlCenter.setBorder(SwingUtility.createTitledBorder(report.getTitle()));
		paramters = new Hashtable<String, BindingComponent>();
		for (int i = 0; i < report.getParamtersCount(); i++) {
			Paramter param = report.getParamter(i);
			BindingComponent<?> comp = ReportUIComponentFactory.createComponenet(param);
			paramters.put(param.getName(), comp);
			if (param.getCaption().equals("")) {
				pnlCenter.add((JComponent) comp);
			} else {
				pnlCenter.add(new JKLabledComponent(param.getCaption(), comp));
			}
		}
		JKPanel<?> pnlButtons = new JKPanel<Object>();
		pnlButtons.add(btnPrint);
		// pnlButtons.add(btnClear);
		pnlButtons.add(btnClose);
		btnPrint.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileprint.png")));
		btnClose.setIcon(new ImageIcon(GeneralUtility.getIconURL("cancel.png")));
		pnl.add(pnlCenter, BorderLayout.CENTER);
		pnl.add(pnlButtons, BorderLayout.SOUTH);
		add(pnl);
		btnPrint.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				handlePrint();
			}
		});
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				handleClose();
			}
		});

	}

	/**
	 * 
	 */
	protected void handleClose() {
		SwingUtility.closePanel(this);

	}

	/**
	 * 
	 */
	protected void handlePrint() {
		HashMap map = buildMapFromParamters();
		try {
			ReportsUtil.printReport(report, map);
		} catch (ReportException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @throws ValidationException
	 */
	public void validateData() throws ValidationException {
		for (int i = 0; i < report.getParamtersCount(); i++) {
			Paramter param = report.getParamter(i);
			SwingValidator.checkEmpty(paramters.get(param.getName()));
		}
	}

	/**
	 * 
	 * @return
	 */
	private HashMap<String, Object> buildMapFromParamters() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("SUBREPORT_DIR", GeneralUtility.getReportsOutPath() + "/");
		for (int i = 0; i < report.getParamtersCount(); i++) {
			Paramter param = report.getParamter(i);
			Object value = ((BindingComponent<?>) paramters.get(param.getName())).getValue();
			map.put(param.getName(), value);
		}
		return map;
	}
}
