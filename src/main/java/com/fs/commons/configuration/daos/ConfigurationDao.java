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
package com.fs.commons.configuration.daos;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fs.commons.configuration.beans.Language;
import com.fs.commons.configuration.beans.Module;
import com.fs.commons.dao.JKAbstractPlainDataAccess;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.jk.db.dataaccess.plain.JKFinder;

/**
 *
 * @author mkiswani
 *
 */
public class ConfigurationDao extends JKAbstractPlainDataAccess {

	public Language findLang(final int langId) throws JKRecordNotFoundException, JKDataAccessException {
		final Language findRecord = (Language) findRecord(new JKFinder() {
			@Override
			public String getQuery() {
				return "SELECT * FROM conf_languages WHERE conf_languages.lang_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return popualte(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, langId);
			}

		});
		return findRecord;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	public Module findModule(final int moduleId) throws JKRecordNotFoundException, JKDataAccessException {
		final Module findRecord = (Module) findRecord(new JKFinder() {
			@Override
			public String getQuery() {
				return "SELECT * FROM conf_modules WHERE conf_modules.module_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return populateModule(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, moduleId);
			}

		});
		return findRecord;
	}

	////////////////////////////////////////////////////////////////////////////////////////
	private Object popualte(final ResultSet rs) throws SQLException {
		final Language lang = new Language();
		lang.setLangId(rs.getInt("lang_id"));
		lang.setLangName(rs.getString("lang_name"));
		lang.setLangShortName(rs.getString("lang_short_name"));
		return lang;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////
	private Module populateModule(final ResultSet rs) throws SQLException {
		final Module module = new Module();
		module.setModuleId(rs.getInt("module_id"));
		module.setModuleName(rs.getString("module_name"));
		return module;
	}

}
