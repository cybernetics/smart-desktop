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
package com.fs.commons.util.mime;

import java.util.ArrayList;

/*
 */

public class InvalidMagicMimeEntryException extends Exception {

	private static final long serialVersionUID = -6705937358834408523L;

	public InvalidMagicMimeEntryException() {
		super("Invalid Magic Mime Entry: Unknown entry");
	}

	public InvalidMagicMimeEntryException(final ArrayList<?> mimeMagicEntry) {
		super("Invalid Magic Mime Entry: " + mimeMagicEntry.toString());
	}
}
