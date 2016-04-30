package com.fs.commons.desktop.swing.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import com.fs.commons.application.Application;
import com.fs.commons.application.Module;
import com.fs.commons.application.config.UserPreferences;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.apps.executors.CloseExecutor;
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.JKMenu;
import com.fs.commons.desktop.swing.comp.JKMenuItem;
import com.fs.commons.desktop.swing.comp.JKModule;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKStatusBar;
import com.fs.commons.desktop.swing.comp.JKTitle;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp.panels.TitledPanel;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;
import com.fs.license.client.LicenseClientFactory;
//import com.fs.commons.desktop.swing.listener.InactivityListener;

public class ApplicationFrame extends JKFrame {

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_FRAME_WIDTH = 1024;

	private static final int MAX_FAVORITES_SIZE = 10;

	private Application application;

	private Runnable closeExecutor = new CloseExecutor();

	private Hashtable<String, Integer> favoritePanels = new Hashtable<String, Integer>();;

	private ArrayList<MenuItem> history = new ArrayList<MenuItem>();
	private int currentHistoryIndex = -1;

	// UI Components
	JKPanel<?> pnlModules;
	JKPanel<?> pnlMenu;
	JKPanel<?> pnlMenuItems;

	JKButton btnExit = new JKButton("EXIT");

	JKStatusBar txtGeneralStatus = new JKStatusBar();
	JKStatusBar txtUserStatus = new JKStatusBar();
	JKStatusBar txtSystemStatus = new JKStatusBar();

	private JKPanel<?> pnlFavorit;

//	private InactivityListener inactivityListener;

	static {
		try {
			LicenseClientFactory.getClient().validateLicense();
			// } catch (LicenseException e) {
			// ExceptionUtil.handleException(e);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 */
	public ApplicationFrame() {
	}

	/**
	 * 
	 * @param application
	 */
	public ApplicationFrame(Application application) {
		setApplication(application);

	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return application;
	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(Application application) {
		this.application = application;
		init();
//		addAutoLogoutListener();
		buildHomePanel();
		showDefaultModule(application);
	}

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
////		inactivityListener = new InactivityListener(logout, application.getAutoLogoutInterval());
////		inactivityListener.start();
//	}

	/**
	 * 
	 */
	private void buildHomePanel() {
		JKPanel mainPanel = SwingUtility.buildImagePanel(GeneralUtility.getURL(application.getHomeImage()), ImagePanel.SCALED);
		//TODO : Fix the following to be non-hard coded value
		if(GeneralUtility.getURL("/resources/images/home.png")!=null){
			ImagePanel pnlImage = SwingUtility.buildImagePanel(GeneralUtility.getURL("/resources/images/home.png"),ImagePanel.ACTUAL);
			pnlImage.setBorder(BorderFactory.createLineBorder(Colors.MENU_PANEL_BG,5));
			pnlImage.setSizeToFitImage();
			mainPanel.add(pnlImage);
		}
//		mainPanel.setOpaque(true);
//		mainPanel.setGradientType(GradientType.DIAGNOLE);
//		mainPanel.setBackground(SwingUtility.getDefaultBackgroundColor());
		setHomePanel(mainPanel);

//		addFavoritePanelToHome();
	}

	/**
	 * 
	 */
	private void addFavoritePanelToHome() {
		if (pnlFavorit != null) {
			getHomePanel().remove(pnlFavorit);
		}
		pnlFavorit = buildFavoritPanelsToHomePage();
		if (pnlFavorit != null) {
			// pnlFavorit.setPreferredSize(180,
			// (int)pnlFavorit.getHeight()-200);
			// getHomePanel().add(pnlFavorit, BorderLayout.SOUTH);
			// pnlFavorit.requestFocusInWindow();
		}
		getHomePanel().invalidate();
		getHomePanel().repaint();
	}

	/**
	 * 
	 * @param application
	 */
	private void showDefaultModule(Application application) {
		Module defaultModule = application.getDefaultModule();
		if (defaultModule != null) {
			showModuleMenu(defaultModule);
		}
	}

	/**
	 * @param closeExecutor
	 *            the closeExecutor to set
	 */
	public void setCloseExecutor(Runnable closeExecutor) {
		this.closeExecutor = closeExecutor;
	}

	/**
	 * 
	 */
	protected void init() {
		// setSize(DEFAULT_FRAME_WIDTH, DEFAULT_FRAME_HEIGHT);
		setExtendedState(MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// Build UI
		add(buildNorthPanel(), BorderLayout.NORTH);

		JKPanel<?> leadingPanel = buildLeadingPanel();
		leadingPanel.setGredientColor(Color.white);
		add(new JKScrollPane(leadingPanel, JKScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JKScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.LINE_START);
		add(getSouthPanel(), BorderLayout.SOUTH);

		// Register Events
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				handleClose();
			}
		});

//		// For the inactivity
//		long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
//		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
//
//			@Override
//			public void eventDispatched(AWTEvent event) {
//				if (inactivityListener != null) {
//					inactivityListener.resetTimer();
//				}
//			}
//		}, eventMask);
	}

	private JKPanel getSouthPanel() {
		JKPanel pnlSouth = new JKPanel(new GridLayout(1, 3, 5, 5));
		pnlSouth.add(txtGeneralStatus);		
		pnlSouth.add(txtUserStatus);
		pnlSouth.add(txtSystemStatus);
		return pnlSouth;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> buildLeadingPanel() {
		if (pnlMenuItems == null) {
			pnlMenuItems = new JKMainPanel();
			pnlMenuItems.setGradientType(GradientType.VERTICAL_LINEAR);
			pnlMenuItems.setBackground(Colors.MI_PANEL_BG);
			pnlMenuItems.setBorder(BorderFactory.createRaisedBevelBorder());
			pnlMenuItems.setBackground(Colors.MI_PANEL_BG);
		}
		return pnlMenuItems;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> buildNorthPanel() {
		JKPanel<?> pnlNorth = new JKPanel<Object>();
		pnlNorth.setBorder(BorderFactory.createRaisedBevelBorder());
		pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
		// setJMenuBar(getModulesPanel());
		pnlNorth.add(getModulesPanel());
		pnlNorth.add(getMenuPanel());
		return pnlNorth;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getModulesPanel() {
		if (pnlModules == null) {
			pnlModules = new JKPanel<Object>();
			pnlModules.setGradientType(GradientType.HORIZENTAL);
			// pnlModules.setOpaque(false);
			pnlModules.setBackground(Colors.MODULE_PANEL_BG);
			ArrayList<Module> modules = application.getModules();
			for (int i = 0; i < modules.size(); i++) {
				final Module module = modules.get(i);
				if (isAllowedCommand(module.getPrivilige())) {
					JKModule btnModule = new JKModule(module.getModuleName());
					int order = i + 1;
					btnModule.setShortcut("control F" + order, "Ctrl F" + order);
					btnModule.setIcon(module.getIconName());
					// btnModule.setPrivlige(new
					// Privilige(module.getPriviligeId() ,
					// module.getModuleName()));
					pnlModules.add(btnModule);
					btnModule.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							showModuleMenu(module);
						}
					});
					module.getMenu();// to cache the panels
				}
			}
		}
		return pnlModules;
	}

	/**
	 * 
	 * @param module
	 */
	protected void showModuleMenu(Module module) {
		ArrayList<Menu> menus = module.getMenu();
		JKPanel<?> pnlMenu = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));
		// pnlMenu.setOpaque(false);
		for (int i = 0; i < menus.size(); i++) {
			final Menu menu = menus.get(i);
			if (isAllowedCommand(menu.getPrivilige())) {
				JKMenu btnMenu = new JKMenu(menu.getName());
				int order = i + 1;
				SwingUtility.setHotKeyFoButton(btnMenu, "control " + order, "control " + order);
				btnMenu.setShortcutText("Ctrl " + order + "", false);
				btnMenu.setIcon(menu.getIconName());
				// btnMenu.setPrivlige(new Privilige(menu.getPriviligeId(),
				// menu.getName()));
				pnlMenu.add(btnMenu);
				btnMenu.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						showMenuItems(menu);
					}
				});
			}
		}
		this.pnlMenu.removeAll();
		this.pnlMenuItems.removeAll();
		// JKTitle lblTitle = new JKTitle(module.getModuleName());
		// lblTitle.setBackground(Colors.MENU_PANEL_TITLE_BG);
		// lblTitle.setForeground(Colors.MENU_PANEL_TITLE_FG);
		// lblTitle.setPreferredSize(null);
		// this.pnlMenu.add(lblTitle, BorderLayout.LINE_START);
		this.pnlMenu.add(pnlMenu, BorderLayout.CENTER);
		if (module.getMenu().size() > 0) {
			// showHomePanel();
			showMenuItems(module.getMenu().get(0));
		}
	}

	/**
	 * 
	 * @param priviligeId
	 * @return
	 */
	private boolean isAllowedCommand(Privilige priv) {
		try {
			SecurityManager.getAuthorizer().checkAllowed(priv);
			return true;
		} catch (NotAllowedOperationException e) {
			System.err.println("Privlige Id : " + priv.getPriviligeId() + " , with name : " + priv.getPriviligeName() + " is not allowed");
			return false;
		} catch (SecurityException e) {
			ExceptionUtil.handleException(e);
			return false;
		}
	}

	/**
	 * 
	 * @param menu
	 */
	protected void showMenuItems(Menu menu) {
		ArrayList<MenuItem> items = menu.getItems();
		GridLayout layout = new GridLayout(1, 1, 2, 5);
		JKPanel<?> pnlItems = new JKPanel<Object>(layout);
		JKTitle comp = new JKTitle(menu.getName());
		// comp.setBackground(Colors.MI_PANEL_TITLE_BG);
		// comp.setForeground(Colors.MI_PANEL_TITLE_FG);
		pnlItems.add(comp);

		// pnlItems.setOpaque(false);
		int rows = 1;
		for (int i = 0; i < items.size(); i++) {
			final MenuItem item = items.get(i);
			JKMenuItem btnItem = createJKMenuItem(item, false);
			if (btnItem != null) {
				// btnItem.setShowProgress(true);
				if (btnItem != null) {
					int order = i;
					SwingUtility.setHotKeyFoButton(btnItem, "alt " + order, "alt " + order);
					btnItem.setShortcutText("Alt " + order + "", false);
					// btnItem.setPrivlige(new Privilige(item.getPriviligeId(),
					// item.getName()));
					pnlItems.add(btnItem);
					rows++;
				}
			}
		}
		layout.setRows(rows);
		layout.setColumns(1);
		this.pnlMenuItems.removeAll();
		JKScrollPane comp2 = new JKScrollPane(pnlItems);
		this.pnlMenuItems.add(comp2);
		showHomePanel();
		validate();
		repaint();
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private JKMenuItem createJKMenuItem(final MenuItem item, final boolean refreshModuleAndMenu) {
		if (isAllowedCommand(item.getPrivilige())) {
			item.init();
			JKMenuItem btnItem = new JKMenuItem(item.getName());

			btnItem.setIcon(item.getIconName());
			btnItem.setHorizontalTextPosition(SwingConstants.TRAILING);
			btnItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					showMenuItemPanel(item, true, refreshModuleAndMenu);
				}
			});
			btnItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						showMenuItemPanelInFrame(item);
					}
				}
			});
			return btnItem;
		}
		return null;
	}

	protected void showMenuItemPanelInFrame(MenuItem item) {
		try {
			JKPanel<?> panel = new MenuItemTitledPanel(item);
			JKFrame frame = SwingUtility.showPanelFrame(panel, item.getName());
			frame.setExtendedState(JKFrame.NORMAL);
		} catch (UIOPanelCreationException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @param item
	 * @param b
	 */
	protected void showMenuItemPanelInDialog(MenuItem item) {
		try {
			JKDialog dialog = new JKDialog(new JKFrame(), item.getName());
			dialog.setSize(new Dimension(850, 600));
			dialog.setResizable(false);
			dialog.setModal(false);
			JKPanel<?> panel = new MenuItemTitledPanel(item);
			dialog.add(panel);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
			panel.requestFocus();
		} catch (UIOPanelCreationException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * @param item
	 * @param refreshModuleAndMenu
	 */
	public void showMenuItemPanel(final MenuItem item, final boolean addToHistory, final boolean refreshModuleAndMenu) {
//		AnimationUtil.disable(this, Lables.get("Loading.."));
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					if (item.isExecutor()) {
						item.createPanel();
					} else {
						MenuItemTitledPanel panel = new MenuItemTitledPanel(item);
						if (addToHistory) {
							history.add(item);
							currentHistoryIndex = history.size() - 1;
						}
						if (refreshModuleAndMenu) {
							// this block will be called from history navigation
							showModuleMenu(item.getParentMenu().getParentModule());
							showMenuItems(item.getParentMenu());
						}
						handleShowPanel(panel);
//						captureCurrentPanelImage(panel, item.getName());
//						addOpenPanelLog(item);
						setUserStatus(item.getFullQualifiedPath());
					}
				} catch (UIOPanelCreationException e) {
					ExceptionUtil.handleException(e);
				} finally {
//					AnimationUtil.enable(ApplicationFrame.this);
				}
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * 
	 * @param panel
	 * @param name
	 */
	private void captureCurrentPanelImage(final JKPanel<?> panel, final String name) {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					File file = new File(getPanelFileName(name));
					if (!file.exists()) {
						BufferedImage image = SwingUtility.convertPanelToImage(panel, 100, 100);// robot.createScreenCapture(panel.getBounds());
						ImageIO.write(image, "gif", file);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 * 
	 * @param item
	 */
	protected void addOpenPanelLog(MenuItem item) {
		Integer value = favoritePanels.get(item.getName());
		int visitCount = 0;
		if (value != null) {
			visitCount = value;
		}
		if (visitCount < Integer.MAX_VALUE) {
			visitCount++;
			favoritePanels.put(item.getName(), visitCount);
			UserPreferences.putHashTable(getFavortiesPanelKeyName(), favoritePanels);
		}
	}

	/**
	 * 
	 * @return
	 */
	private String getFavortiesPanelKeyName() {
		return SecurityManager.getCurrentUser().getUserId() + "-panels";
	}

	/**
	 * @return
	 * 
	 */
	private JKPanel<?> buildFavoritPanelsToHomePage() {
		loadFavoritesPanel();
		ArrayList<Entry> list = GeneralUtility.sortHashTable(favoritePanels, false);
		int minSize = Math.min(list.size(), MAX_FAVORITES_SIZE);
		if (minSize > 0) {
			JKPanel<?> pnl = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));
			// pnl.setBorder(SwingUtility.createTitledBorder("Favorties_pages"));
			// pnl.setPreferredSize(new Dimension(600, 180));
			int count = 0;
			for (int i = 0; i < minSize; i++) {
				String menuItemName = (String) list.get(i).getKey();
				try {
					MenuItem menuItem = application.findMenuItem(menuItemName);
					if (menuItem != null) {

						final JKPanel<?> pnlThumb = new JKPanel<Object>(new BorderLayout());
						pnlThumb.setPreferredSize(new Dimension(150, 130));
						byte[] file = GeneralUtility.readFile(new File(getPanelFileName(menuItem.getName())));
						final JPanel pnlItem = SwingUtility.buildImagePanel(file, ImagePanel.SCALED);
						final JKMenuItem btnItem = createJKMenuItem(menuItem, true);

						if (btnItem != null && pnlItem != null) {
							btnItem.setShortcut(++count + "", count + "");
							// final Border normalBorder =
							// BorderFactory.createLineBorder(Colors.FAVORITE_ITEM_BORDER);
							final Border normalBorder = BorderFactory.createRaisedBevelBorder();
							pnlThumb.setBorder(normalBorder);
							btnItem.setBorder(null);

							pnlThumb.add(btnItem, BorderLayout.NORTH);
							pnlThumb.add(pnlItem, BorderLayout.CENTER);

							pnl.add(pnlThumb);
							MouseAdapter adapter = new MouseAdapter() {
								@Override
								public void mouseExited(MouseEvent e) {
									pnlThumb.setBorder(normalBorder);
								}

								@Override
								public void mouseEntered(MouseEvent e) {
									pnlThumb.setBorder(BorderFactory.createLoweredBevelBorder());
								}

								@Override
								public void mouseClicked(MouseEvent e) {
									btnItem.doClick();
								}
							};
							btnItem.addMouseListener(adapter);
							pnlThumb.addMouseListener(adapter);
						}
					}
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (SecurityException e) {
					// Its safe to eat this exception
				}
			}
			return pnl;
		}
		return null;
	}

	private void loadFavoritesPanel() {
		Hashtable<String, String> hashtable = UserPreferences.getHashtable(getFavortiesPanelKeyName());
		Enumeration<String> keys = hashtable.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			favoritePanels.put(key, Integer.parseInt(hashtable.get(key)));
		}

	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	protected String getPanelFileName(String name) {
		String pathName = GeneralUtility.getUserFolderPath(true) + "images";
		File path = new File(pathName);
		if (!path.exists()) {
			path.mkdir();
		}

		return pathName + "/" + name + ".gif";
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getMenuPanel() {
		if (pnlMenu == null) {
			pnlMenu = new JKMainPanel(new BorderLayout());
			pnlMenu.setBackground(Colors.MENU_PANEL_BG);
			pnlMenu.setGradientType(GradientType.HORIZENTAL);
			pnlMenu.setBorder(BorderFactory.createLineBorder(Color.white));
		}
		return pnlMenu;
	}

	/**
	 * 
	 */
	protected void handleClose() {
		closeExecutor.run();
	}

	public void setUserStatus(String status) {
		txtUserStatus.setText(status);
	}

	public void setSystemStatus(String status) {
		txtSystemStatus.setText(status);
	}

	public void setGeneralStatus(String status) {
		txtGeneralStatus.setText(status);
	}

	/**
	 * @author jk
	 */
	class MenuItemTitledPanel extends TitledPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private MenuItem item;

		public MenuItemTitledPanel(MenuItem item) throws UIOPanelCreationException {
			super(item.getName(), item.createPanel(), item.getIconName());
			this.item = item;
		}

		@Override
		protected void handleReload() {
			try {
				remove((Container) this.panel);
				AbstractDao.resetCache();
				SwingUtility.resetComponents();
				this.panel = item.createPanel(true);
				showPanel();
			} catch (UIOPanelCreationException e) {
				ExceptionUtil.handleException(e);
			}
		}

		@Override
		protected void handleAddToFavorites() {
			captureCurrentPanelImage(this, item.getName());
			favoritePanels.put(item.getName(), Integer.MAX_VALUE);
			UserPreferences.putHashTable(getFavortiesPanelKeyName(), favoritePanels);
			loadFavoritesPanel();
			addFavoritePanelToHome();
		}

		@Override
		protected void handleNext() {
			if (currentHistoryIndex < history.size() - 1) {
				currentHistoryIndex++;
				showMenuItemPanel(history.get(currentHistoryIndex), false, true);
			}
		}

		@Override
		protected void handlePreviouse() {
			if (currentHistoryIndex > 0) {
				currentHistoryIndex--;
				showMenuItemPanel(history.get(currentHistoryIndex), false, true);
			}
		}
	}

	/**
	 * 
	 * @param menuItemName
	 * @param addToHistory
	 * @param refreshModuleAndMenu
	 * @throws NotAllowedOperationException
	 * @throws SecurityException
	 */
	public void showMenuItemPanel(String menuItemName, boolean addToHistory, boolean refreshModuleAndMenu) throws NotAllowedOperationException,
			SecurityException {
		MenuItem item = getApplication().findMenuItem(menuItemName);
		showMenuItemPanel(item, addToHistory, refreshModuleAndMenu);
	}

	public void handleShowPanel(String menuItemName) throws NotAllowedOperationException, SecurityException {
		MenuItem item = getApplication().findMenuItem(menuItemName);
		showMenuItemPanel(item, false, false);
	}

}
