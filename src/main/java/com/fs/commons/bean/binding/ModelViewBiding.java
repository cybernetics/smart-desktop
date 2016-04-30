package com.fs.commons.bean.binding;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fs.commons.bean.util.BeanUtil;
import com.fs.commons.bean.util.BeanUtilException;

public class ModelViewBiding {
	private final ViewContainer container;

	private BeanUtil<Object> beanUtil;

	/**
	 * 
	 * @param beanClass
	 * @param container
	 * @throws BeanUtilException
	 */
	public ModelViewBiding(Class beanClass, ViewContainer container) throws BeanUtilException {
		this.container = container;
		beanUtil = new BeanUtil<Object>(beanClass);
	}

	/**
	 * 
	 * @param bean
	 * @throws BeanUtilException
	 */
	public void modelToView(Object bean) throws BeanUtilException {
		Map<?, ?> fields = BeanUtil.descript(bean);
		Set<?> keys = fields.keySet();
		for (Iterator<?> iter = keys.iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			if (!propertyName.equals("class")) {// ignore Class property
				BindingComponent<String> comp = container.getViewComponent(propertyName);
				if (comp != null) {
					comp.setValue(beanUtil.getPorperty(propertyName, bean));
				}
			}
		}
	}

	/**
	 * create new instance 
	 * @return
	 * @throws BeanUtilException
	 */
	@SuppressWarnings("unchecked")
	public Object viewToModel() throws BeanUtilException {
		Object o = (Object) beanUtil.createNewInstance();
		return viewToModel(o);
	}

	/**
	 * 
	 * @param o
	 * @return
	 * @throws BeanUtilException
	 */
	@SuppressWarnings("unchecked")
	private Object viewToModel(Object o) throws BeanUtilException {
		Map<?, ?> components = container.getViewComponents();
		Set<?> keys = components.keySet();
		for (Iterator<?> iter = keys.iterator(); iter.hasNext();) {
			String propertyName = (String) iter.next();
			Object value=container.getViewComponent(propertyName).getValue();
			beanUtil.setProperty(propertyName,value, o);
		}
		return o;
	}

	/**
	 * 
	 * @return
	 * @throws BeanUtilException
	 */

	public static void main(String[] args) throws BeanUtilException {
		// ModelViewBiding<ModelViewBiding> binding=new
		// ModelViewBiding<ModelViewBiding>(ModelViewBiding.class,null);
		// binding.modelToView(binding);
	}
}
