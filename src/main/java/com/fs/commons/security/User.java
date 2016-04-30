package com.fs.commons.security;

import com.fs.commons.util.TextUtil;


public class User {
	private int userRecordId;
	private String userId;
	private String fullName;
	private String password;
	private int status;

	private boolean disabled;
	
	public User() {
	}

	/**
	 * 
	 * @param userRecordId
	 */
	public User(int userRecordId) {
		this.userRecordId = userRecordId;
	}

	public User(int id, String name, String fullName) {
		userRecordId = id;
		userId = name;
		this.fullName = fullName;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 
	 * @return
	 */
	public String getFullName() {
		if(TextUtil.isEmpty(fullName)){
			return getUserId();
		}
		return fullName;
	}

	/**
	 * 
	 * @param name
	 */
	public void setFullName(String name) {
		this.fullName = name;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * 
	 * @param disabled
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public int getUserRecordId() {
		return userRecordId;
	}

	public void setUserRecordId(int userRecordId) {
		this.userRecordId = userRecordId;
	}

	public int getId() {
		return getUserRecordId();
	}

}
