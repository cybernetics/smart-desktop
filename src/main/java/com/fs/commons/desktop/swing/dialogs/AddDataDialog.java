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
public class AddDataDialog extends JKDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DataPanel dataPanel;

	JButton btnAdd = new JKButton("ADD");

	JButton btnCancel = new JKButton("CANCEL");
	
	String newRecordId;

	private boolean autoIncrement;

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
	public static String showAddDialog(Frame frm, DataPanel pnl, boolean autoIncrement) throws DaoException {
		pnl.resetComponents();
		AddDataDialog dlg = new AddDataDialog(frm, pnl, autoIncrement);
		dlg.setVisible(true);
		dlg.dispose();
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
	public static String showAddDialog(Dialog frm, DataPanel pnl, boolean autoIncrement) throws DaoException {
		pnl.resetComponents();
		AddDataDialog dlg = new AddDataDialog(frm, pnl, autoIncrement);
		dlg.setVisible(true);
		return dlg.getNewRecordId();		
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
	public AddDataDialog(Frame parent, DataPanel dataPanel, boolean autoIncrement) {
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
	private AddDataDialog(Dialog parent, DataPanel dataPanel, boolean autoIncrement) {
		super(parent);
		setTitle(Lables.get("ADD_RECORD"));
		this.autoIncrement = autoIncrement;
		this.dataPanel = dataPanel;
		init();
	}

	/**
	 * init
	 */
	protected void init() {
		
		dataPanel.getIdField().setEnabled(!autoIncrement);
		dataPanel.enableDataFields(true);
		JKPanel<?> southPanel = new JKMainPanel();
		southPanel.add(btnAdd);
		btnAdd.setIcon(new ImageIcon(GeneralUtility.getIconURL("button_ok.png")));
		southPanel.add(btnCancel);
		btnCancel.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileclose.png")));
		add(dataPanel);
		add(southPanel, BorderLayout.SOUTH);

		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAdd();
			}
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		super.initDialog();
	}

	/**
	 * 
	 */
	private void handleAdd() {
		try {
			dataPanel.validateAddData(!autoIncrement);
			String newId=dataPanel.handleAddEvent();
			if(newId!=null){
				setNewRecordId(newId);
			}
			SwingUtility.showSuccessDialog(this, "SUCC_RECORD_ADDED");
			dispose();
		} catch (ValidationException ex) {
			SwingUtility.showUserErrorDialog(this, ex.getMessage(), ex);
		} catch (DaoException ex) {
			SwingUtility.showDatabaseErrorDialog(this, ex.getMessage(), ex);
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getNewRecordId() {
		return newRecordId;
	}

	/**
	 * 
	 * @param newRecordId
	 */
	public void setNewRecordId(String newRecordId) {
		this.newRecordId = newRecordId;
	}
}
