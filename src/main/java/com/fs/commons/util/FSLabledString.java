package com.fs.commons.util;

import com.fs.commons.locale.Lables;

/**
 * 
 * @author mkiswani
 *
 */
public class FSLabledString {

	private StringBuffer buffer;

	public FSLabledString() {
		buffer = new StringBuffer();
	}

	public FSLabledString(int capacity) {
		buffer = new StringBuffer(capacity);
	}

	public FSLabledString(String str) {
		buffer = new StringBuffer(str);
	}

	public FSLabledString(CharSequence seq) {
		buffer = new StringBuffer(seq);
	}

	public FSLabledString append(String str){
		buffer.append(Lables.get(str));
		return this;
	}
	
	public StringBuffer getBuffer() {
		return buffer;
	}

	@Override
	public String toString() {
		return buffer.toString();
	}
}
