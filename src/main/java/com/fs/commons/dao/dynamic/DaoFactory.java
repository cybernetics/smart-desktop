package com.fs.commons.dao.dynamic;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.mysql.MysqlAbstractDao;
import com.fs.commons.dao.oracle.OracleAbstractDao;
import com.fs.commons.dao.oracle.OracleDynamicDao;

public class DaoFactory {
	// ///////////////////////////////////////////////////////////////////////////////////////
	public static DynamicDao createDynamicDao(TableMeta tableMeta) {
		DataSource dataSource = tableMeta.getDataSource();
		if (dataSource.getDatabaseUrl().toLowerCase().contains("oracle")) {
			return new OracleDynamicDao(tableMeta);
		}
		return new DynamicDao(tableMeta);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////
	public static AbstractDao createDao(DataSource dataSource) {
		String url = dataSource.getDatabaseUrl().toLowerCase();
		if (url.contains("oracle")) {
			return new OracleAbstractDao();
		}
		if(url.contains("mysql")){
			return new MysqlAbstractDao();
		}
		return new AbstractDao();
	}

	public static AbstractDao createDao() {
		return createDao(DataSourceFactory.getDefaultDataSource());
	}

	public static DynamicDao createDynamicDao(String tableMetaName) {
		return createDynamicDao(AbstractTableMetaFactory.getTableMeta(tableMetaName));
	}

}
