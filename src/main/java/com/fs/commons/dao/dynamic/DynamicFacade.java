package com.fs.commons.dao.dynamic;

import java.util.ArrayList;

import com.fs.commons.dao.Session;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;

public class DynamicFacade {
	
	/**
	 * @param master
	 * @param detailRecords
	 * @return 
	 * @throws DaoException
	 */
	public String addMasterDetailRecord(Record master, ArrayList<Record> detailRecords) throws DaoException {
		Session session = DataSourceFactory.getDefaultDataSource().createSession();
		boolean commit = false;
		try {
			DynamicDao dao = new DynamicDao(master.getTableMeta());
			dao.setSession(session);
			String id = dao.insertRecord(master);
			for (Record record : detailRecords) {
				record.setFieldValue(master.getIdField().getFieldName(), id);
				DynamicDao detailDao = new DynamicDao(record.getTableMeta());
				detailDao.setSession(session);
				detailDao.insertRecord(record);
			}
			commit = true;
			return id;
		} finally {
			session.close(commit);
		}
	}
}
