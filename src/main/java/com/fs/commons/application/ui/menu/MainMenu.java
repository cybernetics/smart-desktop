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
public class MainMenu {
	ArrayList<MenuSection> sections = new ArrayList<MenuSection>();

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#add(java.lang.Object)
	 */
	public boolean add(final MenuSection o) {
		return this.sections.add(o);
	}

	/**
	 * @param c
	 * @return
	 * @see java.util.ArrayList#addAll(java.util.Collection)
	 */
	public boolean addAll(final Collection<? extends MenuSection> c) {
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
	public MenuSection get(final int index) {
		return this.sections.get(index);
	}

	/**
	 * @return
	 */
	public ArrayList<MenuSection> getSections() {
		return this.sections;
	}

	/**
	 * @param elem
	 * @return
	 * @see java.util.ArrayList#indexOf(java.lang.Object)
	 */
	public int indexOf(final Object elem) {
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
	public MenuSection remove(final int index) {
		return this.sections.remove(index);
	}

	/**
	 * @param o
	 * @return
	 * @see java.util.ArrayList#remove(java.lang.Object)
	 */
	public boolean remove(final Object o) {
		return this.sections.remove(o);
	}

	/**
	 * @return
	 * @see java.util.ArrayList#size()
	 */
	public int size() {
		return this.sections.size();
	}
}
