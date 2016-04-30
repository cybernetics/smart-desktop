/**
 * 
 */
package com.fs.commons.dao.dynamic.constraints.exceptions;

import com.fs.commons.dao.dynamic.constraints.DataRangeConstraint;

/**
 * @author u087
 * 
 */
public class DataOutOfRangeException extends ConstraintException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param string
	 */
	public DataOutOfRangeException(String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param dataRangeConstraint
	 */
	public DataOutOfRangeException(String string, DataRangeConstraint dataRangeConstraint) {
		super(string, dataRangeConstraint);
	}

}
