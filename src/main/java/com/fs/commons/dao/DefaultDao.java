package com.fs.commons.dao;

import com.fs.commons.dao.connection.DataSource;

public class DefaultDao extends AbstractDao {

	public DefaultDao() {
		super();
	}

	public DefaultDao(DataSource connectionManager) {
		super(connectionManager);
	}

	public DefaultDao(Session session) {
		super(session);
	}



	

}
