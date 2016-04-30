package com.fs.commons.desktop.swing;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;

import com.fs.commons.application.Application;
import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.Module;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.desktop.swing.comp.JKTabbedPane;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.User;
import com.fs.commons.util.GeneralUtility;

public class ApplicationPanel extends JKMainPanel {

	private final Application appliaction;

	public ApplicationPanel(Application appliaction) throws UIOPanelCreationException {
		this.appliaction = appliaction;
		init();
	}

	private void init() throws UIOPanelCreationException {
		setLayout(new BorderLayout());
		JTabbedPane tab = new JKTabbedPane();
		tab.setOpaque(true); 
		tab.setUI(new AquaBarTabbedPaneUI());
		ArrayList<Module> modules= appliaction.getModules();
		for (int i=0;i<modules.size();i++) {
			Module module = modules.get(i);
			String title = Lables.get(module.getModuleName());
			JKPanel panel = new ModulePanel(module);
			ImageIcon icon = GeneralUtility.getIcon(module.getIconName());
			tab.addTab(title, icon, panel, title);
		}		
		
		add(tab);
	}

	public static void main(String[] args) throws FileNotFoundException, ApplicationException, UIOPanelCreationException {
		
		ApplicationManager.getInstance().init();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		Application application = ApplicationManager.getInstance().getApplication();
		
		SecurityManager.setCurrentUser(new User(1));
		//ArrayList<Module> modules = application.getModules();
		ApplicationPanel m = new ApplicationPanel(application);
		
		SwingUtility.showPanelFrame(m,"");
		
//		SwingUtility.printInstalledLookAndFeel();
	}

}
