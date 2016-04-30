package com.fs.commons.dao.mysql;

import java.sql.SQLException;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.generator.AbstractDataBaseAnaylazer;
import com.fs.commons.dao.exception.DaoException;

public class MySqlDatabaseAnalasyer extends AbstractDataBaseAnaylazer {

	public MySqlDatabaseAnalasyer() throws DaoException, SQLException{
	}

	public MySqlDatabaseAnalasyer(DataSource connectionManager) throws DaoException, SQLException {
		super(connectionManager);
	}
	
	@Override
	protected String buildEmptyRowQuery(String catalogName, String tableName) {
		return "select * from `" + catalogName + "`.`" + tableName + "` limit 1";
	}
}
