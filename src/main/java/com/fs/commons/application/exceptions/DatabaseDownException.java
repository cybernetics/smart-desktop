package com.fs.commons.application.exceptions;

import com.fs.commons.locale.Lables;

public class DatabaseDownException extends ServerDownException{

	private final String dbName;

	public DatabaseDownException(Exception ex, String dbName, String host, int port) {
		super(ex, host, port);
		this.dbName = dbName;
	}
	
	public String getDbName() {
		return dbName;
	}
	
	@Override
	public String getMessage() {
		return Lables.get("DATABASE_IS_DOWN")+" : ("+getDbName()+") ON Server ("+getHost()+") ";
	}

}
