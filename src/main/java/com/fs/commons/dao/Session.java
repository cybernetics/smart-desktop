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
	private Session(Connection connection) throws DaoException {
		this.connection = connection;
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

	/**
	 * 
	 * @param parentSession
	 */
	public Session(Session parentSession) {
		this.parentSession = parentSession;
	}

	public Session(DataSource connectionManager) throws DaoException {
		this(connectionManager.getConnection());
		this.connectionManager = connectionManager;
	}

	/**
	 * @return the commit
	 */
	public boolean isCommit() {
		return parentSession != null ? parentSession.isCommit() : commit;
	}

	/**
	 * The commit value could be overriden , but the rollbackOnly , if set to
	 * true ,it cannot be changed
	 * 
	 * @param commit
	 *            the commit to set
	 */
	protected void commit(boolean commit) {
		if (parentSession != null) {
			parentSession.commit(commit);
		} else {
			if (!commit) {
				this.rollBackOnly = true;
			} else {
				this.commit = true;
			}
		}
	}

	/**
	 * 
	 * @param commit
	 * @throws DaoException
	 */
	public void close(boolean commit) throws DaoException {
		commit(commit);
		close();
	}

	/**
	 * 
	 * @throws DaoException
	 */
	public void close() throws DaoException {
		if (parentSession != null) {
			// Just return , because this would be called from method that
			// doesn't know it has been
			// called from outer session, so we just wait until the parent
			// session close it self
			return;
		}
		try {
			if (connection == null || connection.isClosed()) {
				throw new IllegalStateException("Invalid call to Session.close on closed session");
			}
			if (isCommit() && !isRollbackOnly()) {
				connection.commit();
			} else {
				connection.rollback();
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			// the abstract resource manage will check internally on the
			// nullable and isClosed properties for the connection
			//GeneralUtility.printStackTrace();
			//System.err.println("Closing connection from session");
			connectionManager.close(connection);
			setClosed(true);
		}
	}

	/**
	 * 
	 * @return
	 */
	private boolean isRollbackOnly() {
		return rollBackOnly;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isClosed() {
		return parentSession != null ? parentSession.isClosed() : closed;
	}

	/**
	 * @param closed
	 *            the closed to set
	 */
	private void setClosed(boolean closed) {
		this.closed = closed;
	}

	public Session getParentSession() {
		return parentSession;
	}

	public DataSource getConnectionManager() {
		DataSource manager= parentSession != null ? parentSession.getConnectionManager() : connectionManager;
		if(manager==null){
			throw new IllegalStateException("Connectino manager connot be null");
		}
		return manager;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return parentSession != null ? parentSession.getConnection() : connection;
	}
}
