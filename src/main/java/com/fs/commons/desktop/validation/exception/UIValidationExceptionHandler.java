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
package com.fs.commons.desktop.validation.exception;

import javax.swing.JComponent;

import com.fs.commons.desktop.validation.Problems;
import com.fs.commons.desktop.validation.Severity;
import com.fs.commons.desktop.validation.ui.ComponentDecorator;
import com.jk.exceptions.handler.JKDefaultExceptionHandler;
import com.jk.exceptions.handler.JKExceptionHandler;

public class UIValidationExceptionHandler extends JKDefaultExceptionHandler<UIValidationException> {

	@Override
	public void handle(final UIValidationException e, boolean throwRuntime) {
		final Problems problems = e.getProblems();
		if (e.getComponent() != null) {
			final ComponentDecorator d = new ComponentDecorator();
			final JComponent component = (JComponent) e.getComponent();
			component.setBorder(d.createProblemBorder(component, component.getBorder(), Severity.FATAL));
			component.requestFocus();
			if (problems != null) {
				component.setToolTipText(problems.getLeadProblem().getMessage());
			}
		} else {
			super.handle(e, throwRuntime);
		}
	}
}
