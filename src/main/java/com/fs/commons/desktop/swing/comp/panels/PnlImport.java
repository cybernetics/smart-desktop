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
	protected JKFilePanel filePanel = new JKFilePanel();
	protected JKButton btnImport = new JKButton("IMPORT","",true);
	protected Collection<ImportListener> listeners = new ArrayList<ImportListener>();
	private String pnlTitle;
	private Class importerClass;

	public PnlImport(String pnlTitle,Class importerClass, String... extensions) {
		this.pnlTitle = pnlTitle;
		this.importerClass = importerClass;
		filePanel.setExtensions(extensions);
		init();
	}

	public PnlImport(String pnlTitle, Class importerClass, String allowedExtensions) {
		this(pnlTitle, importerClass , allowedExtensions.split(","));
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	private void init() {
		JKPanel jkPanel = getMainPnl();
		btnImport.setIcon("import.png");
		add(jkPanel);
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	protected JKPanel getMainPnl() {
		JKPanel jkPanel = new JKPanel();
		setContainerPnlPreferedSize(jkPanel);
		Border createTitledBorder = SwingUtility.createTitledBorder(pnlTitle);
		jkPanel.setBorder(createTitledBorder);
		jkPanel.setLayout(new BorderLayout());
		jkPanel.add(getCenterPnl(), BorderLayout.NORTH);
		btnImport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				handleImport();

			}
		});
		return jkPanel;
	}

	protected void setContainerPnlPreferedSize(JKPanel jkPanel) {
		jkPanel.setPreferredSize(650, 70);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	protected void handleImport() {
		try {
			validateFields();
			final File selectedFile = filePanel.getSelectedFile();
			boolean result = SwingUtility.showConfirmationDialog(Lables.get("YOU_ARE_TRYING_TO_IMPORT")+ "\n" + selectedFile.getAbsolutePath() + "\n" + Lables.get("ARE_YOU_SURE"));
			if (result) {
				handleImport(selectedFile);
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	protected void handleImport(final File selectedFile) throws Exception {
		File file = new File(selectedFile.getAbsolutePath());// Weird case filechooser obj returns a subclass of File which is
					 //Win32Shell obj and i need and io.File object to call the importer class
					 //By using the reflection  
		ReflicationUtil<Importer> util = new ReflicationUtil<Importer>();
		Importer importer2 = util.newInstance(importerClass, file);
		importer2.addListener(new ImportListenerImp());
		for (ImportListener importListener : listeners) {
			importer2.addListener(importListener);
		}
		if (importer2.imporT()) {
			SwingUtility.showSuccessDialog("IMPORTED_SUCC");
		} else {
			SwingUtility.showSuccessDialog("IMPORTED_FAILED");
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	protected class ImportListenerImp extends ImportListenerAdapter {
		@Override
		public void onException(Exception e, Importer importer) {
			boolean countine = SwingUtility.showConfirmationDialog("ERROR_HAPPENED_WHILE_IMPORTING:POSSIBLE " 
																	+ "REASON" 
																	+ e.getMessage()
																	+ " DO_YOU_WANT_TO_CONTINUE");
			if (!countine) {
				importer.setStopIfErrorOccuers(true);
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	protected JKPanel getCenterPnl() {
		JKPanel pnl = new JKPanel();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.LINE_AXIS));
		pnl.add(new JKLabledComponent("FILE", filePanel));
		pnl.add(btnImport);
		return pnl;
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	protected JKPanel getBtnsPnl() {
		JKPanel pnl = new JKPanel();
		pnl.add(btnImport);
		return pnl;
	}

	public JKFilePanel getFilePanel() {
		return filePanel;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setFilePanel(JKFilePanel filePanel) {
		this.filePanel = filePanel;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public Collection<ImportListener> getListeners() {
		return listeners;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void setListeners(Collection<ImportListener> listeners) {
		this.listeners = listeners;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addListener(ImportListener listener) {
		this.listeners.add(listener);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void validateFields() throws ValidationException {
		getFilePanel().checkFields();
	}
	
	
}
