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
public class MainMenu {
	ArrayList<MenuSection> sections = new ArrayList<MenuSection>();

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(MenuSection o) {
		return this.sections.add(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends MenuSection> c) {
		return this.sections.addAll(c);
	}

	/**
	 * 
	 * @see java.util.ArrayList#clear()
	 */
	public void clear() {
		this.sections.clear();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#get(int)
	 */
	public MenuSection get(int index) {
		return this.sections.get(index);
	}

	/**
	 * @param elem
	 * @return
	 * @see java.util.ArrayList#indexOf(java.lang.Object)
	 */
	public int indexOf(Object elem) {
		return this.sections.indexOf(elem);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#isEmpty()
	 */
	public boolean isEmpty() {
		return this.sections.isEmpty();
	}

	/**
	 * @param index
	 * @return
	 * @see java.util.ArrayList#remove(int)
	 */
	public MenuSection remove(int index) {
		return this.sections.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#remove(java.lang.Object)
	 */
	public boolean remove(Object o) {
		return this.sections.remove(o);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return this.sections.size();
	}

	/**
	 * @return
	 */
	public ArrayList<MenuSection> getSections() {
		return sections;
	}
}
