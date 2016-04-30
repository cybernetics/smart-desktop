package com.fs.commons.dao.dynamic.constraints;

import java.util.ArrayList;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;

public class DefaultTableDataValidator implements TableDataValidator {

	/**
	 * 
	 */
	public void validate(TableMeta table, Record record) throws ValidationException {
		if (record.isNewRecord()&& !((IdFieldMeta)record.getIdField().getMeta()).isAutoIncrement()) {
//			if(record.getIdValue()==null){
//				throw new ValidationException("REUIRED_FIELD", record.getIdField());
//			}
		}
		for (int i = 0; i < record.getFieldsCount(); i++) {
			Field field = record.getField(i);
			if (field.getMeta().isVisible() &&  field.getMeta().isRequired()&& (field.getValueObject()==null || field.getValue().trim().equals(""))) {
				throw new ValidationException("REUIRED_FIELD", field);
			}
		}
		
		ArrayList<Constraint> constraints = table.getConstraints();
		for (int i = 0; i < constraints.size(); i++) {
			try {
				constraints.get(i).validate(record);
			} catch (ConstraintException e) {
				throw new ValidationException(e.getMessage(),e);
			} catch (DaoException e) {
				throw new ValidationException(e);
			}
		}

	}
}
