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
package com.fs.commons.desktop.swing.comp2;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;

public class FSFixedSizePanel extends JKLabledComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = -5894749030327142078L;

	public FSFixedSizePanel() {
		super();
	}

	public FSFixedSizePanel(final JKLabel lbl, final int labelWidth, final BindingComponent txt) {
		super(lbl, labelWidth, txt);
		// TODO Auto-generated constructor stub
	}

	public FSFixedSizePanel(final JKLabel lbl, final JKTextField txt) {
		super(lbl, txt);
		// TODO Auto-generated constructor stub
	}

	public FSFixedSizePanel(final String labelKey, final BindingComponent comp) {
		super(labelKey, comp);
		// TODO Auto-generated constructor stub
	}

	public FSFixedSizePanel(final String labelKey, final int labelWidth, final BindingComponent comp) {
		super(labelKey, labelWidth, comp);
		// TODO Auto-generated constructor stub
	}

}
