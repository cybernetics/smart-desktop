package com.fs.commons.dao.dynamic.meta.generator;

import java.sql.SQLException;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;

public class DefaultDatabaseAnalasyer extends AbstractDataBaseAnaylazer {

	public DefaultDatabaseAnalasyer() throws DaoException, SQLException{
	}

	public DefaultDatabaseAnalasyer(DataSource connectionManager) throws DaoException, SQLException {
		super(connectionManager);
	}
	
	@Override
	protected String buildEmptyRowQuery(String catalogName, String tableName) {
		return "select * from " + catalogName +"." + tableName ;
	}

}
