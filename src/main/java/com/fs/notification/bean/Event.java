package com.fs.notification.bean;

public class Event {

	
	int id;
	NotificationType notificationType;
	String title;
	String text;
	Status status;

	// //////////////////////////////////////////////////////
	public int getId() {
		return id;
	}

	// //////////////////////////////////////////////////////
	public void setId(int id) {
		this.id = id;
	}

	// //////////////////////////////////////////////////////
	public String getTitle() {
		return title;
	}

	// //////////////////////////////////////////////////////
	public void setTitle(String title) {
		this.title = title;
	}

	// //////////////////////////////////////////////////////
	public String getText() {
		return text;
	}

	// //////////////////////////////////////////////////////
	public void setText(String text) {
		this.text = text;
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
		Event that = (Event) obj;
		return this.getId() == that.getId();
	}

	public NotificationType getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

}
