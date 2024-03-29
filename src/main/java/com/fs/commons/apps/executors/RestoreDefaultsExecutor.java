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
package com.fs.commons.apps.executors;

import com.fs.commons.application.config.UserPreferences;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.JKExceptionUtil;

public class RestoreDefaultsExecutor implements Runnable {

	@Override
	public void run() {
		try {
			UserPreferences.clear();
			GeneralUtility.clearTempFiles();
			SwingUtility.showSuccessDialog("DEFAULTS_RESTORED_SUCCESSFULLY,PLEASE_RESTART_THE_APPLICATION");
		} catch (final Exception e) {
			JKExceptionUtil.handle(e);
		}
	}

}
