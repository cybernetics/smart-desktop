package com.fs.commons.configuration.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fs.commons.configuration.beans.Language;
import com.fs.commons.configuration.beans.Module;
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;

/**
 * 
 * @author mkiswani
 *
 */
public class ConfigurationDao extends AbstractDao{

	public Language findLang(final int langId) throws RecordNotFoundException, DaoException{
		Language findRecord = (Language) findRecord(new DaoFinder(){
			@Override
			public String getFinderSql() {
				return "SELECT * FROM conf_languages WHERE conf_languages.lang_id = ?";
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1,langId);
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return popualte( rs);
			}

		});
		return findRecord;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	private Object popualte(ResultSet rs) throws SQLException {
		Language lang = new Language();
		lang.setLangId(rs.getInt("lang_id"));
		lang.setLangName(rs.getString("lang_name"));
		lang.setLangShortName(rs.getString("lang_short_name"));
		return lang;
	}
	
	
	////////////////////////////////////////////////////////////////////////////////////////
	public Module findModule(final int moduleId) throws RecordNotFoundException, DaoException{
		Module findRecord = (Module) findRecord(new DaoFinder(){
			@Override
			public String getFinderSql() {
				return "SELECT * FROM conf_modules WHERE conf_modules.module_id = ?";
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1,moduleId);
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateModule(rs);
			}

			
			
		});
		return findRecord;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////	
	private Module populateModule(ResultSet rs) throws SQLException {
		Module module = new Module();
		module.setModuleId(rs.getInt("module_id"));
		module.setModuleName(rs.getString("module_name"));
		return module;
	}
	
}
