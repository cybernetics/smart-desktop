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
package com.fs.commons.desktop.validation.builtin;

import com.fs.commons.locale.Lables;

public class NbBundle {

	public static String getMessage(final Class class1, final String string, final int port) {
		return Lables.get(string, port);
	}

	public static String getMessage(final Class class1, final String string, final Object[] objects) {
		return Lables.get(string, objects);
	}

	public static String getMessage(final Class class1, final String string, final String compName) {
		return Lables.get(string, compName);
	}

	public static String getMessage(final Class class1, final String string, final String compName, final String model) {
		return Lables.get(string, compName);
	}

	public static String getMessage(final Class class1, final String string, final String compName, final String curr, final String charsetName) {
		return Lables.get(string, compName);
	}

}