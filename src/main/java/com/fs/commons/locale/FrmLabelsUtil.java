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
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

public class FrmLabelsUtil extends JKFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1382026294839748400L;

	public static void main(final String[] args) {
		new FrmLabelsUtil().setVisible(true);
	}

	JFileChooser chooser = new JFileChooser();
	JKTextField txtFile1 = new JKTextField();
	JKTextField txtFile2 = new JKTextField();
	JKButton btnBrowseFile1 = new JKButton("Browse");
	JKButton btnBrowseFile2 = new JKButton("Browse");
	JKButton btnRemoveDuplicatesInTwoFiles = new JKButton("Remove Duplicates From Two Files");
	JKButton btnRemoveDuplicatesFromSecondFile = new JKButton("Remove Duplicates From Second File");
	JKButton btnRemoveKeysWithSpacesFromFile2 = new JKButton("Remove Keys with Spaces from file2");

	JKButton btncaptilizeKeys = new JKButton("Captilise Keys On file2");

	public FrmLabelsUtil() {
		setSize(400, 200);
		setLocationRelativeTo(null);
		final JKPanel pnl = new JKPanel();
		pnl.add(new JKLabledComponent("First File", this.txtFile1));
		pnl.add(this.btnBrowseFile1);
		pnl.add(new JKLabledComponent("Second File", this.txtFile2));
		pnl.add(this.btnBrowseFile2);
		pnl.add(this.btnRemoveDuplicatesInTwoFiles);
		pnl.add(this.btnRemoveDuplicatesFromSecondFile);
		pnl.add(this.btnRemoveKeysWithSpacesFromFile2);
		pnl.add(this.btncaptilizeKeys);
		add(pnl);
		this.btnBrowseFile1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				FrmLabelsUtil.this.chooser.showOpenDialog(FrmLabelsUtil.this);
				final File selectedFile = FrmLabelsUtil.this.chooser.getSelectedFile();
				if (selectedFile != null) {
					FrmLabelsUtil.this.txtFile1.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		this.btnBrowseFile2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				FrmLabelsUtil.this.chooser.showOpenDialog(FrmLabelsUtil.this);
				final File selectedFile = FrmLabelsUtil.this.chooser.getSelectedFile();
				if (selectedFile != null) {
					FrmLabelsUtil.this.txtFile2.setText(selectedFile.getAbsolutePath());
				}
			}
		});
		this.btnRemoveDuplicatesInTwoFiles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleRemoveReplicateInTwoFiles();
			}
		});
		this.btnRemoveKeysWithSpacesFromFile2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleRemoveKeysWithSpacesFromFile2();
			}
		});
		this.btnRemoveDuplicatesFromSecondFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleRemoveDuplicateFromSecondFile();
			}
		});
	}

	/**
	 *
	 */
	protected void handleRemoveDuplicateFromSecondFile() {
		try {
			System.out.println("Start Processing ....");
			this.txtFile2.checkEmpty();
			final Properties prop2 = GeneralUtility.readPropertyStream(new FileInputStream(this.txtFile2.getText()));
			final Properties newProp = new Properties();
			final Enumeration<Object> keys = prop2.keys();
			while (keys.hasMoreElements()) {
				final String key = (String) keys.nextElement();
				newProp.put(key, prop2.getProperty(key));
			}
			newProp.store(new FileOutputStream(this.txtFile2.getText()), "");
			System.out.println("Done ....");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}

	}

	/**
	 *
	 */
	protected void handleRemoveKeysWithSpacesFromFile2() {
		try {
			System.out.println("Start Processing ....");
			this.txtFile2.checkEmpty();
			final Properties prop2 = GeneralUtility.readPropertyStream(new FileInputStream(this.txtFile2.getText()));
			final Enumeration<Object> keys = prop2.keys();
			while (keys.hasMoreElements()) {
				final String key = (String) keys.nextElement();
				if (key.contains(" ") && !key.contains("CONCAT_WS")) {
					final Object removed = prop2.remove(key);
					if (removed != null) {
						System.out.println(key.toString() + " = " + removed + "  .::. Removed");
					}
				}
			}
			prop2.store(new FileOutputStream(this.txtFile2.getText()), "");
			System.out.println("Done ....");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 */
	protected void handleRemoveReplicateInTwoFiles() {
		try {
			System.out.println("Start Processing ....");
			this.txtFile1.checkEmpty();
			this.txtFile2.checkEmpty();
			final Properties prop1 = GeneralUtility.readPropertyStream(new FileInputStream(this.txtFile1.getText()));
			final Properties prop2 = GeneralUtility.readPropertyStream(new FileInputStream(this.txtFile2.getText()));
			final Enumeration<Object> keys = prop1.keys();
			while (keys.hasMoreElements()) {
				final Object key = keys.nextElement();
				final Object removed = prop2.remove(key);
				if (removed != null) {
					System.out.println(key.toString() + " = " + removed + "  .::. Removed");
				}
				prop2.store(new FileOutputStream(this.txtFile2.getText()), "");
			}
			System.out.println("Done ....");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}

	}
}
