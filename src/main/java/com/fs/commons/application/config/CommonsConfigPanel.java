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
package com.fs.commons.application.config;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class CommonsConfigPanel extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// key : property name
	Hashtable<String, BindingComponent<String>> components = new Hashtable<String, BindingComponent<String>>();
	private final CommonsConfigManager configManager;

	JKButton btnSave = new JKButton("Save");
	JKButton btnReload = new JKButton("Reload");
	JKButton btnAddProperty = new JKButton("Add Property");
	JFileChooser chooser = SwingUtility.getDefaultFileChooser();
	private JKPanel mainPanel;

	/**
	 *
	 */
	public CommonsConfigPanel(final CommonsConfigManager configManager) {
		this.configManager = configManager;
		init();
		populateData();
	}

	/**
	 *
	 * @param mainPanel
	 * @param key
	 * @return
	 */
	private JKTextField buildPropertyPanel(final JKPanel mainPanel, final String key) {
		final JKPanel pnl = new JKPanel();

		final JKTextField txt = new JKTextField(20);
		final JKButton btnEncode = new JKButton("Encode");
		final JKButton btnDecode = new JKButton("Decode");

		pnl.add(new JKLabledComponent(key, txt));
		pnl.add(btnEncode);
		pnl.add(btnDecode);
		mainPanel.add(pnl);
		mainPanel.add(Box.createVerticalStrut(3));
		this.components.put(key, txt);
		btnEncode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String result = CommonsConfigManager.encode(txt.getText());
				txt.setText(result);
			}
		});

		btnDecode.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final String result = CommonsConfigManager.decode(txt.getText());
				txt.setText(result);
			}
		});
		return txt;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		final JKPanel panel = new JKPanel();
		panel.add(this.btnSave);
		panel.add(this.btnReload);
		panel.add(this.btnAddProperty);
		return panel;
	}

	/**
	 *
	 */
	protected void handleAddProperyt() {
		final String property = SwingUtility.showInputDialog("Enter new property name");
		if (property != null && !property.equals("")) {
			buildPropertyPanel(this.mainPanel, property);
			SwingUtility.packJFrameWindow(this);
		}
	}

	/**
	 *
	 */
	protected void handleSave() {
		final Enumeration keys = this.components.keys();
		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			final String value = this.components.get(key).getValue();
			this.configManager.setProperty(key, value);
		}
		try {
			final int choice = this.chooser.showSaveDialog(null);
			if (choice == JFileChooser.APPROVE_OPTION) {
				final String fileName = this.chooser.getSelectedFile().getAbsolutePath();
				this.configManager.storeToXML(fileName);
				SwingUtility.showSuccessDialog("File has been saved succesfully");
			}
		} catch (final Exception e) {
			SwingUtility.showUserErrorDialog("Error saving file:" + e.getMessage(), e);
		}
	}

	/**
	 *
	 */
	private void init() {
		setBorder(BorderFactory.createRaisedBevelBorder());
		final JKPanel containerPanel = new JKPanel();
		containerPanel.setBorder(SwingUtility.createTitledBorder("Config Properties"));
		containerPanel.setLayout(new BorderLayout());
		this.mainPanel = new JKPanel();
		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
		final Properties prop = this.configManager.getProperties();
		final Enumeration keys = prop.propertyNames();
		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			prop.getProperty(key);
			buildPropertyPanel(this.mainPanel, key);
		}
		containerPanel.add(this.mainPanel, BorderLayout.CENTER);
		containerPanel.add(getButtonsPanel(), BorderLayout.SOUTH);

		add(containerPanel);

		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.btnReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				populateData();
			}
		});
		this.btnAddProperty.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAddProperyt();
			}
		});
	}

	/**
	 *
	 */
	protected void populateData() {
		final Enumeration keys = this.components.keys();
		while (keys.hasMoreElements()) {
			final String key = (String) keys.nextElement();
			final String originalValue = this.configManager.getProperty(key, "");
			this.components.get(key).setValue(originalValue);
		}
	}

	/**
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	// private JKPanel createComponent(String key, String value) {
	// JKPanel pnl=new JKPanel();
	// pnl.add(new JKLabledComponent(key,new JKTextField(20)));
	// return pnl;
	// }
}
