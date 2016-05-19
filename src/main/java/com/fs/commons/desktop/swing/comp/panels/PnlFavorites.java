// package com.fs.commons.desktop.swing.comp.panels;
//
// import java.awt.BorderLayout;
// import java.awt.Dimension;
// import java.awt.event.MouseAdapter;
// import java.awt.event.MouseEvent;
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Enumeration;
// import java.util.Hashtable;
// import java.util.Map.Entry;
//
// import javax.imageio.ImageIO;
// import javax.swing.BorderFactory;
// import javax.swing.JPanel;
// import javax.swing.SwingUtilities;
// import javax.swing.border.Border;
//
// import com.fs.commons.application.Application;
// import com.fs.commons.application.config.UserPreferences;
// import com.fs.commons.application.ui.menu.MenuItem;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKMenuItem;
// import
// com.fs.commons.desktop.swing.comp.panels.PnlMenuItems.MenuItemSelectionListener;
// import com.jk.security.JKSecurityManager;
// import com.fs.commons.util.GeneralUtility;
//
// public class PnlFavorites extends JKPanel {
// private Hashtable<String, Integer> favoritePanels = new Hashtable<String,
// Integer>();;
//
// private static final int MAX_FAVORITES_SIZE = 10;
//
// private final Application application;
//
// private final FavoritePanelSelectionListener listener;
//
// // ////////////////////////////////////////////////////////////////////////
// public PnlFavorites(Application application, FavoritePanelSelectionListener
// listener) {
// this.application = application;
// this.listener = listener;
// init();
// }
//
// // ////////////////////////////////////////////////////////////////////////
// private void init() {
// loadFavoritesPanel();
// ArrayList<Entry> list = GeneralUtility.sortHashTable(favoritePanels, false);
// int minSize = Math.min(list.size(), MAX_FAVORITES_SIZE);
// if (minSize > 0) {
// int count = 0;
// for (int i = 0; i < minSize; i++) {
// String menuItemName = (String) list.get(i).getKey();
// MenuItem menuItem = application.findMenuItem(menuItemName);
// if (menuItem != null) {
// try {
// final JKPanel pnlThumb = new JKPanel(new BorderLayout());
// pnlThumb.setPreferredSize(new Dimension(150, 130));
// byte[] file = GeneralUtility.readFile(new
// File(getIconPath(menuItem.getName())));
// final JPanel pnlItem = SwingUtility.buildImagePanel(file, ImagePanel.SCALED);
// final JKMenuItem btnItem = PnlMenuItems.createJKMenuItem(menuItem, new
// MenuItemSelectionListener() {
//
// @Override
// public void menuItemSelected(MenuItem item, boolean optional) {
// listener.favoriteSelected(item);
// }
// });
// if (pnlItem != null) {
// btnItem.setShortcut(++count + "", count + "");
// // final Border normalBorder =
// // BorderFactory.createLineBorder(Colors.FAVORITE_ITEM_BORDER);
// final Border normalBorder = BorderFactory.createRaisedBevelBorder();
// pnlThumb.setBorder(normalBorder);
// btnItem.setBorder(null);
//
// pnlThumb.add(btnItem, BorderLayout.NORTH);
// pnlThumb.add(pnlItem, BorderLayout.CENTER);
//
// add(pnlThumb);
// MouseAdapter adapter = new MouseAdapter() {
// @Override
// public void mouseExited(MouseEvent e) {
// pnlThumb.setBorder(normalBorder);
// }
//
// @Override
// public void mouseEntered(MouseEvent e) {
// pnlThumb.setBorder(BorderFactory.createLoweredBevelBorder());
// }
//
// @Override
// public void mouseClicked(MouseEvent e) {
// btnItem.doClick();
// }
// };
// btnItem.addMouseListener(adapter);
// pnlThumb.addMouseListener(adapter);
// }
// } catch (IOException e) {
// }
// }
// // pnl.setOpaque(false);
// }
// }
// }
//
// // ///////////////////////////////////////////////////////
// private void loadFavoritesPanel() {
// Hashtable<String, String> hashtable =
// UserPreferences.getHashtable(getKeyName());
// Enumeration<String> keys = hashtable.keys();
// while (keys.hasMoreElements()) {
// String key = keys.nextElement();
// favoritePanels.put(key, Integer.parseInt(hashtable.get(key)));
// }
// }
//
// // ////////////////////////////////////////////////////////////////////////
// public void incrementCount(String name) {
// Integer value = favoritePanels.get(name);
// int visitCount = 0;
// if (value != null) {
// visitCount = value;
// }
// if (visitCount < Integer.MAX_VALUE) {
// visitCount++;
// favoritePanels.put(name, visitCount);
// String keyName = getKeyName();
// UserPreferences.putHashTable(keyName, favoritePanels);
// }
// }
//
// // ////////////////////////////////////////////////////////////////////////
// private String getKeyName() {
// return JKSecurityManager.getCurrentUser().getUserId() + "-panels";
// }
//
// // ///////////////////////////////////////////////////////
// protected String getIconPath(String name) {
// return "images/" + name + ".gif";
// }
//
// // ///////////////////////////////////////////////////////
// public void captureCurrentPanelImage(final JKPanel panel, final String name)
// {
// Runnable runnable = new Runnable() {
//
// @Override
// public void run() {
// try {
// File file = new File("images");
// if (!file.exists()) {
// file.mkdir();
// }
// file = new File(getIconPath(name));
// if (!file.exists()) {
// BufferedImage image = SwingUtility.convertPanelToImage(panel, 100, 100);//
// robot.createScreenCapture(panel.getBounds());
// ImageIO.write(image, "gif", file);
// }
// } catch (Exception e) {
// e.printStackTrace();
// }
// }
// };
// SwingUtilities.invokeLater(runnable);
// }
//
// // ////////////////////////////////////////////////////////////////////////
// public static interface FavoritePanelSelectionListener {
// public void favoriteSelected(MenuItem item);
// }
//
// // ////////////////////////////////////////////////////////////////////////
// public void addToFavorites(JKPanel pnl, String name) {
// captureCurrentPanelImage(this, name);
// favoritePanels.put(name, Integer.MAX_VALUE);
// UserPreferences.putHashTable(getKeyName(), favoritePanels);
// loadFavoritesPanel();
// }
//
// // ////////////////////////////////////////////////////////////////////////
// public void reload() {
// removeAll();
// init();
// refreshComponents();
// }
// // ////////////////////////////////////////////////////////////////////////
// }
