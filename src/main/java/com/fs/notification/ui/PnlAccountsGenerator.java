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
import com.jk.exceptions.handler.ExceptionUtil;

public class PnlAccountsGenerator extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 5651844701066302120L;

	public static void main(final String[] args) throws FileNotFoundException, ApplicationException, JKDataAccessException {
		ApplicationManager.getInstance().init();
		SwingUtility.testPanel(new PnlAccountsGenerator());
	}

	DaoComboBox cmbAccountsCreationQuery = new DaoComboBox(AbstractTableMetaFactory.getTableMeta("vi_accont_creation_query"));

	JKButton btnGenerateAccounts = new JKButton("GENERATE_ACCOUNTS");

	/**
	 * @throws JKDataAccessException
	 */
	public PnlAccountsGenerator() throws JKDataAccessException {
		init();
	}

	/**
	 *
	 * @return
	 */
	private Component getButtonsPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(this.btnGenerateAccounts);
		this.btnGenerateAccounts.setShowProgress(true);
		return pnl;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getInfoPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(new JKLabledComponent("ACCOUNT_GENERATION_QUERY", this.cmbAccountsCreationQuery));
		return pnl;
	}

	/**
	 *
	 */
	protected void handleGenerate() {
		try {
			this.cmbAccountsCreationQuery.checkEmpty();
			final NotificationFacade facade = new NotificationFacade();
			facade.syncAccounts(this.cmbAccountsCreationQuery.getSelectedIdValueAsInteger());
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void init() {
		final JKPanel container = new JKPanel();
		container.setLayout(new BorderLayout());
		container.add(getInfoPanel(), BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		add(container);
		this.btnGenerateAccounts.setIcon("generate_accounts.png");
		this.btnGenerateAccounts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGenerate();
			}
		});
	}
}
