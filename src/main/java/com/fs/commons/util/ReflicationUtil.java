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
package com.fs.commons.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 *
 * @author mkiswani
 *
 * @param <T>
 */
public class ReflicationUtil<T> {

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void callMethod(final Object obj, final String methodName, final boolean includePrivateMehtods, final Object... args)
			throws InvocationTargetException {
		final Class[] intArgsClass = initParamsClasses(args);
		try {
			Class<?> current = obj.getClass();
			Method method = null;
			while (current != Object.class) {
				try {
					method = current.getDeclaredMethod(methodName, intArgsClass);
					break;
				} catch (final NoSuchMethodException ex) {
					current = current.getSuperclass();
				}
			}
			if (method == null) {
				throw new NoSuchMethodException("Mehtod is not found in " + current);
			}
			method.setAccessible(true);
			method.invoke(obj, args);
		} catch (final InvocationTargetException e) {
			throw new InvocationTargetException(e.getCause());
		} catch (final Exception e) {
			throw new InvocationTargetException(e);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void callMethod(final Object obj, final String methodName, final Object... args) throws InvocationTargetException {
		callMethod(obj, methodName, false, args);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Object createObject(final Constructor<?> constructor, final Object[] arguments) throws InstantiationException {
		Object object = null;
		try {
			object = constructor.newInstance(arguments);
			return object;
		} catch (final Exception e) {
			throw new InstantiationException();
		}
	}

	public static HashMap<Field, Object> getFieldsValues(final Object object) throws Throwable {
		final Field[] declaredFields = object.getClass().getDeclaredFields();
		final HashMap<Field, Object> map = new HashMap<Field, Object>();
		for (final Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field, field.get(object));
		}
		return map;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Class[] initParamsClasses(final Object... args) {
		int count = 0;
		Class[] intArgsClass = null;
		if (args != null) {
			intArgsClass = new Class[args.length];
			for (final Object arg : args) {
				intArgsClass[count++] = arg.getClass();
			}
		}
		return intArgsClass;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Object[] initParamsValues(final Object... args) {
		if (args == null) {
			return null;
		}
		final Object[] intArgs = new Object[args.length];
		int count = 0;
		for (final Object arg : args) {
			intArgs[count++] = arg;
		}
		return intArgs;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws InvocationTargetException {
	}

	public T getInstance(final String className) throws Exception {
		return getInstance(className, null);
	}

	/**
	 * Returns an object but the object has a default constructor
	 *
	 * @param className
	 * @param claz
	 * @return
	 * @throws Exception
	 * @author mkiswani
	 */
	public T getInstance(final String className, final Class<?> claz) throws Exception {
		if (GeneralUtility.isEmpty(className)) {
			throw new IllegalArgumentException(className + " is invaild class name");
		}
		try {
			return (T) Class.forName(className).newInstance();
		} catch (final ClassCastException e) {
			if (claz != null) {
				throw new IllegalArgumentException("Class should be subClass of " + claz.getName());
			} else {
				throw new IllegalArgumentException(e);
			}
		}
	}

	/**
	 * Returns an object but the object has a default constructor
	 *
	 * @param className
	 * @param claz
	 * @return
	 * @throws Exception
	 * @author mkiswani
	 */

	public T newInstance(final Class<?> class1, final Object... args) throws InstantiationException, NoSuchMethodException {
		final Class[] intArgsClass = initParamsClasses(args);
		final Object[] intArgs = initParamsValues(args);
		Constructor<?> intArgsConstructor;
		final Constructor[] constructors = class1.getConstructors();
		for (final Constructor<?> constructor : constructors) {
			System.out.println(constructor);
		}
		intArgsConstructor = class1.getConstructor(intArgsClass);
		return (T) createObject(intArgsConstructor, intArgs);
	}
}
