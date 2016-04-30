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
package com.fs.commons.apps.feedback;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.apache.commons.mail.EmailException;

import com.fs.commons.application.config.CommonsConfigManager;
import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.apps.backup.CompressionException;
import com.fs.commons.apps.backup.DatabaseInfo;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKCheckBox;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKTextArea;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

public class PnlFeedBack extends JKPanel<Object> {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static final String SUPPORT_EMAIL = System.getProperty("mail-to", "Bug-Report@final-solutions.net");

	/**
	 *
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(final String[] args) throws FileNotFoundException {
		SwingUtility.testPanel(new PnlFeedBack());
	}

	JKTextField txtScreenName = new JKTextField(50);
	JKTextArea txtDescription = new JKTextArea(10, 50);
	JKTextArea txtScenario = new JKTextArea(10, 50);
	JKButton btnSend = new JKButton("ERR_SEND");

	JKCheckBox chkInlcudeDatabase = new JKCheckBox("INCLUDE_DATABASE");

	/**
	 *
	 */
	public PnlFeedBack() {
		init();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> getButtonsPanel() {
		final JKPanel<?> pnl = new JKPanel<Object>();
		pnl.add(this.btnSend);
		this.btnSend.setShowProgress(true);
		return pnl;
	}

	/**
	 * @1.1
	 */

	protected void handleSend() {
		try {
			this.txtScreenName.checkEmpty();
			SwingValidator.checkEmpty(this.txtDescription);
			SwingValidator.checkEmpty(this.txtScenario);
			final DatabaseInfo info = new DatabaseInfo();
			info.setDatabaseHost(System.getProperty("db-host", "localhost"));
			info.setDatabaseName(System.getProperty("db-name", "db"));
			info.setDatabasePort(Integer.parseInt(System.getProperty("db-port", "3306")));
			info.setDatabaseUser(CommonsConfigManager.decode(System.getProperty("db-user")));
			info.setDatabasePassword(CommonsConfigManager.decode(System.getProperty("db-password")));

			final Message msg = new Message();
			msg.setPanelName(this.txtScreenName.getText().trim());
			msg.setErrorDesc(this.txtDescription.getText().trim());
			msg.setErrorScenario(this.txtScenario.getText().trim());

			final FeedbackManager feedbackManager = new FeedbackManager();
			feedbackManager.sendFeddback(SUPPORT_EMAIL, info, msg, isSendDatabaseBackup());
			SwingUtility.showSuccessDialog("FEED_BACK_SENT_SUCCESSFULLY");
			SwingUtility.closePanel(this);
		} catch (final ValidationException e) {
			ExceptionUtil.handleException(e);
		} catch (final IOException e) {
			ExceptionUtil.handleException(e);
		} catch (final CompressionException e) {
			ExceptionUtil.handleException(e);
		} catch (final EmailException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 *
	 */
	private void init() {
		final JKPanel<?> container = new JKPanel<Object>();
		container.setBorder(SwingUtility.createTitledBorder("FEED_BACK_INFO"));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(new JKLabledComponent("ERROR_SCREEN_NAME", this.txtScreenName));
		container.add(Box.createVerticalStrut(3));
		container.add(new JKLabledComponent("ERROR_DESCRIPTION", new JKScrollPane(this.txtDescription)));
		container.add(Box.createVerticalStrut(3));
		container.add(new JKLabledComponent("ERROR_SCENARIO", new JKScrollPane(this.txtScenario)));
		container.add(this.chkInlcudeDatabase);
		container.add(Box.createVerticalStrut(3));
		container.add(getButtonsPanel());
		this.btnSend.setIcon("send_feed_back.png");
		add(container);

		this.btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSend();
			}
		});
	}

	/**
	 *
	 * @return
	 */
	private boolean isSendDatabaseBackup() {
		return this.chkInlcudeDatabase.isSelected();
	}

}
