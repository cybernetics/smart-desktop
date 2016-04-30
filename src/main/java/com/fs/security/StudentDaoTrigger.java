package com.fs.security;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.trigger.TriggerAdapter;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.util.GeneralUtility;

public class StudentDaoTrigger extends TriggerAdapter{
	@Override
	public void beforeAdd(Record record) throws DaoException {
		String value=record.getField("password").getValue();
		value=GeneralUtility.encode(value);
		record.getField("password").setValue(value);
	}
	
	@Override
	public void beforeUpdate(Record oldRecord, Record newRecord)throws DaoException {
		beforeAdd(newRecord);
	}
}
