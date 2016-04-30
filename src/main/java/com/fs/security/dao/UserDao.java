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
package com.fs.security.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.DaoUpdater;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.User;
import com.fs.commons.util.GeneralUtility;

/**
 * @author user
 *
 */
public class UserDao extends AbstractDao {

	/**
	 *
	 * @param privilige
	 * @throws DaoException
	 */
	public void addPrivlige(final Privilige privilige) throws DaoException {
		final DaoUpdater updater = new DaoUpdater() {

			@Override
			public String getUpdateSql() {
				return "INSERT INTO sec_privileges VALUES(?,?,?,?)";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				int counter = 1;
				ps.setInt(counter++, privilige.getPriviligeId());
				ps.setString(counter++, privilige.getPriviligeName());
				ps.setString(counter++, privilige.getDesc());
				if (privilige.getParent() != null) {
					ps.setInt(counter++, privilige.getParentPrivlige().getPriviligeId());
				} else {
					ps.setNull(counter++, Types.INTEGER);
				}
			}
		};
		executeUpdate(updater);
	}

	/**
	 *
	 * @param priviligeId
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public Privilige findPrivilige(final int priviligeId) throws RecordNotFoundException, DaoException {
		final DaoFinder finder = new DaoFinder() {
			@Override
			public String getFinderSql() {
				final String sql = "select * from sec_privileges where privilege_id=?";
				return sql;
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return pouplatePrivilige(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, priviligeId);
			}
		};
		return (Privilige) findRecord(finder);
	}

	/**
	 *
	 * @param userRecordId
	 * @return
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	public User findUser(final int userRecordId) throws RecordNotFoundException, DaoException {
		// TODO Auto-generated method stub
		final DaoFinder finder = new DaoFinder() {
			@Override
			public String getFinderSql() {
				final String sql = "SELECT " + "user_record_id," + "user_id," + "user_full_name," + "`password`," + "disabled " + "FROM "
						+ "sec_users " + "WHERE " + "user_record_id=?";
				return sql;
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateUser(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, userRecordId);
			}

		};
		return (User) findRecord(finder);

	}

	/**
	 * s
	 *
	 * @param userName
	 * @param password
	 * @return
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	public User getUser(final String userId) throws RecordNotFoundException, DaoException {
		final DaoFinder finder = new DaoFinder() {

			/**
			 *
			 */

			@Override
			public String getFinderSql() {
				return " SELECT user_record_id,user_id,user_full_name,password,disabled FROM sec_users WHERE user_id = ? ";
			}

			/**
			 *
			 */
			@Override
			public Object populate(final ResultSet rs) throws SQLException {
				return populateUser(rs);
			}

			/**
			 *
			 */
			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
		};
		return (User) findRecord(finder);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<Privilige> lstPrivilige(final Integer parentPrivligeId) throws DaoException {
		final DaoFinder finder = new DaoFinder() {
			// /////////////////////////////////////////////////////////////////////
			@Override
			public String getFinderSql() {
				return "SELECT * FROM sec_privileges WHERE " + (parentPrivligeId == null ? " PARENT_PRIVILEGE IS NULL " : "PARENT_PRIVILEGE=?")
						+ " ORDER BY privilege_id";
			}
			// /////////////////////////////////////////////////////////////////////

			// /////////////////////////////////////////////////////////////////////
			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				final Privilige privilige = populatePrivilige(rs);
				if (parentPrivligeId != null) {
					privilige.setParentPrivlige(findPrivilige(parentPrivligeId));
				}
				privilige.setChilds(lstPrivilige(privilige.getPriviligeId()));
				return privilige;
			}

			// /////////////////////////////////////////////////////////////////////
			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				if (parentPrivligeId != null) {
					ps.setInt(1, parentPrivligeId);
				}
			}
		};
		return lstRecords(finder);
	}

	/**
	 * @param userName
	 * @return
	 * @throws DaoException
	 */
	public ArrayList<Privilige> lstUserPriviliges(final String userName) throws DaoException {
		final DaoFinder finder = new DaoFinder() {

			/** 
			   *  
			   */

			@Override
			public String getFinderSql() {
				final String sql = "SELECT " + "sec_privileges.privilege_id, " + "sec_privileges.privilege_name " + "FROM " + "sec_role_privileges "
						+ "Inner Join sec_roles ON sec_role_privileges.role_id = sec_roles.role_id "
						+ "Inner Join sec_user_roles ON sec_user_roles.role_id = sec_roles.role_id "
						+ "Inner Join sec_privileges ON sec_privileges.privilege_id = sec_role_privileges.privilege_id "
						+ "Inner Join sec_users ON sec_users.user_record_id = sec_user_roles.user_id " + "WHERE sec_users.user_id=?  ";
				return sql;
			}

			/**
			 * s
			 */

			@Override
			public Object populate(final ResultSet rs) throws SQLException {

				return pouplatePrivilige(rs);
			}

			/** 
			   *  
			   */
			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setString(1, userName);
			}
		};

		return lstRecords(finder);
	}

	/**
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Privilige populatePrivilige(final ResultSet rs) throws SQLException {
		final Privilige privilige = new Privilige();
		privilige.setPriviligeId(rs.getInt("privilege_id"));
		privilige.setPriviligeName(rs.getString("privilege_name"));
		privilige.setDesc(rs.getString("PRIVILEGE_DESC"));
		return privilige;
	}

	/**
	 *
	 * @param user
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private User populateUser(final ResultSet rs) throws SQLException {
		final User user = new User();
		user.setUserId(rs.getString("user_id"));
		user.setFullName(rs.getString("user_full_name"));
		user.setPassword(rs.getString("password"));
		user.setDisabled(rs.getInt("disabled") == 1);
		user.setUserRecordId(rs.getInt("user_record_id"));
		return user;
	}

	/**
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Object pouplatePrivilige(final ResultSet rs) throws SQLException {
		final Privilige privilige = new Privilige();
		privilige.setPriviligeId(rs.getInt("privilege_id"));
		privilige.setPriviligeName(rs.getString("privilege_name"));
		return privilige;
	}

	/**
	 * t
	 *
	 * @param user
	 */
	public void updateUser(final User user) throws DaoException {
		final DaoUpdater updater = new DaoUpdater() {
			@Override
			public String getUpdateSql() {
				return "UPDATE  sec_users  SET user_full_name=? ,password = ? ,disabled=? WHERE user_id = ?";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {

				ps.setString(1, user.getFullName());
				ps.setString(2, GeneralUtility.encode(user.getPassword()));
				ps.setInt(3, user.getStatus());
				ps.setString(4, user.getUserId());
			}
		};
		executeUpdate(updater);
	}

}
