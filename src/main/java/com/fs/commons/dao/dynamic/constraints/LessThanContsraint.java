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
package com.fs.commons.dao.dynamic.constraints;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ValueLessThanAnotherValueException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;

public class LessThanContsraint extends Constraint {
	@Override
	public String getTypeString() {
		return "less-than";
	}

	@Override
	public String toString() {
		return getName() + "Less than Data Constraint ";
	}

	@Override
	public void validate(final Record record) throws ConstraintException, JKDataAccessException {
		final FieldMeta field1 = getFields().get(0);
		final FieldMeta field2 = getFields().get(1);
		final Float value1 = record.getField(field1.getName()).getValueAsFloat();
		final float value2 = record.getField(field2.getName()).getValueAsFloat();
		if (value1 > value2) {
			final ValueLessThanAnotherValueException ex = new ValueLessThanAnotherValueException("LESS_THAN_CONSTRAINT");
			ex.addField(record.getField(this.fields.get(0).getName()));
			ex.addField(record.getField(this.fields.get(1).getName()));
			throw ex;
		}
	}
}
