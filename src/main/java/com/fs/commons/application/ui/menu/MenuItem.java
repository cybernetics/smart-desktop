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

import java.util.Properties;
import java.util.Vector;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.application.ui.UIPanelFactory;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.UIPanelFactoryImpl;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.locale.Lables;
import com.jk.security.JKPrivilige;

/**
 * @author u087
 *
 */
public class MenuItem implements UIPanelFactory {
	String name;

	Properties properties = new Properties();

	boolean cachePanel = Boolean.valueOf(System.getProperty("fs.panel.cache", "false"));

	String iconName;

	int priviligeId;

	// boolean cacheAtStartup =
	// Boolean.valueOf(System.getProperty("fs.panel.cacheAtStartup", "true"));

	UIPanelFactory factory = new UIPanelFactoryImpl(this);

	Menu parentMenu;

	private boolean iniitialized;

	/**
	 *
	 * @see java.util.Hashtable#clear()
	 */
	public void clear() {
		this.properties.clear();
	}

	/**
	 *
	 * @param createNew
	 * @return
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createPanel() throws UIOPanelCreationException {
		return createPanel(!this.cachePanel);
	}

	/**
	 *
	 * @return
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createPanel(final boolean createNew) throws UIOPanelCreationException {
		return createPanel(getProperties(), createNew);
		// return factory.createPanel(getProperties());
	}

	/**
	 *
	 */
	@Override
	public UIPanel createPanel(final Properties prop, final boolean createNew) throws UIOPanelCreationException {
		final UIPanel pnl = this.factory.createPanel(getProperties(), createNew);
		if (pnl != null) {
			pnl.setName(getName());
			if (pnl instanceof DaoComponent) {
				((DaoComponent) pnl).setDataSource(getParentMenu().getParentModule().getDataSource());
			}
		}
		return pnl;
	}

	//////////////////////////////////////////////////////////////////////////////
	public Vector<TableMeta> getDetailTables() {
		if (!isHasDetailTables()) {
			throw new IllegalStateException("getTableMeta() is only allowed on table-meta");
		}
		final String[] tablesName = this.properties.getProperty("detail-tables").split(",");
		final Vector<TableMeta> tables = new Vector<TableMeta>();
		for (final String tableMetaName : tablesName) {
			tables.add(AbstractTableMetaFactory.getTableMeta(tableMetaName));
		}
		return tables;
	}

	public String getFullQualifiedPath() {
		final StringBuffer buf = new StringBuffer();
		buf.append(getParentMenu().getFullQualifiedPath());
		buf.append("-->");
		buf.append(Lables.get(getName(), true));
		return buf.toString();
	}

	/**
	 * @return the iconName
	 */
	public String getIconName() {
		return this.iconName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the parentMenu
	 */
	public Menu getParentMenu() {
		return this.parentMenu;
	}

	//////////////////////////////////////////////////////////////////////////////
	public JKPrivilige getPrivilige() {
		final int privId = (getParentMenu().getParentModule().toString() + getParentMenu().getName() + getName()).hashCode();
		return new JKPrivilige(privId, getName(), getParentMenu().getPrivilige());
	}

	/**
	 * @return the priviligeId
	 */
	public int getPriviligeId() {
		return this.priviligeId;
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public String getProperty(final String key) {
		return this.properties.getProperty(key);
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String, java.lang.String)
	 */
	public String getProperty(final String key, final String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	//////////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta() {
		if (!isDynamicTableMeta()) {
			throw new IllegalStateException("getTableMeta() is only allowed on table-meta");
		}
		return AbstractTableMetaFactory.getTableMeta(this.properties.getProperty("table-meta"));
	}

	/**
	 *
	 */
	public void init() {
		if (this.iniitialized) {
			return;
		}
		this.iniitialized = true;
		// UIPanelFactoryImpl factory = new UIPanelFactoryImpl();
		// to avoid executing this executor at startup
		if (this.properties.getProperty("executor") == null) {
			// if (isCachePanel()) {
			// try {
			// factory.createPanel(properties, true);
			// } catch (UIOPanelCreationException e) {
			// throw new RuntimeException(e);
			// }
			// }
		}
	}

	/**
	 * @return the cachePanel
	 */
	public boolean isCachePanel() {
		return this.cachePanel;
	}

	//////////////////////////////////////////////////////////////////////////////
	public boolean isDynamicTableMeta() {
		return this.properties.getProperty("table-meta") != null;
	}

	/**
	 * @return
	 * @see java.util.Hashtable#isEmpty()
	 */
	public boolean isEmpty() {
		return this.properties.isEmpty();
	}

	//////////////////////////////////////////////////////////////////////////////
	public boolean isExecutor() {
		return this.properties.getProperty("executor") != null;
	}

	//////////////////////////////////////////////////////////////////////////////
	public boolean isHasDetailTables() {
		return !this.properties.getProperty("detail-tables", "").equals("");
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	public Object remove(final Object key) {
		return this.properties.remove(key);
	}

	/**
	 *
	 * @param createNew
	 * @return
	 * @throws UIOPanelCreationException
	 */
	// public JKPanel createPanel(boolean createNew) throws
	// UIOPanelCreationException {
	// // if (pnl == null || createNew) {
	// // this could be happend in executor classes
	//
	// JKPanel pnl = (JKPanel) factory.createPanel(getProperties(),createNew);
	// if (pnl != null) {
	// pnl.setName(getName());
	// }
	// // }
	// return pnl;
	// }

	/**
	 * @param cachePanel
	 *            the cachePanel to set
	 */
	public void setCachePanel(final boolean cachePanel) {
		this.cachePanel = cachePanel;
	}

	// /**
	// * @return the cacheAtStartup
	// */
	// public boolean isCacheAtStartup() {
	// return cacheAtStartup;
	// }

	// /**
	// * @param cacheAtStartup
	// * the cacheAtStartup to set
	// */
	// public void setCacheAtStartup(boolean cacheAtStartup) {
	// this.cacheAtStartup = cacheAtStartup;
	// setCachePanel(cacheAtStartup);
	// }

	/**
	 * @param iconName
	 *            the iconName to set
	 */
	public void setIconName(final String iconName) {
		this.iconName = iconName;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @param parentMenu
	 *            the parentMenu to set
	 */
	public void setParentMenu(final Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	/**
	 * @param priviligeId
	 *            the priviligeId to set
	 */
	public void setPriviligeId(final int priviligeId) {
		this.priviligeId = priviligeId;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(final Properties properties) {
		this.properties = properties;
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public Object setProperty(final String key, final String value) {
		return this.properties.setProperty(key, value);
	}

	/**
	 * @return
	 * @see java.util.Hashtable#size()
	 */
	public int size() {
		return this.properties.size();
	}
}
