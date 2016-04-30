/**
 * 
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

	/**
	 * @return the parentModule
	 */
	public Module getParentModule() {
		return parentModule;
	}

	/**
	 * @param parentModule
	 *            the parentModule to set
	 */
	public void setParentModule(Module parentModule) {
		this.parentModule = parentModule;
	}

	int priviligeId;

	/**
	 * @return the priviligeId
	 */
	public Privilige getPrivilige() {
		int privId=(getParentModule().getModuleName()+getName()).hashCode();
		return new Privilige(privId,getName(),getParentModule().getPrivilige());
	}

//	/**
//	 * @param priviligeId
//	 *            the priviligeId to set
//	 */
//	public void setPriviligeId(int priviligeId) {
//		this.priviligeId = priviligeId;
//	}

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(MenuItem o) {
		return this.items.add(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends MenuItem> c) {
		return this.items.addAll(c);
	}

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
	public MenuItem get(int index) {
		return this.items.get(index);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return this.items.size();
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
	public ArrayList<MenuItem> getItems() {
		return items;
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	public ArrayList<Menu> getGroups() {
		return groups;
	}

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @param items
	 *            the items to set
	 */
	public void setItems(ArrayList<MenuItem> items) {
		this.items = items;
	}

	public void addGroup(Menu menu) {
		groups.add(menu);

	}

	public void init() {
		new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < items.size(); i++) {
					items.get(i).init();
				}	
			}
		};
	}

	public String getFullQualifiedPath() {
		StringBuffer buf=new StringBuffer();
		buf.append(Lables.get(getParentModule().getModuleName(),true));
		buf.append("-->");
		buf.append(Lables.get(getName(),true));
		return buf.toString();
	}
}
