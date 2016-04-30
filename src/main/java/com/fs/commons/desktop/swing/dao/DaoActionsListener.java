package com.fs.commons.desktop.swing.dao;

import com.fs.commons.dao.exception.DaoException;

public interface DaoActionsListener {
	/**
	 * 
	 */
	public String handleAddEvent() throws DaoException;

	/**
	 * 
	 */
	public void handleFindEvent(Object id) throws DaoException;

	/**
	 * 
	 */
	public void handleSaveEvent() throws DaoException;

	/**
	 * 
	 */
	public void handleDeleteEvent() throws DaoException;

}
