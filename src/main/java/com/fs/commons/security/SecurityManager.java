package com.fs.commons.security;

import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;

public class SecurityManager {
	static Authenticaor authenticaor;

	/**
	 * @return the authenticaor
	 */
	public static Authenticaor getAuthenticaor() {
		if(authenticaor==null){
			throw new IllegalStateException("Please set Auth implmentation");
		}
		return authenticaor;
	}

	/**
	 * @param authenticaor
	 *            the authenticaor to set
	 */
	public static void setAuthenticaor(Authenticaor authenticaor) {
		SecurityManager.authenticaor = authenticaor;
	}

	/**
	 * @return the authorizer
	 */
	public static Authorizer getAuthorizer() {
		if(authorizer==null){
			throw new IllegalStateException("Please set Auth implmentation");
		}
		return authorizer;
	}

	/**
	 * @param authorizer
	 *            the authorizer to set
	 */
	public static void setAuthorizer(Authorizer authorizer) {
		SecurityManager.authorizer = authorizer;
	}

	static Authorizer authorizer;
	private static User currentUser;

	/**
	 * 
	 * @param user
	 */
	public static void setCurrentUser(User currentUser) {
		SecurityManager.currentUser = currentUser;
	}

	/**
	 * @return the currentUser
	 */
	public static User getCurrentUser() {
		if(currentUser == null){
			throw new IllegalStateException("Current user cannot be null");
		}
		return currentUser;
	}
	
	public static boolean isUserLoggedIn(){
		return currentUser!=null;
	}
	/**
	 * 
	 * @param priviligeId
	 * @param String
	 * @throws SecurityException 
	 * @throws NotAllowedOperationException 
	 */
	public static void checkAllowedPrivilige(Privilige privilige) throws NotAllowedOperationException, SecurityException{
		Authorizer auth = getAuthorizer();
		auth.checkAllowed(privilige);		
	}
}
