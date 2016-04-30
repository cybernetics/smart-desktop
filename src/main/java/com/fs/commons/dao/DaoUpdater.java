/**
 * 
 */
package com.fs.commons.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.fs.commons.dao.exception.DaoException;

/**
 * @author u087
 * 
 */
public interface DaoUpdater {
	public String getUpdateSql();

	public void setParamters(PreparedStatement ps) throws SQLException, DaoException;
}
