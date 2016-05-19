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

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import org.apache.ibatis.jdbc.ScriptRunner;

import com.fs.commons.application.config.DefaultConfigManager;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.application.exceptions.util.ExceptionHandlerFactory;
import com.fs.commons.application.listener.ApplicationListener;
import com.fs.commons.application.util.ResourceLoaderFactory;
import com.fs.commons.application.xml.ApplicationXmlParser;
import com.fs.commons.apps.backup.AutomaticDBBackup;
import com.fs.commons.apps.instance.InstanceManager;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKDefaultDataAccess;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.connection.JKDataSourceUtil;
import com.fs.commons.dao.connection.JKPoolingDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXmlParser;
import com.fs.commons.desktop.DesktopExceptionHandler;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.TableModelHtmlBuilder;
import com.fs.commons.desktop.swing.frames.ApplicationFrame;
import com.fs.commons.desktop.swing.frames.Splash;
import com.fs.commons.locale.Lables;
import com.fs.commons.locale.Locale;
import com.fs.commons.reports.JKReportManager;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.JKInvalidUserException;
import com.jk.exceptions.handler.ExceptionUtil;
import com.jk.license.client.LicenseClientFactory;
import com.jk.security.JKSecurityManager;
import com.jk.security.JKUser;

public class ApplicationManager {
	static Logger logger = Logger.getLogger(ApplicationManager.class.getName());
	private static final String[] DEFAULT_SYSTEM_FILES = { "/system.xml", "/default.system.xml" };
	private static final int LOGIN_RETRIES = 3;
	private static ApplicationManager instance;
	private static ClientInfo client;
	private static boolean firstRun = true;

	public static String getClientName() {
		if (client == null) {
			return "N/A";
		}
		if (client.getNls() != null && client.getNls().getLocale() == getInstance().getCurrentLocale()) {
			return client.getNls().getName();
		}
		return client.getClientName();
	}

	// public static String getCompnayName(){
	// return client==null?"":client.getClientName();
	// }
	public static ClientInfo getCurrentClient() {
		return client;
	}

	/**
	 * @return
	 */
	public static ApplicationManager getInstance() {
		if (instance == null) {
			logger.info("set default exception handler");
			ExceptionHandlerFactory.setDefaultHandler(new DesktopExceptionHandler());
			try {
				logger.info("set default datasource");
				JKDataSourceFactory.setDefaultDataSource(new JKPoolingDataSource());
				checkScriptsInstalled();
				logger.info("set default instance");
				instance = new ApplicationManager();
			} catch (final Exception e) {
				ExceptionUtil.handle(e);
			}
		}
		return instance;
	}

	private static void checkScriptsInstalled() throws FileNotFoundException, IOException {
		// TODO : add generic way to apply db-scripts
		JKDataSource source = JKDataSourceFactory.getDefaultDataSource();
		if (JKDataSourceUtil.isMysql(source)) {
			try {
				TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta("sec_users");
			} catch (TableMetaNotFoundException e) {

				System.err.println("It looks like a first usage , apply security script on DB?(y,n)");
				Scanner scanner = new Scanner(System.in);
				String next = scanner.next();
				if (next.toLowerCase().startsWith("y")) {
					JKDefaultDataAccess dao = new JKDefaultDataAccess();
					dao.runScript("/scripts/mysql/security.sql");
				}
			}
		}
	}

	public static void main(final String[] args) {
		// processDatabaseAutobackup();
	}

	public static void setCurrentClient(final ClientInfo client) {
		logger.info("Set default client info : " + client);
		ApplicationManager.client = client;
		TableModelHtmlBuilder.setCompanyName(client.getClientName());
		TableModelHtmlBuilder.setCompanyLogo(client.getLogo());
	}

	private Application application;

	private ApplicationFrame applicationFrame;

	/**
	 *
	 */
	public void closeMainFrame() {
		logger.info("close main frame");
		if (this.application != null && this.applicationFrame != null) {
			this.applicationFrame.dispose();
		}
		this.applicationFrame = null;
	}

	// //
	// ////////////////////////////////////////////////////////////////////////////////////
	// private void loadDefaultLables() throws IOException {
	// ResourceLoader loader = ResourceLoaderFactory.getResourceLoaderImp();
	// InputStream stream = loader.getResourceAsStream("/lables.properties");
	// if (stream != null) {
	// Lables.getDefaultInstance().addLables(stream);
	// } else {
	// System.err.println("default lables file is not available");
	// }
	// }

	// ////////////////////////////////////////////////////////////////////////////////////
	private Application createDefaultApplication() {
		logger.info("create default application");
		final Application a = new Application();
		return a;
	}

	/**
	 *
	 */
	private void fireAfterApplicationInit() {
		final List<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterInit(this.application);
		}
	}

	private void fireAfterApplicationStart() {
		final List<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterStart(this.application);
		}
	}

	/**
	 *
	 */
	private void fireBeforeApplicationInit() {
		final List<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeInit(this.application);
		}
	}

	private void fireBeforeApplicationStart() {
		final List<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeStart(this.application);
		}
	}

	/**
	 *
	 * @param e
	 */
	private void fireException(final Exception e) {
		final List<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).onException(this.application, e);
		}
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return this.application;
	}

	/**
	 * @return the applicationFrame
	 */
	public ApplicationFrame getApplicationFrame() {
		if (this.applicationFrame == null) {
			throw new IllegalStateException("ApplicatinFrame is not initialized , please call start first");
		}
		return this.applicationFrame;
	}

	/**
	 *
	 * @return
	 */
	public Locale getCurrentLocale() {
		return Locale.valueOf(this.application.getLocale());
	}

	/**
	 *
	 */
	protected void handleResize() {
		if (this.applicationFrame != null) {
			final Dimension dimension = this.applicationFrame.getSize();
			final Dimension defaultDimension = new Dimension(1024, 700);
			if (dimension.getWidth() < 1024 || dimension.getHeight() < 700) {
				this.applicationFrame.setSize(defaultDimension);
			}
		}
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws ApplicationException
	 */
	public Application init() throws FileNotFoundException, ApplicationException {
		for (String fileName : DEFAULT_SYSTEM_FILES) {
			logger.info("trying to init application with file :" + fileName);
			try {
				return init(GeneralUtility.getFileInputStream(fileName));
			} catch (final FileNotFoundException e) {
				logger.info("not found ");
			}
		}
		System.err.println(" config files doesnot exist, init with defaults");
		return init(null);

	}

	/**
	 * @return
	 * @throws JKXmlException
	 */
	public Application init(final InputStream in) throws ApplicationException {
		logger.info("init with inputstream");
		Splash splash = null;
		try {
			logger.info("initConfig");
			initConfig();
			logger.info("init labels");
			Lables.getDefaultInstance();// to foce init
			// loadDefaultLables();
			logger.info("loadDefaultMeta()");
			loadDefaultMeta();
			if (in != null) {
				logger.info("parse application");
				final ApplicationXmlParser parser = new ApplicationXmlParser();
				this.application = parser.parseApplication(in);
			} else {
				logger.info("create default application");
				this.application = createDefaultApplication();
			}
			if (this.application.getSplashImage() != null) {
				logger.info("show splash");
				splash = new Splash(this.application.getSplashImage());
				splash.setVisible(true);
			}
			logger.info("validate license");
			LicenseClientFactory.getClient().validateLicense();
			fireBeforeApplicationInit();
			if (this.application.getLocale() != null) {
				logger.info("set locale to : " + application.getLocale());
				SwingUtility.setDefaultLocale(this.application.getLocale());
			} else {
				System.err.println("null locale");
			}
			// ExceptionUtil.initExceptionLogging();
			logger.info("application.init");
			this.application.init();
			// WE DELAY THE CHECK UNTIL NOW TO BE SURE THAT WE HAVE LOADED THE
			// LABLES
			if (firstRun && isAllowSingleInstanceOnly()) {
				logger.info("single instance only");
				// to avoid any issues with swicthLocale or restart
				InstanceManager.registerInstance(this.application.getApplicationId());
				firstRun = false;
			}
			fireAfterApplicationInit();

			// new
			// InstanceManager().registerInstance(application.getApplicationId());
			return this.application;
		} catch (final JKXmlException e) {
			e.printStackTrace();
			fireException(e);
			throw new ApplicationException(e);
		} catch (final FileNotFoundException e) {
			fireException(e);
			throw new ApplicationException(e);
		} catch (final Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (splash != null) {
				GeneralUtility.sleep(2);
				splash.dispose();
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private void initConfig() throws FileNotFoundException, IOException {
		final DefaultConfigManager manager = new DefaultConfigManager();
		// Properties prop = new Properties();
		// prop.loadFromXML(new FileInputStream("system.config"));
		System.getProperties().putAll(manager.getProperties());
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private boolean isAllowSingleInstanceOnly() {
		return System.getProperty("SINGLE_INSTANCE", "false").toLowerCase().equals("true");
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private void loadDefaultMeta() throws JKXmlException, JKDataAccessException {
		logger.info("start loading default meta");
		final TableMetaXmlParser parser = new TableMetaXmlParser();
		String resourceName = "/resources/meta/meta.xml";
		logger.info("load default meta at :" + resourceName);
		final InputStream in = ResourceLoaderFactory.getResourceLoaderImp().getResourceAsStream(resourceName);
		if (in != null) {
			logger.info("parse default meta");
			final Hashtable<String, TableMeta> meta = parser.parse(in, "default");
			logger.info("add meta to abstract table meta factory : " + meta);
			AbstractTableMetaFactory.addTablesMeta(JKDataSourceFactory.getDefaultDataSource(), meta);
		}

	}

	/**
	 * @throws ApplicationException
	 *
	 */
	public void logout() throws ApplicationException {
		// SecurityFacade facade=new SecurityFacade();
		// try {
		// facade.addLogoutAudit();
		// } catch (DaoException e) {
		// throw new ApplicationException(e);
		// }
		closeMainFrame();
		JKSecurityManager.setCurrentUser(null);
		start();
	}

	/**
	 *
	 */
	public void restart() {
		if (SwingUtility.showConfirmationDialog("WE_NEED_TO_RESTART_THE_SYSTEM,RESTART_NOW?")) {
			try {
				GeneralUtility.executeFile("run.bat");
				System.exit(0);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void restartFrame() {
		try {
			this.application.getApplicationFrame().dispose();
			init();
			start();
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	/**
	 * @throws ApplicationException
	 *
	 */
	public void start() throws ApplicationException {
		fireBeforeApplicationStart();
		Splash splash = null;
		try {
			if (!JKSecurityManager.isUserLoggedIn()) {
				final JKUser user = JKSecurityManager.getAuthenticaor().authenticate(this.application.getApplicationName(), LOGIN_RETRIES);
				JKSecurityManager.setCurrentUser(user);
				AutomaticDBBackup.processDatabaseAutobackup();
			}
			if (this.application.getSplashImage() != null) {
				splash = new Splash(this.application.getSplashImage());
				splash.setVisible(true);
			}
			this.applicationFrame = new ApplicationFrame(this.application);
			this.application.setApplicationFrame(this.applicationFrame);
			this.applicationFrame.setTitle(this.application.getApplicationName());
			SwingUtility.setDefaultMainFrame(this.applicationFrame);
			// applicationFrame.setExtendedState(ApplicationFrame.MAXIMIZED_BOTH);

			this.applicationFrame.setVisible(true);
			this.applicationFrame.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(final ComponentEvent evt) {
					handleResize();
				}
			});
			fireAfterApplicationStart();
		} catch (final JKInvalidUserException e) {
			fireException(e);
			throw new ApplicationException(e);
		} catch (final SecurityException e) {
			fireException(e);
			throw new ApplicationException(e);
		} finally {
			if (splash != null) {
				splash.dispose();
			}
		}
	}

	/**
	 *
	 * @param locale
	 * @throws ApplicationException
	 */
	public void switchLocale() {
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				closeMainFrame();
				JKReportManager.clearReports();
				ApplicationManager.this.application = getApplication();
				ApplicationManager.this.application.setLocale(ApplicationManager.this.application.getInActiveLocale(), false);
				SwingUtility.setDefaultLocale(ApplicationManager.this.application.getLocale());
				Lables.getDefaultInstance().clear();
				restartFrame();
			}
		};
		final Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 *
	 * @param panel
	 */
	public void testPanel(final JKPanel panel) {
		try {
			// init();
			final JKUser currentUser = new JKUser(1);
			currentUser.setUserId("admin");
			JKSecurityManager.setCurrentUser(currentUser);
			SwingUtility.testPanel(panel);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
