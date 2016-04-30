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

import java.util.HashSet;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.text.Document;

import com.fs.commons.desktop.validation.Validator;
import com.fs.commons.desktop.validation.builtin.FSValidators;

/**
 * Converts a validator of one type to a validator that works against a
 * different type. In this way, it is possible to write only validators for
 * Strings, but use them against <code>javax.swing.text.Document</code>s, etc.
 *
 * @author Tim Boudreau
 */
public abstract class Converter<From, To> {
	private static final class Entry<From, To> {
		private final Class<From> from;
		private final Class<To> to;
		private final Converter<From, To> converter;

		public Entry(final Class<From> from, final Class<To> to, final Converter<From, To> converter) {
			this.from = from;
			this.to = to;
			this.converter = converter;
		}

		Converter<From, To> getConverter() {
			return this.converter;
		}

		public boolean match(final Class<?> from, final Class<?> to) {
			return this.from.equals(from) && this.to.equals(to);
		}
	}

	private static Set<Entry> registry = new HashSet<Entry>();

	static {
		Converter.<String, Document> register(String.class, Document.class, new StringToDocumentConverter());
		Converter.<String, ComboBoxModel> register(String.class, ComboBoxModel.class, new StringToComboBoxModelConverter());
	}

	/**
	 * Find a converter to create validators for one type from validators for
	 * another type.
	 * 
	 * @param <From>
	 *            The type of object we get from a component, such as a
	 *            <code>javax.swing.text.Document</code>
	 * @param <To>
	 *            The type of object we want to process, such as a
	 *            <code>java.lang.String</code>
	 * @param from
	 *            A class, such as <code>Document.class</code>
	 * @param to
	 *            A class such as <code>String.class</code>
	 * @return An object which can take validators for type <code>From</code>
	 *         and produce validators for type <code>To</code>
	 */
	public static <From, To> Converter<From, To> find(final Class<From> from, final Class<To> to) {
		for (final Entry e : registry) {
			if (e.match(from, to)) {
				return e.getConverter();
			}
		}
		throw new IllegalArgumentException("No registered converter from " + from.getName() + " to " + to.getName());
	}

	/**
	 * Register a converter
	 * 
	 * @param <From>
	 * @param <To>
	 * @param from
	 * @param to
	 * @param converter
	 */
	public static <From, To> void register(final Class<From> from, final Class<To> to, final Converter<From, To> converter) {
		registry.add(new Entry(from, to, converter));
	}

	/**
	 * Create a validator for type To by wrapping a validator for type From. For
	 * example, a converter that is a factory for Validators of
	 * javax.swing.text.Documents may be created from a validator that only
	 * handles Strings. Convert would simply return a Validator that first calls
	 * Document.getText(), and passes the result to the Validator<String>.
	 * 
	 * @param from
	 *            The original validator.
	 * @return A validator of the type requested
	 */
	public abstract Validator<To> convert(Validator<From> from);

	public final Validator<To> convert(final Validator<From>... froms) {
		return convert(FSValidators.merge(froms));
	}
}
