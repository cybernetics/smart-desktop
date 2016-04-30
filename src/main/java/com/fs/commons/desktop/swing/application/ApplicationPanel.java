//package com.fs.commons.desktop.swing.application;
//
//import java.awt.BorderLayout;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//
//import javax.swing.ImageIcon;
//import javax.swing.JPanel;
//import javax.swing.JTabbedPane;
//
//import com.fs.commons.application.Application;
//import com.fs.commons.application.ApplicationException;
//import com.fs.commons.application.Module;
//import com.fs.commons.application.ui.UIOPanelCreationException;
//import com.fs.commons.desktop.swing.Colors;
//import com.fs.commons.desktop.swing.comp.JKTabbedPane;
//import com.fs.commons.desktop.swing.jtabbedpaneui.MyTabbedPaneUI;
//import com.fs.commons.locale.Lables;
//import com.fs.commons.util.GeneralUtility;
//import com.lowagie.text.Font;
//
//public class ApplicationPanel extends JPanel {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private final Application appliaction;
//
//	public ApplicationPanel(Application appliaction) throws UIOPanelCreationException {
//		this.appliaction = appliaction;
//		init();
//	}
//
//	private void init() throws UIOPanelCreationException {
//		setLayout(new BorderLayout());
//		JTabbedPane tab = new JKTabbedPane();
//		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
//		MyTabbedPaneUI ui = new MyTabbedPaneUI();
//		tab.setUI(ui);	
//		ui.setSelectedTabBackground(Colors.MODULE_SELECTED_BG);
//		ui.setSelectedTabForColor(Colors.MODULE_SELECTED_FC);
//		tab.setFont(tab.getFont().deriveFont(Font.BOLD));
//		tab.setBackground(Colors.MODULE_BUTTON_BG);
//		tab.setForeground(Colors.MODULE_BUTTON_FC);
//		
//		ArrayList<Module> modules= appliaction.getModules();
//		for (int i=0;i<modules.size();i++) {
//			Module module = modules.get(i);
//			String title = Lables.get(module.getModuleName());
//			ModulePanel panel = new ModulePanel(module);
//			ImageIcon icon = GeneralUtility.getIcon(module.getIconName());
//			tab.addTab(title, icon, panel, title);
//		}		
//		add(tab);
//	}
//
//	public static void main(String[] args) throws FileNotFoundException, ApplicationException, UIOPanelCreationException {		
//	}
//
//}
