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

/**
 * @author u087
 *
 */
public class MenuSection {

	String name;

	String iconName;

	ArrayList<Menu> menus = new ArrayList<Menu>();

	ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

	private int priviligeId;

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(final Menu o) {
		return this.menus.add(o);
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	public boolean addAll(final int index, final Collection<? extends Menu> c) {
		return this.menus.addAll(index, c);
	}

	public void addMenuItem(final MenuItem item) {
		this.menuItems.add(item);
	}

	/**
	 *
	 * @see java.util.ArrayList#clear()
	 */
	public void clear() {
		this.menus.clear();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public Menu get(final int index) {
		return this.menus.get(index);
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	/**
	 * @return the menuItems
	 */
	public ArrayList<MenuItem> getMenuItems() {
		return this.menuItems;
	}

	/**
	 * @return
	 */
	public ArrayList<Menu> getMenus() {
		return this.menus;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the priviligeId
	 */
	public int getPriviligeId() {
		return this.priviligeId;
	}

	public void init() {
		for (int i = 0; i < this.menus.size(); i++) {
			this.menus.get(i).init();
		}
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(final String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @param menuItems
	 *            the menuItems to set
	 */
	public void setMenuItems(final ArrayList<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * @param menus
	 *            the menus to set
	 */
	public void setMenus(final ArrayList<Menu> menus) {
		this.menus = menus;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param priviligeId
	 *            the priviligeId to set
	 */
	public void setPriviligeId(final int priviligeId) {
		this.priviligeId = priviligeId;
	}

	/**
	 * @param parseInt
	 */
	public void setPrivligeId(final int priviligeId) {
		this.priviligeId = priviligeId;

	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return this.menus.size();
	}
}
