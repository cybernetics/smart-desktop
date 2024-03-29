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
package com.fs.security;

import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.desktop.swing.SwingUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

public class Startup {
	public static void main(final String[] args) {
		try {
			final SwingUtility s = new SwingUtility();
			final ApplicationManager instance = ApplicationManager.getInstance();
			instance.init();
			instance.start();
		} catch (final FileNotFoundException e) {
			JKExceptionUtil.handle(e);
		} catch (final ApplicationException e) {
			JKExceptionUtil.handle(e);
		}

	}
}
