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
package com.fs.security.facade;

import java.util.ArrayList;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.User;
import com.fs.security.dao.UserDao;

public class SecurityFacade {
	UserDao dao = new UserDao();

	public void checkPrivlige(final Privilige privilige) throws DaoException {
		try {
			this.dao.findPrivilige(privilige.getPriviligeId());
		} catch (final RecordNotFoundException e) {
			this.dao.addPrivlige(privilige);
			System.err.println("Adding new privilige : " + privilige);
		}
	}

	/**
	 *
	 * @param priviligeId
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public Privilige findPrivilige(final int priviligeId) throws RecordNotFoundException, DaoException {
		return this.dao.findPrivilige(priviligeId);
	}

	/**
	 *
	 * @param userId
	 * @return
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	public User findUser(final int userId) throws RecordNotFoundException, DaoException {
		return this.dao.findUser(userId);
	}

	/**
	 * It could be done on the database level , by modifying the query to
	 * include the privilige id , but i made it this way to be more clear for
	 * the new developers Jalal
	 *
	 * @param userName
	 * @param userPrivilige
	 * @return
	 * @throws DaoException
	 */
	public boolean isAllowedOperation(final User user, final Privilige userPrivilige) throws DaoException {
		checkPrivlige(userPrivilige);
		final ArrayList<Privilige> list = this.dao.lstUserPriviliges(user.getUserId());

		for (int i = 0; i < list.size(); i++) {
			final Privilige currentPrivilige = list.get(i);
			if (currentPrivilige.getPriviligeId() == userPrivilige.getPriviligeId()) {
				if (userPrivilige.getPriviligeName().equals("private")
						|| currentPrivilige.getPriviligeName().equals(userPrivilige.getPriviligeName())) {
					return true;
				}
			}
		}
		// check if prilige exists : debuging info
		// try{
		// dao.findPrivilige(privilige.getPriviligeId());
		// }catch(RecordNotFoundException e){
		// System.err.println("Privilige ID: "+privilige.getPriviligeId()+" ,
		// Privilige Name : "+privilige.getPriviligeName());
		// }
		return false;
	}

	/**
	 *
	 * @param userName
	 * @param password
	 * @return
	 * @throws DaoException
	 */

	public boolean isValidUser(final User user) throws DaoException {
		try {
			// the method will fill the user object by reference
			final User localUser = this.dao.getUser(user.getUserId());
			user.setUserRecordId(localUser.getUserRecordId());
			user.setUserId(localUser.getUserId());
			user.setFullName(localUser.getFullName());
			user.setDisabled(localUser.isDisabled());
			return user.getPassword().equals(localUser.getPassword());
		} catch (final RecordNotFoundException e) {
			return false;
		}
	}

	/**
	 *
	 * @return
	 * @throws DaoException
	 */
	public ArrayList<Privilige> lstPrivilige() throws DaoException {
		return this.dao.lstPrivilige(null);
	}

	/**
	 *
	 * @param user
	 * @throws DaoException
	 */
	public void updateUser(final User user) throws DaoException {
		final UserDao dao = new UserDao();
		dao.updateUser(user);
	}

	// public void addUserLoginAudit() throws DaoException {
	// Audit audit=new Audit();
	// audit.setAuditType(AuditType.AUDIT_LOGIN);
	// dao.addAudit(audit);
	// }
	//
	// public void addLogoutAudit() throws DaoException {
	// Audit audit=new Audit();
	// audit.setAuditType(AuditType.AUDIT_LOGOUT);
	// dao.addAudit(audit);
	// }
}
