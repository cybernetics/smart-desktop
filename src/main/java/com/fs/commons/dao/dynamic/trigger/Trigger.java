package com.fs.commons.dao.dynamic.trigger;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;

public interface Trigger {
	public void beforeAdd(Record record)throws DaoException;
	public void afterAdd(Record record)throws DaoException;
	public void beforeUpdate(Record oldRecord,Record newRecord)throws DaoException;
	public void afterUpdate(Record oldRecord,Record newRecord)throws DaoException;

	public void afterFind(Record record)throws DaoException;

	public void beforeDelete(Record record)throws DaoException;
	public void afterDelete(Record record)throws DaoException;
}
