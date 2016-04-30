package com.fs.commons.locale;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.fs.commons.application.AbstractModule;
import com.fs.commons.application.Module;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.util.GeneralUtility;

/**
 * 
 * @author mkiswani
 *
 */
public class FilesLablesLoader implements LablesLoader {

	private AbstractModule module;
	private int locale;
	
	@Override
	public void init(Module module, int locale) throws LablesLoaderException {
		this.module = (AbstractModule) module;
		this.locale = locale;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public List<Lable> getLables() throws LablesLoaderException {
		try {
			Properties lables;
			InputStream in = module.getLablesFile();
			if (in != null) {
				lables = GeneralUtility.readPropertyStream(in);
				return bindLables(lables);
			}
			return null;
		} catch (Exception e) {
			throw new LablesLoaderException(e);
		}

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private List<Lable> bindLables(Properties lables) throws RecordNotFoundException, DaoException {
		List<Lable> lablesList = new ArrayList<Lable>();
		Enumeration<Object> keys = lables.keys();
		while(keys.hasMoreElements()){
			String key = keys.nextElement().toString();
			Lable lable = new Lable();
			lable.setLableKey(key);
			lable.setLableValue(lables.getProperty(key));
			lable.setModuleId(module.getModuleId());
			lable.setLanguageId(Locale.valueOf(locale).getLanguageId());
			lablesList.add(lable);
		}
		return lablesList;
	}

}
