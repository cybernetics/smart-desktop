package com.fs.security.facade;

import java.util.ArrayList;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.security.Privilige;
import com.fs.commons.security.User;
import com.fs.security.dao.UserDao;

public class SecurityFacade {
	UserDao dao = new UserDao();

	/**
	 * 
	 * @param userName
	 * @param password
	 * @return
	 * @throws DaoException
	 */

	public boolean isValidUser(User user) throws DaoException {
		try {
			// the method will fill the user object by reference
			User localUser = dao.getUser(user.getUserId());
			user.setUserRecordId(localUser.getUserRecordId());
			user.setUserId(localUser.getUserId());
			user.setFullName(localUser.getFullName());
			user.setDisabled(localUser.isDisabled());
			return user.getPassword().equals(localUser.getPassword());
		} catch (RecordNotFoundException e) {
			return false;
		}
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
	public boolean isAllowedOperation(User user, Privilige userPrivilige) throws DaoException {
		checkPrivlige(userPrivilige);
		ArrayList<Privilige> list = dao.lstUserPriviliges(user.getUserId());

		for (int i = 0; i < list.size(); i++) {
			Privilige currentPrivilige = list.get(i);
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
		// System.err.println("Privilige ID:  "+privilige.getPriviligeId()+" , Privilige Name : "+privilige.getPriviligeName());
		// }
		return false;
	}

	public void checkPrivlige(Privilige privilige) throws DaoException {
		try {
			dao.findPrivilige(privilige.getPriviligeId());
		} catch (RecordNotFoundException e) {
			dao.addPrivlige(privilige);
			System.err.println("Adding new privilige : " + privilige);
		}
	}

	/**
	 * 
	 * @param userId
	 * @return
	 * @throws RecordNotFoundException
	 * @throws DaoException
	 */
	public User findUser(final int userId) throws RecordNotFoundException, DaoException {
		return dao.findUser(userId);
	}

	/**
	 * 
	 * @param user
	 * @throws DaoException
	 */
	public void updateUser(User user) throws DaoException {
		UserDao dao = new UserDao();
		dao.updateUser(user);
	}

	/**
	 * 
	 * @param priviligeId
	 * @return
	 * @throws DaoException
	 * @throws RecordNotFoundException
	 */
	public Privilige findPrivilige(int priviligeId) throws RecordNotFoundException, DaoException {
		return dao.findPrivilige(priviligeId);
	}

	/**
	 * 
	 * @return
	 * @throws DaoException
	 */
	public ArrayList<Privilige> lstPrivilige() throws DaoException {
		return dao.lstPrivilige(null);
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
