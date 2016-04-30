package com.fs.notification.bean;

public class Status {
	int id;
	String name;
	boolean active;

	public Status() {
		// TODO Auto-generated constructor stub
	}
	public Status(int id) {
		this.id = id;
	}

	// //////////////////////////////////////////////////////
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

	// //////////////////////////////////////////////////////
	public boolean isActive() {
		return active;
	}

	// //////////////////////////////////////////////////////
	public void setActive(boolean active) {
		this.active = active;
	}

	// //////////////////////////////////////////////////////
	@Override
	public boolean equals(Object obj) {
		Status that = (Status) obj;
		return this.getId() == that.getId();
	}
}
