package com.fs.commons.dao.dynamic.trigger;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;

public class TriggerAdapter implements Trigger{

	public void afterAdd(Record record) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void afterDelete(Record record) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void afterUpdate(Record oldRecord, Record newRecord)
			throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void beforeAdd(Record record) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void beforeDelete(Record record) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void beforeUpdate(Record oldRecord, Record newRecord)throws DaoException {		
	}

	public void afterFind(Record record) throws DaoException {
	}

}
