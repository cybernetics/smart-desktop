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
	public String addMasterDetailRecord(final Record master, final ArrayList<Record> detailRecords) throws DaoException {
		final Session session = DataSourceFactory.getDefaultDataSource().createSession();
		boolean commit = false;
		try {
			final DynamicDao dao = new DynamicDao(master.getTableMeta());
			dao.setSession(session);
			final String id = dao.insertRecord(master);
			for (final Record record : detailRecords) {
				record.setFieldValue(master.getIdField().getFieldName(), id);
				final DynamicDao detailDao = new DynamicDao(record.getTableMeta());
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
