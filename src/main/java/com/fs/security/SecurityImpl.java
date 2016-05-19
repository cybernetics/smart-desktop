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

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.security.facade.SecurityFacade;
import com.fs.security.ui.dialogs.AuthenicationDialog;
import com.jk.exceptions.JKInvalidUserException;
import com.jk.exceptions.JKNotAllowedOperationException;
import com.jk.security.JKAuthenticaor;
import com.jk.security.JKAuthorizer;
import com.jk.security.JKSecurityManager;
import com.jk.security.JKPrivilige;
import com.jk.security.JKUser;

public class SecurityImpl implements JKAuthenticaor, JKAuthorizer {

	/**
	 *
	 */
	@Override
	public JKUser authenticate(final String title, final int maxRetries) throws JKInvalidUserException, SecurityException {
		return AuthenicationDialog.authenticateUser(null, title, maxRetries);
	}

	/**
	 *
	 */
	@Override
	public void checkAllowed(final JKPrivilige privilige) throws SecurityException {
		checkAllowed(JKSecurityManager.getCurrentUser(), privilige);
	}

	/**
	 *
	 */
	@Override
	public void checkAllowed(final JKUser user, final JKPrivilige privilige) throws SecurityException {
		final SecurityFacade facade = new SecurityFacade();
		try {
			// Moved the admin checking to insure privliges sync with db
			if (facade.isAllowedOperation(user, privilige)) {
				return;
			}
			if (user.getUserId().equals("admin")) {
				return;
			}
		} catch (final JKDataAccessException e) {
			throw new SecurityException(e);
		}
		throw new JKNotAllowedOperationException();
	}

	@Override
	public boolean isValidPrivilige(final JKPrivilige privilige) throws SecurityException {
		final SecurityFacade facade = new SecurityFacade();
		JKPrivilige priv;
		try {
			priv = facade.findPrivilige(privilige.getPriviligeId());
			return priv.getPriviligeName().equals(privilige.getPriviligeName());
		} catch (final JKRecordNotFoundException e) {
			return false;
		} catch (final JKDataAccessException e) {
			throw new SecurityException(e);
		}

	}
}
