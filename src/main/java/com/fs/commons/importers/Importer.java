package com.fs.commons.importers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author mkiswani
 * 
 */
public abstract class Importer {

	protected InputStream selectedFile;

	public Importer(InputStream selectedFile) {
		this.selectedFile = selectedFile;
	}

	public Importer(File selectedFile) throws FileNotFoundException {
		this.selectedFile = new FileInputStream(selectedFile);
	}
	// //////////////////////////////////////////////////////////////////////////////////
	protected abstract boolean doImport() throws Exception;

	// //////////////////////////////////////////////////////////////////////////////////
	private List<ImportListener> listeners = new ArrayList<ImportListener>();
	private boolean isStopIfErrorOccuers = false;

	// //////////////////////////////////////////////////////////////////////////////////
	public boolean imporT() {
		try {
			fireStartImport();
			boolean doImport = doImport();
			fireEndImport();
			return doImport;
		} catch (Exception e) {
			onException(e);
			return false;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////
	protected void onException(Exception e) {
		for (ImportListener importListener : listeners) {
			importListener.onException(e, this);
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////
	protected void fireEndImport() {
		for (ImportListener importListener : listeners) {
			importListener.fireEndImport(this);
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////
	protected void fireStartImport() {
		for (ImportListener importListener : listeners) {
			importListener.fireStartImport(this);
		}
	}

	public void addListener(ImportListener importListener) {
		listeners.add(importListener);
	}

	public boolean isStopIfErrorOccuers() {
		return isStopIfErrorOccuers;
	}

	public void setStopIfErrorOccuers(boolean isStopIfErrorOccuers) {
		this.isStopIfErrorOccuers = isStopIfErrorOccuers;
	}

}
