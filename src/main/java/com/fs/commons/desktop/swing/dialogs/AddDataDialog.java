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
package com.fs.commons.desktop.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DataPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

/**
 * @deprecated
 * @author u087
 *
 */
@Deprecated
public class AddDataDialog extends JKDialog {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param frm
	 *            Frame
	 * @param pnl
	 *            DataPanel
	 * @param autoIncrement
	 *            boolean
	 * @throws DaoException
	 */
	public static String showAddDialog(final Dialog frm, final DataPanel pnl, final boolean autoIncrement) throws DaoException {
		pnl.resetComponents();
		final AddDataDialog dlg = new AddDataDialog(frm, pnl, autoIncrement);
		dlg.setVisible(true);
		return dlg.getNewRecordId();
	}

	/**
	 *
	 * @param frm
	 *            Frame
	 * @param pnl
	 *            DataPanel
	 * @param autoIncrement
	 *            boolean
	 * @throws DaoException
	 */
	public static String showAddDialog(final Frame frm, final DataPanel pnl, final boolean autoIncrement) throws DaoException {
		pnl.resetComponents();
		final AddDataDialog dlg = new AddDataDialog(frm, pnl, autoIncrement);
		dlg.setVisible(true);
		dlg.dispose();
		return dlg.getNewRecordId();
	}

	DataPanel dataPanel;

	JButton btnAdd = new JKButton("ADD");

	JButton btnCancel = new JKButton("CANCEL");

	String newRecordId;

	private final boolean autoIncrement;

	/**
	 *
	 * @param parent
	 *            Frame
	 * @param dataPanel
	 *            DataPanel
	 * @param autoIncrement
	 *            boolean
	 */
	private AddDataDialog(final Dialog parent, final DataPanel dataPanel, final boolean autoIncrement) {
		super(parent);
		setTitle(Lables.get("ADD_RECORD"));
		this.autoIncrement = autoIncrement;
		this.dataPanel = dataPanel;
		init();
	}

	/**
	 *
	 * @param parent
	 *            Frame
	 * @param dataPanel
	 *            DataPanel
	 * @param autoIncrement
	 *            boolean
	 */
	public AddDataDialog(final Frame parent, final DataPanel dataPanel, final boolean autoIncrement) {
		super(parent);
		setTitle(Lables.get("ADD_RECORD"));
		this.autoIncrement = autoIncrement;
		this.dataPanel = dataPanel;
		init();
	}

	/**
	 *
	 * @return
	 */
	public String getNewRecordId() {
		return this.newRecordId;
	}

	/**
	 *
	 */
	private void handleAdd() {
		try {
			this.dataPanel.validateAddData(!this.autoIncrement);
			final String newId = this.dataPanel.handleAddEvent();
			if (newId != null) {
				setNewRecordId(newId);
			}
			SwingUtility.showSuccessDialog(this, "SUCC_RECORD_ADDED");
			dispose();
		} catch (final ValidationException ex) {
			SwingUtility.showUserErrorDialog(this, ex.getMessage(), ex);
		} catch (final DaoException ex) {
			SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
		}
	}

	/**
	 * init
	 */
	protected void init() {

		this.dataPanel.getIdField().setEnabled(!this.autoIncrement);
		this.dataPanel.enableDataFields(true);
		final JKPanel<?> southPanel = new JKMainPanel();
		southPanel.add(this.btnAdd);
		this.btnAdd.setIcon(new ImageIcon(GeneralUtility.getIconURL("button_ok.png")));
		southPanel.add(this.btnCancel);
		this.btnCancel.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		add(this.dataPanel);
		add(southPanel, BorderLayout.SOUTH);

		this.btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAdd();
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});
		super.initDialog();
	}

	/**
	 *
	 * @param newRecordId
	 */
	public void setNewRecordId(final String newRecordId) {
		this.newRecordId = newRecordId;
	}
}
