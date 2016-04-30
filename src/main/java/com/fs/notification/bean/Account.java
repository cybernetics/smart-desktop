package com.fs.notification.bean;

import java.util.ArrayList;

public class Account {
	int id;
	String number;
	String name;
	String mobile;
	String email;
	boolean active;

	// ////////////////////////////////////////////////////////
	public int getId() {
		return id;
	}

	// //////////////////////////////////////////////////////
	public void setId(int id) {
		this.id = id;
	}

	// //////////////////////////////////////////////////////
	public String getName() {
		return name;
	}

	// //////////////////////////////////////////////////////
	public void setName(String name) {
		this.name = name;
	}


	// ////////////////////////////////////////////////////
	@Override
	public boolean equals(Object obj) {
		Account that = (Account) obj;
		return this.getId() == that.getId();
	}

	// ////////////////////////////////////////////////////
	public boolean isActive() {
		return active;
	}

	// ////////////////////////////////////////////////////
	public void setActive(boolean active) {
		this.active = active;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	
}
