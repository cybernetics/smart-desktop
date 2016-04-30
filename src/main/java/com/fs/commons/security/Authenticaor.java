package com.fs.commons.security;

import com.fs.commons.security.exceptions.InvalidUserException;
import com.fs.commons.security.exceptions.SecurityException;

public interface Authenticaor {
	public User authenticate(String title, int maxRetries)throws InvalidUserException, SecurityException;
}
