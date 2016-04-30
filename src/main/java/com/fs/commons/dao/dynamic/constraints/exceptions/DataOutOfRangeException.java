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
	public DataOutOfRangeException(final String string) {
		super(string);
	}

	/**
	 * @param string
	 * @param dataRangeConstraint
	 */
	public DataOutOfRangeException(final String string, final DataRangeConstraint dataRangeConstraint) {
		super(string, dataRangeConstraint);
	}

}
