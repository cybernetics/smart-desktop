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
package com.fs.commons.bean.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * This class used to dynamically load and deal with beans. it contains a
 * <code>BeanUtilsBean</code> object that will do the job, it is actualy a
 * wrapper for this object.
 *
 * @author Yazan A. Jber
 * @version 1.0
 */
public class BeanUtil<T> {
	private static BeanUtilsBean beanUtilBean = new BeanUtilsBean();

	/**
	 *
	 * @param bean
	 * @return
	 * @throws BeanUtilException
	 */
	public static Map<?, ?> descript(final Object bean) throws BeanUtilException {
		try {
			return beanUtilBean.describe(bean);
		} catch (final Exception e) {
			throw new BeanUtilException(e);
		}
	}
	// public static void main(String[] args) {
	// }

	private final Class<T> beanClass;

	/**
	 * Construct a new <code>MetaBean</code> instance for the given clss.
	 *
	 * @version 1.0
	 * @param beanClass
	 *            Class
	 * @throws BeanUtilException
	 */
	public BeanUtil(final Class<T> beanClass) throws BeanUtilException {
		this.beanClass = beanClass;
	}

	/**
	 *
	 * @return
	 * @throws BeanUtilException
	 */
	public T createNewInstance() throws BeanUtilException {
		try {
			return this.beanClass.newInstance();
		} catch (final Exception e) {
			throw new BeanUtilException(e);
		}
	}

	/**
	 *
	 * @param source
	 * @return
	 * @throws BeanUtilException
	 */
	public T getBeanCopy(final T source) throws BeanUtilException {
		final T target = createNewInstance();
		try {
			beanUtilBean.copyProperties(source, target);
		} catch (final Exception e) {
			throw new BeanUtilException(e);
		}
		return target;
	}

	/**
	 * Get the value of the property with the given name.
	 *
	 * @version 1.0
	 * @param propertyName
	 *            String
	 * @return String
	 * @throws BeanUtilException
	 */
	public String getPorperty(final String propertyName, final T sourceObj) throws BeanUtilException {
		String propertyValue = null;
		try {
			propertyValue = beanUtilBean.getProperty(sourceObj, propertyName);
		} catch (final Exception e) {
			throw new BeanUtilException("Failed in getting property " + propertyName);
		}
		return propertyValue;
	}

	/**
	 * Get keys and values from the given <code>Map</code> and set them to this
	 * bean.
	 *
	 * @version 1.0
	 * @param propertyMap
	 *            Map
	 * @throws BeanUtilException
	 */
	public void populate(final Map propertyMap, final T targetObj) throws BeanUtilException {
		try {
			beanUtilBean.populate(targetObj, propertyMap);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new BeanUtilException("Unable to set properties " + propertyMap + "on class " + this.beanClass.getName(), e);
		}
	}

	/**
	 * Set the given value to the property with the given name.
	 *
	 * @version 1.0
	 * @param propertyName
	 *            String
	 * @param value
	 *            Object
	 * @throws BeanUtilException
	 */
	public void setProperty(final String propertyName, final Object value, final T tagetObject) throws BeanUtilException {
		try {
			beanUtilBean.setProperty(tagetObject, propertyName, value);
		} catch (final Exception e) {
			throw new BeanUtilException("Unable to set property " + propertyName + "on class " + this.beanClass.getName(), e);
		}
	}
}