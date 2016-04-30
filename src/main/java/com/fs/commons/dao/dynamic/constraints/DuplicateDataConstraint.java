package com.fs.commons.dao.dynamic.constraints;

import java.util.ArrayList;

import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.constraints.exceptions.DuplicateDataException;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.locale.Lables;

public class DuplicateDataConstraint extends Constraint {

	/**
	 * 
	 */
	@Override
	public void validate(Record record) throws DuplicateDataException, DaoException {
		if(!record.isNewRecord()){
			return ;
		}
		Record filterRecord = getTableMeta().createEmptyRecord();
		for (int i = 0; i < fields.size(); i++) {
			FieldMeta meta = fields.get(i);
			filterRecord.setFieldValue(meta.getName(),record.getField(meta.getName()).getValue());
		}
		ArrayList<Record> result = dao.lstRecords(filterRecord);
		if (result.size() > 0) {
			// if an record exist except the current one , throw validation
			// exception
			if (!result.get(0).getIdValue().equals(record.getIdField().getValue())) {
				String fieldName = fields.get(0).getName();
				StringBuffer msg = new StringBuffer();
				msg.append(Lables.get("DUPLICATE_DATA_EXCEPTION"));
				msg.append(":");
				msg.append(Lables.get(fieldName,true));
				ConstraintException ex = new DuplicateDataException(msg.toString(), result.get(0).getIdValue().toString());
				
				ex.addField(record.getField(fieldName));
				throw ex;
			}
		}
	}

	@Override
	public String toString() {
		return getName() + "Duplicate Data Constraint ";
	}

	@Override
	public String getTypeString() {
		return "no-duplicate";
	}
}
