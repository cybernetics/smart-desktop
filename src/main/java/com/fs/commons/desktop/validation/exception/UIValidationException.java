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
package com.fs.commons.desktop.validation.exception;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.exceptions.util.ExceptionHandlerFactory;
import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;

public class UIValidationException extends ValidationException {
	/**
	 *
	 */
	private static final long serialVersionUID = -3842491515338579076L;

	static {
		ExceptionHandlerFactory.registerExceptionHandler(UIValidationException.class, new UIValidationExceptionHandler());
	}

	public UIValidationException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final BindingComponent comp, final Problem problem) {
		super(comp, problem);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final BindingComponent comp, final Problems problems) {
		super(comp, problems);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final Exception e) {
		super(e);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final Exception e, final BindingComponent component) {
		super(e, component);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final Field field) {
		super(field);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final String string) {
		super(string);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final String error, final BindingComponent component) {
		super(error, component);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final String message, final ConstraintException e, final BindingComponent component) {
		super(message, e, component);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final String message, final Exception e) {
		super(message, e);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final String message, final Field field) {
		super(message, field);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final String message, final Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UIValidationException(final Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
