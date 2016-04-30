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
	 * s
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	public User getUser(final String userId) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			/**
			 * 
			 */

			public String getFinderSql() {
				return " SELECT user_record_id,user_id,user_full_name,password,disabled FROM sec_users WHERE user_id = ? ";
			}

			/**
			 * 
			 */
			public Object populate(ResultSet rs) throws SQLException {
				return populateUser(rs);
			}

			/**
			 * 
			 */
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, userId);
			}
		};
		return (User) findRecord(finder);
	}

	/**
	 * @param userName
	 * @return
	 * @throws DaoException
	 */
	public ArrayList<Privilige> lstUserPriviliges(final String userName) throws DaoException {
		DaoFinder finder = new DaoFinder() {

			/** 
		       *  
		       */

			public String getFinderSql() {
				String sql = "SELECT " + "sec_privileges.privilege_id, " + "sec_privileges.privilege_name " + "FROM " + "sec_role_privileges "
						+ "Inner Join sec_roles ON sec_role_privileges.role_id = sec_roles.role_id "
						+ "Inner Join sec_user_roles ON sec_user_roles.role_id = sec_roles.role_id "
						+ "Inner Join sec_privileges ON sec_privileges.privilege_id = sec_role_privileges.privilege_id "
						+ "Inner Join sec_users ON sec_users.user_record_id = sec_user_roles.user_id " + "WHERE sec_users.user_id=?  ";
				return sql;
			}

			/**
			 * s
			 */

			public Object populate(ResultSet rs) throws SQLException {

				return pouplatePrivilige(rs);
			}

			/** 
		       *  
		       */
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, userName);
			}
		};

		return lstRecords(finder);
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
		DaoFinder finder = new DaoFinder() {
			public String getFinderSql() {
				String sql = "SELECT " + "user_record_id," + "user_id," + "user_full_name," + "`password`," + "disabled " + "FROM " + "sec_users "
						+ "WHERE " + "user_record_id=?";
				return sql;
			}

			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateUser(rs);
			}

			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, userRecordId);
			}

		};
		return (User) findRecord(finder);

	}

	/**
	 * t
	 * 
	 * @param user
	 */
	public void updateUser(final User user) throws DaoException {
		DaoUpdater updater = new DaoUpdater() {
			public String getUpdateSql() {
				return "UPDATE  sec_users  SET user_full_name=? ,password = ? ,disabled=? WHERE user_id = ?";
			}

			public void setParamters(PreparedStatement ps) throws SQLException {

				ps.setString(1, user.getFullName());
				ps.setString(2, GeneralUtility.encode(user.getPassword()));
				ps.setInt(3, user.getStatus());
				ps.setString(4, user.getUserId());
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
		DaoFinder finder = new DaoFinder() {
			@Override
			public String getFinderSql() {
				String sql = "select * from sec_privileges where privilege_id=?";
				return sql;
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return pouplatePrivilige(rs);
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, priviligeId);
			}
		};
		return (Privilige) findRecord(finder);
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Object pouplatePrivilige(ResultSet rs) throws SQLException {
		Privilige privilige = new Privilige();
		privilige.setPriviligeId(rs.getInt("privilege_id"));
		privilige.setPriviligeName(rs.getString("privilege_name"));
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
		User user = new User();
		user.setUserId(rs.getString("user_id"));
		user.setFullName(rs.getString("user_full_name"));
		user.setPassword(rs.getString("password"));
		user.setDisabled(rs.getInt("disabled") == 1);
		user.setUserRecordId(rs.getInt("user_record_id"));
		return user;
	}

	/**
	 * 
	 * @param privilige
	 * @throws DaoException
	 */
	public void addPrivlige(final Privilige privilige) throws DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int counter = 1;
				ps.setInt(counter++, privilige.getPriviligeId());
				ps.setString(counter++, privilige.getPriviligeName());
				ps.setString(counter++, privilige.getDesc());
				if (privilige.getParent() != null) {
					ps.setInt(counter++, privilige.getParentPrivlige().getPriviligeId());
				}else{
					ps.setNull(counter++, Types.INTEGER);
				}
			}

			@Override
			public String getUpdateSql() {
				return "INSERT INTO sec_privileges VALUES(?,?,?,?)";
			}
		};
		executeUpdate(updater);
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<Privilige> lstPrivilige(final Integer parentPrivligeId) throws DaoException {
		DaoFinder finder = new DaoFinder() {
			// /////////////////////////////////////////////////////////////////////
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				if (parentPrivligeId != null) {
					ps.setInt(1, parentPrivligeId);
				}
			}

			// /////////////////////////////////////////////////////////////////////
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Privilige privilige = populatePrivilige(rs);
				if (parentPrivligeId != null) {
					privilige.setParentPrivlige(findPrivilige(parentPrivligeId));
				}
				privilige.setChilds(lstPrivilige(privilige.getPriviligeId()));
				return privilige;
			}

			// /////////////////////////////////////////////////////////////////////
			@Override
			public String getFinderSql() {
				return "SELECT * FROM sec_privileges WHERE " + (parentPrivligeId == null ? " PARENT_PRIVILEGE IS NULL " : "PARENT_PRIVILEGE=?")
						+ " ORDER BY privilege_id";
			}
			// /////////////////////////////////////////////////////////////////////
		};
		return lstRecords(finder);
	}

	/**
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Privilige populatePrivilige(ResultSet rs) throws SQLException {
		Privilige privilige = new Privilige();
		privilige.setPriviligeId(rs.getInt("privilege_id"));
		privilige.setPriviligeName(rs.getString("privilege_name"));
		privilige.setDesc(rs.getString("PRIVILEGE_DESC"));
		return privilige;
	}

}
