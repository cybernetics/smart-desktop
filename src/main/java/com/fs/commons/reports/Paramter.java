/**
 * 
 */
package com.fs.commons.reports;

import java.util.Properties;

/**
 * @author u087
 * 
 */
public class Paramter {
	String name;

	Properties properties = new Properties();

	private String type;

	Object value;

	private String caption;

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
	 * @param key
	 * @param value
	 * @return
	 * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
	 */
	public Object setProperty(String key, String value) {
		return this.properties.setProperty(key, value);
	}

	/**
	 * @param attribute
	 */
	public void setType(String type) {
		this.type = type;

	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{Paramter name:" + getName());
		buffer.append("," + properties.toString());
		buffer.append("}");
		return buffer.toString();
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * @param attribute
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}
}
