package com.fs.commons.dao.event;

public interface RecordActionListener<ID> {

	/**
	 * 
	 * @param recordId
	 */
	public void recordSelected(ID recordId);

	/**
	 * 
	 * @param recordId
	 */
	public void recordAdded(ID recordId);

	/**
	 * 
	 * @param recordId
	 */
	public void recordUpdated(ID recordId);

	/**
	 * 
	 * @param recordId
	 */
	public void recordDeleted(ID recordId);

	/**
	 * 
	 * @param e
	 */
	public void onException(Exception e);
}
