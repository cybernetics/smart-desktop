package com.fs.commons.util.mime;

import java.util.ArrayList;

/*
 */

public class InvalidMagicMimeEntryException extends Exception {

	private static final long serialVersionUID = -6705937358834408523L;

	public InvalidMagicMimeEntryException() {
        super("Invalid Magic Mime Entry: Unknown entry");
    }
    
    public InvalidMagicMimeEntryException(ArrayList<?> mimeMagicEntry) {
    	super("Invalid Magic Mime Entry: " + mimeMagicEntry.toString());
    }
}
