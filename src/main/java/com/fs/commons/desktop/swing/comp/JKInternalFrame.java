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
package com.fs.commons.desktop.swing.comp;

import java.awt.ComponentOrientation;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.beans.PropertyVetoException;

import javax.swing.JInternalFrame;

import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;

public class JKInternalFrame extends JInternalFrame implements UIPanel, DaoComponent {

	private static final long serialVersionUID = 675269917755097366L;
	private static final int DEFAULT_HEIGHT = 400;
	private static final int DEFAULT_WIDTH = 400;
	private DataSource connectionManager;

	public JKInternalFrame() throws HeadlessException {
		super();
		init();
	}

	public JKInternalFrame(final String title) throws HeadlessException {
		super(title);
		init();
	}

	public void addWindowListener(final WindowAdapter windowAdapter) {
		// TODO : handle me
	}

	public void applyDataSource() {
		SwingUtility.applyDataSource(this, this.connectionManager);
	}

	@Override
	public DataSource getDataSource() {
		if (this.connectionManager == null) {
			return DataSourceFactory.getDefaultDataSource();
		}
		return this.connectionManager;
	}

	private void init() {
		setOpaque(true);
		getContentPane().setBackground(Colors.MAIN_PANEL_BG);
		setBackground(Colors.MAIN_PANEL_BG);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		// setExtendedState(MAXIMIZED_BOTH);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		// setLocationRelativeTo(null);
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
	}

	public void initDefaults() throws PropertyVetoException {
		setVisible(true);
		setMaximum(true);
		setMaximizable(true);
		setClosable(true);
		setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
		setIconifiable(true);
	}

	@Override
	public void setDataSource(final DataSource manager) {
		this.connectionManager = manager;
		applyDataSource();
	}

	public void setIconImage(final Image image) {
		// TODO : handle me

	}

	public void setLocationRelativeTo(final Object object) {
		// TODO :handle me

	}

	public void setRightToLeft() {
		applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	@Override
	public void setTitle(final String text) {
		super.setTitle(Lables.get(text));
	}
}
