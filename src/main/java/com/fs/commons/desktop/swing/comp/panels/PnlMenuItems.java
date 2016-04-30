// package com.fs.commons.desktop.swing.comp.panels;
//
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import java.util.ArrayList;
//
// import javax.swing.SwingConstants;
//
// import com.fs.commons.application.ui.menu.Menu;
// import com.fs.commons.application.ui.menu.MenuItem;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKMenuItem;
// import com.fs.commons.desktop.swing.comp.JKTitle;
//
// public class PnlMenuItems extends JKPanel {
// private final Menu menu;
// private final MenuItemSelectionListener listener;
// ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();
//
// public PnlMenuItems(Menu menu, MenuItemSelectionListener listener) {
// this.menu = menu;
// this.listener = listener;
// init();
// }
//
// private void init() {
// ArrayList<MenuItem> items = menu.getItems();
// JKTitle comp = new JKTitle(menu.getName());
// add(comp);
// menuItems.clear();
// // pnlItems.setOpaque(false);
// int rows = 1;
// for (int i = 0; i < items.size(); i++) {
// final MenuItem item = items.get(i);
// JKMenuItem btnItem = createJKMenuItem(item, listener);
// if (btnItem != null) {
// menuItems.add(item);
// if (btnItem != null) {
// int order = i;
// SwingUtility.setHotKeyFoButton(btnItem, "alt " + order, "alt " + order);
// btnItem.setShortcutText("Alt " + order + "", false);
// add(btnItem);
// rows++;
// }
// }
// }
// }
//
// // ///////////////////////////////////////////////////////
// public static JKMenuItem createJKMenuItem(final MenuItem item, final
// MenuItemSelectionListener listener) {
// if
// (item.getParentMenu().getParentModule().getApplication().isAllowedCommand(item.getPriviligeId(),
// item.getName())) {
// item.init();
// JKMenuItem btnItem = new JKMenuItem(item.getName());
//
// btnItem.setIcon(item.getIconName());
// btnItem.setHorizontalTextPosition(SwingConstants.TRAILING);
// btnItem.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// listener.menuItemSelected(item, false);
// }
// });
// btnItem.addMouseListener(new MouseAdapter() {
// @Override
// public void mouseClicked(MouseEvent e) {
// if (e.getButton() == MouseEvent.BUTTON3) {
// listener.menuItemSelected(item, true);
// }
// }
// });
// return btnItem;
// }
// return null;
// }
//
// public static interface MenuItemSelectionListener {
// public void menuItemSelected(MenuItem item, boolean optional);
// }
//
// public ArrayList<MenuItem> getMenuItems() {
// return menuItems;
// }
//
// public int getItemsCount(){
// return menuItems.size();
// }
//
// }
