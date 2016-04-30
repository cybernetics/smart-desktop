package com.fs.reflection;

import java.io.Serializable;
import java.util.Arrays;

public class MethodCallInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String className;
	private String methodName;
	private Object[] paramters;
	private boolean failed;
	private Exception exception;
	private Object result;

	/**
	 * 
	 */
	public MethodCallInfo() {
	}

	/**
	 * 
	 * @param className
	 * @param methodName
	 * @param paramters
	 */
	public MethodCallInfo(String className, String methodName, Object... paramters) {
		this.className = className;
		this.methodName = methodName;
		this.paramters = paramters;
	}

	/**
	 * 
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 
	 * @param className
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 
	 * @param methodName
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 
	 * @return
	 */
	public Object[] getParamters() {
		return paramters;
	}

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class[] getParamtersTypes() {
		Class[] types = new Class[paramters.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = paramters[i].getClass();
		}
		return types;
	}

	/**
	 * 
	 * @param param
	 */
	public void setParamters(Object... param) {
		this.paramters = param;
	}

	/**
	 * 
	 * @param e
	 */
	public void setException(Exception e) {
		this.exception = e;
		failed = true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isFailed() {
		return failed;
	}

	/**
	 * 
	 * @param failed
	 */
	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	/**
	 * 
	 * @return
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * 
	 * @param result
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	/**
	 * 
	 * @return
	 */
	public Object getResult() {
		if (isFailed()) {
			throw new IllegalStateException("Cannot call getResult on failed method call , "
					+ "please check isFailed() , and if failed , please call getException() for more info\n" + exception.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * @param another
	 */
	public void set(MethodCallInfo another) {
		this.failed = another.failed;
		this.exception = another.exception;
		this.result = another.result;
	}
	
	@Override
	public String toString() {
		StringBuffer b=new StringBuffer();
		b.append("Class "+getClassName()+"\n");
		b.append("Method "+getMethodName()+"\n");
		b.append("Paramters "+Arrays.toString(getParamters())+"\n");
		b.append("Types "+Arrays.toString(getParamtersTypes())+"\n");
		b.append("Result "+getResult()+"\n");
		b.append("Exception "+getException()+"\n");
		
		return b.toString();
	}

}