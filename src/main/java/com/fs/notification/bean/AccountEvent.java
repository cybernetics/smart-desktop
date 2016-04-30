package com.fs.notification.bean;

import com.fs.notification.NotificationConstants;

public class AccountEvent {
	int id;
	Account account;
	Event event;
	Status status;

	// //////////////////////////////////////////////////////
	public AccountEvent(Event event, Account account) {
		this.event = event;
		this.account = account;
		this.status=new Status(NotificationConstants.EVENT_STATUS_NEW);
	}

	// //////////////////////////////////////////////////////
	public AccountEvent() {
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
	public Account getAccount() {
		return account;
	}

	// //////////////////////////////////////////////////////
	public void setAccount(Account account) {
		this.account = account;
	}

	// //////////////////////////////////////////////////////
	public Event getEvent() {
		return event;
	}

	// //////////////////////////////////////////////////////
	public void setEvent(Event event) {
		this.event = event;
	}

	// //////////////////////////////////////////////////////
	public Status getStatus() {
		return status;
	}

	// //////////////////////////////////////////////////////
	public void setStatus(Status status) {
		this.status = status;
	}

	// //////////////////////////////////////////////////////
	@Override
	public boolean equals(Object obj) {
		AccountEvent that = (AccountEvent) obj;
		return this.getId() == that.getId();
	}
}
