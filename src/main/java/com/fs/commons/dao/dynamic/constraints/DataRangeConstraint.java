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

import java.util.ArrayList;

import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.constraints.exceptions.DataOutOfRangeException;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;

public class DataRangeConstraint extends Constraint {
	float valueFrom;

	float valueTo;

	@Override
	public String getTypeString() {
		return "range";
	}

	public float getValueFrom() {
		return this.valueFrom;
	}

	public float getValueTo() {
		return this.valueTo;
	}

	public void setValueFrom(final float valueFrom) {
		this.valueFrom = valueFrom;
	}

	public void setValueTo(final float valueTo) {
		this.valueTo = valueTo;
	}

	@Override
	public String toString() {
		return getName() + "Data Range Constraint ";
	}

	@Override
	public void validate(final Record record) throws ConstraintException, DaoException {
		final ArrayList<FieldMeta> fields = getFields();
		for (int i = 0; i < fields.size(); i++) {
			final FieldMeta fieldMeta = fields.get(i);
			final Field field = record.getField(fieldMeta.getName());
			if (field.getValue() != null && !field.getValue().equals("")) {
				final float value = Float.parseFloat(field.getValue());
				if (value < this.valueFrom || value > this.valueTo) {
					final DataOutOfRangeException ex = new DataOutOfRangeException("OUT_OF_RANGE", this);
					ex.addField(field);
					throw ex;
				}
			}
		}
	}
}
