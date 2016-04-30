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
package com.fs.commons.desktop.swing.comp;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JList;

public class JKList<T> extends JList {
	public static class JKListModel<T> extends AbstractListModel {
		/**
		 *
		 */
		private static final long serialVersionUID = 1051127452978852460L;
		ArrayList<T> data = new ArrayList<T>();

		public void addItem(final T item) {
			this.data.add(item);
			super.fireContentsChanged(this, 0, this.data.size());
		}

		/**
		 * @return the data
		 */
		public ArrayList<T> getData() {
			return this.data;
		}

		/**
		 *
		 */
		@Override
		public T getElementAt(final int index) {
			return this.data.get(index);
		}

		/**
		 *
		 */
		@Override
		public int getSize() {
			return this.data.size();
		}

		/**
		 * @param data
		 *            the data to set
		 */
		public void setData(final ArrayList<T> data) {
			this.data = data;
			super.fireContentsChanged(this, 0, data.size());
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = -3091472358334210438L;

	JKListModel<T> model = new JKListModel<T>();

	/**
	 *
	 */
	public JKList() {
		super();
		setModel(this.model);
		init();
	}

	/**
	 *
	 * @param item
	 */
	public void addItem(final T item) {
		this.model.addItem(item);
	}

	@Override
	public JKListModel getModel() {
		return this.model;
	}

	/**
	 *
	 */
	protected void init() {
	};
}
