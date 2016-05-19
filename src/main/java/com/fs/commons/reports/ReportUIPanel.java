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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.ExceptionUtil;

/**
 * @author u087
 *
 */
public class ReportUIPanel extends JKPanel<Object> implements UIPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private final JKReport report;

	private Hashtable<String, BindingComponent> paramters;

	JKButton btnPrint = new JKButton("PRINT");

	JKButton btnClear = new JKButton("CLEAR");

	JKButton btnClose = new JKButton("CLOSE");

	/**
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 *
	 */
	public ReportUIPanel(final JKReport report) throws TableMetaNotFoundException, JKDataAccessException {
		this.report = report;
		init();
	}

	/**
	 *
	 * @return
	 */
	private HashMap<String, Object> buildMapFromParamters() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("SUBREPORT_DIR", GeneralUtility.getReportsOutPath() + "/");
		for (int i = 0; i < this.report.getParamtersCount(); i++) {
			final Paramter param = this.report.getParamter(i);
			final Object value = ((BindingComponent<?>) this.paramters.get(param.getName())).getValue();
			map.put(param.getName(), value);
		}
		return map;
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
		final HashMap map = buildMapFromParamters();
		try {
			JKReportsUtil.printReport(this.report, map);
		} catch (final ReportException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 *
	 */
	private void init() throws TableMetaNotFoundException, JKDataAccessException {
		final JKPanel<?> pnl = new JKPanel<Object>();
		if (this.report.getParamtersCount() > 1) {
			// to avoid nested borders especially with DynDaoPanel
			pnl.setBorder(SwingUtility.createTitledBorder(""));
		}
		pnl.setLayout(new BorderLayout());
		final JKPanel<?> pnlCenter = new JKPanel<Object>();
		pnlCenter.setBorder(SwingUtility.createTitledBorder(this.report.getTitle()));
		this.paramters = new Hashtable<String, BindingComponent>();
		for (int i = 0; i < this.report.getParamtersCount(); i++) {
			final Paramter param = this.report.getParamter(i);
			final BindingComponent<?> comp = ReportUIComponentFactory.createComponenet(param);
			this.paramters.put(param.getName(), comp);
			if (param.getCaption().equals("")) {
				pnlCenter.add((JComponent) comp);
			} else {
				pnlCenter.add(new JKLabledComponent(param.getCaption(), comp));
			}
		}
		final JKPanel<?> pnlButtons = new JKPanel<Object>();
		pnlButtons.add(this.btnPrint);
		// pnlButtons.add(btnClear);
		pnlButtons.add(this.btnClose);
		this.btnPrint.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileprint.png")));
		this.btnClose.setIcon(new ImageIcon(GeneralUtility.getIconURL("cancel.png")));
		pnl.add(pnlCenter, BorderLayout.CENTER);
		pnl.add(pnlButtons, BorderLayout.SOUTH);
		add(pnl);
		this.btnPrint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handlePrint();
			}
		});
		this.btnClose.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClose();
			}
		});

	}

	/**
	 *
	 * @throws ValidationException
	 */
	public void validateData() throws ValidationException {
		for (int i = 0; i < this.report.getParamtersCount(); i++) {
			final Paramter param = this.report.getParamter(i);
			SwingValidator.checkEmpty(this.paramters.get(param.getName()));
		}
	}
}
