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
	public void validate(Record record) throws ConstraintException, DaoException {
		ArrayList<FieldMeta> fields = getFields();
		for (int i = 0; i < fields.size(); i++) {
			FieldMeta fieldMeta = fields.get(i);
			Field field = record.getField(fieldMeta.getName());
			if (field.getValue()!=null && !field.getValue().equals("")) {
				float value = Float.parseFloat(field.getValue());
				if (value < valueFrom || value > valueTo) {
					DataOutOfRangeException ex = new DataOutOfRangeException("OUT_OF_RANGE", this);
					ex.addField(field);
					throw ex;
				}
			}
		}
	}

	public float getValueFrom() {
		return valueFrom;
	}

	public void setValueFrom(float valueFrom) {
		this.valueFrom = valueFrom;
	}

	public float getValueTo() {
		return valueTo;
	}

	public void setValueTo(float valueTo) {
		this.valueTo = valueTo;
	}

	@Override
	public String toString() {
		return getName() + "Data Range Constraint ";
	}

	@Override
	public String getTypeString() {
		return "range";
	}
}
