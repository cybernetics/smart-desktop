// package com.fs.commons.desktop.swing.application;
//
// import java.awt.BorderLayout;
// import java.awt.Dimension;
// import java.awt.FlowLayout;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.io.FileNotFoundException;
// import java.util.ArrayList;
//
// import javax.swing.BorderFactory;
// import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
// import javax.swing.SwingConstants;
//
// import com.fs.commons.application.Application;
// import com.fs.commons.application.ApplicationException;
// import com.fs.commons.application.ApplicationManager;
// import com.fs.commons.application.Module;
// import com.fs.commons.application.ui.UIOPanelCreationException;
// import com.fs.commons.application.ui.menu.Menu;
// import com.fs.commons.application.ui.menu.MenuItem;
// import com.fs.commons.desktop.swing.Colors;
// import com.fs.commons.desktop.swing.SwingUtility;
// import com.fs.commons.desktop.swing.comp.JKInternalFrame;
// import com.fs.commons.desktop.swing.comp.JKScrollPane;
// import com.fs.commons.desktop.swing.comp.JKToggleButton;
// import com.fs.commons.desktop.swing.comp.panels.JKDesktopPane;
// import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
// import com.fs.commons.desktop.swing.comp.panels.JKPanel;
// import com.fs.commons.locale.Lables;
// import com.fs.commons.util.ExceptionUtil;
// import com.fs.commons.util.GeneralUtility;
//
// public class MenuItemsPanel extends JKMainPanel {
// /**
// *
// */
// private static final long serialVersionUID = 1L;
//
// final JKDesktopPane pane = new JKDesktopPane();
//
// private final Menu menu;
//
// public MenuItemsPanel(Menu menu) throws UIOPanelCreationException {
// this.menu = menu;
// init();
// }
//
// private void init() throws UIOPanelCreationException {
// setLayout(new BorderLayout());
// setBorder(BorderFactory.createRaisedBevelBorder());
// JKPanel<?> pnl = new JKPanel<Object>();
// FlowLayout f= (FlowLayout) pnl.getLayout();
// f.setHgap(0);
// f.setVgap(2);
// pnl.setPreferredSize(new Dimension(162,-1));
// //pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
// //pnl.setLayout(new GridLayout(menu.getItems().size(),1,2,2));
// // final JTabbedPane tab = new JKTabbedPane();
// // tab.setOpaque(true);
// //
// // tab.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI());
// // tab.setTabPlacement(SwingUtility.getTabPaneLeadingPlacement());
// // tab.setBackground(Colors.MI_BUTTON_BG);
// // tab.setForeground(Colors.MI_BUTTON_FC);
// final ArrayList<MenuItem> items = menu.getItems();
// ButtonGroup group=new ButtonGroup();
//
// for (final MenuItem menuItem : items) {
// final String title = Lables.get(menuItem.getName());
// JKPanel<?> panel = new JKMainPanel();
// pane.setBackGroundImage(menu.getParentModule().getApplication().getHomeImage());
// ImageIcon icon = GeneralUtility.getIcon(menuItem.getIconName());
// final JKToggleButton item = new JKToggleButton(title);
// item.setHorizontalAlignment(SwingConstants.LEADING);
// item.setBackground(Colors.MI_BUTTON_BG);
// item.setForeground(Colors.MI_BUTTON_FC);
//
//// item.setBackgroundOnSelection(Colors.MI_BUTTON_FC);
//// item.setColorOnSelection(Colors.MI_BUTTON_BG);
//
// panel.setBackground(Colors.MI_PANEL_BG);
// item.setPreferredSize(new Dimension(160,40));
// item.setIcon(icon);
// pnl.add(item);// , icon, panel, title);
// group.add(item);
// item.addActionListener(new ActionListener() {
// JKInternalFrame frm;
//
// @Override
// public void actionPerformed(ActionEvent e) {
// try {
// JKPanel<?> createPanel = (JKPanel<?>) menuItem.createPanel();
// if(menuItem.isExecutor()){
// menuItem.createPanel();//just execute
// return;
// }
// if (frm == null) {
// frm = new JKInternalFrame(title, true, true, true, true);
//
// frm.add(new JKScrollPane(createPanel));
// //frm.setSize(500, 500);
// //frm.setLocation(counter*5,counter++*5);
//
// frm.setFrameIcon(item.getIcon());
// pane.add(frm);
// frm.pack();
//
// frm.setMaximum(true);
// //frm.setMaximum(false);
// }
// if (frm.isClosed()) {
// pane.add(frm);
// }
// frm.setVisible(true);
// frm.setSelected(true);
// } catch (Exception e1) {
// ExceptionUtil.handleException(e1);
// }
// }
// });
// }
// add(new JKScrollPane(pnl), BorderLayout.LINE_START);
// add(pane, BorderLayout.CENTER);
// }
//
// public static void main(String[] args) throws FileNotFoundException,
// ApplicationException, UIOPanelCreationException {
// ApplicationManager.getInstance().init();
// Application application = ApplicationManager.getInstance().getApplication();
// ArrayList<Module> modules = application.getModules();
// MenuItemsPanel m = new MenuItemsPanel(modules.get(4).getMenu().get(1));
// SwingUtility.testPanel(m);
// }
//
// }
