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
import java.lang.reflect.Method;

public class MethodsCaller {
	/**
	 *
	 * @param args
	 * @throws ReflectionException
	 */
	public static void main(final String[] args) throws ReflectionException {
		final MethodsCaller c = new MethodsCaller();
		final MethodCallInfo info = new MethodCallInfo();
		info.setClassName("test.ToBeReflected");
		info.setMethodName("sayHello");
		info.setParamters("Jalal", "Ata");
		c.callMethod(info);
		System.out.println(info.getResult());
	}

	/**
	 *
	 * @param info
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void callMethod(final MethodCallInfo info) {
		try {
			final Class clas = getClass().getClassLoader().loadClass(info.getClassName());
			final Object object = clas.newInstance();
			final Method method = clas.getMethod(info.getMethodName(), info.getParamtersTypes());
			final Object result = method.invoke(object, info.getParamters());
			if (!(result instanceof Serializable)) {
				throw new IllegalStateException("object " + result + " from type " + result.getClass().getName() + " is not serialzable");
			}
			info.setResult(result);
		} catch (final Exception e) {
			e.printStackTrace();
			System.err.println("Call to " + info.getClassName() + "." + info.getMethodName() + " for the following reason : " + e.getMessage());
			info.setException(e);
		}
	}
}
