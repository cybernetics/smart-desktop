/**
 * 
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
import com.fs.commons.security.Privilige;

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

	/**
	 * @return the parentMenu
	 */
	public Menu getParentMenu() {
		return parentMenu;
	}

	/**
	 * @param parentMenu
	 *            the parentMenu to set
	 */
	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	private boolean iniitialized;

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
	 * 
	 */
	public void init() {
		if (iniitialized) {
			return;
		}
		iniitialized = true;
		// UIPanelFactoryImpl factory = new UIPanelFactoryImpl();
		// to avoid executing this executor at startup
		if (properties.getProperty("executor") == null) {
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

	/**
	 * @param cachePanel
	 *            the cachePanel to set
	 */
	public void setCachePanel(boolean cachePanel) {
		this.cachePanel = cachePanel;
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
	 * 
	 * @param createNew
	 * @return
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createPanel() throws UIOPanelCreationException {
		return createPanel(!cachePanel);
	}

	/**
	 * 
	 * @return
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createPanel(boolean createNew) throws UIOPanelCreationException {
		return createPanel(getProperties(), createNew);
		// return factory.createPanel(getProperties());
	}

	/**
	 * @return the properties
	 */
	public Properties getProperties() {
		return this.properties;
	}

	/**
	 * @param properties
	 *            the properties to set
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * 
	 * @see java.util.Hashtable#clear()
	 */
	public void clear() {
		this.properties.clear();
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String, java.lang.String)
	 */
	public String getProperty(String key, String defaultValue) {
		return this.properties.getProperty(key, defaultValue);
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}

	/**
	 * @return
	 * @see java.util.Hashtable#isEmpty()
	 */
	public boolean isEmpty() {
		return this.properties.isEmpty();
	}

	/**
	 * @param key
	 * @return
	 * @see java.util.Hashtable#remove(java.lang.Object)
	 */
	public Object remove(Object key) {
		return this.properties.remove(key);
	}

	/**
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public Object setProperty(String key, String value) {
		return this.properties.setProperty(key, value);
	}

	/**
	 * @return
	 * @see java.util.Hashtable#size()
	 */
	public int size() {
		return this.properties.size();
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
	 * 
	 */
	public UIPanel createPanel(Properties prop, boolean createNew) throws UIOPanelCreationException {
		UIPanel pnl = factory.createPanel(getProperties(), createNew);
		if (pnl != null) {
			pnl.setName(getName());
			if (pnl instanceof DaoComponent) {
				((DaoComponent) pnl).setDataSource(getParentMenu().getParentModule().getDataSource());
			}
		}
		return pnl;
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

	//////////////////////////////////////////////////////////////////////////////
	public boolean isExecutor() {
		return properties.getProperty("executor") != null;
	}

	//////////////////////////////////////////////////////////////////////////////
	public boolean isDynamicTableMeta() {
		return properties.getProperty("table-meta") != null;
	}

	//////////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta() {
		if (!isDynamicTableMeta()) {
			throw new IllegalStateException("getTableMeta() is only allowed on table-meta");
		}
		return AbstractTableMetaFactory.getTableMeta(properties.getProperty("table-meta"));
	}

	//////////////////////////////////////////////////////////////////////////////
	public Vector<TableMeta> getDetailTables() {
		if (!isHasDetailTables()) {
			throw new IllegalStateException("getTableMeta() is only allowed on table-meta");
		}
		String[] tablesName = properties.getProperty("detail-tables").split(",");
		Vector<TableMeta> tables = new Vector<TableMeta>();
		for (String tableMetaName : tablesName) {
			tables.add(AbstractTableMetaFactory.getTableMeta(tableMetaName));
		}
		return tables;
	}

	//////////////////////////////////////////////////////////////////////////////
	public boolean isHasDetailTables() {		
		return !properties.getProperty("detail-tables", "").equals("");
	}

	//////////////////////////////////////////////////////////////////////////////
	public Privilige getPrivilige() {
		int privId=(getParentMenu().getParentModule().toString() + getParentMenu().getName() + getName()).hashCode();
		return new Privilige(privId,getName(),getParentMenu().getPrivilige());
	}

	public String getFullQualifiedPath() {
		StringBuffer buf=new StringBuffer();
		buf.append(getParentMenu().getFullQualifiedPath());
		buf.append("-->");
		buf.append(Lables.get(getName(),true));
		return buf.toString();
	}
}
