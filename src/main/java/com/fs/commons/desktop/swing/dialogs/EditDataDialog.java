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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DataPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.GeneralUtility;

/**
 * @deprecated
 *             <p>
 *             Title:
 *             </p>
 *
 *             <p>
 *             Description:
 *             </p>
 *
 *             <p>
 *             Copyright: Copyright (c) 2007
 *             </p>
 *
 *             <p>
 *             Company:
 *             </p>
 *
 * @author not attributable
 * @version 1.0
 */
@Deprecated
public class EditDataDialog extends JKDialog {
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
	 * @param idValue
	 *            String
	 * @return boolean true if the data changed , false if not
	 * @throws JKDataAccessException
	 */
	public static boolean showEditDialog(final Frame frm, final DataPanel pnl, final String idValue) throws JKDataAccessException {
		return showEditDialog(frm, pnl, idValue, true);
	}

	/**
	 *
	 * @param frm
	 * @param pnl
	 * @param idValue
	 * @param allowDelete
	 * @return
	 * @throws JKDataAccessException
	 */
	public static boolean showEditDialog(final Frame frm, final DataPanel pnl, final String idValue, final boolean allowDelete) throws JKDataAccessException {
		pnl.resetComponents();
		final EditDataDialog dlg = new EditDataDialog(frm, pnl, idValue);
		dlg.setAllowDelete(allowDelete);
		dlg.setVisible(true);
		// dlg.dispose();
		return !dlg.cancelled;
	}

	DataPanel dataPanel;

	JButton btnEdit = new JKButton("EDIT");

	JButton btnSave = new JKButton("SAVE");

	JButton btnDelete = new JKButton("DELETE");

	JButton btnCancelEdit = new JKButton("CANCEL_EDIT");

	JButton btnCancel = new JKButton("CANCEL"); // close this dialog

	boolean allowDelete = true;

	boolean cancelled = true;

	/**
	 *
	 * @param parent
	 *            Frame
	 * @param dataPanel
	 *            DataPanel
	 * @param idValue
	 *            String
	 */
	public EditDataDialog(final Frame parent, final DataPanel dataPanel, final String idValue) {
		super(parent);
		setTitle(Lables.get("VIEW_RECORD"));
		this.dataPanel = dataPanel;
		init();
		try {
			dataPanel.handleFindEvent(idValue);
		} catch (final JKDataAccessException ex) {
			SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
		}
	}

	public boolean getAllowDelete() {
		return this.allowDelete;
	}

	/**
	 *
	 *
	 */
	void handleCancelEdit() {
		this.btnEdit.setVisible(true);
		this.btnSave.setVisible(false);
		this.btnDelete.setVisible(false);
		this.btnCancelEdit.setVisible(false);
		this.btnCancel.setVisible(true);
		this.dataPanel.enableAllComponents(false);
	}

	/**
	 *
	 */
	void handleDelete() {
		if (SwingUtility.showConfirmationDialog(this, "CONF_DELETE_RECORD")) {
			try {
				this.dataPanel.handleDeleteEvent();
				SwingUtility.showSuccessDialog(this, "SUCC_RECORD_DELETED");
				dispose();
				this.cancelled = false;
			} catch (final JKDataAccessException ex) {
				SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
			}
		}
	}

	/**
	 *
	 */
	void handleEdit() {
		this.btnEdit.setVisible(false);
		this.btnSave.setVisible(true);
		this.btnDelete.setVisible(this.allowDelete);
		this.btnCancelEdit.setVisible(true);
		this.btnCancel.setVisible(false);
		this.dataPanel.enableDataFields(true);
	}

	/**
	 *
	 */
	private void handleSave() {
		try {
			this.dataPanel.validateUpdateData();
			this.dataPanel.handleSaveEvent();
			SwingUtility.showSuccessDialog(this, Lables.get("SUCC_RECORD_UPDATED"));
			dispose();
			this.cancelled = false;
		} catch (final ValidationException ex) {
			SwingUtility.showUserErrorDialog(this, ex.getMessage(), ex);
		} catch (final JKDataAccessException ex) {
			SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
		}

	}

	/**
	 * init
	 */
	protected void init() {
		setModal(true);

		this.dataPanel.getIdField().setEnabled(false);
		final JKPanel<?> southPanel = new JKMainPanel();
		southPanel.add(this.btnEdit);
		this.btnEdit.setIcon(new ImageIcon(GeneralUtility.getIconURL("filesaveas.png")));
		southPanel.add(this.btnSave);
		this.btnSave.setIcon(new ImageIcon(GeneralUtility.getIconURL("filesave.png")));
		southPanel.add(this.btnDelete);
		this.btnDelete.setIcon(new ImageIcon(GeneralUtility.getIconURL("db_remove.png")));

		southPanel.add(this.btnCancelEdit);
		this.btnCancelEdit.setIcon(new ImageIcon(GeneralUtility.getIconURL("back.png")));

		southPanel.add(this.btnCancel);
		this.btnCancel.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		add(this.dataPanel);
		add(southPanel, BorderLayout.SOUTH);

		this.btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleEdit();
			}
		});
		this.btnCancelEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCancelEdit();
			}
		});

		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleDelete();
			}
		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});
		handleCancelEdit();
		super.initDialog();
	}

	public void setAllowDelete(final boolean allowDelete) {
		this.allowDelete = allowDelete;
	}
}
