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
package com.fs.commons.desktop.validation.ui;

import com.fs.commons.desktop.validation.Problem;

/**
 * User interface controller which can show the user a problem, possibly
 * disabling closing a dialog or similar if the problem is of Severity.FATAL.
 * <p>
 * <code><a href="ValidationPanel.html">ValidationPanel</a></code> provides an
 * implementation of this interface; it is also relatively easy to write an
 * adapter to drive an existing UI in an existing application.
 * <p/>
 * Note: An instance of ValidationUI should only be used with one
 * ValidationGroup - otherwise one UI having no problems will clear the problem
 * passed by another UI.
 *
 * @author Tim Boudreau
 */
public interface ValidationUI {
	/**
	 * Set the user interface to the state where no problem is displayed and the
	 * user is free to continue.
	 */
	void clearProblem();

	/**
	 * Set the problem to be displayed to the user. Depending on the severity of
	 * the problem, the user interface may want to block the user from
	 * continuing until it is fixed (for example, disabling the Next button in a
	 * wizard or the OK button in a dialog)
	 * 
	 * @param problem
	 *            A problem that the user should be shown, which may affect the
	 *            state of the UI as a whole
	 */
	void setProblem(Problem problem);
}
