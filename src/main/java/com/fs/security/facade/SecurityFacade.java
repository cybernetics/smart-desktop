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
import java.util.List;

import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.security.dao.UserDao;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;
import com.jk.security.JKPrivilige;
import com.jk.security.JKUser;

public class SecurityFacade {
	JKLogger logger=JKLoggerFactory.getLogger(getClass());
	UserDao dao = new UserDao();

	public void checkPrivlige(final JKPrivilige privilige) throws JKDataAccessException {
		try {
			this.dao.findPrivilige(privilige.getPriviligeId());
		} catch (final JKRecordNotFoundException e) {
			logger.info("Add new privilige : " , privilige);
			this.dao.addPrivlige(privilige);
		}
	}

	/**
	 *
	 * @param priviligeId
	 * @return
	 * @throws JKDataAccessException
	 * @throws JKRecordNotFoundException
	 */
	public JKPrivilige findPrivilige(final int priviligeId) throws JKRecordNotFoundException, JKDataAccessException {
		return this.dao.findPrivilige(priviligeId);
	}

	/**
	 *
	 * @param userId
	 * @return
	 * @throws JKRecordNotFoundException
	 * @throws JKDataAccessException
	 */
	public JKUser findUser(final int userId) throws JKRecordNotFoundException, JKDataAccessException {
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
	 * @throws JKDataAccessException
	 */
	public boolean isAllowedOperation(final JKUser user, final JKPrivilige userPrivilige) throws JKDataAccessException {
		checkPrivlige(userPrivilige);
		if (user.getPriviliges() == null) {
			user.setPriviliges(this.dao.lstUserPriviliges(user.getUserId()));
		}
		List<JKPrivilige> privliges = user.getPriviliges();
		for (JKPrivilige privilige : privliges) {
			if (privilige.getPriviligeId() == userPrivilige.getPriviligeId()) {
				if (userPrivilige.getPriviligeName().equals("private") || privilige.getPriviligeName().equals(userPrivilige.getPriviligeName())) {
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
	 * @throws JKDataAccessException
	 */

	public boolean isValidUser(final JKUser user) throws JKDataAccessException {
		try {
			// the method will fill the user object by reference
			final JKUser localUser = this.dao.getUser(user.getUserId());
			user.setUserRecordId(localUser.getUserRecordId());
			user.setUserId(localUser.getUserId());
			user.setFullName(localUser.getFullName());
			user.setDisabled(localUser.isDisabled());
			return user.getPassword().equals(localUser.getPassword());
		} catch (final JKRecordNotFoundException e) {
			return false;
		}
	}

	/**
	 *
	 * @return
	 * @throws JKDataAccessException
	 */
	public ArrayList<JKPrivilige> lstPrivilige() throws JKDataAccessException {
		return this.dao.lstPrivilige(null);
	}

	/**
	 *
	 * @param user
	 * @throws JKDataAccessException
	 */
	public void updateUser(final JKUser user) throws JKDataAccessException {
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
