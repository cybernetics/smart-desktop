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
package com.fs.commons.locale;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.fs.commons.application.AbstractModule;
import com.fs.commons.application.Module;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.util.GeneralUtility;

/**
 *
 * @author mkiswani
 *
 */
public class FilesLablesLoader implements LablesLoader {

	private AbstractModule module;
	private int locale;

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private List<Lable> bindLables(final Properties lables) throws JKRecordNotFoundException, JKDataAccessException {
		final List<Lable> lablesList = new ArrayList<Lable>();
		final Enumeration<Object> keys = lables.keys();
		while (keys.hasMoreElements()) {
			final String key = keys.nextElement().toString();
			final Lable lable = new Lable();
			lable.setLableKey(key);
			lable.setLableValue(lables.getProperty(key));
			lable.setModuleId(this.module.getModuleId());
			lable.setLanguageId(Locale.valueOf(this.locale).getLanguageId());
			lablesList.add(lable);
		}
		return lablesList;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Lable> getLables() throws LablesLoaderException {
		try {
			Properties lables;
			final InputStream in = this.module.getLablesFile();
			if (in != null) {
				lables = GeneralUtility.readPropertyStream(in);
				return bindLables(lables);
			}
			return null;
		} catch (final Exception e) {
			throw new LablesLoaderException(e);
		}

	}

	@Override
	public void init(final Module module, final int locale) throws LablesLoaderException {
		this.module = (AbstractModule) module;
		this.locale = locale;
	}

}
