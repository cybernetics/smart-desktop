package com.fs.commons.locale;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.JFileChooser;

import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class FrmLabelsUtil extends JKFrame {
	JFileChooser chooser = new JFileChooser();
	JKTextField txtFile1 = new JKTextField();
	JKTextField txtFile2 = new JKTextField();
	JKButton btnBrowseFile1 = new JKButton("Browse");
	JKButton btnBrowseFile2 = new JKButton("Browse");
	JKButton btnRemoveDuplicatesInTwoFiles = new JKButton("Remove Duplicates From Two Files");
	JKButton btnRemoveDuplicatesFromSecondFile= new JKButton("Remove Duplicates From Second File");
	JKButton btnRemoveKeysWithSpacesFromFile2 = new JKButton("Remove Keys with Spaces from file2");
	JKButton btncaptilizeKeys = new JKButton("Captilise Keys On file2");

	public FrmLabelsUtil() {
		setSize(400, 200);
		setLocationRelativeTo(null);
		JKPanel pnl = new JKPanel();
		pnl.add(new JKLabledComponent("First File", txtFile1));
		pnl.add(btnBrowseFile1);
		pnl.add(new JKLabledComponent("Second File", txtFile2));
		pnl.add(btnBrowseFile2);
		pnl.add(btnRemoveDuplicatesInTwoFiles);
		pnl.add(btnRemoveDuplicatesFromSecondFile);
		pnl.add(btnRemoveKeysWithSpacesFromFile2);
		pnl.add(btncaptilizeKeys);
		add(pnl);
		btnBrowseFile1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.showOpenDialog(FrmLabelsUtil.this);
				File selectedFile = chooser.getSelectedFile();
				if (selectedFile != null) {
					txtFile1.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		btnBrowseFile2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				chooser.showOpenDialog(FrmLabelsUtil.this);
				File selectedFile = chooser.getSelectedFile();
				if (selectedFile != null) {
					txtFile2.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		btnRemoveDuplicatesInTwoFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleRemoveReplicateInTwoFiles();
			}
		});
		btnRemoveKeysWithSpacesFromFile2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleRemoveKeysWithSpacesFromFile2();
			}
		});
		btnRemoveDuplicatesFromSecondFile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleRemoveDuplicateFromSecondFile();				
			}
		});
	}

	/**
	 * 
	 */
	protected void handleRemoveKeysWithSpacesFromFile2() {
		try {
			System.out.println("Start Processing ....");
			txtFile2.checkEmpty();
			Properties prop2 = GeneralUtility.readPropertyStream(new FileInputStream(txtFile2.getText()));
			Enumeration<Object> keys = prop2.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				if (key.contains(" ")&& !key.contains("CONCAT_WS")) {
					Object removed = prop2.remove(key);
					if (removed != null) {
						System.out.println(key.toString() + " = " + removed + "  .::. Removed");
					}
				}				
			}
			prop2.store(new FileOutputStream(txtFile2.getText()), "");
			System.out.println("Done ....");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 */
	protected void handleRemoveReplicateInTwoFiles() {
		try {
			System.out.println("Start Processing ....");
			txtFile1.checkEmpty();
			txtFile2.checkEmpty();
			Properties prop1 = GeneralUtility.readPropertyStream(new FileInputStream(txtFile1.getText()));
			Properties prop2 = GeneralUtility.readPropertyStream(new FileInputStream(txtFile2.getText()));
			Enumeration<Object> keys = prop1.keys();
			while (keys.hasMoreElements()) {
				Object key = keys.nextElement();
				Object removed = prop2.remove(key);
				if (removed != null) {
					System.out.println(key.toString() + " = " + removed + "  .::. Removed");
				}
				prop2.store(new FileOutputStream(txtFile2.getText()), "");
			}
			System.out.println("Done ....");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}

	}
	
	/**
	 * 
	 */
	protected void handleRemoveDuplicateFromSecondFile() {
		try {
			System.out.println("Start Processing ....");
			txtFile2.checkEmpty();
			Properties prop2 = GeneralUtility.readPropertyStream(new FileInputStream(txtFile2.getText()));
			Properties newProp=new Properties();
			Enumeration<Object> keys = prop2.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				newProp.put(key, prop2.getProperty(key));
			}
			newProp.store(new FileOutputStream(txtFile2.getText()), "");
			System.out.println("Done ....");
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}

	}

	public static void main(String[] args) {
		new FrmLabelsUtil().setVisible(true);
	}
}
