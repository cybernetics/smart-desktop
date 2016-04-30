package com.fs.commons.desktop.validation.exception;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.exceptions.util.ExceptionHandlerFactory;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;

public class UIValidationException extends ValidationException {
	static {
		ExceptionHandlerFactory.registerExceptionHandler(UIValidationException.class, new UIValidationExceptionHandler());
	}

	public UIValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(BindingComponent comp, Problem problem) {
		super(comp, problem);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(Exception e, BindingComponent component) {
		super(e, component);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(Exception e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(Field field) {
		super(field);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(String error, BindingComponent component) {
		super(error, component);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(String message, ConstraintException e, BindingComponent component) {
		super(message, e, component);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(String message, Exception e) {
		super(message, e);
		// TODO Auto-generated constructor stub
	}
	
	

	public UIValidationException(BindingComponent comp, Problems problems) {
		super(comp, problems);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(String message, Field field) {
		super(message, field);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
