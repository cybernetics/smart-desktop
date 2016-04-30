package com.fs.commons.application.exceptions;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class ValidationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BindingComponent component;
	private Field field;
	private Problems problems=new Problems();

	public ValidationException() {
		// TODO Auto-generated constructor stub
	}

	public void setComponent(BindingComponent component) {
		this.component = component;
	}

	/**
	 * 
	 * @param error
	 */
	public ValidationException(String error, BindingComponent component) {
		super(error);
		this.component = component;
	}

	public ValidationException(Exception e, BindingComponent component) {
		super(e);
		this.component = component;
	}

	public ValidationException(String message, ConstraintException e, BindingComponent component) {
		super(message, e);
		this.component = component;
	}

	/**
	 * @param message
	 * @param e
	 */
	public ValidationException(String message, Exception e) {
		super(message, e);
	}

	/**
	 * @param e
	 */
	public ValidationException(Exception e) {
		super(e);
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(Field field) {
		this.field = field;
	}

	/**
	 * @param string
	 */
	public ValidationException(String string) {
		super(string);
	}

	public ValidationException(Field field) {
		this.field = field;
	}

	public ValidationException(String message, Field field) {
		super(message);
		this.field = field;
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}

	public ValidationException(BindingComponent comp, Problem problem) {
		this.component=comp;
		problems.add(problem);
	}
	
	public ValidationException(BindingComponent comp, Problems problems) {
		this.component=comp;
		this.problems=problems;
	}

	/**
	 * @return the component
	 */
	public BindingComponent getComponent() {
		return this.component;
	}

	public Problems getProblems() {
		return problems;
	}

	public void setProblems(Problems problems) {
		this.problems = problems;
	}

}
