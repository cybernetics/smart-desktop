package com.fs.commons.application.exceptions.util;

public interface ExceptionHandler<T extends Throwable> {
	public void handleException(T e);

	public void showError(T e, String message, String iconName, boolean throwRuntimeException);
}
