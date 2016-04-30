package com.fs.commons.dao.dynamic.constraints;

import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ValueLessThanAnotherValueException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;

public class LessThanContsraint extends Constraint {
	@Override
	public void validate(Record record) throws ConstraintException, DaoException {
		FieldMeta field1 = getFields().get(0);
		FieldMeta field2 = getFields().get(1);
		Float value1 = record.getField(field1.getName()).getValueAsFloat();
		float value2 = record.getField(field2.getName()).getValueAsFloat();
		if (value1 > value2) {
			ValueLessThanAnotherValueException ex = new ValueLessThanAnotherValueException("LESS_THAN_CONSTRAINT");
			ex.addField(record.getField(fields.get(0).getName()));
			ex.addField(record.getField(fields.get(1).getName()));
			throw ex;
		}
	}

	@Override
	public String toString() {
		return getName() + "Less than Data Constraint ";
	}

	@Override
	public String getTypeString() {
		return "less-than";
	}
}
