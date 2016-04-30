package com.fs.commons.application;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import com.fs.commons.application.config.UserPreferences;
import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.application.listener.ApplicationListener;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.frames.ApplicationFrame;
import com.fs.commons.locale.Lables;
import com.fs.commons.reports.Report;
import com.fs.commons.reports.ReportManager;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class Application {
	private int autoLogoutInterval = 15;
	String applicationName;

	ArrayList<Module> modules;
	ArrayList<ApplicationListener> listeners = new ArrayList<ApplicationListener>();

	private String configFileName;

	// MainMenu mainMenu=new MainMenu();

	String splashImage;

	String homeImage;

	private String locale;

	private int applicationId = 8643;// just dummy value

	private ApplicationFrame applicationFrame;

	private boolean viewModules = true;

	// /**
	// * @return the mainMenu
	// */
	// public MainMenu getMainMenu() {
	// return mainMenu;
	// }

	/**
	 * @return the viewModules
	 */
	public boolean isViewModules() {
		return viewModules;
	}

	/**
	 * @return the splashImage
	 */
	public String getSplashImage() {
		return splashImage;
	}

	/**
	 * @param splashImage
	 *            the splashImage to set
	 */
	public void setSplashImage(String splashImage) {
		this.splashImage = splashImage;
	}

	/**
	 * @return the homeImage
	 */
	public String getHomeImage() {
		return homeImage;
	}

	/**
	 * @param homeImage
	 *            the homeImage to set
	 */
	public void setHomeImage(String homeImage) {
		this.homeImage = homeImage;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
		UserPreferences.setKeyPrefix(applicationName);
	}

	/**
	 * @return the modules
	 */
	public ArrayList<Module> getModules() {
		return modules;
	}

	/**
	 * @param modules
	 *            the modules to set
	 */
	public void setModules(ArrayList<Module> modules) {
		this.modules = modules;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Name : " + getApplicationName() + "\n");
		for (int i = 0; i < modules.size(); i++) {
			buf.append(modules.get(i).toString());
		}
		return buf.toString();
	}

	/**
	 * @throws ApplicationException
	 * 
	 */
	public void init() throws ApplicationException {
		try {
			Lables.getDefaultInstance().addLables(loadCommonsLabel(getLocale()));
			//boolean debugPriviliges = System.getProperty("fs.debug.priviliges") != null;
			boolean debugMeta = System.getProperty("fs.debug.meta") != null;
			for (int i = 0; i < modules.size(); i++) {
				Module module = modules.get(i);
				module.init();
				Hashtable<String, TableMeta> tablesMeta = module.getTablesMeta();
				TableMetaFactory tableMetaFactory = AbstractTableMetaFactory.addTablesMeta(module.getDataSource(), tablesMeta);
				Lables.getDefaultInstance().addLables(module.getLables(getLocale()));
				// MenuSection section=new MenuSection();
				// section.setName(module.getModuleName());
				// section.setMenus(module.getMenu());
				// mainMenu.add(section);
				ArrayList<Report> reports = module.getReports(getLocale() + "_", getInActiveLocale() + "_");
				if (reports.size() > 0) {
					ReportManager.addReports(reports);
				}
				// if (debugPriviliges) {
				// printModuleDebugInfo(module, i != 0);
				// }
				if (debugMeta) {
					tableMetaFactory.writeDynamicMeta(new FileOutputStream(module.getModuleName() + "_meta-out.xml"));
				}
			}
		} catch (ModuleException e) {
			throw new ApplicationException(e);
		} catch (IOException e) {
			throw new ApplicationException(e);
		} catch (DaoException e) {
			throw new ApplicationException(e);
		}

	}

	/**
	 * @throws SecurityException
	 * @throws IOException
	 * @throws ModuleException
	 */
	private void setTableMetaPriviliges(Module module) throws SecurityException, IOException, ModuleException {
		ArrayList<Menu> menus = module.getMenu();
		for (int i = 0; i < menus.size(); i++) {
			Menu menu = menus.get(i);
			ArrayList<MenuItem> items = menu.getItems();
			for (int j = 0; j < items.size(); j++) {
				MenuItem item = items.get(j);
				if (item.isDynamicTableMeta()) {
//					TableMeta tableMeta = item.getTableMeta();
//					tableMeta.setpaPrivilige(ne);
//					if (item.isHasDetailTables()) {
//					}
				}
			}
		}
	}

	/**
	 * 
	 * @param priviligeId
	 * @param priviligeName
	 * @param label
	 * @throws SecurityException
	 */
	private String checkValidPrivilige(int priviligeId, String priviligeName, String label, String parentPriilige) throws SecurityException {
		StringBuffer buf = new StringBuffer();
		buf.append("INSERT INTO sec_privileges values (");
		buf.append(priviligeId);
		buf.append(",'");
		buf.append(priviligeName);
		buf.append("','");
		buf.append(label);
		buf.append("',");
		buf.append(parentPriilige);
		buf.append(");\n");
		return buf.toString();
	}

	/**
	 * 
	 * @param locale2
	 * @return
	 * @throws IOException
	 */
	private Properties loadCommonsLabel(String locale) throws IOException {
		String shortFileName = locale + "_lbl.properties";
		InputStream in = this.getClass().getResourceAsStream("/resources/meta/" + shortFileName);
		if (in != null) {
			return GeneralUtility.readPropertyStream(in);
		}
		return new Properties();
	}

	/**
	 * 
	 * @param configFileName
	 */
	public void setConfigFileName(String configFileName) {
		this.configFileName = configFileName;
	}

	/**
	 * @return the configFileName
	 */
	public String getConfigFileName() {
		return configFileName;
	}

	/**
	 * 
	 * @param locale
	 */
	public void setLocale(String locale, boolean checkForUserPref) {
		String localeKey = "locale";
		// to to find the default locale according to user pref , if not found ,
		// passed defaut will be used and
		// and stored as user pref
		if (checkForUserPref) {
			this.locale = UserPreferences.get(localeKey, locale);
		} else {
			this.locale = locale;
		}
		UserPreferences.put(localeKey, this.locale);
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return locale == null ? "en" : locale;
	}

	/**
	 * 
	 * @return
	 */
	public String getInActiveLocale() {
		return getLocale().equals("en") ? "ar" : "en";
	}

	/**
	 * 
	 * @param listener
	 */
	public void addListener(ApplicationListener listener) {
		listeners.add(listener);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<ApplicationListener> getApplicationListeners() {
		return listeners;
	}

	/**
	 * 
	 * @param applicationId
	 */
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * 
	 * @return
	 */
	public int getApplicationId() {
		return applicationId;
	}

	/**
	 * 
	 * @return
	 */
	public Module getDefaultModule() {
		for (int i = 0; i < modules.size(); i++) {
			Module module = modules.get(i);
			if (module.isDefault()) {
				return module;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param applicationFrame
	 */
	public void setApplicationFrame(ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	/**
	 * @return the applicationFrame
	 */
	public ApplicationFrame getApplicationFrame() {
		return applicationFrame;
	}

	public void setViewModules(boolean viewModules) {
		this.viewModules = viewModules;
	}

	/**
	 * 
	 * @param menuItemName
	 * @return
	 * @throws SecurityException
	 * @throws NotAllowedOperationException
	 */
	public MenuItem findMenuItem(String menuItemName) throws NotAllowedOperationException, SecurityException {
		for (Module module : getModules()) {
			for (Menu menu : module.getMenu()) {
				for (MenuItem item : menu.getItems()) {
					if (item.getName().equals(menuItemName)) {
						SecurityManager.getAuthorizer().checkAllowed(item.getPrivilige());
						return item;
					}
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param moduleName
	 * @return
	 */
	public boolean isModuleDefined(String moduleName) {
		for (Module module : modules) {
			if (module.getModuleName().equals(moduleName)) {
				return true;
			}
		}
		return false;
	}

	// ///////////////////////////////////////////////////////
	public boolean isAllowedCommand(Privilige privilige, String name) {
		try {
			SecurityManager.getAuthorizer().checkAllowed(privilige);
			return true;
		} catch (NotAllowedOperationException e) {
			System.err.println("Privlige Id : " + privilige.getPriviligeId() + " , with name : " + name + " is not allowed");
			return false;
		} catch (SecurityException e) {
			ExceptionUtil.handleException(e);
			return false;
		}
	}

	public int getAutoLogoutInterval() {
		return autoLogoutInterval;
	}

	public void setAutoLogoutInterval(int autoLogoutInterval) {
		this.autoLogoutInterval = autoLogoutInterval;
	}

	public void setAutoLogoutInterval(String autoLogoutInterval) {
		if (GeneralUtility.isEmpty(autoLogoutInterval)) {
			return;
		}
		if (!GeneralUtility.isInteger(autoLogoutInterval)) {
			return;
		}
		this.autoLogoutInterval = Integer.parseInt(autoLogoutInterval);
	}

	public Module getModule(String moduleName) {
		if (GeneralUtility.isEmpty(moduleName)) {
			return null;
		}
		for (Module module : getModules()) {
			if (module.getModuleName().trim().equalsIgnoreCase(moduleName.trim())) {
				return module;
			}
		}
		return null;
	}

}
