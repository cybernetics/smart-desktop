/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		final Application application = ApplicationManager.getInstance().getApplication();
		final ArrayList<Module> modules = application.getModules();
		final SecurityFacade facade = new SecurityFacade();
		try {
			for (final Module module : modules) {
				facade.checkPrivlige(module.getPrivilige());
				final ArrayList<Menu> menus = module.getMenu();
				for (final Menu menu : menus) {
					facade.checkPrivlige(menu.getPrivilige());
					final ArrayList<MenuItem> items = menu.getItems();
					for (final MenuItem menuItem : items) {
						facade.checkPrivlige(menuItem.getPrivilige());
					}
				}
			}
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}

	}

}
