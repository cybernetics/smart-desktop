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
package com.fs.commons.locale.database.importer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.configuration.daos.LablesDao;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.importers.Importer;
import com.fs.commons.util.FSProperties;

/**
 *
 * @author mkiswani
 *
 */
public class LablesImporter extends Importer {

	private final LablesDao dao = new LablesDao();
	private FSProperties lables;
	private int moduleId;
	private int langId;
	private int numberOfAddedLables;

	// /////////////////////////////////////////////////////////////////////////////////////
	public LablesImporter(final File selectedFile) throws IOException, ValidationException {
		super(selectedFile);
		this.lables = new FSProperties(selectedFile);
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public LablesImporter(final int moduleId, final File selectedFile, final int langId) throws JKDataAccessException, IOException, ValidationException {
		super(new FileInputStream(selectedFile));
		this.moduleId = moduleId;
		this.langId = langId;
	}

	public LablesImporter(final int moduleId, final InputStream selectedFile, final int langId) {
		super(selectedFile);
		this.moduleId = moduleId;
		this.langId = langId;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private boolean addLables() {
		final Enumeration<Object> keys = this.lables.keys();
		while (keys.hasMoreElements()) {
			if (isStopIfErrorOccuers()) {
				return false;
			}
			final String key = keys.nextElement().toString();
			try {
				if (!this.dao.isLableExist(key)) {
					this.dao.addLable(populateLable(key, this.lables.get(key).toString()));
					this.numberOfAddedLables++;
				}
			} catch (final Exception e) {
				onException(e);
			}
		}
		return true;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected boolean doImport() throws Exception {
		return addLables();
	}

	public int getLangId() {
		return this.langId;
	}

	public int getModuleId() {
		return this.moduleId;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public int getNumberOfAddedLables() {
		return this.numberOfAddedLables;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private Lable populateLable(final String key, final String value) {
		final Lable lable = new Lable();
		lable.setLableKey(key);
		lable.setLableValue(value);
		lable.setLanguageId(this.langId);
		lable.setModuleId(this.moduleId);
		return lable;
	}

	public void setLangId(final int langId) {
		this.langId = langId;
	}

	public void setModuleId(final int moduleId) {
		this.moduleId = moduleId;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void setNumberOfAddedLables(final int numberOfAddedLables) {
		this.numberOfAddedLables = numberOfAddedLables;
	}

}
