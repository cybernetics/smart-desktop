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
import java.util.List;
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
import com.fs.commons.dao.JKAbstractPlainDataAccess;
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
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.JKNotAllowedOperationException;
import com.jk.exceptions.handler.JKExceptionUtil;
import com.jk.license.client.LicenseClientFactory;
import com.jk.security.JKPrivilige;
import com.jk.security.JKSecurityManager;

public class ApplicationFrame extends JKFrame {

	/**
	 * @author jk
	 */
	class MenuItemTitledPanel extends TitledPanel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private final MenuItem item;

		public MenuItemTitledPanel(final MenuItem item) throws UIOPanelCreationException {
			super(item.getName(), item.createPanel(), item.getIconName());
			this.item = item;
		}

		@Override
		protected void handleAddToFavorites() {
			captureCurrentPanelImage(this, this.item.getName());
			ApplicationFrame.this.favoritePanels.put(this.item.getName(), Integer.MAX_VALUE);
			UserPreferences.putHashTable(getFavortiesPanelKeyName(), ApplicationFrame.this.favoritePanels);
			loadFavoritesPanel();
			addFavoritePanelToHome();
		}

		@Override
		protected void handleNext() {
			if (ApplicationFrame.this.currentHistoryIndex < ApplicationFrame.this.history.size() - 1) {
				ApplicationFrame.this.currentHistoryIndex++;
				showMenuItemPanel(ApplicationFrame.this.history.get(ApplicationFrame.this.currentHistoryIndex), false, true);
			}
		}

		@Override
		protected void handlePreviouse() {
			if (ApplicationFrame.this.currentHistoryIndex > 0) {
				ApplicationFrame.this.currentHistoryIndex--;
				showMenuItemPanel(ApplicationFrame.this.history.get(ApplicationFrame.this.currentHistoryIndex), false, true);
			}
		}

		@Override
		protected void handleReload() {
			try {
				remove((Container) this.panel);
				JKAbstractPlainDataAccess.resetCache();
				SwingUtility.resetComponents();
				this.panel = this.item.createPanel(true);
				showPanel();
			} catch (final UIOPanelCreationException e) {
				JKExceptionUtil.handle(e);
			}
		}
	}

	private static final long serialVersionUID = 1L;

	public static final int DEFAULT_FRAME_WIDTH = 1024;

	private static final int MAX_FAVORITES_SIZE = 10;

	static {
		try {
			LicenseClientFactory.getClient().validateLicense();
			// } catch (LicenseException e) {
			// ExceptionUtil.handle(e);
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

	private Application application;;

	private Runnable closeExecutor = new CloseExecutor();
	private final Hashtable<String, Integer> favoritePanels = new Hashtable<String, Integer>();

	private final ArrayList<MenuItem> history = new ArrayList<MenuItem>();
	private int currentHistoryIndex = -1;
	// UI Components
	JKPanel<?> pnlModules;

	JKPanel<?> pnlMenu;

	JKPanel<?> pnlMenuItems;
	JKButton btnExit = new JKButton("EXIT");
	JKStatusBar txtGeneralStatus = new JKStatusBar();

	JKStatusBar txtUserStatus = new JKStatusBar();

	// private InactivityListener inactivityListener;

	JKStatusBar txtSystemStatus = new JKStatusBar();

	private JKPanel<?> pnlFavorit;

	/**
	 *
	 */
	public ApplicationFrame() {
	}

	/**
	 *
	 * @param application
	 */
	public ApplicationFrame(final Application application) {
		setApplication(application);

	}

	/**
	 *
	 */
	private void addFavoritePanelToHome() {
		if (this.pnlFavorit != null) {
			getHomePanel().remove(this.pnlFavorit);
		}
		this.pnlFavorit = buildFavoritPanelsToHomePage();
		if (this.pnlFavorit != null) {
//			pnlFavorit.setPreferredSize(180, 200);
			getHomePanel().add(pnlFavorit, BorderLayout.SOUTH);
			pnlFavorit.requestFocusInWindow();
		}
		getHomePanel().invalidate();
		getHomePanel().repaint();
	}

	// private void addAutoLogoutListener() {
	// Action logout = new ActionAdapter() {
	// @Override
	// public void actionPerformed(ActionEvent e) {
	// try {
	// ApplicationManager.getInstance().logout();
	// } catch (ApplicationException e1) {
	// ExceptionUtil.handle(e1);
	// }
	// }
	// };
	//// inactivityListener = new InactivityListener(logout,
	// application.getAutoLogoutInterval());
	//// inactivityListener.start();
	// }

	/**
	 *
	 * @param item
	 */
	protected void addOpenPanelLog(final MenuItem item) {
		final Integer value = this.favoritePanels.get(item.getName());
		int visitCount = 0;
		if (value != null) {
			visitCount = value;
		}
		if (visitCount < Integer.MAX_VALUE) {
			visitCount++;
			this.favoritePanels.put(item.getName(), visitCount);
			UserPreferences.putHashTable(getFavortiesPanelKeyName(), this.favoritePanels);
		}
	}

	/**
	 * @return
	 *
	 */
	private JKPanel<?> buildFavoritPanelsToHomePage() {
		loadFavoritesPanel();
		final ArrayList<Entry> list = GeneralUtility.sortHashTable(this.favoritePanels, false);
		final int minSize = Math.min(list.size(), MAX_FAVORITES_SIZE);
		if (minSize > 0) {
			final JKPanel<?> pnl = new JKMainPanel(new FlowLayout(FlowLayout.CENTER));
			pnl.setBackground(Colors.FAVORITE_BG);
			 pnl.setBorder(SwingUtility.createTitledBorder("Favorties_pages"));
			 pnl.setPreferredSize(new Dimension(600, 180));
			int count = 0;
			for (int i = 0; i < minSize; i++) {
				final String menuItemName = (String) list.get(i).getKey();
				try {
					final MenuItem menuItem = this.application.findMenuItem(menuItemName);
					if (menuItem != null) {

						final JKPanel<?> pnlThumb = new JKPanel(new BorderLayout());
						pnlThumb.setPreferredSize(new Dimension(150, 130));
						final byte[] file = GeneralUtility.readFile(new File(getPanelFileName(menuItem.getName())));
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
							final MouseAdapter adapter = new MouseAdapter() {
								@Override
								public void mouseClicked(final MouseEvent e) {
									btnItem.doClick();
								}

								@Override
								public void mouseEntered(final MouseEvent e) {
									pnlThumb.setBorder(BorderFactory.createLoweredBevelBorder());
								}

								@Override
								public void mouseExited(final MouseEvent e) {
									pnlThumb.setBorder(normalBorder);
								}
							};
							btnItem.addMouseListener(adapter);
							pnlThumb.addMouseListener(adapter);
						}
					}
				} catch (final IOException e) {
					e.printStackTrace();
				} catch (final SecurityException e) {
					// Its safe to eat this exception
				}
			}
			return pnl;
		}
		return null;
	}

	/**
	 *
	 */
	private void buildHomePanel() {
		final JKPanel mainPanel = SwingUtility.buildImagePanel(GeneralUtility.getURL(this.application.getHomeImage()), ImagePanel.SCALED);
		// TODO : Fix the following to be non-hard coded value
		// if (GeneralUtility.getURL("/resources/images/home.png") != null) {
		// final ImagePanel pnlImage =
		// SwingUtility.buildImagePanel(GeneralUtility.getURL("/resources/images/home.png"),
		// ImagePanel.ACTUAL);
		// pnlImage.setBorder(BorderFactory.createLineBorder(Colors.MENU_PANEL_BG,
		// 5));
		// pnlImage.setSizeToFitImage();
		// mainPanel.add(pnlImage);
		// }
		// mainPanel.setOpaque(true);
		// mainPanel.setGradientType(GradientType.DIAGNOLE);
		// mainPanel.setBackground(SwingUtility.getDefaultBackgroundColor());
		setHomePanel(mainPanel);

		addFavoritePanelToHome();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> buildLeadingPanel() {
		if (this.pnlMenuItems == null) {
			this.pnlMenuItems = new JKMainPanel();
			this.pnlMenuItems.setGradientType(GradientType.VERTICAL_LINEAR);
			this.pnlMenuItems.setBackground(Colors.MI_PANEL_BG);
			this.pnlMenuItems.setBorder(BorderFactory.createRaisedBevelBorder());
			this.pnlMenuItems.setBackground(Colors.MI_PANEL_BG);
		}
		return this.pnlMenuItems;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> buildNorthPanel() {
		final JKPanel<?> pnlNorth = new JKPanel<Object>();
		pnlNorth.setBorder(BorderFactory.createRaisedBevelBorder());
		pnlNorth.setLayout(new BoxLayout(pnlNorth, BoxLayout.Y_AXIS));
		// setJMenuBar(getModulesPanel());
		pnlNorth.add(getModulesPanel());
		pnlNorth.add(getMenuPanel());
		return pnlNorth;
	}

	/**
	 *
	 * @param panel
	 * @param name
	 */
	private void captureCurrentPanelImage(final JKPanel<?> panel, final String name) {
		final Runnable runnable = new Runnable() {

			@Override
			public void run() {
				try {
					final File file = new File(getPanelFileName(name));
					if (!file.exists()) {
						final BufferedImage image = SwingUtility.convertPanelToImage(panel, 100, 100);// robot.createScreenCapture(panel.getBounds());
						ImageIO.write(image, "gif", file);
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 *
	 * @param item
	 * @return
	 */
	private JKMenuItem createJKMenuItem(final MenuItem item, final boolean refreshModuleAndMenu) {
		if (isAllowedCommand(item.getPrivilige())) {
			item.init();
			final JKMenuItem btnItem = new JKMenuItem(item.getName());

			btnItem.setIcon(item.getIconName());
			btnItem.setHorizontalTextPosition(SwingConstants.TRAILING);
			btnItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					showMenuItemPanel(item, true, refreshModuleAndMenu);
				}
			});
			btnItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					if (e.getButton() == MouseEvent.BUTTON3) {
						showMenuItemPanelInFrame(item);
					}
				}
			});
			return btnItem;
		}
		return null;
	}

	/**
	 * @return the application
	 */
	public Application getApplication() {
		return this.application;
	}

	/**
	 *
	 * @return
	 */
	private String getFavortiesPanelKeyName() {
		return JKSecurityManager.getCurrentUser().getUserId() + "-panels";
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> getMenuPanel() {
		if (this.pnlMenu == null) {
			this.pnlMenu = new JKMainPanel(new BorderLayout());
			this.pnlMenu.setBackground(Colors.MENU_PANEL_BG);
			this.pnlMenu.setGradientType(GradientType.HORIZENTAL);
			this.pnlMenu.setBorder(BorderFactory.createLineBorder(Color.white));
		}
		return this.pnlMenu;
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> getModulesPanel() {
		if (this.pnlModules == null) {
			this.pnlModules = new JKMainPanel();
			// this.pnlModules.setGradientType(GradientType.HORIZENTAL);
			// pnlModules.setOpaque(false);
			this.pnlModules.setBackground(Colors.MODULE_PANEL_BG);
			final List<Module> modules = this.application.getModules();
			for (int i = 0; i < modules.size(); i++) {
				final Module module = modules.get(i);
				if (isAllowedCommand(module.getPrivilige())) {
					final JKModule btnModule = new JKModule(module.getModuleName());
					final int order = i + 1;
					btnModule.setShortcut("control F" + order, "Ctrl F" + order);
					btnModule.setIcon(module.getIconName());
					// btnModule.setPrivlige(new
					// Privilige(module.getPriviligeId() ,
					// module.getModuleName()));
					this.pnlModules.add(btnModule);
					btnModule.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(final ActionEvent e) {
							showModuleMenu(module);
						}
					});
					module.getMenu();// to cache the panels
				}
			}
		}
		return this.pnlModules;
	}

	/**
	 *
	 * @param name
	 * @return
	 */
	protected String getPanelFileName(final String name) {
		final String pathName = GeneralUtility.getUserFolderPath(true) + "images";
		final File path = new File(pathName);
		if (!path.exists()) {
			path.mkdir();
		}

		return pathName + "/" + name + ".gif";
	}

	private JKPanel getSouthPanel() {
		final JKPanel pnlSouth = new JKPanel(new GridLayout(1, 3, 5, 5));
		pnlSouth.setOpaque(true);
		pnlSouth.setBackground(Colors.JK_STATUS_BAR_BG);
		pnlSouth.add(this.txtGeneralStatus);
		pnlSouth.add(this.txtUserStatus);
		pnlSouth.add(this.txtSystemStatus);
		return pnlSouth;
	}

	/**
	 *
	 */
	protected void handleClose() {
		this.closeExecutor.run();
	}

	public void handleShowPanel(final String menuItemName) throws JKNotAllowedOperationException, SecurityException {
		final MenuItem item = getApplication().findMenuItem(menuItemName);
		showMenuItemPanel(item, false, false);
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

		final JKPanel<?> leadingPanel = buildLeadingPanel();
		leadingPanel.setGredientColor(Color.white);
		add(new JKScrollPane(leadingPanel, JKScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JKScrollPane.HORIZONTAL_SCROLLBAR_NEVER),
				BorderLayout.LINE_START);
		add(getSouthPanel(), BorderLayout.SOUTH);

		// Register Events
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent e) {
				handleClose();
			}
		});

		// // For the inactivity
		// long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK +
		// AWTEvent.MOUSE_EVENT_MASK + AWTEvent.KEY_EVENT_MASK;
		// Toolkit.getDefaultToolkit().addAWTEventListener(new
		// AWTEventListener() {
		//
		// @Override
		// public void eventDispatched(AWTEvent event) {
		// if (inactivityListener != null) {
		// inactivityListener.resetTimer();
		// }
		// }
		// }, eventMask);
	}

	/**
	 *
	 * @param priviligeId
	 * @return
	 */
	private boolean isAllowedCommand(final JKPrivilige priv) {
		try {
			JKSecurityManager.getAuthorizer().checkAllowed(priv);
			return true;
		} catch (final JKNotAllowedOperationException e) {
			System.err.println("Privlige Id : " + priv.getPriviligeId() + " , with name : " + priv.getPriviligeName() + " is not allowed");
			return false;
		} catch (final SecurityException e) {
			JKExceptionUtil.handle(e);
			return false;
		}
	}

	private void loadFavoritesPanel() {
		final Hashtable<String, String> hashtable = UserPreferences.getHashtable(getFavortiesPanelKeyName());
		final Enumeration<String> keys = hashtable.keys();
		while (keys.hasMoreElements()) {
			final String key = keys.nextElement();
			this.favoritePanels.put(key, Integer.parseInt(hashtable.get(key)));
		}

	}

	/**
	 * @param application
	 *            the application to set
	 */
	public void setApplication(final Application application) {
		this.application = application;
		init();
		// addAutoLogoutListener();
		buildHomePanel();
		showDefaultModule(application);
	}

	/**
	 * @param closeExecutor
	 *            the closeExecutor to set
	 */
	public void setCloseExecutor(final Runnable closeExecutor) {
		this.closeExecutor = closeExecutor;
	}

	public void setGeneralStatus(final String status) {
		this.txtGeneralStatus.setText(status);
	}

	public void setSystemStatus(final String status) {
		this.txtSystemStatus.setText(status);
	}

	public void setUserStatus(final String status) {
		this.txtUserStatus.setText(status);
	}

	/**
	 *
	 * @param application
	 */
	private void showDefaultModule(final Application application) {
		final Module defaultModule = application.getDefaultModule();
		if (defaultModule != null) {
			showModuleMenu(defaultModule);
		}
	}

	/**
	 * @param item
	 * @param refreshModuleAndMenu
	 */
	public void showMenuItemPanel(final MenuItem item, final boolean addToHistory, final boolean refreshModuleAndMenu) {
		// AnimationUtil.disable(this, Lables.get("Loading.."));
		final Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					if (item.isExecutor()) {
						item.createPanel();
					} else {
						final MenuItemTitledPanel panel = new MenuItemTitledPanel(item);
						if (addToHistory) {
							ApplicationFrame.this.history.add(item);
							ApplicationFrame.this.currentHistoryIndex = ApplicationFrame.this.history.size() - 1;
						}
						if (refreshModuleAndMenu) {
							// this block will be called from history navigation
							showModuleMenu(item.getParentMenu().getParentModule());
							showMenuItems(item.getParentMenu());
						}
						handleShowPanel(panel);
						// captureCurrentPanelImage(panel, item.getName());
						// addOpenPanelLog(item);
						setUserStatus(item.getFullQualifiedPath());
					}
				} catch (final UIOPanelCreationException e) {
					JKExceptionUtil.handle(e);
				} finally {
					// AnimationUtil.enable(ApplicationFrame.this);
				}
			}
		};
		SwingUtilities.invokeLater(runnable);
	}

	/**
	 *
	 * @param menuItemName
	 * @param addToHistory
	 * @param refreshModuleAndMenu
	 * @throws JKNotAllowedOperationException
	 * @throws SecurityException
	 */
	public void showMenuItemPanel(final String menuItemName, final boolean addToHistory, final boolean refreshModuleAndMenu)
			throws JKNotAllowedOperationException, SecurityException {
		final MenuItem item = getApplication().findMenuItem(menuItemName);
		showMenuItemPanel(item, addToHistory, refreshModuleAndMenu);
	}

	/**
	 *
	 * @param item
	 * @param b
	 */
	protected void showMenuItemPanelInDialog(final MenuItem item) {
		try {
			final JKDialog dialog = new JKDialog(new JKFrame(), item.getName());
			dialog.setSize(new Dimension(850, 600));
			dialog.setResizable(false);
			dialog.setModal(false);
			final JKPanel<?> panel = new MenuItemTitledPanel(item);
			dialog.add(panel);

			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
			panel.requestFocus();
		} catch (final UIOPanelCreationException e) {
			JKExceptionUtil.handle(e);
		}
	}

	protected void showMenuItemPanelInFrame(final MenuItem item) {
		try {
			final JKPanel<?> panel = new MenuItemTitledPanel(item);
			final JKFrame frame = SwingUtility.showPanelFrame(panel, item.getName());
			frame.setExtendedState(JKFrame.NORMAL);
		} catch (final UIOPanelCreationException e) {
			JKExceptionUtil.handle(e);
		}
	}

	/**
	 *
	 * @param menu
	 */
	protected void showMenuItems(final Menu menu) {
		final ArrayList<MenuItem> items = menu.getItems();
		final GridLayout layout = new GridLayout(1, 1, 2, 5);
		final JKPanel<?> pnlItems = new JKPanel<Object>(layout);
		final JKTitle comp = new JKTitle(menu.getName());
		// comp.setBackground(Colors.MI_PANEL_TITLE_BG);
		// comp.setForeground(Colors.MI_PANEL_TITLE_FG);
		pnlItems.add(comp);

		// pnlItems.setOpaque(false);
		int rows = 1;
		for (int i = 0; i < items.size(); i++) {
			final MenuItem item = items.get(i);
			final JKMenuItem btnItem = createJKMenuItem(item, false);
			if (btnItem != null) {
				// btnItem.setShowProgress(true);
				if (btnItem != null) {
					final int order = i;
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
		final JKScrollPane comp2 = new JKScrollPane(pnlItems);
		this.pnlMenuItems.add(comp2);
		showHomePanel();
		validate();
		repaint();
	}

	/**
	 *
	 * @param module
	 */
	protected void showModuleMenu(final Module module) {
		final ArrayList<Menu> menus = module.getMenu();
		final JKPanel<?> pnlMenu = new JKPanel<Object>(new FlowLayout(FlowLayout.CENTER));
		// pnlMenu.setOpaque(false);
		for (int i = 0; i < menus.size(); i++) {
			final Menu menu = menus.get(i);
			if (isAllowedCommand(menu.getPrivilige())) {
				final JKMenu btnMenu = new JKMenu(menu.getName());
				final int order = i + 1;
				SwingUtility.setHotKeyFoButton(btnMenu, "control " + order, "control " + order);
				btnMenu.setShortcutText("Ctrl " + order + "", false);
				btnMenu.setIcon(menu.getIconName());
				// btnMenu.setPrivlige(new Privilige(menu.getPriviligeId(),
				// menu.getName()));
				pnlMenu.add(btnMenu);
				btnMenu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
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

}
