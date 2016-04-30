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

public class SecurityImpl implements Authenticaor,Authorizer {

	/**
	 * 
	 */
	public User authenticate(String title, int maxRetries) throws InvalidUserException, SecurityException {
		return AuthenicationDialog.authenticateUser(null, title, maxRetries);
	}

	/**
	 * 
	 */
	public void checkAllowed(User user, Privilige privilige)throws SecurityException {
		SecurityFacade facade=new SecurityFacade();
		try {
			//Moved the admin checking to insure privliges sync with db
			if(facade.isAllowedOperation(user, privilige)){
				return;
			}
			if(user.getUserId().equals("admin")){
				return;
			}
		} catch (DaoException e) {
			throw new SecurityException(e);
		}
		throw new NotAllowedOperationException();
	}

	/**
	 * 
	 */
	public void checkAllowed(Privilige privilige) throws SecurityException{
		checkAllowed(SecurityManager.getCurrentUser(), privilige);
	}

	@Override
	public boolean isValidPrivilige(Privilige privilige)throws SecurityException {
		SecurityFacade facade=new SecurityFacade();
		Privilige priv;
		try {
			priv = facade.findPrivilige(privilige.getPriviligeId());
			return priv.getPriviligeName().equals(privilige.getPriviligeName());
		} catch (RecordNotFoundException e) {
			return false;
		} catch (DaoException e) {
			throw new SecurityException(e);
		}
		
	}
}
