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
package com.fs.commons.application;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.locale.LablesLoader;
import com.fs.commons.reports.JKReport;
import com.jk.security.JKPrivilige;

public interface Module {

	/**
	 *
	 * @return
	 */
	public Application getApplication();

	/**
	 *
	 * @return
	 */
	public JKDataSource getDataSource();

	/**
	 *
	 * @return
	 * @throws ModuleException
	 */
	public String getFileFullPath(String shortFileName);

	/**
	 *
	 * @return
	 */
	public String getIconName();

	/**
	 *
	 * @return
	 * @throws ModuleException
	 */
	public List<Lable> getLables(String locale) throws ModuleException;

	/**
	 *
	 * @return
	 */
	public LablesLoader getLablesLoader();

	/**
	 *
	 * @return
	 * @throws ModuleException
	 */
	public ArrayList<Menu> getMenu();

	public int getModuleId();

	/**
	 *
	 * @return
	 */
	public String getModuleName();

	/**
	 *
	 * @return
	 */
	public JKPrivilige getPrivilige();

	/**
	 *
	 * @param string
	 * @return
	 * @throws ModuleException
	 */
	public ArrayList<JKReport> getReports(String prefix, String prefix2) throws ModuleException;

	/**
	 *
	 * @param privligeId
	 */
	// public void setPriviligeId(int privligeId);

	/**
	 *
	 * @return
	 * @throws ModuleException
	 */
	public Hashtable<String, TableMeta> getTablesMeta() throws ModuleException;

	/**
	 *
	 * @throws ModuleException
	 */
	public void init() throws ModuleException;

	/**
	 *
	 * @return
	 */
	public boolean isDefault();

	/**
	 *
	 * @param application
	 */
	public void setApplication(Application application);

	/**
	 *
	 * @param connectionManager
	 */
	public void setDataSource(JKDataSource connectionManager);

	/**
	 *
	 * @param defaultModule
	 */
	public void setDefault(boolean defaultModule);

	/**
	 *
	 * @param lablesLoader
	 */
	public void setLablesLoader(LablesLoader lablesLoader);

	public void setModuleId(int moduleId);

	/*
	 *
	 */
	public void setModuleName(String moduleName);

}
