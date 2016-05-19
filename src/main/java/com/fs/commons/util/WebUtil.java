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

import java.util.HashMap;
import java.util.Map;

import com.jk.security.JKSecurityManager;
import com.jk.security.JKUser;

public class WebUtil {
	public static final String FS_HEADER_PASSWORD = "fs-password";
	public static final String FS_HEADER_USERNAME = "fs-user";
	public static final String FS_HEADER_DESKTOP = "desktop";

	/**
	 *
	 * @return
	 */
	public static Map<String, String> getDefaultHeaders() {
		final Map<String, String> map = new HashMap<String, String>();
		final JKUser user = JKSecurityManager.getCurrentUser();
		map.put(FS_HEADER_DESKTOP, "true");
		map.put(FS_HEADER_USERNAME, user.getUserId());
		map.put(FS_HEADER_PASSWORD, user.getPassword());
		return map;
	}

	/**
	 *
	 * @return
	 */
	public static String getFSWebServerUrl() {
		return System.getProperty("fs.server.url", "http://localhost:8080/fs");
	}

	/**
	 *
	 * @param path
	 * @return
	 */
	public static String getFSWebServerUrl(final String path) {
		return getFSWebServerUrl() + path;
	}
}
