package com.fs.commons.apps.executors;

import java.util.ArrayList;

import com.fs.commons.application.Application;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.Module;
import com.fs.commons.application.ui.menu.Menu;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.util.ExceptionUtil;
import com.fs.security.facade.SecurityFacade;

public class PnlSyncPriviliges implements Runnable {

	@Override
	public void run() {
		Application application = ApplicationManager.getInstance().getApplication();
		ArrayList<Module> modules = application.getModules();
		SecurityFacade facade = new SecurityFacade();
		try {
			for (Module module : modules) {
				facade.checkPrivlige(module.getPrivilige());
				ArrayList<Menu> menus = module.getMenu();
				for (Menu menu : menus) {
					facade.checkPrivlige(menu.getPrivilige());
					ArrayList<MenuItem> items = menu.getItems();
					for (MenuItem menuItem : items) {
						facade.checkPrivlige(menuItem.getPrivilige());
					}
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}

	}

}
