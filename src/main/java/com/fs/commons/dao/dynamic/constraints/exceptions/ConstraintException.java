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
package com.fs.commons.dao.dynamic.constraints.exceptions;

import java.util.ArrayList;

import com.fs.commons.dao.dynamic.constraints.Constraint;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.exception.DaoException;

public class ConstraintException extends DaoException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	Constraint constraint;

	ArrayList<Field> fields = new ArrayList<Field>();

	public ConstraintException(final String string) {
		super(string);
	}

	public ConstraintException(final String message, final Constraint constraint) {
		super(message);
		setConstraint(constraint);
	}

	/**
	 * @param field
	 */
	public void addField(final Field field) {
		this.fields.add(field);

	}

	public Constraint getConstraint() {
		return this.constraint;
	}

	/**
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return this.fields;
	}

	public void setConstraint(final Constraint constraint) {
		this.constraint = constraint;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(final ArrayList<Field> fields) {
		this.fields = fields;
	}

}
