/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.commons.util;

import com.fs.commons.locale.Lables;

/**
 *
 * @author mkiswani
 *
 */
public class FSLabledString {

	private final StringBuffer buffer;

	public FSLabledString() {
		this.buffer = new StringBuffer();
	}

	public FSLabledString(final CharSequence seq) {
		this.buffer = new StringBuffer(seq);
	}

	public FSLabledString(final int capacity) {
		this.buffer = new StringBuffer(capacity);
	}

	public FSLabledString(final String str) {
		this.buffer = new StringBuffer(str);
	}

	public FSLabledString append(final String str) {
		this.buffer.append(Lables.get(str));
		return this;
	}

	public StringBuffer getBuffer() {
		return this.buffer;
	}

	@Override
	public String toString() {
		return this.buffer.toString();
	}
}
