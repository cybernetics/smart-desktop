package com.fs.security.ui.database;

import java.awt.ComponentOrientation;
import java.io.IOException;

import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.connection.PoolingDataSource;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
//////////////////////////////////////////////////////////////////////
//Author Mohamed Kiswani
//since  3-2-2010
// becuase we need to sperate menu items (privilege issue)
//////////////////////////////////////////////////////////////////
public class PnlDbBackup extends PnlDbManagment{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PnlDbBackup() {
		super();
		btnImport.setVisible(false);
	}
	
	// //////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws ServerDownException, DaoException, IOException {
		DataSourceFactory.setDefaultDataSource(new PoolingDataSource("system.config"));
		// user =SecurityManager.getCurrentUser();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.showPanelInDialog(new PnlDbBackup(), "TEST");
	}
}
