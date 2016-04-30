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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.JTextComponent;

import com.fs.commons.desktop.validation.Severity;

/**
 * Class which can decorate a component when it has errors. Decoration is done
 * through providing a border that can be applied to a component.
 *
 * @author Tim Boudreau
 */
public class ComponentDecorator {
	private static class ColorizingBorder implements Border {
		private final Border real;
		private final Severity severity;

		public ColorizingBorder(final Border real, final Severity severity) {
			this.real = real;
			this.severity = severity;
		}

		@Override
		public Insets getBorderInsets(final Component c) {
			return this.real.getBorderInsets(c);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}

		@Override
		public void paintBorder(final Component c, final Graphics g, final int x, final int y, final int width, final int height) {
			this.real.paintBorder(c, g, x, y, width, height);
			g.setColor(this.severity.color());
			final Graphics2D gg = (Graphics2D) g;
			final Composite composite = gg.getComposite();
			final AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05F);
			final Insets ins = getBorderInsets(c);
			try {
				gg.setComposite(alpha);
				gg.fillRect(x, y, width, height);
			} finally {
				gg.setComposite(composite);
			}
			// BufferedImage badge = severity.image();
			// int by = (c.getHeight() / 2) - (badge.getHeight() / 2);
			// int w = Math.max (2, ins.left);
			// int bx = x + width - (badge.getHeight() + (w * 2));
			// gg.drawRenderedImage(badge,
			// AffineTransform.getTranslateInstance(bx, by));
		}
	}

	public static final ComponentDecorator noOpComponentDecorator() {
		return new ComponentDecorator() {
			@Override
			public Border createProblemBorder(final Component c, final Border originalBorder, final Severity severity) {
				return originalBorder;
			}
		};
	}

	/**
	 * Create a border to apply to the component which shows an error. A useful
	 * way to create custom borders is to wrap the original border and paint it,
	 * then paint over it. If the insets of the border returned by this method
	 * are different than the insets of the original border, then the UI layout
	 * will "jump".
	 * <p>
	 * Severity.color() and Severity.image() are handy here.
	 *
	 * @param c
	 *            The component
	 * @param originalBorder
	 *            The original border of the component
	 * @param severity
	 *            The severity of the problem
	 * @return A border. May not be null. To have no effect, simply return the
	 *         original border.
	 */
	public Border createProblemBorder(final Component c, final Border originalBorder, final Severity severity) {
		if (originalBorder instanceof EmptyBorder || !(c instanceof JTextComponent)) {
			return BorderFactory.createLineBorder(Color.red, 1);
		}
		return new ColorizingBorder(originalBorder, severity);
	}

}
