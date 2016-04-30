package com.fs.commons.configuration.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fs.commons.configuration.beans.Lable;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.DaoUpdater;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

/**
 * 
 * @author mkiswani
 *
 */
public class LablesDao extends ConfigurationDao{
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	public List<Lable> listModuleLabels(final int moduleId) throws DaoException{
		ArrayList<Lable> lstRecords = lstRecords(new DaoFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int index = 1;
				ps.setInt(index, moduleId);
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateLable(rs);
			}

			@Override
			public String getFinderSql() {
				return "SELECT  * FROM conf_lables WHERE conf_lables.module_id = ?";
			}
		});
		return lstRecords;
	}

	public List<Lable> listModuleLabels(final int moduleId, final int langId) throws DaoException{
		ArrayList<Lable> lstRecords = lstRecords(new DaoFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int index = 1;
				ps.setInt(index++, moduleId);
				ps.setInt(index++, langId);	
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateLable(rs);
			}

			@Override
			public String getFinderSql() {
				return "SELECT  * FROM conf_lables WHERE conf_lables.module_id = ? and conf_lables.lang_id = ?";
			}
		});
		return lstRecords;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Lable> listLabels() throws DaoException{
		ArrayList<Lable> lstRecords = lstRecords(new DaoFinder() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateLable(rs);
			}

			@Override
			public String getFinderSql() {
				return "SELECT  * FROM conf_lables ";
			}
		});
		return lstRecords;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private Object populateLable(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
		Lable lable = new Lable();
		lable.setLableId(rs.getInt("lable_id"));
		lable.setLableKey(rs.getString("lable_key"));
		lable.setLableValue(rs.getString("lable_value"));
		lable.setModuleId(rs.getInt("module_id"));
		lable.setLanguageId(rs.getInt("lang_id"));
		return lable;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isLableExist(final String key) throws DaoException {
		DaoFinder daoFinder = new DaoFinder(){

			@Override
			public String getFinderSql() {
				return "select * from conf_lables where lable_key=?";
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, key);
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return null;
			}
			
		};
		try{
			findRecord(daoFinder);
			return true;
		}catch (RecordNotFoundException e) {
			return false;
		}
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addLable(final Lable lable) throws DaoException {
		executeUpdate(new DaoUpdater() {
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int index = 1;
				ps.setString(index++, lable.getLableKey());
				ps.setString(index++, lable.getLableValue());
				ps.setInt(index++, lable.getModuleId());
				ps.setInt(index++, lable.getLanguageId());
			}

			@Override
			public String getUpdateSql() {
				return "insert into conf_lables(lable_key,lable_value,module_id,lang_id) values(?,?,?,?)";
			}
		});
	}
}
