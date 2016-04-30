// package com.fs.commons.desktop.swing.application;
//
// import java.awt.BorderLayout;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;
//
// import javax.swing.ImageIcon;
// import javax.swing.JTabbedPane;
//
// import com.fs.commons.application.Application;
// import com.fs.commons.application.ApplicationException;
// import com.fs.commons.application.ApplicationManager;
// import com.fs.commons.application.Module;
// import com.fs.commons.application.ui.UIOPanelCreationException;
// import com.fs.commons.application.ui.menu.Menu;
// import com.fs.commons.desktop.swing.Colors;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.panels.JKPanel;
// import com.fs.commons.desktop.swing.jtabbedpaneui.AquaBarTabbedPaneUI;
// import com.fs.commons.locale.Lables;
// import com.fs.commons.util.GeneralUtility;
//
// public class ModulePanel extends JKPanel<Object> {
//
//
// /**
// *
// */
// private static final long serialVersionUID = 1L;
// private final Module module;
//
// public ModulePanel(Module module) throws UIOPanelCreationException {
// this.module = module;
// init();
// }
//
// private void init() throws UIOPanelCreationException {
// setLayout(new BorderLayout());
// JTabbedPane tab = new JTabbedPane();
// tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
// //tab.setUI(new javax.swing.plaf.metal.*);
//
// //tab.setUI(new com.jgoodies.looks.plastic.PlasticTabbedPaneUI());
// tab.setUI(new AquaBarTabbedPaneUI());
// tab.setBackground(Colors.MENU_BUTTON_BG);
// tab.setForeground(Colors.MENU_BUTTON_FC);
// ArrayList<Menu> menus = module.getMenu();
////
// for (Menu menu : menus) {
// String title = Lables.get(menu.getName());
// JKPanel<?> panel = new MenuItemsPanel(menu);
// ImageIcon icon = GeneralUtility.getIcon(menu.getIconName());
// tab.addTab(title, icon, panel, title);
// }
// add(tab);
// }
//
// public static void main(String[] args) throws FileNotFoundException,
// ApplicationException, UIOPanelCreationException {
// ApplicationManager.getInstance().init();
// //ApplicationManager.getInstance().start();
// Application application = ApplicationManager.getInstance().getApplication();
// ArrayList<Module> modules = application.getModules();
// ModulePanel m = new ModulePanel(modules.get(7));
// SwingUtility.testPanel(m);
// }
//
// }
