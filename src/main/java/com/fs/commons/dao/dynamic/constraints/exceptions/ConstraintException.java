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

	public ConstraintException(String string) {
		super(string);
	}

	public ConstraintException(String message, Constraint constraint) {
		super(message);
		setConstraint(constraint);
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	/**
	 * @param field
	 */
	public void addField(Field field) {
		fields.add(field);

	}

	/**
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return this.fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

}
