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
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.connection.JKPoolingDataSource;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.jk.exceptions.handler.JKExceptionUtil;

////////////////////////////////////////////////////////////////////
// Author Mohamed Kiswani
// since  3-2-2010
//////////////////////////////////////////////////////////////////

public class PnlDbManagment extends JKPanel<Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// //////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws ServerDownException, JKDataAccessException, IOException {
		JKDataSourceFactory.setDefaultDataSource(new JKPoolingDataSource("system.config"));
		// user =SecurityManager.getCurrentUser();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.showPanelInDialog(new PnlDbManagment(), "TEST");
	}

	// ////////////////////////////////////////////////////////////////////////////////
	private final JKTextField bcFilePath = new JKTextField(12, 20, false);
	private final JKButton btnBrowse = new JKButton("SELECT_PATH", "F1");
	protected JKButton btnBackup = new JKButton("BACK_UP", "F8", true);
	protected JKButton btnImport = new JKButton("IMPORT_DB", "F7", true);

	private final JKButton btnCancel = new JKButton("CLOSE_PANEL", "F6");

	// ///////////////////////////////////////////////////////////////////////////////////
	public PnlDbManagment() {
		init();
	}

	// ////////////////////////////////////////////////////////////////////////////
	private JKPanel<?> getButtonsPanel() {
		final JKPanel<?> panel = new JKPanel<Object>();
		panel.add(this.btnBrowse);
		panel.add(this.btnBackup);
		panel.add(this.btnImport);
		panel.add(this.btnCancel);
		return panel;
	}

	// //////////////////////////////////////////////////////////////////////////////
	private JKPanel<?> getMainPanel() {
		final JKPanel<?> pnlInfo = new JKPanel<Object>();
		pnlInfo.setBorder(SwingUtility.createTitledBorder("BACKUP_INFO"));
		pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.add(new JKLabledComponent("BC_FILE_PATH", this.bcFilePath));
		return pnlInfo;
	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void handleBackup() {
		try {
			this.bcFilePath.checkEmpty();
			final DataBaseBackup dataBaseBackup = new DataBaseBackup();
			dataBaseBackup.setOutputPath(this.bcFilePath.getText());
			dataBaseBackup.setCompress(true);
			dataBaseBackup.start();
			SwingUtility.showSuccessDialog("BACKUP_CREATION_DONE");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}

	}

	// //////////////////////////////////////////////////////////////////////////////
	protected void handleBrowse() {
		this.bcFilePath.setText(showFileChoser());
	}

	////////////////////////////////////////////////////////////////////////////////////
	protected void handleCancel() {
		SwingUtility.closePanel(this);
	}

	////////////////////////////////////////////////////////////////////////////////////
	protected void handleImport() {

		try {
			this.bcFilePath.checkEmpty();
			final ImportManager importManager = new ImportManager(this.bcFilePath.getText());
			importManager.doImport();
			SwingUtility.showSuccessDialog("IMPORT_DB_DONE");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}

	}

	// /////////////////////////////////////////////////////////////////////////////////
	private void init() {
		final JKPanel<?> container = new JKPanel<Object>(new BorderLayout());
		container.setBorder(SwingUtility.createTitledBorder(""));
		container.add(getMainPanel(), BorderLayout.CENTER);
		container.add(getButtonsPanel(), BorderLayout.SOUTH);
		this.btnBrowse.setIcon("window_list.png");
		this.btnBackup.setIcon("db_add.png");
		this.btnCancel.setIcon("close.png");
		this.btnImport.setIcon("DB_Backup.gif");
		add(container);

		this.btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleBrowse();
			}
		});
		this.btnBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleBackup();
			}
		});
		this.btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleImport();
			}

		});
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCancel();
			}

		});
	}

	// //////////////////////////////////////////////////////////////////////////////
	private String showFileChoser() {
		String strPath = "";
		final JFrame frame = new JFrame();
		final JFileChooser fc = SwingUtility.getFileChooser();// new
																// JFileChooser(".");
		final int rVal = fc.showSaveDialog(frame);
		if (rVal == JFileChooser.APPROVE_OPTION) {
			strPath = fc.getSelectedFile().getAbsolutePath();

		}
		return strPath;

	}
}
