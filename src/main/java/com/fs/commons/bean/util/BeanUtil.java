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
	private Class<T> beanClass;

	private static BeanUtilsBean beanUtilBean = new BeanUtilsBean();

	/**
	 * Construct a new <code>MetaBean</code> instance for the given clss.
	 * 
	 * @version 1.0
	 * @param beanClass
	 *            Class
	 * @throws BeanUtilException
	 */
	public BeanUtil(Class<T> beanClass) throws BeanUtilException {
		this.beanClass = beanClass;
	}

	/**
	 * Get keys and values from the given <code>Map</code> and set them to
	 * this bean.
	 * 
	 * @version 1.0
	 * @param propertyMap
	 *            Map
	 * @throws BeanUtilException
	 */
	public void populate(Map propertyMap, T targetObj) throws BeanUtilException {
		try {
			beanUtilBean.populate(targetObj, propertyMap);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BeanUtilException("Unable to set properties " + propertyMap + "on class " + beanClass.getName(), e);
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
	public void setProperty(String propertyName, Object value, T tagetObject) throws BeanUtilException {
		try {
			beanUtilBean.setProperty(tagetObject, propertyName, value);
		} catch (Exception e) {
			throw new BeanUtilException("Unable to set property " + propertyName + "on class " + beanClass.getName(), e);
		}
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
	public String getPorperty(String propertyName, T sourceObj) throws BeanUtilException {
		String propertyValue = null;
		try {
			propertyValue = beanUtilBean.getProperty(sourceObj, propertyName);
		} catch (Exception e) {
			throw new BeanUtilException("Failed in getting property " + propertyName);
		}
		return propertyValue;
	}

	/**
	 * 
	 * @return
	 * @throws BeanUtilException
	 */
	public T createNewInstance() throws BeanUtilException {
		try {
			return (T) beanClass.newInstance();
		} catch (Exception e) {
			throw new BeanUtilException(e);
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws BeanUtilException
	 */
	public T getBeanCopy(T source) throws BeanUtilException {
		T target = createNewInstance();
		try {
			beanUtilBean.copyProperties(source, target);
		} catch (Exception e) {
			throw new BeanUtilException(e);
		}
		return target;
	}

	/**
	 * 
	 * @param bean
	 * @return
	 * @throws BeanUtilException 
	 */
	public static Map<?, ?> descript(Object bean) throws BeanUtilException{
		try {
			return beanUtilBean.describe(bean);
		} catch (Exception e) {
			throw new BeanUtilException(e);
		}
	}
//	public static void main(String[] args) {
//	}
}