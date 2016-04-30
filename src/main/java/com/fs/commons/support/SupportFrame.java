/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      29/12/2008     Jamil Shreet    -Add method setDefaultCloseOperation(EXIT_ON_CLOSE) to method init()
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
		String password = JOptionPane.showInputDialog("Please Enter Password");
		if (password == null || !password.equals(deHash("102-115-95-115-117-112-112-111-114-116-"))) {
			System.exit(0);
		}
		init();
	}

	public String deHash(String src) {
		String str = "";
		String[] bytes = src.split("-");
		for (int i = 0; i < bytes.length; i++) {
			String digit = bytes[i];
			if (digit != null && !digit.equals("")) {
				str += "" + ((char) Integer.parseInt(digit));
			}
		}
		return str;
	}

	/**
	 * @1.1
	 */
	private void init() {
		setSize(new Dimension(300, 400));
		setLocationRelativeTo(null);

		JPanel panel = new JKMainPanel();
		panel.setLayout(new GridLayout(7, 1, 2, 2));
		panel.setBorder(BorderFactory.createTitledBorder("Main menu"));

		panel.add(btnShowServerRepo);
		panel.add(btnCreateClientLicense);
		panel.add(btnImportClientLicense);
		panel.add(btnShowClientLicense);
		panel.add(btnViewDatabaseUsers);
		panel.add(btnExit);

		add(panel, BorderLayout.CENTER);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		btnShowServerRepo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleShowServerRepo();
			}
		});
		btnShowClientLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleShowClientLicense();
			}
		});
		btnCreateClientLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleCreateClientLicense();
			}
		});
		btnImportClientLicense.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleImportClientLicense();
			}
		});
		btnViewDatabaseUsers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleViewDatabaseUsers();
			}
		});
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	// ///////////////////////////////////////////////////////////////////////
	protected void handleViewDatabaseUsers() {
		SwingUtility.showPanelInDialog(new PnlEncodeDecode(), "Encode Decode");
	}

	// ///////////////////////////////////////////////////////////////////////
	protected void handleImportClientLicense() {
		chooser.setMultiSelectionEnabled(true);
		chooser.showOpenDialog(this);
		File[] files = chooser.getSelectedFiles();
		if (files.length > 0) {
			Date expiryDate = getExpiryDate();
			for (File file : files) {
				try {
					if (file.getName().endsWith("lic")) {
						com.fs.license.server.Installer.importFile(file, expiryDate);
					}
				} catch (Exception e) {
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
	protected void handleCreateClientLicense() {
		try {
			Installer.main(null);
			JOptionPane.showMessageDialog(this, "License Created succ");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void handleShowClientLicense() {
		try {
			Class<?> clas = Class.forName("com.fs.license.client.HttpLicenseClient");
			Method method = clas.getDeclaredMethod("readLocalLicenseInfo");
			method.setAccessible(true);

			HttpLicenseClient client = new HttpLicenseClient();
			String[] data = (String[]) method.invoke(client);
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < data.length; i++) {
				buffer.append(data[i] + "\n");
			}
			JOptionPane.showMessageDialog(this, buffer.toString());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void handleShowServerRepo() {
		try {
			String data = com.fs.license.server.Installer.readServerLicenseRepositoroy();
			JOptionPane.showMessageDialog(this, data);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SupportFrame frame = new SupportFrame();
		frame.setVisible(true);

	}

	/*
	 * 
	 */
	private Date getExpiryDate() {
		JKDate dt = new JKDate();
		dt.setDate(com.fs.license.server.Installer.SEX_MONTHS_DATE);
		JKDialog dialog = new JKDialog();
		dialog.add(new JKLabledComponent("expiry date", dt));
		dialog.pack();
		dialog.setVisible(true);

		return dt.getDate();
	}

}
