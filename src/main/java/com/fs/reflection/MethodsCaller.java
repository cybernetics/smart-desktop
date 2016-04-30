package com.fs.reflection;

import java.io.Serializable;
import java.lang.reflect.Method;

public class MethodsCaller {
	/**
	 * 
	 * @param info
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void callMethod(MethodCallInfo info){
		try {
			Class clas = getClass().getClassLoader().loadClass(info.getClassName());
			Object object = clas.newInstance();
			Method method = clas.getMethod(info.getMethodName(), info.getParamtersTypes());
			Object result = method.invoke(object, info.getParamters());
			if(!(result instanceof Serializable)){
				throw new IllegalStateException("object "+result+" from type "+result.getClass().getName()+" is not serialzable");
			}
			info.setResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Call to "+info.getClassName()+"."+info.getMethodName()+" for the following reason : "+e.getMessage());
			info.setException(e);
		}
	}
	
	/**
	 * 
	 * @param args
	 * @throws ReflectionException
	 */
	public static void main(String[] args) throws ReflectionException {
		MethodsCaller c=new MethodsCaller();
		MethodCallInfo info=new MethodCallInfo();
		info.setClassName("test.ToBeReflected");
		info.setMethodName("sayHello");
		info.setParamters("Jalal","Ata");
		c.callMethod(info);
		System.out.println(info.getResult());
	}
}
