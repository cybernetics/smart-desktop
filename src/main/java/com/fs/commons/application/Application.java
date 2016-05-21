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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import com.fs.commons.application.config.UserPreferences;
import com.fs.commons.application.exceptions.ModuleException;
import com.fs.commons.application.listener.ApplicationListener;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaFactory;
import com.fs.commons.desktop.swing.frames.ApplicationFrame;
import com.fs.commons.locale.Lables;
import com.fs.commons.reports.JKReport;
import com.fs.commons.reports.JKReportManager;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.JKNotAllowedOperationException;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.security.JKPrivilige;
import com.jk.security.JKSecurityManager;

public class Application {
	private int autoLogoutInterval = 15;
	String applicationName;

	List<Module> modules;
	List<ApplicationListener> listeners = new ArrayList<ApplicationListener>();

	private String configFileName;

	// MainMenu mainMenu=new MainMenu();

	String splashImage;

	String homeImage;

	private String locale;

	private int applicationId = 8643;// just dummy value

	private ApplicationFrame applicationFrame;

	private boolean viewModules = true;
	private String backgroundImage;

	// /**
	// * @return the mainMenu
	// */
	// public MainMenu getMainMenu() {
	// return mainMenu;
	// }

	/**
	 *
	 * @param listener
	 */
	public void addListener(final ApplicationListener listener) {
		this.listeners.add(listener);
	}

	/**
	 *
	 * @param priviligeId
	 * @param priviligeName
	 * @param label
	 * @throws SecurityException
	 */
	private String checkValidPrivilige(final int priviligeId, final String priviligeName, final String label, final String parentPriilige)
			throws SecurityException {
		final StringBuffer buf = new StringBuffer();
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
	 * @param menuItemName
	 * @return
	 * @throws SecurityException
	 * @throws JKNotAllowedOperationException
	 */
	public MenuItem findMenuItem(final String menuItemName) throws JKNotAllowedOperationException, SecurityException {
		for (final Module module : getModules()) {
			for (final Menu menu : module.getMenu()) {
				for (final MenuItem item : menu.getItems()) {
					if (item.getName().equals(menuItemName)) {
						JKSecurityManager.getAuthorizer().checkAllowed(item.getPrivilige());
						return item;
					}
				}
			}
		}
		return null;
	}

	/**
	 * @return the applicationFrame
	 */
	public ApplicationFrame getApplicationFrame() {
		return this.applicationFrame;
	}

	/**
	 *
	 * @return
	 */
	public int getApplicationId() {
		return this.applicationId;
	}

	/**
	 *
	 * @return
	 */
	public List<ApplicationListener> getApplicationListeners() {
		return this.listeners;
	}

	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return this.applicationName;
	}

	public int getAutoLogoutInterval() {
		return this.autoLogoutInterval;
	}

	/**
	 * @return the configFileName
	 */
	public String getConfigFileName() {
		return this.configFileName;
	}

	/**
	 *
	 * @return
	 */
	public Module getDefaultModule() {
		for (int i = 0; i < this.modules.size(); i++) {
			final Module module = this.modules.get(i);
			if (module.isDefault()) {
				return module;
			}
		}
		return null;
	}

	/**
	 * @return the homeImage
	 */
	public String getHomeImage() {
		return this.homeImage;
	}

	/**
	 *
	 * @return
	 */
	public String getInActiveLocale() {
		return getLocale().equals("en") ? "ar" : "en";
	}

	/**
	 * @return the locale
	 */
	public String getLocale() {
		return this.locale == null ? "en" : this.locale;
	}

	public Module getModule(final String moduleName) {
		if (GeneralUtility.isEmpty(moduleName)) {
			return null;
		}
		for (final Module module : getModules()) {
			if (module.getModuleName().trim().equalsIgnoreCase(moduleName.trim())) {
				return module;
			}
		}
		return null;
	}

	/**
	 * @return the modules
	 */
	public List<Module> getModules() {
		return this.modules;
	}

	/**
	 * @return the splashImage
	 */
	public String getSplashImage() {
		return this.splashImage;
	}

	/**
	 * @throws ApplicationException
	 *
	 */
	public void init() throws ApplicationException {
		try {
			Lables.getDefaultInstance().addLables(loadCommonsLabel(getLocale()));
			// boolean debugPriviliges =
			// System.getProperty("fs.debug.priviliges") != null;
			if (modules != null) {
				for (int i = 0; i < this.modules.size(); i++) {
					final Module module = this.modules.get(i);
					module.init();
					final Hashtable<String, TableMeta> tablesMeta = module.getTablesMeta();
					final TableMetaFactory tableMetaFactory = AbstractTableMetaFactory.addTablesMeta(module.getDataSource(), tablesMeta);
					Lables.getDefaultInstance().addLables(module.getLables(getLocale()));
					// MenuSection section=new MenuSection();
					// section.setName(module.getModuleName());
					// section.setMenus(module.getMenu());
					// mainMenu.add(section);
					final ArrayList<JKReport> reports = module.getReports(getLocale() + "_", getInActiveLocale() + "_");
					if (reports.size() > 0) {
						JKReportManager.addReports(reports);
					}
					// if (debugPriviliges) {
					// printModuleDebugInfo(module, i != 0);
					// }
					//tableMetaFactory.writeDynamicMeta(new FileOutputStream(module.getModuleName() + "_meta-out.xml"));
				}
			}
		} catch (final ModuleException e) {
			throw new ApplicationException(e);
		} catch (final IOException e) {
			throw new ApplicationException(e);
		} catch (final JKDataAccessException e) {
			throw new ApplicationException(e);
		}

	}

	// ///////////////////////////////////////////////////////
	public boolean isAllowedCommand(final JKPrivilige privilige, final String name) {
		try {
			JKSecurityManager.getAuthorizer().checkAllowed(privilige);
			return true;
		} catch (final JKNotAllowedOperationException e) {
			System.err.println("Privlige Id : " + privilige.getPriviligeId() + " , with name : " + name + " is not allowed");
			return false;
		} catch (final SecurityException e) {
			JKExceptionUtil.handle(e);
			return false;
		}
	}

	/**
	 *
	 * @param moduleName
	 * @return
	 */
	public boolean isModuleDefined(final String moduleName) {
		for (final Module module : this.modules) {
			if (module.getModuleName().equals(moduleName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the viewModules
	 */
	public boolean isViewModules() {
		return this.viewModules;
	}

	/**
	 *
	 * @param locale2
	 * @return
	 * @throws IOException
	 */
	private Properties loadCommonsLabel(final String locale) throws IOException {
		final String shortFileName = locale + "_lbl.properties";
		final InputStream in = this.getClass().getResourceAsStream("/resources/meta/" + shortFileName);
		if (in != null) {
			return GeneralUtility.readPropertyStream(in);
		}
		return new Properties();
	}

	/**
	 *
	 * @param applicationFrame
	 */
	public void setApplicationFrame(final ApplicationFrame applicationFrame) {
		this.applicationFrame = applicationFrame;
	}

	/**
	 *
	 * @param applicationId
	 */
	public void setApplicationId(final int applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * @param applicationName
	 *            the applicationName to set
	 */
	public void setApplicationName(final String applicationName) {
		this.applicationName = applicationName;
		UserPreferences.setKeyPrefix(applicationName);
	}

	public void setAutoLogoutInterval(final int autoLogoutInterval) {
		this.autoLogoutInterval = autoLogoutInterval;
	}

	public void setAutoLogoutInterval(final String autoLogoutInterval) {
		if (GeneralUtility.isEmpty(autoLogoutInterval)) {
			return;
		}
		if (!GeneralUtility.isInteger(autoLogoutInterval)) {
			return;
		}
		this.autoLogoutInterval = Integer.parseInt(autoLogoutInterval);
	}

	/**
	 *
	 * @param configFileName
	 */
	public void setConfigFileName(final String configFileName) {
		this.configFileName = configFileName;
	}

	/**
	 * @param homeImage
	 *            the homeImage to set
	 */
	public void setHomeImage(final String homeImage) {
		this.homeImage = homeImage;
	}

	/**
	 *
	 * @param locale
	 */
	public void setLocale(final String locale, final boolean checkForUserPref) {
		final String localeKey = "locale";
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
	 * @param modules
	 *            the modules to set
	 */
	public void setModules(final ArrayList<Module> modules) {
		this.modules = modules;
	}

	/**
	 * @param splashImage
	 *            the splashImage to set
	 */
	public void setSplashImage(final String splashImage) {
		this.splashImage = splashImage;
	}

	/**
	 * @throws SecurityException
	 * @throws IOException
	 * @throws ModuleException
	 */
	private void setTableMetaPriviliges(final Module module) throws SecurityException, IOException, ModuleException {
		final ArrayList<Menu> menus = module.getMenu();
		for (int i = 0; i < menus.size(); i++) {
			final Menu menu = menus.get(i);
			final ArrayList<MenuItem> items = menu.getItems();
			for (int j = 0; j < items.size(); j++) {
				final MenuItem item = items.get(j);
				if (item.isDynamicTableMeta()) {
					// TableMeta tableMeta = item.getTableMeta();
					// tableMeta.setpaPrivilige(ne);
					// if (item.isHasDetailTables()) {
					// }
				}
			}
		}
	}

	public void setViewModules(final boolean viewModules) {
		this.viewModules = viewModules;
	}

	@Override
	public String toString() {
		final StringBuffer buf = new StringBuffer("Name : " + getApplicationName() + "\n");
		for (int i = 0; i < this.modules.size(); i++) {
			buf.append(this.modules.get(i).toString());
		}
		return buf.toString();
	}

	public String getBackgroundImage() {
		return backgroundImage==null?getHomeImage():backgroundImage;
	}
	
	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

}
