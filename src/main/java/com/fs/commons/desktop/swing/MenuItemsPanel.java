package com.fs.commons.desktop.swing;

import java.awt.BorderLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.fs.commons.application.Application;
import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.Module;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.desktop.swing.comp.JKTabbedPane;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp.panels.TitledPanel;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class MenuItemsPanel extends JKMainPanel {

	private final Menu menu;

	public MenuItemsPanel(Menu menu) throws UIOPanelCreationException {
		this.menu = menu;
		init();
	}

	private void init() throws UIOPanelCreationException {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createRaisedBevelBorder());
		final JTabbedPane tab = new JKTabbedPane();
		tab.setOpaque(true);

		tab.setUI(new javax.swing.plaf.metal.MetalTabbedPaneUI());
		tab.setTabPlacement(SwingUtility.getTabPaneLeadingPlacement());
		tab.setBackground(Colors.MI_BUTTON_BG);
		tab.setForeground(Colors.MI_BUTTON_FC);
		final ArrayList<MenuItem> items = menu.getItems();
		for (MenuItem menuItem : items) {
			String title = Lables.get(menuItem.getName());
			JKPanel panel = new JKPanel();

			ImageIcon icon = GeneralUtility.getIcon(menuItem.getIconName());
			tab.addTab(title, icon, panel, title);
		}
		tab.addChangeListener(new ChangeListener() {

			// This method is called whenever the selected tab changes
			public void stateChanged(ChangeEvent evt) {
				// Get current tab
				int sel = tab.getSelectedIndex();
				if (sel >= 0 && sel < tab.getTabCount()) {
					// handle the index for visible components only ,
					// (priviloges)
					try {
						MenuItem menuItem = items.get(sel);
						String title = Lables.get(menuItem.getName());

						ImageIcon icon = GeneralUtility.getIcon(menuItem.getIconName());
						JKPanel panel = new TitledPanel(title, (JKPanel) menuItem.createPanel(), menuItem.getIconName());

						tab.insertTab(title, icon, panel, title, sel);
					} catch (UIOPanelCreationException e) {
						ExceptionUtil.handleException(e);
					}
				}
			}
		});
		add(tab);
	}

	public static void main(String[] args) throws FileNotFoundException, ApplicationException, UIOPanelCreationException {
		ApplicationManager.getInstance().init();
		Application application = ApplicationManager.getInstance().getApplication();
		ArrayList<Module> modules = application.getModules();
		MenuItemsPanel m = new MenuItemsPanel(modules.get(4).getMenu().get(1));
		SwingUtility.testPanel(m);
	}

}
