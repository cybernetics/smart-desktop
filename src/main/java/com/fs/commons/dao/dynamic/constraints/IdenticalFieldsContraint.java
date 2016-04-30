package com.fs.commons.dao.dynamic.constraints;

import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.constraints.exceptions.IdenticalFieldException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;

public class IdenticalFieldsContraint extends Constraint {
	@Override
	public void validate(Record record) throws ConstraintException, DaoException {

		FieldMeta field1 = getFields().get(0);
		FieldMeta field2 = getFields().get(1);
		float value1 = record.getField(field1.getName()).getValueAsFloat();
		float value2 = record.getField(field2.getName()).getValueAsFloat();
		if (value1 == value2) {
			IdenticalFieldException ex = new IdenticalFieldException("IDENTICAL_DATA_CONSTRAINT");
			// String message = field1.getName() + " and " + field2.getName() +
			// " has same value :" + value1;
			ex.addField(record.getField(0));
			ex.addField(record.getField(1));
			throw ex;
		}
	}

	@Override
	public String toString() {
		return getName() + "Identical Fields Constraint ";
	}

	@Override
	public String getTypeString() {
		return "no-idenetical";
	}
}
