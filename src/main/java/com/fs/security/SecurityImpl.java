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
package com.fs.security;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.security.Authenticaor;
import com.fs.commons.security.Authorizer;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.User;
import com.fs.commons.security.exceptions.InvalidUserException;
import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.security.facade.SecurityFacade;
import com.fs.security.ui.dialogs.AuthenicationDialog;

public class SecurityImpl implements Authenticaor, Authorizer {

	/**
	 *
	 */
	@Override
	public User authenticate(final String title, final int maxRetries) throws InvalidUserException, SecurityException {
		return AuthenicationDialog.authenticateUser(null, title, maxRetries);
	}

	/**
	 *
	 */
	@Override
	public void checkAllowed(final Privilige privilige) throws SecurityException {
		checkAllowed(SecurityManager.getCurrentUser(), privilige);
	}

	/**
	 *
	 */
	@Override
	public void checkAllowed(final User user, final Privilige privilige) throws SecurityException {
		final SecurityFacade facade = new SecurityFacade();
		try {
			// Moved the admin checking to insure privliges sync with db
			if (facade.isAllowedOperation(user, privilige)) {
				return;
			}
			if (user.getUserId().equals("admin")) {
				return;
			}
		} catch (final DaoException e) {
			throw new SecurityException(e);
		}
		throw new NotAllowedOperationException();
	}

	@Override
	public boolean isValidPrivilige(final Privilige privilige) throws SecurityException {
		final SecurityFacade facade = new SecurityFacade();
		Privilige priv;
		try {
			priv = facade.findPrivilige(privilige.getPriviligeId());
			return priv.getPriviligeName().equals(privilige.getPriviligeName());
		} catch (final RecordNotFoundException e) {
			return false;
		} catch (final DaoException e) {
			throw new SecurityException(e);
		}

	}
}
