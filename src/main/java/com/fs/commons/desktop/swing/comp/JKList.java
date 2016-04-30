package com.fs.commons.desktop.swing.comp;

import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.JList;

public class JKList<T> extends JList {
	JKListModel<T> model = new JKListModel<T>();
	
	/**
	 * 
	 */
	public JKList() {
		super();		
		setModel(model);
		init();
	}
	
	@Override
	public JKListModel getModel() {
		return model;
	}
	
	/**
	 * 
	 * @param item
	 */
	public void addItem(T item) {
		model.addItem(item);
	}
	
	/**
	 * 
	 */
	protected void init() {
	}
	
	
	public static class JKListModel<T> extends AbstractListModel {
		ArrayList<T> data = new ArrayList<T>();

		public void addItem(T item) {
			data.add(item);
			super.fireContentsChanged(this, 0, data.size());
		}

		/**
		 * @return the data
		 */
		public ArrayList<T> getData() {
			return data;
		}

		/**
		 * @param data
		 *            the data to set
		 */
		public void setData(ArrayList<T> data) {
			this.data = data;
			super.fireContentsChanged(this, 0, data.size());
		}

		/**
		 * 
		 */
		public int getSize() {
			return data.size();
		}

		/**
		 * 
		 */
		public T getElementAt(int index) {
			return data.get(index);
		}
	};
}

