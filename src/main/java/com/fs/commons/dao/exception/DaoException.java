package com.fs.commons.dao.exception;

import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.locale.Lables;

public class DaoException extends Exception {
	ArrayList<DaoException> exception = new ArrayList<DaoException>();

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * 
	 */
	public DaoException() {
		super();
	}

	/**
	 * Method 'DaoException'
	 * 
	 * @param message
	 *            String
	 */
	public DaoException(String message) {
		super(message);
	}

	/**
	 * Method 'DaoException'
	 * 
	 * @param message
	 *            String
	 * @param throwable
	 *            Throwable
	 */
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}


	@Override
	public String getMessage() {
		Throwable t = getCause();
		// TODO : add support for other DBMS other than mysql
		if (t instanceof SQLException) {
			SQLException e = (SQLException) t;
			System.out.println("\n\n\n" + e.getErrorCode() + " " + e.getLocalizedMessage());
			if (e.getErrorCode() == 1216) { // integrity violation od duplicate
				// key
				return "ERROR_DATABASE_CONSTRAINTS";
			}
			if (e.getErrorCode() == 1062) { // integrity violation od duplicate
				// key
				return "ERROR_DATABASE_DUPLICATE";
			}
			if (e.getErrorCode() == 1216) {
				return "ERROR_DATABASE_FOREIGN_KEY_DEP";
			}
			if (e.getErrorCode() == 1217 || e.getErrorCode() == 1451) {
				return "ERROR_DATABASE_FORIGN_KEY_CONS";
			}
			if (e.getErrorCode() == 1452) {
				return "ERROR_CANNOT_ADD_OR_UPDATE_MISSING_FORIEGN_KEY";
			}
			if (isInvalidUsernamePassword()) {
				return "INVALID_DATABASE_USERNAME_OR_PASSWORD";
			}
			if (isInvalidDatabaseName()) {
				return "INVALID_DATABASE_NAME";
			}
			return super.getMessage() + "\nError code : " + e.getErrorCode();

		}
		if (exception.size() > 0) {
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < exception.size(); i++) {
				DaoException e = exception.get(i);
				buf.append(Lables.get("ERROR") + "  " + i + " : " + e.toString() + "\n");
			}
		}
		return super.getMessage();
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isDuplicateKeyErrorCode() {
		Throwable t = getCause();
		if (t instanceof SQLException) {
			SQLException e = (SQLException) t;
			if (e.getErrorCode() == 1062) { // integrity violation od duplicate
				// key
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isInvalidUsernamePassword() {
		Throwable t = getCause();
		if (t instanceof SQLException) {
			SQLException e = (SQLException) t;
			return e.getMessage().contains("Access denied for user");
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isInvalidDatabaseName() {
		Throwable t = getCause();
		if (t instanceof SQLException) {
			SQLException e = (SQLException) t;
			return e.getMessage().contains("Unknown database");
		}
		return false;
	}

	/**
	 * 
	 * @param e
	 */
	public void add(DaoException e) {
		exception.add(e);
	}

	public int getErrorCode() {
		if (getCause() instanceof SQLException) {
			return ((SQLException) getCause()).getErrorCode();
		}
		return -1;
	}
}
