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

	public T getInstance(String className) throws Exception {
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
	public T getInstance(String className, Class<?> claz) throws Exception {
		if (GeneralUtility.isEmpty(className)) {
			throw new IllegalArgumentException(className + " is invaild class name");
		}
		try {
			return (T) Class.forName(className).newInstance();
		} catch (ClassCastException e) {
			if(claz != null){
				throw new IllegalArgumentException("Class should be subClass of " + claz.getName());
			}else{
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

	public  T newInstance(Class<?> class1, Object... args) throws InstantiationException, NoSuchMethodException {
		Class[] intArgsClass = initParamsClasses(args);
		Object[] intArgs = initParamsValues(args);
		Constructor<?> intArgsConstructor;
		Constructor[] constructors = class1.getConstructors();
		for (Constructor<?> constructor : constructors) {
			System.out.println(constructor);
		}
		intArgsConstructor = class1.getConstructor(intArgsClass);
		return  (T)createObject(intArgsConstructor, intArgs);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Object[] initParamsValues(Object... args) {
		if(args == null){
			return null;
		}
		Object[] intArgs = new Object[args.length];
		int count = 0;
		for (Object arg : args) {
			intArgs[count++] = arg;
  		}
		return intArgs;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Object createObject(Constructor<?> constructor, Object[] arguments) throws  InstantiationException {
		Object object = null;
		try {
			object = constructor.newInstance(arguments);
			return object;
		} catch (Exception e) {
			throw new InstantiationException();
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws InvocationTargetException {
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void callMethod(Object obj, String methodName,Object... args) throws InvocationTargetException {
		callMethod(obj, methodName,false, args);	
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void callMethod(Object obj, String methodName,boolean includePrivateMehtods,Object... args) throws InvocationTargetException {
		Class[] intArgsClass = initParamsClasses(args);
		try {
			Class<?> current = obj.getClass();
			Method method = null;
			while (current != Object.class) {
			     try {
			          method = current.getDeclaredMethod(methodName, intArgsClass);
			          break;
			     } catch (NoSuchMethodException ex) {
			          current = current.getSuperclass();
			     }
			}
			if(method == null){
				throw new NoSuchMethodException("Mehtod is not found in "+current);
			}
			method.setAccessible(true); 
			method.invoke(obj, args);
		} catch (InvocationTargetException e) {
			throw new InvocationTargetException(e.getCause());
		} catch (Exception e) {
			throw new InvocationTargetException(e);
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static Class[] initParamsClasses(Object... args) {
		int count = 0;
		Class[] intArgsClass = null;
		if(args != null){
			intArgsClass = new Class[args.length];
			for (Object arg : args) {
				intArgsClass[count++] = arg.getClass();
			}
		}
		return intArgsClass;
	}
	public static HashMap<Field, Object> getFieldsValues(Object object) throws Throwable {
		Field[] declaredFields = object.getClass().getDeclaredFields();
		HashMap<Field, Object> map = new HashMap<Field, Object>();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			map.put(field, field.get(object));
		}
		return map;
	}
}
