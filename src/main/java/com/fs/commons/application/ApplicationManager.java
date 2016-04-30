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

import com.fs.commons.application.config.DefaultConfigManager;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.application.exceptions.util.ExceptionHandlerFactory;
import com.fs.commons.application.listener.ApplicationListener;
import com.fs.commons.application.util.ResourceLoaderFactory;
import com.fs.commons.application.xml.ApplicationXmlParser;
import com.fs.commons.apps.backup.AutomaticDBBackup;
import com.fs.commons.apps.instance.InstanceManager;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.connection.PoolingDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXmlParser;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.DesktopExceptionHandler;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.TableModelHtmlBuilder;
import com.fs.commons.desktop.swing.frames.ApplicationFrame;
import com.fs.commons.desktop.swing.frames.Splash;
import com.fs.commons.locale.Lables;
import com.fs.commons.locale.Locale;
import com.fs.commons.reports.ReportManager;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.User;
import com.fs.commons.security.exceptions.InvalidUserException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;
import com.fs.license.client.LicenseClientFactory;

public class ApplicationManager {

	private static final String DEFAULT_SYSTEM_FILE = "/system.xml";
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
			ExceptionHandlerFactory.setDefaultHandler(new DesktopExceptionHandler());
			try {
				DataSourceFactory.setDefaultDataSource(new PoolingDataSource());
				instance = new ApplicationManager();
			} catch (final ServerDownException e) {
				ExceptionUtil.handleException(e);
			} catch (final DaoException e) {
				// unable to get database connection for the following reasons
				// the username / password / databse name are invalid
				ExceptionUtil.handleException(e);
			}
		}
		return instance;
	}

	public static void main(final String[] args) {
		// processDatabaseAutobackup();
	}

	public static void setCurrentClient(final ClientInfo client) {
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
		final Application a = new Application();
		return a;
	}

	/**
	 *
	 */
	private void fireAfterApplicationInit() {
		final ArrayList<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterInit(this.application);
		}
	}

	private void fireAfterApplicationStart() {
		final ArrayList<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterStart(this.application);
		}
	}

	/**
	 *
	 */
	private void fireBeforeApplicationInit() {
		final ArrayList<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeInit(this.application);
		}
	}

	private void fireBeforeApplicationStart() {
		final ArrayList<ApplicationListener> listeners = this.application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeStart(this.application);
		}
	}

	/**
	 *
	 * @param e
	 */
	private void fireException(final Exception e) {
		final ArrayList<ApplicationListener> listeners = this.application.getApplicationListeners();
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
		final String fileName = DEFAULT_SYSTEM_FILE;
		try {
			return init(GeneralUtility.getFileInputStream(fileName));
		} catch (final FileNotFoundException e) {
			System.err.println(DEFAULT_SYSTEM_FILE + " doesnot exist, init with defaults");
			return init(null);
		}
	}

	/**
	 * @return
	 * @throws JKXmlException
	 */
	public Application init(final InputStream in) throws ApplicationException {
		Splash splash = null;
		try {
			initConfig();
			Lables.getDefaultInstance();// to foce init
			// loadDefaultLables();
			loadDefaultMeta();
			if (in != null) {
				final ApplicationXmlParser parser = new ApplicationXmlParser();
				this.application = parser.parseApplication(in);
			} else {
				this.application = createDefaultApplication();
			}
			if (this.application.getSplashImage() != null) {
				splash = new Splash(this.application.getSplashImage());
				splash.setVisible(true);
			}
			LicenseClientFactory.getClient().validateLicense();
			fireBeforeApplicationInit();
			if (this.application.getLocale() != null) {
				SwingUtility.setDefaultLocale(this.application.getLocale());
			} else {
				System.err.println("null locale");
			}
			ExceptionUtil.initExceptionLogging();
			this.application.init();
			// WE DELAY THE CHECK UNTIL NOW TO BE SURE THAT WE HAVE LOADED THE
			// LABLES
			if (firstRun && isAllowSingleInstanceOnly()) {
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
	private void loadDefaultMeta() throws JKXmlException, DaoException {
		final TableMetaXmlParser parser = new TableMetaXmlParser();
		final InputStream in = ResourceLoaderFactory.getResourceLoaderImp().getResourceAsStream("/resources/meta/meta.xml");
		if (in != null) {
			final Hashtable<String, TableMeta> meta = parser.parse(in, "default");
			AbstractTableMetaFactory.addTablesMeta(DataSourceFactory.getDefaultDataSource(), meta);
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
		SecurityManager.setCurrentUser(null);
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
			ExceptionUtil.handleException(e);
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
			if (!SecurityManager.isUserLoggedIn()) {
				final User user = SecurityManager.getAuthenticaor().authenticate(this.application.getApplicationName(), LOGIN_RETRIES);
				SecurityManager.setCurrentUser(user);
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
		} catch (final InvalidUserException e) {
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
				ReportManager.clearReports();
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
			final User currentUser = new User(1);
			currentUser.setUserId("admin");
			SecurityManager.setCurrentUser(currentUser);
			SwingUtility.testPanel(panel);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
