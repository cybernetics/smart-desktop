/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2009 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2006 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
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
 * different type.  In this way, it is possible to write only validators
 * for Strings, but use them against <code>javax.swing.text.Document</code>s, etc.
 *
 * @author Tim Boudreau
 */
public abstract class Converter<From,To> {
    private static Set <Entry> registry = new HashSet<Entry>();
    /**
     * Create a validator for type To by wrapping a validator for type
     * From.  For example, a converter that is a factory for Validators
     * of javax.swing.text.Documents may be created from a validator
     * that only handles Strings.  Convert would simply return a Validator
     * that first calls Document.getText(), and passes the result to
     * the Validator<String>.
     * @param from The original validator.
     * @return A validator of the type requested
     */
    public abstract Validator<To> convert (Validator<From> from);

    public final Validator<To> convert (Validator<From>... froms) {
        return (convert(FSValidators.merge(froms)));
    }

    /**
     * Register a converter
     * @param <From>
     * @param <To>
     * @param from
     * @param to
     * @param converter
     */
    public static <From, To> void register (Class<From> from, Class<To> to, Converter<From,To> converter) {
        registry.add (new Entry (from, to, converter));
    }

    static {
        Converter.<String,Document>register (String.class, Document.class, new StringToDocumentConverter());
        Converter.<String,ComboBoxModel>register (String.class, ComboBoxModel.class, new StringToComboBoxModelConverter());
    }

    /**
     * Find a converter to create validators for one type from validators for
     * another type.
     * @param <From> The type of object we get from a component, such as a
     * <code>javax.swing.text.Document</code>
     * @param <To> The type of object we want to process, such as a
     * <code>java.lang.String</code>
     * @param from A class, such as <code>Document.class</code>
     * @param to A class such as <code>String.class</code>
     * @return An object which can take validators for type <code>From</code>
     * and produce validators for type <code>To</code>
     */
    public static<From, To> Converter<From, To> find (Class<From> from, Class<To> to) {
        for (Entry e : registry) {
            if (e.match(from, to)) {
                return e.getConverter();
            }
        }
        throw new IllegalArgumentException ("No registered converter from " +
                from.getName() + " to " + to.getName());
    }

    private static final class Entry<From, To> {
        private final Class<From> from;
        private final Class<To> to;
        private Converter<From,To> converter;

        public boolean match (Class<?> from, Class<?> to) {
            return this.from.equals(from) && this.to.equals(to);
        }

        public Entry(Class<From> from, Class<To> to, Converter<From, To> converter) {
            this.from = from;
            this.to = to;
            this.converter = converter;
        }

        Converter<From, To> getConverter() {
            return converter;
        }
    }
}
