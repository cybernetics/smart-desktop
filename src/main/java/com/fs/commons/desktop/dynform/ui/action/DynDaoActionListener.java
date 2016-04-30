package com.fs.commons.desktop.dynform.ui.action;

import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;

public interface DynDaoActionListener {

	public void onDaoException(Record recod, DaoException ex);

	public void beforeAddRecord(Record record) throws DaoException;

	public void afterAddRecord(Record record) throws DaoException;

	public void beforeUpdateRecord(Record record) throws DaoException;

	public void afterUpdateRecord(Record record) throws DaoException;

	public void beforeDeleteRecord(Record record) throws DaoException;

	public void afterDeleteRecord(Record record) throws DaoException;

	public void beforeClosePanel();

	public void afterClosePanel();

	public void onRecordFound(Record record);

	public void onRecordNotFound(Object recordId, DaoException e);

	public void beforeResetComponents(Record record);

	public void afterResetComponents();
	public void afterSetMode(DynDaoMode mode) ;

	public void beforeSetMode(DynDaoMode mode) ;
}
