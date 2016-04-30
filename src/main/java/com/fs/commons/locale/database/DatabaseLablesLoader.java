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
package com.fs.commons.locale.database;

import java.io.InputStream;
import java.util.List;

import com.fs.commons.application.AbstractModule;
import com.fs.commons.application.Module;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.configuration.daos.LablesDao;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.importers.ImportListener;
import com.fs.commons.importers.Importer;
import com.fs.commons.locale.LablesLoader;
import com.fs.commons.locale.LablesLoaderException;
import com.fs.commons.locale.database.importer.ui.LablesImporter;
import com.fs.commons.logging.Logger;

/**
 *
 * @author mkiswani
 *
 */
public class DatabaseLablesLoader implements LablesLoader {
	private AbstractModule module;
	private int locale;

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Lable> getLables() throws LablesLoaderException {
		try {
			final LablesDao dao = new LablesDao();
			return dao.listModuleLabels(this.module.getModuleId(), this.locale);
		} catch (final DaoException e) {
			throw new LablesLoaderException(e);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void init(final Module module, final int locale) throws LablesLoaderException {
		this.module = (AbstractModule) module;
		this.locale = locale;
		processModuleLables();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private void processModuleLables() throws LablesLoaderException {
		final InputStream moduleLablesFile = this.module.getLablesFile();
		try {
			if (moduleLablesFile == null) {
				return;
			}
			final LablesImporter importer = new LablesImporter(this.module.getModuleId(), moduleLablesFile, this.locale);
			importer.addListener(new ImportListener() {
				String moduleName = DatabaseLablesLoader.this.module.getModuleName();

				@Override
				public void fireEndImport(final Importer importer) {
					Logger.info("Number Of Added Lables is : " + ((LablesImporter) importer).getNumberOfAddedLables());
					Logger.info("Import " + this.moduleName + " Lables Finished ");
				}

				@Override
				public void fireStartImport(final Importer importer) {
					Logger.info("Import " + this.moduleName + " Lables Started ");
				}

				@Override
				public void onException(final Exception e, final Importer importer) {
					Logger.fatal("Error occuers while importing " + this.moduleName + " lables possabile reson : " + e.getMessage());
				}
			});
			importer.imporT();
		} catch (final Exception e) {
			throw new LablesLoaderException(e);
		}
	}
}
