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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.border.Border;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.importers.ImportListener;
import com.fs.commons.importers.ImportListenerAdapter;
import com.fs.commons.importers.Importer;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.ReflicationUtil;

/**
 *
 * @author mkiswani
 *
 */
public class PnlImport extends JKPanel {
	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	protected class ImportListenerImp extends ImportListenerAdapter {
		@Override
		public void onException(final Exception e, final Importer importer) {
			final boolean countine = SwingUtility
					.showConfirmationDialog("ERROR_HAPPENED_WHILE_IMPORTING:POSSIBLE " + "REASON" + e.getMessage() + " DO_YOU_WANT_TO_CONTINUE");
			if (!countine) {
				importer.setStopIfErrorOccuers(true);
			}
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 8981633765691991651L;
	protected JKFilePanel filePanel = new JKFilePanel();
	protected JKButton btnImport = new JKButton("IMPORT", "", true);
	protected Collection<ImportListener> listeners = new ArrayList<ImportListener>();
	private final String pnlTitle;

	private final Class importerClass;

	public PnlImport(final String pnlTitle, final Class importerClass, final String... extensions) {
		this.pnlTitle = pnlTitle;
		this.importerClass = importerClass;
		this.filePanel.setExtensions(extensions);
		init();
	}

	public PnlImport(final String pnlTitle, final Class importerClass, final String allowedExtensions) {
		this(pnlTitle, importerClass, allowedExtensions.split(","));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addListener(final ImportListener listener) {
		this.listeners.add(listener);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	protected JKPanel getBtnsPnl() {
		final JKPanel pnl = new JKPanel();
		pnl.add(this.btnImport);
		return pnl;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	protected JKPanel getCenterPnl() {
		final JKPanel pnl = new JKPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.LINE_AXIS));
		pnl.add(new JKLabledComponent("FILE", this.filePanel));
		pnl.add(this.btnImport);
		return pnl;
	}

	public JKFilePanel getFilePanel() {
		return this.filePanel;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public Collection<ImportListener> getListeners() {
		return this.listeners;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	protected JKPanel getMainPnl() {
		final JKPanel jkPanel = new JKPanel();
		setContainerPnlPreferedSize(jkPanel);
		final Border createTitledBorder = SwingUtility.createTitledBorder(this.pnlTitle);
		jkPanel.setBorder(createTitledBorder);
		jkPanel.setLayout(new BorderLayout());
		jkPanel.add(getCenterPnl(), BorderLayout.NORTH);
		this.btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				handleImport();

			}
		});
		return jkPanel;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	protected void handleImport() {
		try {
			validateFields();
			final File selectedFile = this.filePanel.getSelectedFile();
			final boolean result = SwingUtility.showConfirmationDialog(
					Lables.get("YOU_ARE_TRYING_TO_IMPORT") + "\n" + selectedFile.getAbsolutePath() + "\n" + Lables.get("ARE_YOU_SURE"));
			if (result) {
				handleImport(selectedFile);
			}
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	protected void handleImport(final File selectedFile) throws Exception {
		final File file = new File(selectedFile.getAbsolutePath());// Weird case
																	// filechooser
																	// obj
																	// returns a
																	// subclass
																	// of File
																	// which is
		// Win32Shell obj and i need and io.File object to call the importer
		// class
		// By using the reflection
		final ReflicationUtil<Importer> util = new ReflicationUtil<Importer>();
		final Importer importer2 = util.newInstance(this.importerClass, file);
		importer2.addListener(new ImportListenerImp());
		for (final ImportListener importListener : this.listeners) {
			importer2.addListener(importListener);
		}
		if (importer2.imporT()) {
			SwingUtility.showSuccessDialog("IMPORTED_SUCC");
		} else {
			SwingUtility.showSuccessDialog("IMPORTED_FAILED");
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	private void init() {
		final JKPanel jkPanel = getMainPnl();
		this.btnImport.setIcon("import.png");
		add(jkPanel);
	}

	protected void setContainerPnlPreferedSize(final JKPanel jkPanel) {
		jkPanel.setPreferredSize(650, 70);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setFilePanel(final JKFilePanel filePanel) {
		this.filePanel = filePanel;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setListeners(final Collection<ImportListener> listeners) {
		this.listeners = listeners;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void validateFields() throws ValidationException {
		getFilePanel().checkFields();
	}

}
