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
package com.fs.commons.dao.exception;

import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.locale.Lables;

public class DaoException extends Exception {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	ArrayList<DaoException> exception = new ArrayList<DaoException>();

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
	public DaoException(final String message) {
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
	public DaoException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public DaoException(final Throwable cause) {
		super(cause);
	}

	/**
	 *
	 * @param e
	 */
	public void add(final DaoException e) {
		this.exception.add(e);
	}

	public int getErrorCode() {
		if (getCause() instanceof SQLException) {
			return ((SQLException) getCause()).getErrorCode();
		}
		return -1;
	}

	@Override
	public String getMessage() {
		final Throwable t = getCause();
		// TODO : add support for other DBMS other than mysql
		if (t instanceof SQLException) {
			final SQLException e = (SQLException) t;
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
		if (this.exception.size() > 0) {
			final StringBuffer buf = new StringBuffer();
			for (int i = 0; i < this.exception.size(); i++) {
				final DaoException e = this.exception.get(i);
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
		final Throwable t = getCause();
		if (t instanceof SQLException) {
			final SQLException e = (SQLException) t;
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
	public boolean isInvalidDatabaseName() {
		final Throwable t = getCause();
		if (t instanceof SQLException) {
			final SQLException e = (SQLException) t;
			return e.getMessage().contains("Unknown database");
		}
		return false;
	}

	/**
	 *
	 * @return
	 */
	public boolean isInvalidUsernamePassword() {
		final Throwable t = getCause();
		if (t instanceof SQLException) {
			final SQLException e = (SQLException) t;
			return e.getMessage().contains("Access denied for user");
		}
		return false;
	}
}
