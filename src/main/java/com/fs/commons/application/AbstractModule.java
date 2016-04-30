package com.fs.commons.application;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.xml.ModuleMenuXmlParser;
import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXmlParser;
import com.fs.commons.locale.FilesLablesLoader;
import com.fs.commons.locale.LablesLoader;
import com.fs.commons.locale.LablesLoaderException;
import com.fs.commons.locale.Locale;
import com.fs.commons.reports.Report;
import com.fs.commons.reports.ReportManager;
import com.fs.commons.security.Privilige;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public abstract class AbstractModule implements Module {
	String moduleName;
	String configFilePath;
	// int priviligeId;
	String iconName;
	private ArrayList<Menu> menu;
	private boolean defaultModule;
	private DataSource dataSource;
	private Application application;
	private LablesLoader lablesLoader = new FilesLablesLoader();
	private int moduleId;

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return iconName;
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @return the priviligeId
	 */
	public Privilige getPrivilige() {
		return new Privilige(getModuleName().hashCode(), getModuleName(), null);
	}

	// /**
	// * @param priviligeId the priviligeId to set
	// */
	// public void setPriviligeId(int priviligeId) {
	// this.priviligeId = priviligeId;
	// }

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param moduleName
	 *            the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * 
	 */
	public List<Lable> getLables(String locale) throws ModuleException {
		try {
			LablesLoader lablesLoader2 = getLablesLoader();
			lablesLoader2.init(this, Locale.valueOf(locale).getLanguageId());
			List<Lable> lables = lablesLoader2.getLables();
			if (lables == null) {
				return new ArrayList<Lable>();
			}
			return lables;
		} catch (LablesLoaderException e) {
			throw new ModuleException(this, e);
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	public InputStream getLablesFile() {
		String shortFileName = application.getLocale() + "_lbl.properties";
		InputStream in = this.getClass().getResourceAsStream(getFileFullPath(shortFileName));
		return in;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////

	public LablesLoader getLablesLoader() {
		return lablesLoader;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	public void setLablesLoader(LablesLoader lablesLoader) {
		this.lablesLoader = lablesLoader;
	}

	/**
	 * 
	 */
	public ArrayList<Menu> getMenu() {
		try {
			if (menu != null) {
				return menu;
			}
			String fileFullPath = getFileFullPath("menu.xml");
			System.out.println(fileFullPath);
			InputStream in = GeneralUtility.getFileInputStream(fileFullPath);
			if (in != null) {
				ModuleMenuXmlParser p = new ModuleMenuXmlParser(this);
				menu = p.parse(in);
			} else {
				System.err.println("No menu.xml found for module : " + getModuleName());
				menu = new ArrayList<Menu>();
			}
			return menu;
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
			// unreachable
			return null;
		}
	}

	/**
	 * 
	 */
	public Hashtable<String, TableMeta> getTablesMeta() throws ModuleException {
		try {
			TableMetaXmlParser parser = new TableMetaXmlParser();
			Hashtable<String, TableMeta> tables;
			InputStream in = this.getClass().getResourceAsStream(getFileFullPath("meta.xml"));
			if (in != null) {
				tables = parser.parse(in,this.getModuleName());
				return tables;
			}
			System.err.println("meta.xml not found in module in :" + getModuleName());
			return new Hashtable<String, TableMeta>();
		} catch (JKXmlException e) {
			throw new ModuleException(this, e);
		}
	}

	/**
	 * @param shortFileName
	 * @return
	 */
	public String getFileFullPath(String shortFileName) {
		if (configFilePath != null) {
			return configFilePath + "/" + shortFileName;
		}
		String packageName = "/" + this.getClass().getPackage().getName();
		packageName = packageName.replaceAll("\\.", "/");
		return packageName + "/meta/" + shortFileName;
	}

	/**
	 * 
	 */
	public void init() throws ModuleException {
	}

	/**
	 * 
	 */
	public ArrayList<Report> getReports(String prefix, String prefix2) throws ModuleException {
		try {
			InputStream in = this.getClass().getResourceAsStream(getFileFullPath("reports.xml"));
			if (in != null) {
				ReportManager report = new ReportManager(in, prefix, prefix2);
				return report.getInstanceReports();
			}
			return new ArrayList<Report>();
		} catch (JKXmlException e) {
			throw new ModuleException(this, e);
		}
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		return buf.toString();
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

	/**
	 * @return the configFilePath
	 */
	public String getConfigFilePath() {
		return configFilePath;
	}

	/**
	 * @param configFilePath
	 *            the configFilePath to set
	 */
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	@Override
	public boolean isDefault() {
		return defaultModule;
	}

	@Override
	public void setDefault(boolean defaultModule) {
		this.defaultModule = defaultModule;
	}

	@Override
	public DataSource getDataSource() {
		if (dataSource == null) {
			return DataSourceFactory.getDefaultDataSource();
		}
		return dataSource;
	}

	@Override
	public void setDataSource(DataSource datasource) {
		this.dataSource = datasource;
	}

	@Override
	public void setApplication(Application application) {
		this.application = application;
	}

	@Override
	public Application getApplication() {
		return application;
	}

	// public int getModuleId() {
	// return priviligeId;
	// }
	@Override
	public int getModuleId() {
		return moduleId;
	}

	@Override
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}
}
