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
package com.fs.security.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKPasswordField;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.TextDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.commons.util.GeneralUtility;
import com.fs.security.facade.SecurityFacade;
import com.jk.exceptions.JKInvalidUserException;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.security.JKUser;

/**
 * @author u087
 */
public class AuthenicationDialog extends JKDialog {

	private static final long serialVersionUID = 1L;

	/**
	 * @param parent
	 * @param title
	 * @return
	 * @throws SecurityException
	 */
	public static JKUser authenticateUser(final JFrame parent, final String title, final int maxRetries) throws JKInvalidUserException {
		final AuthenicationDialog dlg = new AuthenicationDialog(parent, title);
		for (int i = 0; i < maxRetries; i++) {
			dlg.reset();
			dlg.setVisible(true);
			if (dlg.getUser() != null) {
				return dlg.getUser();
			} else if (dlg.isCancelled()) {
				break;
			} else {
				SwingUtility.showUserErrorDialog("INVALID_USER_NAME_OR_PASSWORD", false);
			}
		}
		throw new JKInvalidUserException("INVALID_USER");
	}

	public static void main(final String[] args) throws SecurityException {
		final JKUser info = authenticateUser(null, "Test", 3);
		System.out.println(info);

	}

	JKTextField txtUserName = new JKTextField(new TextDocument(14), 15);

	JKPasswordField txtPassword = new JKPasswordField(14, 15);

	JKButton btnAuthenticate = new JKButton("AUTHENTICATE");

	JKButton btnCancel = new JKButton("CLOSE");

	private JKUser user;

	boolean cancelled;

	/**
	 *
	 * @param parent
	 */
	public AuthenicationDialog(final Dialog parent) {
		super(parent, "AUTHINECATION");
		initUI();
	}

	/**
	 *
	 * @param parent
	 * @param title
	 */
	public AuthenicationDialog(final Dialog parent, final String title) {
		super(parent, title);
		initUI();
	}

	/**
	 *
	 * @param parent
	 */
	public AuthenicationDialog(final Frame parent) {
		this(parent, "AUTHINECATION");
		initUI();
	}

	/**
	 *
	 * @param parent
	 * @param title
	 */
	public AuthenicationDialog(final Frame parent, final String title) {
		super(parent, title);
		initUI();
	}

	/**
	 * @return the authenticationInfo
	 */
	public JKUser getUser() {
		return this.user;
	}

	private void handleAuthenticate() {
		try {
			validateInput();
			final JKUser user = viewToModel();
			final SecurityFacade facade = new SecurityFacade();
			try {
				if (facade.isValidUser(user)) {
					if (user.isDisabled()) {
						SwingUtility.showUserErrorDialog("DISABLED_USER");
					} else {
						// facade.addUserLoginAudit();
						this.user = user;
						dispose();
					}
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AuthenicationDialog.this.txtUserName.requestFocus();
						}
					});
					SwingUtility.showUserErrorDialog("INVAILD_USER_NAME_OR_PASSWORD", false);
				}
			} catch (final JKDataAccessException e) {
				JKExceptionUtil.handle(e);
			}
		} catch (final ValidationException e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 */
	private void handleClose() {
		setCancelled(true);
		dispose();
	}

	/**
	 *
	 */
	private void initUI() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		final JKPanel<?> pnlInfo = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));
		pnlInfo.setLayout(new GridLayout(2, 1, 5, 2));
		pnlInfo.setBorder(BorderFactory.createTitledBorder("USER_CRED"));
		pnlInfo.add(new JKLabledComponent("USER_NAME", 85, this.txtUserName));
		pnlInfo.add(new JKLabledComponent("PASSWORD", 85, this.txtPassword));

		final JKPanel<?> pnlButtons = new JKPanel<Object>(new FlowLayout(FlowLayout.CENTER));

		this.btnAuthenticate.setIcon("unlock.png");
		this.btnCancel.setIcon("cancel.png");
		pnlButtons.add(this.btnAuthenticate);
		pnlButtons.add(this.btnCancel);
		final JKPanel<?> container = new JKMainPanel(new BorderLayout());

		container.add(pnlInfo, BorderLayout.CENTER);
		container.add(pnlButtons, BorderLayout.SOUTH);
		add(container);
		pack();
		setLocationRelativeTo(null);

		this.txtUserName.grabFocus();
		this.btnAuthenticate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAuthenticate();
			}
		});

		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleClose();
			}

		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				handleClose();
			}
		});
		this.txtUserName.requestFocusInWindow();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				handleClose();
			}
		});
		this.txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					AuthenicationDialog.this.btnAuthenticate.doClick();
				}
			}
		});
	}

	/**
	 * @return
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 *
	 */
	private void reset() {
		this.txtUserName.setText("");
		this.txtPassword.setText("");
	}

	/**
	 * @param cancelled
	 */
	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @param authenticationInfo
	 *            the authenticationInfo to set
	 */
	public void setUser(final JKUser user) {
		this.user = user;
	}

	/**
	 * @throws ValidationException
	 *
	 */
	private void validateInput() throws ValidationException {
		SwingValidator.checkEmpty(this.txtUserName);
		SwingValidator.checkEmpty(this.txtPassword);
	}

	/**
	 *
	 * @return
	 */
	private JKUser viewToModel() {
		final JKUser user = new JKUser();
		user.setUserId(this.txtUserName.getText());
		user.setPassword(GeneralUtility.encode(this.txtPassword.getText()));
		return user;
	}
}
