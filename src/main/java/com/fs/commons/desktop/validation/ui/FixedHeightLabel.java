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

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 *
 * @author Tim Boudreau
 */
final class FixedHeightLabel extends JLabel {
	/**
	 *
	 */
	private static final long serialVersionUID = 8941805173808099166L;
	private int fixedHeight = -1;

	public FixedHeightLabel() {
		setText("   "); // avoid 0 initial width
	}

	@Override
	public Dimension getPreferredSize() {
		final Dimension result = super.getPreferredSize();
		if (result.height > 0 && result.height > this.fixedHeight) {
			this.fixedHeight = result.height;
		}
		if (this.fixedHeight > 0) {
			result.height = this.fixedHeight;
		}
		if (result.height < 10) {
			result.height = 16;
		}
		return result;
	}

	@Override
	public void setFont(final Font font) {
		super.setFont(font);
		this.fixedHeight = -1;
	}

	@Override
	public void setIcon(final Icon icon) {
		this.fixedHeight = -1;
		super.setIcon(icon);
	}
}
