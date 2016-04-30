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
package com.fs.commons.dao.paging;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.util.DateTimeUtil;

public class DataBasePager implements DataPager {

	// /////////////////////////////////////////////////////
	public static void main(final String[] args) throws NumberFormatException, DaoException, PagingException {
		final DataBasePager p = new DataBasePager();
		p.setQuery("SELECT * FROM GEN_NATIONAL_NUMBERS");
		DateTimeUtil.printCurrentTime(1);
		System.out.println(p.getAllRowsCount());
		DateTimeUtil.printCurrentTime(2);
		System.out.println(p.getPagesCount());
		DateTimeUtil.printCurrentTime(3);
		p.moveToFirstPage();
		DateTimeUtil.printCurrentTime(4);
		p.moveToNextPage();
		DateTimeUtil.printCurrentTime(5);
		p.moveToNextPage();
		DateTimeUtil.printCurrentTime(6);
		p.moveToNextPage();
		DateTimeUtil.printCurrentTime(7);
		// p.moveToNextPage();
		// p.moveToNextPage();
		// p.moveToNextPage();
		// p.moveToLastPage();

	}

	private String query;
	private int pageRowsCount = -1;
	private int pagesCount;
	private int currentPage;
	private int allRowsCount;
	DataSource datasource;

	private CachedRowSet resultSet;

	// /////////////////////////////////////////////////////
	public DataBasePager() {
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getAllRowsCount() {
		return this.allRowsCount;
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getCurrentPage() {
		return this.currentPage;
	}

	// /////////////////////////////////////////////////////
	private AbstractDao getDao() {
		final AbstractDao dao = DaoFactory.createDao(getDatasource());
		return dao;
	}

	// /////////////////////////////////////////////////////
	public DataSource getDatasource() {
		return this.datasource == null ? DataSourceFactory.getDefaultDataSource() : this.datasource;
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getPageRowsCount() {
		return this.pageRowsCount == -1 ? getDatasource().getQueryLimit() : this.pageRowsCount;
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getPagesCount() {
		return this.pagesCount;
	}

	// /////////////////////////////////////////////////////
	public String getQuery() {
		return this.query;
	}

	public CachedRowSet getResultSet() {
		return this.resultSet;
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToFirstPage() throws PagingException {
		final int currentPage = 0;
		moveToPage(currentPage);
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToLastPage() throws PagingException {
		moveToPage(getPagesCount() - 1);
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToNextPage() throws PagingException {
		moveToPage(this.currentPage + 1);
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToPage(final int page) throws PagingException {
		if (page >= 0 && page < getPagesCount()) {
			try {
				if (this.pagesCount == 1) {
					this.resultSet = getDao().executeQuery(this.query);
				} else {
					this.resultSet = getDao().executeQuery(this.query, page * getPageRowsCount(), page * getPageRowsCount() + getPageRowsCount());
				}
				this.currentPage = page;
				// getDao().printRecordResultSet(resultSet);
			} catch (final Exception e) {
				throw new PagingException(e);
			}
		} else {
			this.resultSet = null;
		}
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToPreviousePage() throws PagingException {
		moveToPage(this.currentPage - 1);
	}

	// /////////////////////////////////////////////////////
	public void setDatasource(final DataSource datasource) {
		this.datasource = datasource;
	}

	// /////////////////////////////////////////////////////
	@Override
	public void setPageRowsCount(final int pageRowsCount) {
		this.pageRowsCount = pageRowsCount;
	}

	// /////////////////////////////////////////////////////
	public void setQuery(final String query) throws NumberFormatException, DaoException, PagingException {
		this.query = query;
		final AbstractDao dao = getDao();
		if (getPageRowsCount() == 0) {
			this.pagesCount = 1;
		} else {
			this.allRowsCount = dao.getRowsCount(query);
			if (this.allRowsCount <= getPageRowsCount()) {
				this.pagesCount = 1;
			} else {
				this.pagesCount = this.allRowsCount / getPageRowsCount();
				if (this.allRowsCount % getPageRowsCount() > 0) {
					this.pagesCount++;
				}
			}
		}
		moveToPage(getCurrentPage());
	}
}
