package com.fs.commons.application;

import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;

import org.apache.poi.hssf.record.LabelSSTRecord;

import com.fs.commons.application.config.DefaultConfigManager;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.application.exceptions.util.ExceptionHandlerFactory;
import com.fs.commons.application.listener.ApplicationListener;
import com.fs.commons.application.util.ResourceLoader;
import com.fs.commons.application.util.ResourceLoaderFactory;
import com.fs.commons.application.xml.ApplicationXmlParser;
import com.fs.commons.apps.backup.AutomaticDBBackup;
import com.fs.commons.apps.instance.InstanceManager;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.connection.PoolingDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaFactory;
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
	private Application application;
	private ApplicationFrame applicationFrame;
	private static ClientInfo client;
	private static boolean firstRun = true;

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
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
			} catch (ServerDownException e) {
				ExceptionUtil.handleException(e);
			} catch (DaoException e) {
				// unable to get database connection for the following reasons
				// the username / password / databse name are invalid
				ExceptionUtil.handleException(e);
			}
		}
		return instance;
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws ApplicationException
	 */
	public Application init() throws FileNotFoundException, ApplicationException {
		String fileName = DEFAULT_SYSTEM_FILE;
		try {
			return init(GeneralUtility.getFileInputStream(fileName));
		} catch (FileNotFoundException e) {
			System.err.println(DEFAULT_SYSTEM_FILE + " doesnot exist, init with defaults");
			return init(null);
		}
	}

	/**
	 * @return
	 * @throws JKXmlException
	 */
	public Application init(InputStream in) throws ApplicationException {
		Splash splash = null;
		try {
			initConfig();
			Lables.getDefaultInstance();//to foce init
//			loadDefaultLables();
			loadDefaultMeta();
			if (in != null) {
				ApplicationXmlParser parser = new ApplicationXmlParser();
				application = parser.parseApplication(in);
			} else {
				application = createDefaultApplication();
			}
			if (application.getSplashImage() != null) {
				splash = new Splash(application.getSplashImage());
				splash.setVisible(true);
			}
			LicenseClientFactory.getClient().validateLicense();
			fireBeforeApplicationInit();
			if (application.getLocale() != null) {
				SwingUtility.setDefaultLocale(application.getLocale());
			} else {
				System.err.println("null locale");
			}
			ExceptionUtil.initExceptionLogging();
			application.init();
			// WE DELAY THE CHECK UNTIL NOW TO BE SURE THAT WE HAVE LOADED THE
			// LABLES
			if (firstRun && isAllowSingleInstanceOnly()) {
				// to avoid any issues with swicthLocale or restart
				InstanceManager.registerInstance(application.getApplicationId());
				firstRun = false;
			}
			fireAfterApplicationInit();

			// new
			// InstanceManager().registerInstance(application.getApplicationId());
			return application;
		} catch (JKXmlException e) {
			e.printStackTrace();
			fireException(e);
			throw new ApplicationException(e);
		} catch (FileNotFoundException e) {
			fireException(e);
			throw new ApplicationException(e);
		} catch (Exception e) {
			throw new ApplicationException(e);
		} finally {
			if (splash != null) {
				splash.dispose();
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private void loadDefaultMeta() throws JKXmlException, DaoException {
		TableMetaXmlParser parser = new TableMetaXmlParser();
		InputStream in = ResourceLoaderFactory.getResourceLoaderImp().getResourceAsStream("/resources/meta/meta.xml");
		if (in != null) {
			Hashtable<String, TableMeta> meta = parser.parse(in,"default");
			AbstractTableMetaFactory.addTablesMeta(DataSourceFactory.getDefaultDataSource(), meta);
		}

	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private boolean isAllowSingleInstanceOnly() {
		return System.getProperty("SINGLE_INSTANCE", "false").toLowerCase().equals("true");
	}

//	// ////////////////////////////////////////////////////////////////////////////////////
//	private void loadDefaultLables() throws IOException {
//		ResourceLoader loader = ResourceLoaderFactory.getResourceLoaderImp();
//		InputStream stream = loader.getResourceAsStream("/lables.properties");
//		if (stream != null) {
//			Lables.getDefaultInstance().addLables(stream);
//		} else {
//			System.err.println("default lables file is not available");
//		}
//	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private Application createDefaultApplication() {
		Application a = new Application();
		return a;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private void initConfig() throws FileNotFoundException, IOException {
		DefaultConfigManager manager = new DefaultConfigManager();
		// Properties prop = new Properties();
		// prop.loadFromXML(new FileInputStream("system.config"));
		System.getProperties().putAll(manager.getProperties());
	}

	/**
	 * 
	 * @param e
	 */
	private void fireException(Exception e) {
		ArrayList<ApplicationListener> listeners = application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).onException(application, e);
		}
	}

	/**
	 * 
	 */
	private void fireBeforeApplicationInit() {
		ArrayList<ApplicationListener> listeners = application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeInit(application);
		}
	}

	/**
	 * 
	 */
	private void fireAfterApplicationInit() {
		ArrayList<ApplicationListener> listeners = application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterInit(application);
		}
	}

	private void fireBeforeApplicationStart() {
		ArrayList<ApplicationListener> listeners = application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).beforeStart(application);
		}
	}

	private void fireAfterApplicationStart() {
		ArrayList<ApplicationListener> listeners = application.getApplicationListeners();
		for (int i = 0; i < listeners.size(); i++) {
			listeners.get(i).afterStart(application);
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
				User user = SecurityManager.getAuthenticaor().authenticate(application.getApplicationName(), LOGIN_RETRIES);
				SecurityManager.setCurrentUser(user);
				AutomaticDBBackup.processDatabaseAutobackup();
			}
			if (application.getSplashImage() != null) {
				splash = new Splash(application.getSplashImage());
				splash.setVisible(true);
			}
			applicationFrame = new ApplicationFrame(application);
			application.setApplicationFrame(applicationFrame);
			applicationFrame.setTitle(application.getApplicationName());
			SwingUtility.setDefaultMainFrame(applicationFrame);
			// applicationFrame.setExtendedState(ApplicationFrame.MAXIMIZED_BOTH);

			applicationFrame.setVisible(true);
			applicationFrame.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent evt) {
					handleResize();
				}
			});
			fireAfterApplicationStart();
		} catch (InvalidUserException e) {
			fireException(e);
			throw new ApplicationException(e);
		} catch (SecurityException e) {
			fireException(e);
			throw new ApplicationException(e);
		} finally {
			if (splash != null) {
				splash.dispose();
			}
		}
	}

	/**
	 * @return the applicationFrame
	 */
	public ApplicationFrame getApplicationFrame() {
		if (applicationFrame == null) {
			throw new IllegalStateException("ApplicatinFrame is not initialized , please call start first");
		}
		return applicationFrame;
	}

	/**
	 * 
	 * @param panel
	 */
	public void testPanel(JKPanel panel) {
		try {
			// init();
			User currentUser = new User(1);
			currentUser.setUserId("admin");
			SecurityManager.setCurrentUser(currentUser);
			SwingUtility.testPanel(panel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void handleResize() {
		if (applicationFrame != null) {
			Dimension dimension = applicationFrame.getSize();
			Dimension defaultDimension = new Dimension(1024, 700);
			if ((dimension.getWidth() < 1024) || (dimension.getHeight() < 700)) {
				applicationFrame.setSize(defaultDimension);
			}
		}
	}

	/**
	 * 
	 */
	public void restart() {
		if (SwingUtility.showConfirmationDialog("WE_NEED_TO_RESTART_THE_SYSTEM,RESTART_NOW?")) {
			try {
				GeneralUtility.executeFile("run.bat");
				System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public void closeMainFrame() {
		if (application != null && applicationFrame!=null) {
			applicationFrame.dispose();
		}
		applicationFrame = null;
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
	 * @param locale
	 * @throws ApplicationException
	 */
	public void switchLocale() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				closeMainFrame();
				ReportManager.clearReports();
				application = getApplication();
				application.setLocale(application.getInActiveLocale(), false);
				SwingUtility.setDefaultLocale(application.getLocale());
				Lables.getDefaultInstance().clear();
				restartFrame();
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * 
	 * @return
	 */
	public Locale getCurrentLocale() {
		return Locale.valueOf(application.getLocale());
	}

	public static void setCurrentClient(ClientInfo client) {
		ApplicationManager.client = client;
		TableModelHtmlBuilder.setCompanyName(client.getClientName());
		TableModelHtmlBuilder.setCompanyLogo(client.getLogo());
	}

	public void restartFrame() {
		try {
			application.getApplicationFrame().dispose();
			init();
			start();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	public static void main(String[] args) {
		// processDatabaseAutobackup();
	}

	// public static String getCompnayName(){
	// return client==null?"":client.getClientName();
	// }
	public static ClientInfo getCurrentClient() {
		return client;
	}

	public static String getClientName() {
		if (client == null) {
			return "N/A";
		}
		if (client.getNls() != null && client.getNls().getLocale() == getInstance().getCurrentLocale()) {
			return client.getNls().getName();
		}
		return client.getClientName();
	}
}
