// package com.fs.commons.desktop.swing.comp.panels;
//
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.util.ArrayList;
//
// import com.fs.commons.application.Application;
// import com.fs.commons.application.Module;
// import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;
// import com.fs.commons.desktop.swing.Colors;
// import com.fs.commons.desktop.swing.comp.JKModule;
//
// public class PnlModules extends JKPanel {
// private final Application application;
// private final ModuleSelectionListener listener;
//
// // ////////////////////////////////////////////////////////
// public PnlModules(Application application, ModuleSelectionListener listener)
// {
// this.application = application;
// this.listener = listener;
// init();
// }
//
// private void init() {
// setGradientType(GradientType.VERTICAL_LINEAR);
// setBackground(Colors.MODULE_PANEL_BG);
// ArrayList<Module> modules = application.getModules();
// for (int i = 0; i < modules.size(); i++) {
// final Module module = modules.get(i);
// if (application.isAllowedCommand(module.getPriviligeId(),
// module.getModuleName())) {
// JKModule btnModule = new JKModule(module.getModuleName());
// int order = i + 1;
// btnModule.setShortcut("control F" + order, "Ctrl F" + order);
// btnModule.setIcon(module.getIconName());
// add(btnModule);
// btnModule.addActionListener(new ActionListener() {
// public void actionPerformed(ActionEvent e) {
// listener.moduleSelected(module);
// }
// });
// module.getMenu();// to cache the panels
// }
// }
//
// }
//
// public static interface ModuleSelectionListener {
// public void moduleSelected(Module module);
// }
// }
