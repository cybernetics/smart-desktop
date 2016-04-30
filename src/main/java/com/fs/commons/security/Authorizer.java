package com.fs.commons.security;
import com.fs.commons.security.exceptions.NotAllowedOperationException;
import com.fs.commons.security.exceptions.SecurityException;
public interface Authorizer {
	
	public void checkAllowed(User user,Privilige privilige)throws NotAllowedOperationException, SecurityException;

	/**
	 * check againest current user
	 * @param privilige
	 * @return
	 * @throws SecurityException 
	 */
	public void checkAllowed(Privilige privilige) throws NotAllowedOperationException,SecurityException;
	
	/**
	 * 
	 * @param privilige
	 * @return
	 * @throws SecurityException
	 */
	public boolean isValidPrivilige(Privilige privilige)throws SecurityException;
	
}
