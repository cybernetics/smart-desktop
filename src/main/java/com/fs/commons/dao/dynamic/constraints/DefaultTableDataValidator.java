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

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;

public class DefaultTableDataValidator implements TableDataValidator {

	/**
	 *
	 */
	@Override
	public void validate(final TableMeta table, final Record record) throws ValidationException {
		if (record.isNewRecord() && !((IdFieldMeta) record.getIdField().getMeta()).isAutoIncrement()) {
			// if(record.getIdValue()==null){
			// throw new ValidationException("REUIRED_FIELD",
			// record.getIdField());
			// }
		}
		for (int i = 0; i < record.getFieldsCount(); i++) {
			final Field field = record.getField(i);
			if (field.getMeta().isVisible() && field.getMeta().isRequired()
					&& (field.getValueObject() == null || field.getValue().trim().equals(""))) {
				throw new ValidationException("REUIRED_FIELD", field);
			}
		}

		final ArrayList<Constraint> constraints = table.getConstraints();
		for (int i = 0; i < constraints.size(); i++) {
			try {
				constraints.get(i).validate(record);
			} catch (final ConstraintException e) {
				throw new ValidationException(e.getMessage(), e);
			} catch (final JKDataAccessException e) {
				throw new ValidationException(e);
			}
		}

	}
}
