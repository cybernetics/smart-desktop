// package com.fs.commons.desktop.swing.comp.panels;
//
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.util.ArrayList;
//
// import com.fs.commons.application.Module;
// import com.fs.commons.application.ui.menu.Menu;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKMenu;
//
// public class PnlMenu extends JKPanel{
// private final Module module;
// private final MenuSelectetionListener listener;
//
// //////////////////////////////////////////////////////////////////////
// public PnlMenu(Module module,MenuSelectetionListener listener) {
// this.module = module;
// this.listener = listener;
// init();
// }
//
// //////////////////////////////////////////////////////////////////////
// private void init() {
// ArrayList<Menu> menus = module.getMenu();
// for (int i = 0; i < menus.size(); i++) {
// final Menu menu = menus.get(i);
// if (module.getApplication().isAllowedCommand(menu.getPriviligeId(),
// menu.getName())) {
// JKMenu btnMenu = new JKMenu(menu.getName());
// int order = i + 1;
// SwingUtility.setHotKeyFoButton(btnMenu, "control " + order, "control " +
// order);
// btnMenu.setShortcutText("Ctrl " + order + "", false);
// btnMenu.setIcon(menu.getIconName());
// add(btnMenu);
// btnMenu.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// listener.menuSelected(menu);
// }
// });
// }
// }
// }
//
// public static interface MenuSelectetionListener{
// public void menuSelected(Menu menu);
// }
// }
