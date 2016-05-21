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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.xml.ModuleMenuXmlParser;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXmlParser;
import com.fs.commons.locale.FilesLablesLoader;
import com.fs.commons.locale.Lables;
import com.fs.commons.locale.LablesLoader;
import com.fs.commons.locale.LablesLoaderException;
import com.fs.commons.locale.Locale;
import com.fs.commons.reports.JKReport;
import com.fs.commons.reports.JKReportManager;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;
import com.jk.security.JKPrivilige;
import com.jk.security.JKSecurityManager;

public abstract class AbstractModule implements Module {
	JKLogger logger = JKLoggerFactory.getLogger(getClass());
	String moduleName;
	String configFilePath;
	// int priviligeId;
	String iconName;
	private ArrayList<Menu> menu;
	private boolean defaultModule;
	private JKDataSource dataSource;
	private Application application;
	private LablesLoader lablesLoader = new FilesLablesLoader();
	private int moduleId;

	@Override
	public Application getApplication() {
		return this.application;
	}

	/**
	 * @return the configFilePath
	 */
	public String getConfigFilePath() {
		return this.configFilePath;
	}

	@Override
	public JKDataSource getDataSource() {
		if (this.dataSource == null) {
			return JKDataSourceFactory.getDefaultDataSource();
		}
		return this.dataSource;
	}

	// /**
	// * @param priviligeId the priviligeId to set
	// */
	// public void setPriviligeId(int priviligeId) {
	// this.priviligeId = priviligeId;
	// }

	/**
	 * @param shortFileName
	 * @return
	 */
	@Override
	public String getFileFullPath(final String shortFileName) {
		if (this.configFilePath != null) {
			return this.configFilePath + "/" + shortFileName;
		}
		String packageName = "/" + this.getClass().getPackage().getName();
		packageName = packageName.replaceAll("\\.", "/");
		return packageName + "/meta/" + shortFileName;
	}

	/**
	 * @return the iconName
	 */
	@Override
	public String getIconName() {
		return this.iconName;
	}

	/**
	 *
	 */
	@Override
	public List<Lable> getLables(final String locale) throws ModuleException {
		try {
			final LablesLoader lablesLoader2 = getLablesLoader();
			lablesLoader2.init(this, Locale.valueOf(locale).getLanguageId());
			final List<Lable> lables = lablesLoader2.getLables();
			if (lables == null) {
				return new ArrayList<Lable>();
			}
			return lables;
		} catch (final LablesLoaderException e) {
			throw new ModuleException(this, e);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	public InputStream getLablesFile() {
		final String shortFileName = this.application.getLocale() + "_lbl.properties";
		final InputStream in = this.getClass().getResourceAsStream(getFileFullPath(shortFileName));
		return in;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////

	@Override
	public LablesLoader getLablesLoader() {
		return this.lablesLoader;
	}

	/**
	 *
	 */
	@Override
	public ArrayList<Menu> getMenu() {
		try {
			if (this.menu != null) {
				return this.menu;
			}
			final String fileFullPath = getFileFullPath("menu.xml");
			final InputStream in = GeneralUtility.getFileInputStream(fileFullPath);
			if (in != null) {
				final ModuleMenuXmlParser p = new ModuleMenuXmlParser(this);
				this.menu = p.parse(in);
			} else {
				logger.debug("No menu.xml found for module : ", getModuleName());
				this.menu = new ArrayList<Menu>();
			}
			return this.menu;
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
			// unreachable
			return null;
		}
	}

	// public int getModuleId() {
	// return priviligeId;
	// }
	@Override
	public int getModuleId() {
		return this.moduleId;
	}

	/**
	 * @return the moduleName
	 */
	@Override
	public String getModuleName() {
		return this.moduleName;
	}

	/**
	 * @return the priviligeId
	 */
	@Override
	public JKPrivilige getPrivilige() {
		return JKSecurityManager.createPrivilige(Lables.get(getModuleName(), true), null);
	}

	/**
	 *
	 */
	@Override
	public ArrayList<JKReport> getReports(final String prefix, final String prefix2) throws ModuleException {
		try {
			final InputStream in = this.getClass().getResourceAsStream(getFileFullPath("reports.xml"));
			if (in != null) {
				final JKReportManager report = new JKReportManager(in, prefix, prefix2);
				return report.getInstanceReports();
			}
			return new ArrayList<JKReport>();
		} catch (final JKXmlException e) {
			throw new ModuleException(this, e);
		}
	}

	/**
	 *
	 */
	@Override
	public Hashtable<String, TableMeta> getTablesMeta() throws ModuleException {
		try {
			final TableMetaXmlParser parser = new TableMetaXmlParser();
			Hashtable<String, TableMeta> tables;
			final InputStream in = this.getClass().getResourceAsStream(getFileFullPath("meta.xml"));
			if (in != null) {
				tables = parser.parse(in, getModuleName());
				return tables;
			}
			logger.debug("meta.xml not found in module in :", getModuleName());
			return new Hashtable<String, TableMeta>();
		} catch (final JKXmlException e) {
			throw new ModuleException(this, e);
		}
	}

	/**
	 *
	 */
	@Override
	public void init() throws ModuleException {
	}

	@Override
	public boolean isDefault() {
		return this.defaultModule;
	}

	@Override
	public void setApplication(final Application application) {
		this.application = application;
	}

	/**
	 * @param configFilePath
	 *            the configFilePath to set
	 */
	public void setConfigFilePath(final String configFilePath) {
		this.configFilePath = configFilePath;
	}

	/**
	 *
	 * @param configFilePath
	 */
	public void setConfigPath(String configFilePath) {
		if (configFilePath != null) {
			configFilePath = configFilePath.replaceAll("\\.", "/");
			if (!configFilePath.startsWith("/")) {
				configFilePath = "/" + configFilePath;
			}
		}
		this.configFilePath = configFilePath;
	}

	@Override
	public void setDataSource(final JKDataSource datasource) {
		this.dataSource = datasource;
	}

	@Override
	public void setDefault(final boolean defaultModule) {
		this.defaultModule = defaultModule;
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(final String iconName) {
		this.iconName = iconName;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setLablesLoader(final LablesLoader lablesLoader) {
		this.lablesLoader = lablesLoader;
	}

	@Override
	public void setModuleId(final int moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	@Override
	public void setModuleName(final String moduleName) {
		this.moduleName = moduleName;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer();
		return buf.toString();
	}
}
