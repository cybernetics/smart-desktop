package com.fs.commons.locale.database.importer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.configuration.daos.LablesDao;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.importers.Importer;
import com.fs.commons.util.FSProperties;

/**
 * 
 * @author mkiswani
 *
 */
public class LablesImporter extends Importer {
	
	private LablesDao dao = new LablesDao();
	private FSProperties lables;
	private int moduleId;
	private int langId;
	private int numberOfAddedLables;

	// /////////////////////////////////////////////////////////////////////////////////////
	public LablesImporter(File selectedFile) throws IOException, ValidationException  {
		super(selectedFile);
		this.lables = new FSProperties(selectedFile);
	}
	
	// /////////////////////////////////////////////////////////////////////////////////////
	public LablesImporter(int moduleId, File selectedFile, int langId) throws DaoException, IOException, ValidationException {
		super(new FileInputStream(selectedFile));
		this.moduleId = moduleId;
		this.langId = langId;
	}

	public LablesImporter(int moduleId, InputStream selectedFile, int langId) {
		super(selectedFile);
		this.moduleId = moduleId;
		this.langId = langId;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected boolean doImport() throws Exception{
		return addLables();
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private boolean addLables()  {
		Enumeration<Object> keys = lables.keys();
		while (keys.hasMoreElements()) {
			if(isStopIfErrorOccuers()){
				return false;
			}
			String key = keys.nextElement().toString();
			try{
				if (!dao.isLableExist(key)) {
					dao.addLable(populateLable(key, lables.get(key).toString()));
					numberOfAddedLables ++ ;
				}
			}catch (Exception e) {
				onException(e);
			}
		}
		return true;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private Lable populateLable(String key, String value) {
		Lable lable = new Lable();
		lable.setLableKey(key);
		lable.setLableValue(value);
		lable.setLanguageId(langId);
		lable.setModuleId(moduleId);
		return lable;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public int getNumberOfAddedLables() {
		return numberOfAddedLables;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void setNumberOfAddedLables(int numberOfAddedLables) {
		this.numberOfAddedLables = numberOfAddedLables;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

}
