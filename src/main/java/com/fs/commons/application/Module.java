package com.fs.commons.application;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.locale.LablesLoader;
import com.fs.commons.reports.Report;
import com.fs.commons.security.Privilige;

public interface Module {

	/**
	 * 
	 * @throws ModuleException
	 */
	public void init()throws ModuleException;
	
	/**
	 * 
	 * @return
	 * @throws ModuleException
	 */
	public Hashtable<String, TableMeta> getTablesMeta()throws ModuleException;
	
	/**
	 * 
	 * @return
	 * @throws ModuleException
	 */
	public List<Lable>  getLables(String locale)throws ModuleException;
	
	/**
	 * 
	 * @return
	 */
	public LablesLoader getLablesLoader();
	
	/**
	 * 
	 * @param lablesLoader
	 */
	public void setLablesLoader(LablesLoader lablesLoader);

	/**
	 * 
	 * @return
	 * @throws ModuleException
	 */
	public String getFileFullPath(String shortFileName) ;
	/**
	 * 
	 * @return
	 * @throws ModuleException
	 */
	public ArrayList<Menu> getMenu();
	
	/**
	 * 
	 * @param string 
	 * @return
	 * @throws ModuleException
	 */
	public ArrayList<Report> getReports(String prefix, String prefix2)throws ModuleException;
	
	/**
	 * 
	 * @return
	 */
	public String getModuleName();

	/*
	 * 
	 */
	public void setModuleName(String moduleName);

	/**
	 * 
	 * @return
	 */
	public Privilige getPrivilige();
	
	/**
	 * 
	 * @param privligeId
	 */
//	public void setPriviligeId(int privligeId);

	/**
	 * 
	 * @return
	 */
	public String getIconName();

	/**
	 * 
	 * @param defaultModule
	 */
	public void setDefault(boolean defaultModule);
	
	/**
	 * 
	 * @return
	 */
	public boolean isDefault();
	
	/**
	 * 
	 * @return
	 */
	public DataSource getDataSource();
	
	/**
	 * 
	 * @param connectionManager
	 */
	public void setDataSource(DataSource connectionManager);

	/**
	 * 
	 * @return
	 */
	public Application getApplication();

	/**
	 * 
	 * @param application
	 */
	public void setApplication(Application application);

	public void setModuleId(int moduleId);
	public int getModuleId();

}
