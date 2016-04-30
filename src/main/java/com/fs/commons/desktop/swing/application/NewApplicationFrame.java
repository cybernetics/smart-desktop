//package com.fs.commons.desktop.swing.application;
//
//import java.awt.ComponentOrientation;
//import java.awt.event.ActionEvent;
//import java.awt.event.ComponentAdapter;
//import java.awt.event.ComponentEvent;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
//import java.io.FileNotFoundException;
//
//import javax.swing.Action;
//
//import com.fs.commons.application.Application;
//import com.fs.commons.application.ApplicationException;
//import com.fs.commons.application.ApplicationManager;
//import com.fs.commons.application.ui.UIOPanelCreationException;
//import com.fs.commons.apps.executors.CloseExecutor;
//import com.fs.commons.desktop.swing.SwingUtility;
//import com.fs.commons.desktop.swing.comp.JKFrame;
//import com.fs.commons.desktop.swing.comp.JKStatusBar;
//import com.fs.commons.desktop.swing.listener.ActionAdapter;
//import com.fs.commons.desktop.swing.listener.InactivityListener;
//import com.fs.commons.security.SecurityManager;
//import com.fs.commons.security.User;
//import com.fs.commons.util.ExceptionUtil;
//
//public class NewApplicationFrame extends JKFrame {
//
//	private static final int AUTO_LOGOUT_INTERVAL = Integer.parseInt(System.getProperty("logout.interval","15"));
//
//	private static final long serialVersionUID = 1L;
//
//	public static final int DEFAULT_FRAME_WIDTH = 1024;
//
//	protected static final int MIN_WIDTH = 1000;
//
//	protected static final int MIN_HEIGHT = 700;
//
//	private Runnable closeExecutor = new CloseExecutor();
//
//	JKStatusBar pnlStatus = new JKStatusBar();
//
//	private Application application;
//
//	/**
//	 * 
//	 */
//	public NewApplicationFrame() {
//	}
//
//	/**
//	 * 
//	 * @param application
//	 * @throws UIOPanelCreationException
//	 */
//	public NewApplicationFrame(Application application) throws UIOPanelCreationException {
//		setApplication(application);
//
//	}
//
//	/**
//	 * @return the application
//	 */
//	public Application getApplication() {
//		return application;
//	}
//
//	/**
//	 * @param application
//	 *            the application to set
//	 * @throws UIOPanelCreationException
//	 */
//	public void setApplication(Application application) throws UIOPanelCreationException {
//		this.application = application;
//		init();
//		addAutoLogoutListener();
//		this.addComponentListener(new ComponentAdapter() {
//			public void componentResized(ComponentEvent e) {
//				handleFrameResized();
//			}
//		});
//	}
//
//	private void addAutoLogoutListener() {
//		Action logout = new ActionAdapter() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					ApplicationManager.getInstance().logout();
//				} catch (ApplicationException e1) {
//					ExceptionUtil.handleException(e1);
//				}
//			}
//		};
//		InactivityListener listener = new InactivityListener(logout, AUTO_LOGOUT_INTERVAL);
//		listener.start();
//	}
//
//	/**
//	 * @param closeExecutor
//	 *            the closeExecutor to set
//	 */
//	public void setCloseExecutor(Runnable closeExecutor) {
//		this.closeExecutor = closeExecutor;
//	}
//
//	/**
//	 * @throws UIOPanelCreationException
//	 * 
//	 */
//	protected void init() throws UIOPanelCreationException {
//		// setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
//		//setExtendedState(MAXIMIZED_BOTH);
//		setLocationRelativeTo(null);
//		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
//		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//
//		add(getApplicationPanel());
//		// Register Events
//		addWindowListener(new WindowAdapter() {
//			@Override
//			public void windowClosing(WindowEvent e) {
//				handleClose();
//			}
//		});
//	}
//
//	private ApplicationPanel getApplicationPanel() throws UIOPanelCreationException {
//		return new ApplicationPanel(getApplication());
//	}
//
////	/*
////	 * @param priviligeId
////	 * 
////	 * @return
////	 */
////	private boolean isAllowedCommand(int priviligeId, String name) {
////		try {
////			SecurityManager.getAuthorizer().checkAllowed(new Privilige(priviligeId, name));
////			return true;
////		} catch (NotAllowedOperationException e) {
////			System.err.println("Privlige Id : " + priviligeId + " , with name : " + name + " is not allowed");
////			return false;
////		} catch (SecurityException e) {
////			ExceptionUtil.handleException(e);
////			return false;
////		}
////	}
//
//	/**
//	 * 
//	 */
//	protected void handleClose() {
//		closeExecutor.run();
//	}
//
//	private void handleFrameResized() {
//		int width = getWidth();
//		int height = getHeight();
//		// we check if either the width
//		// or the height are below minimum
//		boolean resize = false;
//		if (width < MIN_WIDTH) {
//			resize = true;
//			width = MIN_WIDTH;
//		}
//		if (height < MIN_HEIGHT) {
//			resize = true;
//			height = MIN_HEIGHT;
//		}
//		if (resize) {
//			setSize(width, height);
//		}
//	}
//
//	public static void main(String[] args) throws UIOPanelCreationException, FileNotFoundException, ApplicationException {
//		ApplicationManager.getInstance().init();
//		SecurityManager.setCurrentUser(new User(1));
//		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
//		Application application = ApplicationManager.getInstance().getApplication();
//
//		NewApplicationFrame f = new NewApplicationFrame(application);
//		f.setVisible(true);
//	}
//
//	public void setStatusBarText(String string) {
//		// TODO Auto-generated method stub
//		
//	}
//
//	public JKStatusBar getStatusBar() {
//		return new JKStatusBar();
//	}
//
//}
