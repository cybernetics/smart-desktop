package com.fs.commons.dao.paging;

import javax.sql.rowset.CachedRowSet;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.util.DateTimeUtil;

public class DataBasePager implements DataPager {

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
	public void setQuery(String query) throws NumberFormatException, DaoException, PagingException {
		this.query = query;
		AbstractDao dao = getDao();
		if (getPageRowsCount() == 0) {
			pagesCount = 1;
		} else {
			allRowsCount = dao.getRowsCount(query);
			if (allRowsCount <= getPageRowsCount()) {
				pagesCount = 1;
			} else {
				pagesCount = (int) (allRowsCount / getPageRowsCount());
				if (allRowsCount % getPageRowsCount() > 0) {
					pagesCount++;
				}
			}
		}
		moveToPage(getCurrentPage());
	}

	// /////////////////////////////////////////////////////
	private AbstractDao getDao() {
		AbstractDao dao = DaoFactory.createDao(getDatasource());
		return dao;
	}

	// /////////////////////////////////////////////////////
	public String getQuery() {
		return query;
	}

	// /////////////////////////////////////////////////////
	@Override
	public void setPageRowsCount(int pageRowsCount) {
		this.pageRowsCount = pageRowsCount;
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getPagesCount() {
		return pagesCount;
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToFirstPage() throws PagingException {
		int currentPage = 0;
		moveToPage(currentPage);
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToPage(int page) throws PagingException {
		if (page >= 0 && page < getPagesCount()) {
			try {
				if (pagesCount == 1) {
					resultSet = getDao().executeQuery(query);
				} else {
					resultSet = getDao().executeQuery(query, (page ) * getPageRowsCount(), (page) * getPageRowsCount() + getPageRowsCount());
				}
				currentPage = page;
				// getDao().printRecordResultSet(resultSet);
			} catch (Exception e) {
				throw new PagingException(e);
			}
		} else {
			resultSet = null;
		}
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToLastPage() throws PagingException {
		moveToPage(getPagesCount()-1);
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToNextPage() throws PagingException {
		moveToPage(currentPage + 1);
	}

	// /////////////////////////////////////////////////////
	@Override
	public void moveToPreviousePage() throws PagingException {
		moveToPage(currentPage - 1);
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getAllRowsCount() {
		return allRowsCount;
	}

	// /////////////////////////////////////////////////////
	@Override
	public int getPageRowsCount() {
		return pageRowsCount == -1 ? getDatasource().getQueryLimit() : pageRowsCount;
	}

	// /////////////////////////////////////////////////////
	public DataSource getDatasource() {
		return datasource == null ? DataSourceFactory.getDefaultDataSource() : datasource;
	}

	// /////////////////////////////////////////////////////
	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

	// /////////////////////////////////////////////////////
	public static void main(String[] args) throws NumberFormatException, DaoException, PagingException {
		DataBasePager p = new DataBasePager();
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

	public CachedRowSet getResultSet() {
		return resultSet;
	}
}
