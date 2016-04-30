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

	// //////////////////////////////////////////////////////////////////////////////////
	private final List<ImportListener> listeners = new ArrayList<ImportListener>();

	private boolean isStopIfErrorOccuers = false;

	public Importer(final File selectedFile) throws FileNotFoundException {
		this.selectedFile = new FileInputStream(selectedFile);
	}

	public Importer(final InputStream selectedFile) {
		this.selectedFile = selectedFile;
	}

	public void addListener(final ImportListener importListener) {
		this.listeners.add(importListener);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	protected abstract boolean doImport() throws Exception;

	// //////////////////////////////////////////////////////////////////////////////////
	protected void fireEndImport() {
		for (final ImportListener importListener : this.listeners) {
			importListener.fireEndImport(this);
		}

	}

	// //////////////////////////////////////////////////////////////////////////////////
	protected void fireStartImport() {
		for (final ImportListener importListener : this.listeners) {
			importListener.fireStartImport(this);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public boolean imporT() {
		try {
			fireStartImport();
			final boolean doImport = doImport();
			fireEndImport();
			return doImport;
		} catch (final Exception e) {
			onException(e);
			return false;
		}
	}

	public boolean isStopIfErrorOccuers() {
		return this.isStopIfErrorOccuers;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	protected void onException(final Exception e) {
		for (final ImportListener importListener : this.listeners) {
			importListener.onException(e, this);
		}

	}

	public void setStopIfErrorOccuers(final boolean isStopIfErrorOccuers) {
		this.isStopIfErrorOccuers = isStopIfErrorOccuers;
	}

}
