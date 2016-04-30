package com.fs.commons.application.config;


public class ConfigManagerFactory {
	private static DefaultConfigManager defaultConfigManager;

	// /////////////////////////////////////////////////////////////////////
	public static DefaultConfigManager getDefaultConfigManager() {
		if (defaultConfigManager == null) {
			defaultConfigManager = new DefaultConfigManager();
		}
		return defaultConfigManager;
	}
}