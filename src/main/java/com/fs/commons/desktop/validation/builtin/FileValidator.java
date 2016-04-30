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
package com.fs.commons.desktop.validation.builtin;

import java.io.File;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class FileValidator implements Validator<String> {
	enum Type {
		MUST_EXIST, MUST_NOT_EXIST, MUST_BE_DIRECTORY, MUST_BE_FILE,
	}

	private final Type type;

	FileValidator(final Type type) {
		this.type = type;
	}

	@Override
	public boolean validate(final Problems problems, final String compName, final String model) {
		final File file = new File(model);
		String key;
		boolean ok;
		switch (this.type) {
		case MUST_EXIST:
			key = "FILE_DOES_NOT_EXIST"; // NOI18N
			ok = file.exists();
			break;
		case MUST_BE_DIRECTORY:
			key = "FILE_IS_NOT_A_DIRECTORY"; // NOI18N
			ok = file.isDirectory();
			break;
		case MUST_BE_FILE:
			key = "FILE_IS_NOT_A_FILE"; // NOI18N
			ok = file.isFile();
			break;
		case MUST_NOT_EXIST:
			key = "FILE_EXISTS"; // NOI18N
			ok = !file.exists();
			break;
		default:
			throw new AssertionError();
		}
		if (!ok) {
			final String problem = NbBundle.getMessage(FileValidator.class, key, file.getName());
			problems.add(problem, Severity.FATAL);
		}
		return ok;
	}

}
