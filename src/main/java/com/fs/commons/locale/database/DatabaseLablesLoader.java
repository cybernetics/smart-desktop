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
public class DatabaseLablesLoader implements LablesLoader{
	private AbstractModule module;
	private int locale;

	////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void init(Module module, int locale) throws LablesLoaderException {
		this.module = (AbstractModule) module;
		this.locale = locale;
		processModuleLables();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Lable> getLables() throws LablesLoaderException {
		try {
			LablesDao dao = new LablesDao();
			return dao.listModuleLabels(module.getModuleId(), locale);
		} catch (DaoException e) {
			throw new LablesLoaderException(e);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////
	private void processModuleLables() throws LablesLoaderException {
		InputStream moduleLablesFile = module.getLablesFile();
		try {
			if(moduleLablesFile == null){
				return;
			}
			LablesImporter importer = new LablesImporter(module.getModuleId(),moduleLablesFile, locale );
			importer.addListener(new ImportListener() {
				String moduleName = module.getModuleName();
				@Override
				public void onException(Exception e, Importer importer) {
					Logger.fatal("Error occuers while importing " + moduleName + " lables possabile reson : "+e.getMessage());
				}
				
				@Override
				public void fireStartImport(Importer importer) {
					Logger.info("Import " + moduleName +" Lables Started ");
				}
				
				@Override
				public void fireEndImport(Importer importer) {
					Logger.info("Number Of Added Lables is : " + ((LablesImporter)importer).getNumberOfAddedLables());
					Logger.info("Import " + moduleName +" Lables Finished ");
				}
			});
			importer.imporT();
		} catch (Exception e) {
			throw new LablesLoaderException(e);
		}
	}
}
