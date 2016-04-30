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
package com.fs.commons.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.exception.DaoException;

/**
 * Manage all related trx managed in non-trx container
 *
 * @author jalal
 *
 */
public class Session {
	private Connection connection;
	private boolean commit;
	private boolean rollBackOnly;
	boolean closed;
	private Session parentSession;
	private DataSource connectionManager;

	/**
	 *
	 * @param connection
	 * @throws DaoException
	 */
	private Session(final Connection connection) throws DaoException {
		this.connection = connection;
		try {
			connection.setAutoCommit(false);
		} catch (final SQLException e) {
			throw new DaoException(e);
		}
	}

	public Session(final DataSource connectionManager) throws DaoException {
		this(connectionManager.getConnection());
		this.connectionManager = connectionManager;
	}

	/**
	 *
	 * @param parentSession
	 */
	public Session(final Session parentSession) {
		this.parentSession = parentSession;
	}

	/**
	 *
	 * @throws DaoException
	 */
	public void close() throws DaoException {
		if (this.parentSession != null) {
			// Just return , because this would be called from method that
			// doesn't know it has been
			// called from outer session, so we just wait until the parent
			// session close it self
			return;
		}
		try {
			if (this.connection == null || this.connection.isClosed()) {
				throw new IllegalStateException("Invalid call to Session.close on closed session");
			}
			if (isCommit() && !isRollbackOnly()) {
				this.connection.commit();
			} else {
				this.connection.rollback();
			}
		} catch (final SQLException e) {
			throw new DaoException(e);
		} finally {
			// the abstract resource manage will check internally on the
			// nullable and isClosed properties for the connection
			// GeneralUtility.printStackTrace();
			// System.err.println("Closing connection from session");
			this.connectionManager.close(this.connection);
			setClosed(true);
		}
	}

	/**
	 *
	 * @param commit
	 * @throws DaoException
	 */
	public void close(final boolean commit) throws DaoException {
		commit(commit);
		close();
	}

	/**
	 * The commit value could be overriden , but the rollbackOnly , if set to
	 * true ,it cannot be changed
	 *
	 * @param commit
	 *            the commit to set
	 */
	protected void commit(final boolean commit) {
		if (this.parentSession != null) {
			this.parentSession.commit(commit);
		} else {
			if (!commit) {
				this.rollBackOnly = true;
			} else {
				this.commit = true;
			}
		}
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return this.parentSession != null ? this.parentSession.getConnection() : this.connection;
	}

	public DataSource getConnectionManager() {
		final DataSource manager = this.parentSession != null ? this.parentSession.getConnectionManager() : this.connectionManager;
		if (manager == null) {
			throw new IllegalStateException("Connectino manager connot be null");
		}
		return manager;
	}

	public Session getParentSession() {
		return this.parentSession;
	}

	/**
	 *
	 * @return
	 */
	public boolean isClosed() {
		return this.parentSession != null ? this.parentSession.isClosed() : this.closed;
	}

	/**
	 * @return the commit
	 */
	public boolean isCommit() {
		return this.parentSession != null ? this.parentSession.isCommit() : this.commit;
	}

	/**
	 *
	 * @return
	 */
	private boolean isRollbackOnly() {
		return this.rollBackOnly;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	private void setClosed(final boolean closed) {
		this.closed = closed;
	}
}
