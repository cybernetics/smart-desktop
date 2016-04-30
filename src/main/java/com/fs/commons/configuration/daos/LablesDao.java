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
public class LablesDao extends ConfigurationDao {
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public void addLable(final Lable lable) throws DaoException {
		executeUpdate(new DaoUpdater() {
			@Override
			public String getUpdateSql() {
				return "insert into conf_lables(lable_key,lable_value,module_id,lang_id) values(?,?,?,?)";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				int index = 1;
				ps.setString(index++, lable.getLableKey());
				ps.setString(index++, lable.getLableValue());
				ps.setInt(index++, lable.getModuleId());
				ps.setInt(index++, lable.getLanguageId());
			}
		});
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public boolean isLableExist(final String key) throws DaoException {
		final DaoFinder daoFinder = new DaoFinder() {

			@Override
			public String getFinderSql() {
				return "select * from conf_lables where lable_key=?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return null;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setString(1, key);
			}

		};
		try {
			findRecord(daoFinder);
			return true;
		} catch (final RecordNotFoundException e) {
			return false;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Lable> listLabels() throws DaoException {
		final ArrayList<Lable> lstRecords = lstRecords(new DaoFinder() {
			@Override
			public String getFinderSql() {
				return "SELECT  * FROM conf_lables ";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateLable(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		});
		return lstRecords;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public List<Lable> listModuleLabels(final int moduleId) throws DaoException {
		final ArrayList<Lable> lstRecords = lstRecords(new DaoFinder() {
			@Override
			public String getFinderSql() {
				return "SELECT  * FROM conf_lables WHERE conf_lables.module_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateLable(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				final int index = 1;
				ps.setInt(index, moduleId);
			}
		});
		return lstRecords;
	}

	public List<Lable> listModuleLabels(final int moduleId, final int langId) throws DaoException {
		final ArrayList<Lable> lstRecords = lstRecords(new DaoFinder() {
			@Override
			public String getFinderSql() {
				return "SELECT  * FROM conf_lables WHERE conf_lables.module_id = ? and conf_lables.lang_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateLable(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				int index = 1;
				ps.setInt(index++, moduleId);
				ps.setInt(index++, langId);
			}
		});
		return lstRecords;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private Object populateLable(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
		final Lable lable = new Lable();
		lable.setLableId(rs.getInt("lable_id"));
		lable.setLableKey(rs.getString("lable_key"));
		lable.setLableValue(rs.getString("lable_value"));
		lable.setModuleId(rs.getInt("module_id"));
		lable.setLanguageId(rs.getInt("lang_id"));
		return lable;
	}
}
