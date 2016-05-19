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

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.constraints.exceptions.DuplicateDataException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.locale.Lables;

public class DuplicateDataConstraint extends Constraint {

	@Override
	public String getTypeString() {
		return "no-duplicate";
	}

	@Override
	public String toString() {
		return getName() + "Duplicate Data Constraint ";
	}

	/**
	 *
	 */
	@Override
	public void validate(final Record record) throws DuplicateDataException, JKDataAccessException {
		if (!record.isNewRecord()) {
			return;
		}
		final Record filterRecord = getTableMeta().createEmptyRecord();
		for (int i = 0; i < this.fields.size(); i++) {
			final FieldMeta meta = this.fields.get(i);
			filterRecord.setFieldValue(meta.getName(), record.getField(meta.getName()).getValue());
		}
		final ArrayList<Record> result = this.dao.lstRecords(filterRecord);
		if (result.size() > 0) {
			// if an record exist except the current one , throw validation
			// exception
			if (!result.get(0).getIdValue().equals(record.getIdField().getValue())) {
				final String fieldName = this.fields.get(0).getName();
				final StringBuffer msg = new StringBuffer();
				msg.append(Lables.get("DUPLICATE_DATA_EXCEPTION"));
				msg.append(":");
				msg.append(Lables.get(fieldName, true));
				final ConstraintException ex = new DuplicateDataException(msg.toString(), result.get(0).getIdValue().toString());

				ex.addField(record.getField(fieldName));
				throw ex;
			}
		}
	}
}
