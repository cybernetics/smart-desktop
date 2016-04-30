package com.fs.commons.dao.connection;

public class DataSourceUtil {
	public static boolean isOracle(DataSource manager) {
		return manager.getDatabaseUrl().toLowerCase().contains("oracle");
	}
}
