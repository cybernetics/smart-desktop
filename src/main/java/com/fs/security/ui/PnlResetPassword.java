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
package com.fs.security.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKPasswordField;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.security.facade.SecurityFacade;
import com.jk.exceptions.handler.ExceptionUtil;
import com.jk.security.JKSecurityManager;
import com.jk.security.JKUser;

public class PnlResetPassword extends JKPanel<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JKTextField txtUserName = new JKTextField(12, 20, false);
	private final JKPasswordField txtOldPassword = new JKPasswordField(20, 20);
	private final JKPasswordField txtNewPassword = new JKPasswordField(12, 20);
	private final JKPasswordField txtConfirmPassword = new JKPasswordField(12, 20);
	private final JKButton btnSave = new JKButton("SAVE");
	private final JKButton btnCancel = new JKButton("CLOSE_PANEL");
	private final JKUser user;

	/**
	 *
	 */
	public PnlResetPassword() {
		this(JKSecurityManager.getCurrentUser());
	}

	/**
	 *
	 */
	public PnlResetPassword(final JKUser user) {
		this.user = user;
		init();
		modelToView();
	}

	/**
	 * @return
	 */
	protected void checkValues() throws ValidationException {
		this.txtUserName.checkEmpty();
		this.txtOldPassword.checkEmpty();
		this.txtNewPassword.checkEmpty();
		this.txtConfirmPassword.checkEmpty();

		if (!this.txtOldPassword.getText().equals(this.user.getPassword())) {
			throw new ValidationException("OLD_PASSWORD_INCORRECT", this.txtOldPassword);
		}
		if (!this.txtNewPassword.getText().equals(this.txtConfirmPassword.getText())) {
			throw new ValidationException("CONFIRM_PASSWORD_MISMATCH", this.txtNewPassword);
		}
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> getButtonsPanel() {
		final JKPanel<?> panel = new JKPanel<Object>();
		panel.add(this.btnSave);
		panel.add(this.btnCancel);
		return panel;
	}

	/**
	 * @return
	 */
	private JKPanel<?> getUserInfoPanel() {
		final JKPanel<?> pnlInfo = new JKPanel<Object>(new GridLayout(4, 1, 3, 2));
		pnlInfo.setBorder(SwingUtility.createTitledBorder("USER_INFO"));
		// pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.add(new JKLabledComponent("USER_NAME", this.txtUserName));
		pnlInfo.add(new JKLabledComponent("OLD_PASSWORD", this.txtOldPassword));
		pnlInfo.add(new JKLabledComponent("NEW_PASSWORD", this.txtNewPassword));
		pnlInfo.add(new JKLabledComponent("_CONFIRM_PASSWORD", this.txtConfirmPassword));
		return pnlInfo;
	}

	/**
	 *
	 */
	protected void handleCancel() {
		SwingUtility.closePanel(this);
	}

	/**
	 * @throws JKDataAccessException
	 */
	protected void handleSave() {
		try {
			checkValues();
			final JKUser user = viewToModel();
			final SecurityFacade facade = new SecurityFacade();
			facade.updateUser(user);
			SwingUtility.showSuccessDialog("PASSWORD_CHANGED_SUCC");
			SwingUtility.closePanel(this);
		} catch (final ValidationException e) {
			ExceptionUtil.handle(e);
		} catch (final JKDataAccessException e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * @1.2
	 */
	private void init() {
		final JKPanel<?> container = new JKPanel<Object>(new BorderLayout());
		container.setBorder(SwingUtility.createTitledBorder(""));
		container.add(getUserInfoPanel(), BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		this.btnSave.setIcon("small_filesave.png");
		this.btnCancel.setIcon("close_x_red_commons_model_icon.png");
		add(container);

		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCancel();
			}
		});
	}

	/**
	 * @param employee
	 * @throws JKDataAccessException
	 */
	public void modelToView() {
		this.txtUserName.setText(this.user.getUserId());
	}

	/**
	 *
	 * @return
	 */
	private JKUser viewToModel() {
		this.user.setPassword(this.txtNewPassword.getText());
		return this.user;
	}

	/**
	 * @param args
	 */
	// public static void main(String[] args) {
	// User user=new User();
	// user.setUserRecordId(1);
	// user.setUserId("admin");
	// user.setFullName("admin");
	// user.setPassword("admin");
	// user.setStatus(0);
	//
	// // user =SecurityManager.getCurrentUser();
	// SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	// SwingUtility.showPanelInDialog(new PnlResetPassword(user),"TEST");
	// }
}
