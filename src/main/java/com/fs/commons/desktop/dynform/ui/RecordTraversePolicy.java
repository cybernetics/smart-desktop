package com.fs.commons.desktop.dynform.ui;

public interface RecordTraversePolicy {
	public int getFirstRecord();
	public int getNextRecord(int recordId);
	public int getPreviouseRecord(int recordId);
	public int getLastRecord();
	public void setCurrentRecord(int recordId);
}
