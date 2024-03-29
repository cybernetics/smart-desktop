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
package com.fs.commons.application.config;

import java.io.InputStream;

public class ConfigManagerFactory {
	private static DefaultConfigManager defaultConfigManager;

	// /////////////////////////////////////////////////////////////////////
	public static DefaultConfigManager getDefaultConfigManager() {
		if (defaultConfigManager == null) {
			defaultConfigManager = new DefaultConfigManager();
		}
		return defaultConfigManager;
	}

	public static DefaultConfigManager DefaultConfigManager(InputStream fileInputStream) {
		// TODO : insure who calls this method
		throw new IllegalStateException("Check me");
	}
}