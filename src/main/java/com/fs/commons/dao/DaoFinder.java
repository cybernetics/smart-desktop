/**
 * 
 */
package com.fs.commons.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

/**
 * @author u087
 * 
 */
public interface DaoFinder {
	public String getFinderSql();

	/**
	 * @param ps
	 */
	public void setParamters(PreparedStatement ps) throws SQLException;

	/**
	 * @param rs
	 * @throws SQLException
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException;
}
