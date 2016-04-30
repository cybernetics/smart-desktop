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
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class CommonsConfigFrame extends JFrame {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	JFileChooser chooser = SwingUtility.getDefaultFileChooser();
	JKButton btnLoad = new JKButton("Load");
	JKButton btnNew = new JKButton("New Config File");
	JKButton btnExit = new JKButton("Exit");
	JKPanel mainPanel;

	/**
	 *
	 */
	public CommonsConfigFrame() {
		init();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		final JKPanel pnl = new JKPanel();
		pnl.add(this.btnNew);
		pnl.add(this.btnLoad);
		pnl.add(this.btnExit);
		return pnl;
	}

	/**
	 *
	 */
	protected void handleExit() {
		System.exit(0);
	}

	/**
	 *
	 */
	protected void handleLoad() {
		final int choice = this.chooser.showOpenDialog(this);
		if (choice == JFileChooser.APPROVE_OPTION) {
			final File file = this.chooser.getSelectedFile();
			if (file.isFile()) {
				try {
					final CommonsConfigManager configManager = new CommonsConfigManager(file.getAbsolutePath());
					final CommonsConfigPanel configPanel = new CommonsConfigPanel(configManager);
					showConfigPanel(configPanel);
				} catch (final IOException e) {
					SwingUtility.showErrorDialog(e.getMessage(), e);
				}
			} else {
				SwingUtility.showUserErrorDialog("Only normal files are allowed");
			}
		}
	}

	/**
	 *
	 */
	protected void handleNew() {
		final CommonsConfigManager configManager = new CommonsConfigManager();
		final CommonsConfigPanel configPanel = new CommonsConfigPanel(configManager);
		showConfigPanel(configPanel);
	}

	/**
	 *
	 */
	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(getButtonsPanel(), BorderLayout.SOUTH);
		packWindow();
		this.btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleNew();
			}
		});
		this.btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleLoad();
			}
		});
		this.btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleExit();
			}
		});
	}

	/**
	 *
	 */
	private void packWindow() {
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 *
	 * @param configPanel
	 */
	private void showConfigPanel(final CommonsConfigPanel configPanel) {
		if (this.mainPanel != null) {
			remove(this.mainPanel);
		}
		this.mainPanel = configPanel;
		add(configPanel, BorderLayout.CENTER);
		validate();
		repaint();
		packWindow();
	}
}
