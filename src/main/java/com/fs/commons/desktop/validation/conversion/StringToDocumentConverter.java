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
package com.fs.commons.desktop.validation.conversion;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
final class StringToDocumentConverter extends Converter<String, Document> {

	private static class DocValidator implements Validator<Document> {
		private final Validator<String> wrapped;

		private DocValidator(final Validator<String> from) {
			this.wrapped = from;
		}

		@Override
		public String toString() {
			return "DocValidator for [" + this.wrapped + "]";
		}

		@Override
		public boolean validate(final Problems problems, final String compName, final Document model) {
			try {
				final String text = model.getText(0, model.getLength());
				return this.wrapped.validate(problems, compName, text);
			} catch (final BadLocationException ex) {
				throw new IllegalStateException(ex);
			}
		}
	}

	@Override
	public Validator<Document> convert(final Validator<String> from) {
		return new DocValidator(from);
	}
}
