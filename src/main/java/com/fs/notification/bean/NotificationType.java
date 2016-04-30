package com.fs.notification.bean;

public class NotificationType {
	int id;
	String name;
	String impl;

	// //////////////////////////////////////////////////////
	public String getImpl() {
		return impl;
	}

	// //////////////////////////////////////////////////////
	public void setImpl(String impl) {
		this.impl = impl;
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
	@Override
	public boolean equals(Object obj) {
		NotificationType that = (NotificationType) obj;
		return this.getId() == that.getId();
	}
}
