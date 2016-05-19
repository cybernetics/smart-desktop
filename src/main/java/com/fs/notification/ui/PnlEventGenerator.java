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
package com.fs.notification.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.notification.facade.NotificationFacade;
import com.jk.exceptions.handler.JKExceptionUtil;

public class PnlEventGenerator extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 4792625330224062265L;

	public static void main(final String[] args) throws FileNotFoundException, ApplicationException, JKDataAccessException {
		ApplicationManager.getInstance().init();
		SwingUtility.testPanel(new PnlEventGenerator());
	}

	DaoComboBox cmbTask = new DaoComboBox(AbstractTableMetaFactory.getTableMeta("not_event_generation_task"));

	JKButton btnGenerateEvent = new JKButton("GENERATE_EVENTS");

	/**
	 * @throws JKDataAccessException
	 */
	public PnlEventGenerator() throws JKDataAccessException {
		init();
	}

	/**
	 *
	 * @return
	 */
	private Component getButtonsPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(this.btnGenerateEvent);
		this.btnGenerateEvent.setShowProgress(true);
		return pnl;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getInfoPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(new JKLabledComponent("TASK_GENERATOR", this.cmbTask));
		return pnl;
	}

	/**
	 *
	 */
	protected void handleGenerate() {
		try {
			this.cmbTask.checkEmpty();
			final NotificationFacade facade = new NotificationFacade();
			facade.generateEvent(this.cmbTask.getSelectedIdValueAsInteger());
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	private void init() {
		final JKPanel container = new JKPanel();
		container.setLayout(new BorderLayout());
		container.add(getInfoPanel(), BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		add(container);
		this.btnGenerateEvent.setIcon("gen_event.png");
		this.btnGenerateEvent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGenerate();
			}
		});
	}
}
