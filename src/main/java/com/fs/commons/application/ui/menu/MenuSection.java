/**
 * 
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
	public boolean add(Menu o) {
		return this.menus.add(o);
	}

	public void addMenuItem(MenuItem item) {
		menuItems.add(item);
	}

	/**
	 * @return the menuItems
	 */
	public ArrayList<MenuItem> getMenuItems() {
		return this.menuItems;
	}

	/**
	 * @param menuItems
	 *            the menuItems to set
	 */
	public void setMenuItems(ArrayList<MenuItem> menuItems) {
		this.menuItems = menuItems;
	}

	/**
	 * @param index
	 * @param c
	 * @return
	 * @see java.util.ArrayList#addAll(int, java.util.Collection)
	 */
	public boolean addAll(int index, Collection<? extends Menu> c) {
		return this.menus.addAll(index, c);
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
	public Menu get(int index) {
		return this.menus.get(index);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return this.menus.size();
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return
	 */
	public ArrayList<Menu> getMenus() {
		return menus;
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @param parseInt
	 */
	public void setPrivligeId(int priviligeId) {
		this.priviligeId = priviligeId;

	}

	/**
	 * @return the priviligeId
	 */
	public int getPriviligeId() {
		return this.priviligeId;
	}

	/**
	 * @param priviligeId
	 *            the priviligeId to set
	 */
	public void setPriviligeId(int priviligeId) {
		this.priviligeId = priviligeId;
	}
	
	/**
	 * @param menus the menus to set
	 */
	public void setMenus(ArrayList<Menu> menus) {
		this.menus = menus;
	}

	public void init() {
		for (int i = 0; i < menus.size(); i++) {
			menus.get(i).init();
		}
	}	
}
