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
//////////////////////////////////////////////////////////////////

public class PnlImportDb extends PnlDbManagment {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) throws ServerDownException, DaoException, IOException {
		DataSourceFactory.setDefaultDataSource(new PoolingDataSource("system.config"));
		// user =SecurityManager.getCurrentUser();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.showPanelInDialog(new PnlImportDb(), "TEST");
	}

	public PnlImportDb() {
		super();
		this.btnBackup.setVisible(false);
	}
	// ////////////////////////////////////////////////////////////////
}
