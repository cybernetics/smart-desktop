package com.fs.security.ui.database;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.apps.backup.DataBaseBackup;
import com.fs.commons.apps.backup.ImportManager;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.connection.PoolingDataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

////////////////////////////////////////////////////////////////////
// Author Mohamed Kiswani
// since  3-2-2010
//////////////////////////////////////////////////////////////////

public class PnlDbManagment extends JKPanel<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ////////////////////////////////////////////////////////////////////////////////
	private JKTextField bcFilePath = new JKTextField(12, 20, false);
	private JKButton btnBrowse = new JKButton("SELECT_PATH","F1");
	protected JKButton btnBackup = new JKButton("BACK_UP","F8",true);
	protected JKButton btnImport = new JKButton("IMPORT_DB","F7",true);
	private JKButton btnCancel = new JKButton("CLOSE_PANEL","F6");

	// ///////////////////////////////////////////////////////////////////////////////////
	public PnlDbManagment() {
		init();
	}

	// /////////////////////////////////////////////////////////////////////////////////
	private void init() {
		JKPanel<?> container = new JKPanel<Object>(new BorderLayout());
		container.setBorder(SwingUtility.createTitledBorder(""));
		container.add(getMainPanel(), BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		btnBrowse.setIcon("window_list.png");
		btnBackup.setIcon("db_add.png");
		btnCancel.setIcon("close.png");
		btnImport.setIcon("DB_Backup.gif");
		add(container);

		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBrowse();
			}
		});
		btnBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleBackup();
			}
		});
		btnImport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleImport();
			}

			
		});
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleCancel();
			}

			
		});
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void handleBackup() {
			try {
				bcFilePath.checkEmpty();	
				DataBaseBackup dataBaseBackup = new DataBaseBackup();
				dataBaseBackup.setOutputPath(bcFilePath.getText());
				dataBaseBackup.setCompress(true);
				dataBaseBackup.start();
				SwingUtility.showSuccessDialog("BACKUP_CREATION_DONE");
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}

	}
	////////////////////////////////////////////////////////////////////////////////////
	protected void handleImport() {

		try {
			bcFilePath.checkEmpty();
			ImportManager importManager = new ImportManager(bcFilePath.getText());
			importManager.doImport();
			SwingUtility.showSuccessDialog("IMPORT_DB_DONE");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
		
	}
	////////////////////////////////////////////////////////////////////////////////////
	protected void handleCancel() {
		SwingUtility.closePanel(this);
	}
	// ////////////////////////////////////////////////////////////////////////////
	private JKPanel<?> getButtonsPanel() {
		JKPanel<?> panel = new JKPanel<Object>();
		panel.add(btnBrowse);
		panel.add(btnBackup);
		panel.add(btnImport);
		panel.add(btnCancel);
		return panel;
	}

	// //////////////////////////////////////////////////////////////////////////////
	private JKPanel<?> getMainPanel() {
		JKPanel<?> pnlInfo = new JKPanel<Object>();
		pnlInfo.setBorder(SwingUtility.createTitledBorder("BACKUP_INFO"));
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.add(new JKLabledComponent("BC_FILE_PATH", bcFilePath));
		return pnlInfo;
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void handleBrowse() {
		bcFilePath.setText(showFileChoser());
	}
	// //////////////////////////////////////////////////////////////////////////////
	private String showFileChoser() {
		String strPath = "";
		JFrame frame = new JFrame();
		JFileChooser fc =SwingUtility.getFileChooser() ;//new JFileChooser(".");
		int rVal = fc.showSaveDialog(frame);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			strPath = fc.getSelectedFile().getAbsolutePath();

		}
		return strPath;

	}
	// //////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws ServerDownException, DaoException, IOException {
		DataSourceFactory.setDefaultDataSource(new PoolingDataSource("system.config"));
		// user =SecurityManager.getCurrentUser();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.showPanelInDialog(new PnlDbManagment(), "TEST");
	}
}
