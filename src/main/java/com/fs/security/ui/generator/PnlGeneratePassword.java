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
package com.fs.security.ui.generator;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.NumberDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp.panels.JKRadioGroup;
import com.jk.security.JK;
import com.jk.security.JKUser;

/////////////////////////////////////////////////////////////////////////////////////////
// Author : Mohamed Kiswani
// Since  : 5-2-2010
///////////////////////////////////////////////////////////////////////////////////////
public class PnlGeneratePassword extends JKPanel {
	/////////////////////////////////////////////////////////////////////////////////////
	public enum GenerationType {
		ALPH_NEMRIC, ALPHA, NUMERIC;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		final JKUser user = new JKUser();
		user.setUserRecordId(1);
		user.setUserId("admin");
		user.setFullName("admin");
		user.setPassword("admin");
		user.setStatus(0);

		// user =SecurityManager.getCurrentUser();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.showPanelInDialog(new PnlGeneratePassword(), "TEST");
	}

	private final JKTextField txtNewPassword = new JKTextField(9, 20);
	private final JKTextField txtPasswordLength = new JKTextField(new NumberDocument(1), 20);
	private JKRadioGroup rbgPasswordType = null;

	private final JKButton btnCancel = new JKButton("CLOSE_PANEL");

	/////////////////////////////////////////////////////////////////////////////////////
	public PnlGeneratePassword() {
		init();
	}

	/////////////////////////////////////////////////////////////////////////////////////
	private JKPanel getButtonsPanel() {
		final JKPanel panel = new JKPanel();
		panel.add(this.btnCancel);
		return panel;
	}

	/**
	 * @return
	 */
	private JKPanel getMainPanel() {
		final JKPanel pnlInfo = new JKPanel(new GridLayout(4, 1, 3, 2));
		pnlInfo.setBorder(SwingUtility.createTitledBorder("USER_INFO"));
		pnlInfo.add(new JKLabledComponent("NEW_PASSWORD", this.txtNewPassword));
		pnlInfo.add(new JKLabledComponent("PASSWORD_LENGTH", this.txtPasswordLength));
		pnlInfo.add(this.rbgPasswordType);
		return pnlInfo;
	}

	/////////////////////////////////////////////////////////////////////////////////////
	private int getPasswordLength() {
		if (!this.txtPasswordLength.getText().equals("")) {
			return Integer.parseInt(this.txtPasswordLength.getText());
		} else {
			return 4;
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	protected void handleCancel() {
		SwingUtility.closePanel(this);
	}

	/**
	 * @throws JKDataAccessException
	 */
	protected void handleGenerate() {

	}

	/////////////////////////////////////////////////////////////////////////////////////
	protected void handleGeneratePassword() {
		final GenerationType type = (GenerationType) this.rbgPasswordType.getSelectedItem();
		switch (type) {
		case NUMERIC:
			this.txtNewPassword.setText(JK.generateNumricPassword(getPasswordLength()));
			break;
		case ALPHA:
			this.txtNewPassword.setText(JK.getAlphapticPassowrds(getPasswordLength(), false));
			break;
		case ALPH_NEMRIC:
			this.txtNewPassword.setText(JK.generateMixPassword(getPasswordLength()));
			break;

		}

	}

	/////////////////////////////////////////////////////////////////////////////////////
	private void init() {
		this.rbgPasswordType = new JKRadioGroup(GenerationType.values());
		final JKPanel container = new JKPanel(new BorderLayout());
		container.setBorder(SwingUtility.createTitledBorder(""));
		container.add(getMainPanel(), BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		this.txtNewPassword.setEnabled(false);
		this.txtPasswordLength.setEnabled(true);
		this.btnCancel.setIcon("close.png");
		add(container);

		this.rbgPasswordType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGeneratePassword();
			}
		});

		this.txtPasswordLength.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(final KeyEvent e) {
				handleGeneratePassword();
			}

		});

		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCancel();
			}
		});
	}
}
