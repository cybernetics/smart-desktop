package com.fs.commons.util;

import java.util.HashMap;
import java.util.Map;

import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.User;

public class WebUtil {
	public static final String FS_HEADER_PASSWORD = "fs-password";
	public static final String FS_HEADER_USERNAME = "fs-user";
	public static final String FS_HEADER_DESKTOP = "desktop";

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
	public static String getFSWebServerUrl(String path) {
		return getFSWebServerUrl() + path;
	}

	/**
	 * 
	 * @return
	 */
	public static Map<String, String> getDefaultHeaders() {
		Map<String, String> map = new HashMap<String, String>();
		User user = SecurityManager.getCurrentUser();
		map.put(FS_HEADER_DESKTOP, "true");
		map.put(FS_HEADER_USERNAME, user.getUserId());
		map.put(FS_HEADER_PASSWORD, user.getPassword());
		return map;
	}
}
