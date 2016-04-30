package com.fs.commons.dao.dynamic.constraints.exceptions;

public class DuplicateDataException extends ConstraintException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idValue;

	public DuplicateDataException(String string) {
		super(string);
	}

	public DuplicateDataException(String string, String idValue) {
		super(string);
		this.idValue = idValue;

	}

	public String getIdValue() {
		return idValue;
	}

}
