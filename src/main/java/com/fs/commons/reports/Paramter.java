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
	 * @return the caption
	 */
	public String getCaption() {
		return this.caption;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
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

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return this.value;
	}

	/**
	 * @param attribute
	 */
	public void setCaption(final String caption) {
		this.caption = caption;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
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
	 * @param attribute
	 */
	public void setType(final String type) {
		this.type = type;

	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(final Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer();
		buffer.append("{Paramter name:" + getName());
		buffer.append("," + this.properties.toString());
		buffer.append("}");
		return buffer.toString();
	}
}
