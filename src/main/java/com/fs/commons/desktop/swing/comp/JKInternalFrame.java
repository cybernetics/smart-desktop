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

	public JKInternalFrame(String title) throws HeadlessException {
		super(title);
		init();
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

	@Override
	public void setTitle(String text) {
		super.setTitle(Lables.get(text));
	}

	public void setRightToLeft() {
		applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	}

	public void addWindowListener(WindowAdapter windowAdapter) {
		// TODO : handle me
	}

	public void setLocationRelativeTo(Object object) {
		// TODO :handle me

	}

	public void setIconImage(Image image) {
		// TODO : handle me

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
	public void setDataSource(DataSource manager) {
		this.connectionManager = manager;
		applyDataSource();
	}

	@Override
	public DataSource getDataSource() {
		if (connectionManager == null) {
			return DataSourceFactory.getDefaultDataSource();
		}
		return connectionManager;
	}

	public void applyDataSource() {
		SwingUtility.applyDataSource(this, connectionManager);
	}
}
