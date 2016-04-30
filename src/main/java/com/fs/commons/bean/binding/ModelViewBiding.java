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
package com.fs.commons.bean.binding;

import java.util.Map;
import java.util.Set;

import com.fs.commons.bean.util.BeanUtil;
import com.fs.commons.bean.util.BeanUtilException;

public class ModelViewBiding {
	/**
	 *
	 * @return
	 * @throws BeanUtilException
	 */

	public static void main(final String[] args) throws BeanUtilException {
		// ModelViewBiding<ModelViewBiding> binding=new
		// ModelViewBiding<ModelViewBiding>(ModelViewBiding.class,null);
		// binding.modelToView(binding);
	}

	private final ViewContainer container;

	private final BeanUtil<Object> beanUtil;

	/**
	 *
	 * @param beanClass
	 * @param container
	 * @throws BeanUtilException
	 */
	public ModelViewBiding(final Class beanClass, final ViewContainer container) throws BeanUtilException {
		this.container = container;
		this.beanUtil = new BeanUtil<Object>(beanClass);
	}

	/**
	 *
	 * @param bean
	 * @throws BeanUtilException
	 */
	public void modelToView(final Object bean) throws BeanUtilException {
		final Map<?, ?> fields = BeanUtil.descript(bean);
		final Set<?> keys = fields.keySet();
		for (final Object name : keys) {
			final String propertyName = (String) name;
			if (!propertyName.equals("class")) {// ignore Class property
				final BindingComponent<String> comp = this.container.getViewComponent(propertyName);
				if (comp != null) {
					comp.setValue(this.beanUtil.getPorperty(propertyName, bean));
				}
			}
		}
	}

	/**
	 * create new instance
	 * 
	 * @return
	 * @throws BeanUtilException
	 */
	@SuppressWarnings("unchecked")
	public Object viewToModel() throws BeanUtilException {
		final Object o = this.beanUtil.createNewInstance();
		return viewToModel(o);
	}

	/**
	 *
	 * @param o
	 * @return
	 * @throws BeanUtilException
	 */
	@SuppressWarnings("unchecked")
	private Object viewToModel(final Object o) throws BeanUtilException {
		final Map<?, ?> components = this.container.getViewComponents();
		final Set<?> keys = components.keySet();
		for (final Object name : keys) {
			final String propertyName = (String) name;
			final Object value = this.container.getViewComponent(propertyName).getValue();
			this.beanUtil.setProperty(propertyName, value, o);
		}
		return o;
	}
}
