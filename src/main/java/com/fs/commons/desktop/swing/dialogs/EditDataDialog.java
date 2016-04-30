package com.fs.commons.desktop.swing.dialogs;

import java.awt.BorderLayout;
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
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class EditDataDialog extends JKDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	 * @param frm
	 *            Frame
	 * @param pnl
	 *            DataPanel
	 * @param idValue
	 *            String
	 * @return boolean true if the data changed , false if not
	 * @throws DaoException
	 */
	public static boolean showEditDialog(Frame frm, DataPanel pnl, String idValue) throws DaoException {
		return showEditDialog(frm, pnl, idValue, true);
	}

	/**
	 * 
	 * @param frm
	 * @param pnl
	 * @param idValue
	 * @param allowDelete
	 * @return
	 * @throws DaoException
	 */
	public static boolean showEditDialog(Frame frm, DataPanel pnl, String idValue, boolean allowDelete) throws DaoException {
		pnl.resetComponents();
		EditDataDialog dlg = new EditDataDialog(frm, pnl, idValue);
		dlg.setAllowDelete(allowDelete);
		dlg.setVisible(true);
		// dlg.dispose();
		return !dlg.cancelled;
	}

	/**
	 * 
	 * @param parent
	 *            Frame
	 * @param dataPanel
	 *            DataPanel
	 * @param idValue
	 *            String
	 */
	public EditDataDialog(Frame parent, DataPanel dataPanel, String idValue) {
		super(parent);
		setTitle(Lables.get("VIEW_RECORD"));
		this.dataPanel = dataPanel;
		init();
		try {
			dataPanel.handleFindEvent(idValue);
		} catch (DaoException ex) {
			SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
		}
	}

	/**
	 * init
	 */
	protected void init() {
		setModal(true);
		
		dataPanel.getIdField().setEnabled(false);
		JKPanel<?> southPanel = new JKMainPanel();
		southPanel.add(btnEdit);
		btnEdit.setIcon(new ImageIcon(GeneralUtility.getIconURL("filesaveas.png")));
		southPanel.add(btnSave);
		btnSave.setIcon(new ImageIcon(GeneralUtility.getIconURL("filesave.png")));
		southPanel.add(btnDelete);
		btnDelete.setIcon(new ImageIcon(GeneralUtility.getIconURL("db_remove.png")));

		southPanel.add(btnCancelEdit);
		btnCancelEdit.setIcon(new ImageIcon(GeneralUtility.getIconURL("back.png")));

		southPanel.add(btnCancel);
		btnCancel.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		add(dataPanel);
		add(southPanel, BorderLayout.SOUTH);

		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleEdit();
			}
		});
		btnCancelEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleCancelEdit();
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleDelete();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		handleCancelEdit();
		super.initDialog();
	}

	/**
	 * 
	 */
	void handleEdit() {
		btnEdit.setVisible(false);
		btnSave.setVisible(true);
		btnDelete.setVisible(allowDelete);
		btnCancelEdit.setVisible(true);
		btnCancel.setVisible(false);
		dataPanel.enableDataFields(true);
	}

	/**
	 * 
	 * 
	 */
	void handleCancelEdit() {
		btnEdit.setVisible(true);
		btnSave.setVisible(false);
		btnDelete.setVisible(false);
		btnCancelEdit.setVisible(false);
		btnCancel.setVisible(true);
		dataPanel.enableAllComponents(false);
	}

	/**
	 * 
	 */
	private void handleSave() {
		try {
			dataPanel.validateUpdateData();
			dataPanel.handleSaveEvent();
			SwingUtility.showSuccessDialog(this, Lables.get("SUCC_RECORD_UPDATED"));
			dispose();
			cancelled = false;
		} catch (ValidationException ex) {
			SwingUtility.showUserErrorDialog(this, ex.getMessage(), ex);
		} catch (DaoException ex) {
			SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
		}

	}

	/**
	 * 
	 */
	void handleDelete() {
		if (SwingUtility.showConfirmationDialog(this, "CONF_DELETE_RECORD")) {
			try {
				dataPanel.handleDeleteEvent();
				SwingUtility.showSuccessDialog(this, "SUCC_RECORD_DELETED");
				dispose();
				cancelled = false;
			} catch (DaoException ex) {
				SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
			}
		}
	}

	public void setAllowDelete(boolean allowDelete) {
		this.allowDelete = allowDelete;
	}

	public boolean getAllowDelete() {
		return allowDelete;
	}
}
