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
	public MethodCallInfo(final String className, final String methodName, final Object... paramters) {
		this.className = className;
		this.methodName = methodName;
		this.paramters = paramters;
	}

	/**
	 *
	 * @return
	 */
	public String getClassName() {
		return this.className;
	}

	/**
	 *
	 * @return
	 */
	public Exception getException() {
		return this.exception;
	}

	/**
	 *
	 * @return
	 */
	public String getMethodName() {
		return this.methodName;
	}

	/**
	 *
	 * @return
	 */
	public Object[] getParamters() {
		return this.paramters;
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Class[] getParamtersTypes() {
		final Class[] types = new Class[this.paramters.length];
		for (int i = 0; i < types.length; i++) {
			types[i] = this.paramters[i].getClass();
		}
		return types;
	}

	/**
	 *
	 * @return
	 */
	public Object getResult() {
		if (isFailed()) {
			throw new IllegalStateException("Cannot call getResult on failed method call , "
					+ "please check isFailed() , and if failed , please call getException() for more info\n" + this.exception.getMessage());
		}
		return this.result;
	}

	/**
	 *
	 * @return
	 */
	public boolean isFailed() {
		return this.failed;
	}

	/**
	 *
	 * @param another
	 */
	public void set(final MethodCallInfo another) {
		this.failed = another.failed;
		this.exception = another.exception;
		this.result = another.result;
	}

	/**
	 *
	 * @param className
	 */
	public void setClassName(final String className) {
		this.className = className;
	}

	/**
	 *
	 * @param e
	 */
	public void setException(final Exception e) {
		this.exception = e;
		this.failed = true;
	}

	/**
	 *
	 * @param failed
	 */
	public void setFailed(final boolean failed) {
		this.failed = failed;
	}

	/**
	 *
	 * @param methodName
	 */
	public void setMethodName(final String methodName) {
		this.methodName = methodName;
	}

	/**
	 *
	 * @param param
	 */
	public void setParamters(final Object... param) {
		this.paramters = param;
	}

	/**
	 *
	 * @param result
	 */
	public void setResult(final Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		final StringBuffer b = new StringBuffer();
		b.append("Class " + getClassName() + "\n");
		b.append("Method " + getMethodName() + "\n");
		b.append("Paramters " + Arrays.toString(getParamters()) + "\n");
		b.append("Types " + Arrays.toString(getParamtersTypes()) + "\n");
		b.append("Result " + getResult() + "\n");
		b.append("Exception " + getException() + "\n");

		return b.toString();
	}

}