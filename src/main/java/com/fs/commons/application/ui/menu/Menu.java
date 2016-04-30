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
package com.fs.commons.application.ui.menu;

import java.util.ArrayList;
import java.util.Collection;

import com.fs.commons.application.Module;
import com.fs.commons.locale.Lables;
import com.fs.commons.security.Privilige;

/**
 * @author u087
 *
 */
public class Menu {
	String name;

	String iconName;

	ArrayList<MenuItem> items = new ArrayList<MenuItem>();
	ArrayList<Menu> groups = new ArrayList<Menu>();
	Module parentModule;

	int priviligeId;

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(final MenuItem o) {
		return this.items.add(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	public boolean addAll(final Collection<? extends MenuItem> c) {
		return this.items.addAll(c);
	}

	public void addGroup(final Menu menu) {
		this.groups.add(menu);

	}

	// /**
	// * @param priviligeId
	// * the priviligeId to set
	// */
	// public void setPriviligeId(int priviligeId) {
	// this.priviligeId = priviligeId;
	// }

	/**
	 *
	 * @see java.util.ArrayList#clear()
	 */
	public void clear() {
		this.items.clear();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public MenuItem get(final int index) {
		return this.items.get(index);
	}

	public String getFullQualifiedPath() {
		final StringBuffer buf = new StringBuffer();
		buf.append(Lables.get(getParentModule().getModuleName(), true));
		buf.append("-->");
		buf.append(Lables.get(getName(), true));
		return buf.toString();
	}

	public ArrayList<Menu> getGroups() {
		return this.groups;
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	/**
	 * @return
	 */
	public ArrayList<MenuItem> getItems() {
		return this.items;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the parentModule
	 */
	public Module getParentModule() {
		return this.parentModule;
	}

	/**
	 * @return the priviligeId
	 */
	public Privilige getPrivilige() {
		final int privId = (getParentModule().getModuleName() + getName()).hashCode();
		return new Privilige(privId, getName(), getParentModule().getPrivilige());
	}

	public void init() {
		new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < Menu.this.items.size(); i++) {
					Menu.this.items.get(i).init();
				}
			}
		};
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(final String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(final ArrayList<MenuItem> items) {
		this.items = items;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param parentModule
	 *            the parentModule to set
	 */
	public void setParentModule(final Module parentModule) {
		this.parentModule = parentModule;
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return this.items.size();
	}
}
