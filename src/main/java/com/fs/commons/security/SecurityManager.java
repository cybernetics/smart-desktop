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
package com.fs.commons.security;

import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;

public class SecurityManager {
	static Authenticaor authenticaor;

	static Authorizer authorizer;

	private static User currentUser;

	/**
	 *
	 * @param priviligeId
	 * @param String
	 * @throws SecurityException
	 * @throws NotAllowedOperationException
	 */
	public static void checkAllowedPrivilige(final Privilige privilige) throws NotAllowedOperationException, SecurityException {
		final Authorizer auth = getAuthorizer();
		auth.checkAllowed(privilige);
	}

	/**
	 * @return the authenticaor
	 */
	public static Authenticaor getAuthenticaor() {
		if (authenticaor == null) {
			throw new IllegalStateException("Please set Auth implmentation");
		}
		return authenticaor;
	}

	/**
	 * @return the authorizer
	 */
	public static Authorizer getAuthorizer() {
		if (authorizer == null) {
			throw new IllegalStateException("Please set Auth implmentation");
		}
		return authorizer;
	}

	/**
	 * @return the currentUser
	 */
	public static User getCurrentUser() {
		if (currentUser == null) {
			throw new IllegalStateException("Current user cannot be null");
		}
		return currentUser;
	}

	public static boolean isUserLoggedIn() {
		return currentUser != null;
	}

	/**
	 * @param authenticaor
	 *            the authenticaor to set
	 */
	public static void setAuthenticaor(final Authenticaor authenticaor) {
		SecurityManager.authenticaor = authenticaor;
	}

	/**
	 * @param authorizer
	 *            the authorizer to set
	 */
	public static void setAuthorizer(final Authorizer authorizer) {
		SecurityManager.authorizer = authorizer;
	}

	/**
	 *
	 * @param user
	 */
	public static void setCurrentUser(final User currentUser) {
		SecurityManager.currentUser = currentUser;
	}
}
