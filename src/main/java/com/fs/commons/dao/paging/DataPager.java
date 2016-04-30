package com.fs.commons.dao.paging;

public interface DataPager {
	public void setPageRowsCount(int pageRowsCount)throws PagingException;

	public int getPagesCount()throws PagingException;

	public void moveToFirstPage()throws PagingException;

	public void moveToLastPage()throws PagingException;

	public void moveToNextPage()throws PagingException;

	public void moveToPreviousePage()throws PagingException;

	public int getCurrentPage()throws PagingException;

	public int getAllRowsCount()throws PagingException;

	public int getPageRowsCount()throws PagingException;

	public void moveToPage(int page)throws PagingException;
}
