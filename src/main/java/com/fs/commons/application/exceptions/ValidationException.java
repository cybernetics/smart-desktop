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
	private Problems problems = new Problems();

	public ValidationException() {
		// TODO Auto-generated constructor stub
	}

	public ValidationException(final BindingComponent comp, final Problem problem) {
		this.component = comp;
		this.problems.add(problem);
	}

	public ValidationException(final BindingComponent comp, final Problems problems) {
		this.component = comp;
		this.problems = problems;
	}

	/**
	 * @param e
	 */
	public ValidationException(final Exception e) {
		super(e);
	}

	public ValidationException(final Exception e, final BindingComponent component) {
		super(e);
		this.component = component;
	}

	public ValidationException(final Field field) {
		this.field = field;
	}

	/**
	 * @param string
	 */
	public ValidationException(final String string) {
		super(string);
	}

	/**
	 *
	 * @param error
	 */
	public ValidationException(final String error, final BindingComponent component) {
		super(error);
		this.component = component;
	}

	public ValidationException(final String message, final ConstraintException e, final BindingComponent component) {
		super(message, e);
		this.component = component;
	}

	/**
	 * @param message
	 * @param e
	 */
	public ValidationException(final String message, final Exception e) {
		super(message, e);
	}

	public ValidationException(final String message, final Field field) {
		super(message);
		this.field = field;
	}

	public ValidationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public ValidationException(final Throwable cause) {
		super(cause);
	}

	/**
	 * @return the component
	 */
	public BindingComponent getComponent() {
		return this.component;
	}

	/**
	 * @return the field
	 */
	public Field getField() {
		return this.field;
	}

	public Problems getProblems() {
		return this.problems;
	}

	public void setComponent(final BindingComponent component) {
		this.component = component;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(final Field field) {
		this.field = field;
	}

	public void setProblems(final Problems problems) {
		this.problems = problems;
	}

}
