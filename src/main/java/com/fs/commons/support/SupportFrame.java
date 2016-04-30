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

package com.fs.commons.support;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKDate;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.license.client.HttpLicenseClient;
import com.fs.license.client.Installer;

public class SupportFrame extends JKFrame {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param args
	 */
	public static void main(final String[] args) {
		final SupportFrame frame = new SupportFrame();
		frame.setVisible(true);

	}

	JButton btnShowServerRepo = new JKButton("Show Server License Repository");

	JButton btnShowClientLicense = new JKButton("Show Clent License");
	JButton btnCreateClientLicense = new JKButton("Create Clent License");

	JButton btnImportClientLicense = new JKButton("Import Clent License");
	JButton btnViewDatabaseUsers = new JKButton("View Database users");
	JButton btnExit = new JKButton("Exit");

	JFileChooser chooser = new JFileChooser(".");

	/**
	 *
	 */
	public SupportFrame() {
		final String password = JOptionPane.showInputDialog("Please Enter Password");
		if (password == null || !password.equals(deHash("102-115-95-115-117-112-112-111-114-116-"))) {
			System.exit(0);
		}
		init();
	}

	public String deHash(final String src) {
		String str = "";
		final String[] bytes = src.split("-");
		for (final String b : bytes) {
			final String digit = b;
			if (digit != null && !digit.equals("")) {
				str += "" + (char) Integer.parseInt(digit);
			}
		}
		return str;
	}

	/*
	 *
	 */
	private Date getExpiryDate() {
		final JKDate dt = new JKDate();
		dt.setDate(com.fs.license.server.Installer.SEX_MONTHS_DATE);
		final JKDialog dialog = new JKDialog();
		dialog.add(new JKLabledComponent("expiry date", dt));
		dialog.pack();
		dialog.setVisible(true);

		return dt.getDate();
	}

	/**
	 *
	 */
	protected void handleCreateClientLicense() {
		try {
			Installer.main(null);
			JOptionPane.showMessageDialog(this, "License Created succ");
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////////////////////////
	protected void handleImportClientLicense() {
		this.chooser.setMultiSelectionEnabled(true);
		this.chooser.showOpenDialog(this);
		final File[] files = this.chooser.getSelectedFiles();
		if (files.length > 0) {
			final Date expiryDate = getExpiryDate();
			for (final File file : files) {
				try {
					if (file.getName().endsWith("lic")) {
						com.fs.license.server.Installer.importFile(file, expiryDate);
					}
				} catch (final Exception e) {
					JOptionPane.showMessageDialog(this, e.getMessage());
					e.printStackTrace();
				}
			}
			JOptionPane.showMessageDialog(this, "License imported successfully");
		}
	}

	/**
	 *
	 */
	protected void handleShowClientLicense() {
		try {
			final Class<?> clas = Class.forName("com.fs.license.client.HttpLicenseClient");
			final Method method = clas.getDeclaredMethod("readLocalLicenseInfo");
			method.setAccessible(true);

			final HttpLicenseClient client = new HttpLicenseClient();
			final String[] data = (String[]) method.invoke(client);
			final StringBuffer buffer = new StringBuffer();
			for (final String element : data) {
				buffer.append(element + "\n");
			}
			JOptionPane.showMessageDialog(this, buffer.toString());
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 *
	 */
	protected void handleShowServerRepo() {
		try {
			final String data = com.fs.license.server.Installer.readServerLicenseRepositoroy();
			JOptionPane.showMessageDialog(this, data);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	// ///////////////////////////////////////////////////////////////////////
	protected void handleViewDatabaseUsers() {
		SwingUtility.showPanelInDialog(new PnlEncodeDecode(), "Encode Decode");
	}

	/**
	 * @1.1
	 */
	private void init() {
		setSize(new Dimension(300, 400));
		setLocationRelativeTo(null);

		final JPanel panel = new JKMainPanel();
		panel.setLayout(new GridLayout(7, 1, 2, 2));
		panel.setBorder(BorderFactory.createTitledBorder("Main menu"));

		panel.add(this.btnShowServerRepo);
		panel.add(this.btnCreateClientLicense);
		panel.add(this.btnImportClientLicense);
		panel.add(this.btnShowClientLicense);
		panel.add(this.btnViewDatabaseUsers);
		panel.add(this.btnExit);

		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.btnShowServerRepo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleShowServerRepo();
			}
		});
		this.btnShowClientLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleShowClientLicense();
			}
		});
		this.btnCreateClientLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleCreateClientLicense();
			}
		});
		this.btnImportClientLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleImportClientLicense();
			}
		});
		this.btnViewDatabaseUsers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleViewDatabaseUsers();
			}
		});
		this.btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				System.exit(0);
			}
		});
	}

}
