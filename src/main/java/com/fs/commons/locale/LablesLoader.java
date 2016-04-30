package com.fs.commons.locale;

import java.util.List;

import com.fs.commons.application.Module;
import com.fs.commons.configuration.beans.Lable;

/**
 * 
 * @author mkiswani
 *
 */
public interface LablesLoader {
	public void init(Module module,int locale) throws LablesLoaderException;
	public List<Lable> getLables() throws LablesLoaderException;
}
