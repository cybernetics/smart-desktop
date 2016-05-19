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

import com.fs.commons.dao.connection.JKDataSource;

/**
 * Manage all related trx managed in non-trx container
 *
 * @author jalal
 *
 */
public class JKSession {
	private Connection connection;
	private boolean commit;
	private boolean rollBackOnly;
	boolean closed;
	private JKSession parentSession;
	private JKDataSource connectionManager;

	/**
	 *
	 * @param connection
	 * @throws JKDataAccessException
	 */
	private JKSession(final Connection connection) throws JKDataAccessException {
		this.connection = connection;
		try {
			connection.setAutoCommit(false);
		} catch (final SQLException e) {
			throw new JKDataAccessException(e);
		}
	}

	public JKSession(final JKDataSource connectionManager) throws JKDataAccessException {
		this(connectionManager.getConnection());
		this.connectionManager = connectionManager;
	}

	/**
	 *
	 * @param parentSession
	 */
	public JKSession(final JKSession parentSession) {
		this.parentSession = parentSession;
	}

	/**
	 *
	 * @throws JKDataAccessException
	 */
	public void close() throws JKDataAccessException {
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
			throw new JKDataAccessException(e);
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
	 * @throws JKDataAccessException
	 */
	public void close(final boolean commit) throws JKDataAccessException {
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

	public JKDataSource getConnectionManager() {
		final JKDataSource manager = this.parentSession != null ? this.parentSession.getConnectionManager() : this.connectionManager;
		if (manager == null) {
			throw new IllegalStateException("Connectino manager connot be null");
		}
		return manager;
	}

	public JKSession getParentSession() {
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
