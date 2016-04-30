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
package com.fs.commons.desktop.validation.builtin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fs.commons.desktop.validation.Problem;
import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.Validator;

/**
 *
 * @author Tim Boudreau
 */
class EmailAddressValidator implements Validator<String> {

    private final ValidHostNameOrIPValidator hv = new ValidHostNameOrIPValidator(false);
    static final Pattern ADDRESS_PATTERN = Pattern.compile("(.*?)<(.*)>$"); //NOI18N

    public boolean validate(Problems problems, String compName, String model) {
        Matcher m = ADDRESS_PATTERN.matcher(model);
        String realName = null;
        String address;
        if (m.lookingAt()) {
            if (m.groupCount() == 2) {
                address = m.group(2);
                realName = m.group(1);
            } else {
                address = m.group(1);
            }
        } else {
            address = model;
        }
        return validate(realName, address, problems, compName);
    }

    private boolean validate(String realName, String address, Problems problems, String compName) {
        String[] nameAndHost = address.split("@");
        if (nameAndHost.length == 1 && nameAndHost[0].contains("@")) {
            problems.add(NbBundle.getMessage(EmailAddressValidator.class,
                    "EMAIL_MISSING_HOST", compName, nameAndHost[0]));
            return false;
        }
        if (nameAndHost.length > 2) {
            problems.add(NbBundle.getMessage(EmailAddressValidator.class,
                    "EMAIL_HAS_>1_@", compName, address));
            return false;
        }
        String name = nameAndHost[0];
        if (name.length() == 0) {
            problems.add(NbBundle.getMessage(EmailAddressValidator.class,
                    "EMAIL_MISSING_NAME", compName, name));
            return false;
        }
        if (name.length() > 64) {
            problems.add (new Problem (NbBundle.getMessage(EmailAddressValidator.class,
                    "ADDRESS_MAY_BE_TOO_LONG", compName, name), Severity.WARNING));
        }
        String host = nameAndHost.length >= 2 ? nameAndHost[1] : null;
        boolean result = host != null;
        if (result) {
            result = hv.validate(problems, compName, host);
            if (result) {
                MayNotContainSpacesValidator v = new MayNotContainSpacesValidator();
                result = v.validate(problems, compName, name);
            }
            Validator<String> v = new EncodableInCharsetValidator("US-ASCII");
            if (result) {
                result = v.validate(problems, compName, address);
            }
        } else {
            problems.add(NbBundle.getMessage(EmailAddressValidator.class,
                    "EMAIL_MISSING_HOST", compName, nameAndHost[0]));
        }
        return result;
    }

    private boolean validate(String realName, String name, String host, Problems problems, String compName) {
        return false;
    }
}
