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
package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import com.fs.commons.locale.Lables;

public class JKToggleButton extends JToggleButton {
	/**
	 *
	 */
	private static final long serialVersionUID = 352169284492141935L;
	private Color backgroundOnSelection;// = Color.blue;
	private Border borderOnSelection = BorderFactory.createLoweredBevelBorder();
	private Color colorOnSelection;// = Color.white;

	private Color normalBackground;// = Color.LIGHT_GRAY;
	private Color normalColor;
	private Border normalBorder = BorderFactory.createEmptyBorder();

	public JKToggleButton() {
		this("");
	}

	public JKToggleButton(final String string) {
		super(Lables.get(string));
		init();
	}

	public Color getBackgroundOnSelection() {
		return this.backgroundOnSelection;
	}

	public Border getBorderOnSelection() {
		return this.borderOnSelection;
	}

	public Color getColorOnSelection() {
		return this.colorOnSelection;
	}

	public Color getNormalBackground() {
		return this.normalBackground;
	}

	public Border getNormalBorder() {
		return this.normalBorder;
	}

	public Color getNormalColor() {
		return this.normalColor;
	}

	protected void init() {
		setOpaque(true);
		final ItemListener itemListener = new ItemListener() {
			@Override
			public void itemStateChanged(final ItemEvent itemEvent) {
				final int state = itemEvent.getStateChange();
				if (state == ItemEvent.SELECTED) {
					if (JKToggleButton.this.normalBackground == null) {
						JKToggleButton.this.normalBackground = getBackground();
					}
					if (JKToggleButton.this.normalColor == null) {
						JKToggleButton.this.normalColor = getForeground();
					}
					if (JKToggleButton.this.normalBorder == null) {
						JKToggleButton.this.normalBorder = getBorder();
					}
					if (JKToggleButton.this.backgroundOnSelection == null) {
						JKToggleButton.this.backgroundOnSelection = JKToggleButton.this.normalColor;
					}
					if (JKToggleButton.this.colorOnSelection == null) {
						JKToggleButton.this.colorOnSelection = JKToggleButton.this.normalBackground;
					}
					// setBorder(getBorderOnSelection());
					setBackground(getBackgroundOnSelection());
					// setForeground(getColorOnSelection());

				} else {
					// setBorder(getNormalBorder());
					setBackground(getNormalBackground());
					// setForeground(getNormalColor());
				}
			}
		};
		addItemListener(itemListener);
		setBorder(getNormalBorder());
		setBackground(getNormalBackground());
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		// if (isSelected()) {
		// int w = getWidth();
		// int h = getHeight();
		// g.setColor(getBackground()); // selected color
		// g.fillRect(0, 0, w, h);
		// g.setColor(getForeground()); // selected foreground color
		// g.drawString(getText(), (w -
		// g.getFontMetrics().stringWidth(getText())) / 2 + 1, (h +
		// g.getFontMetrics().getAscent()) / 2 - 1);
		// }
	}

	public void setBackgroundOnSelection(final Color backgroundOnSelection) {
		this.backgroundOnSelection = backgroundOnSelection;
	}

	public void setBorderOnSelection(final Border borderOnSelection) {
		this.borderOnSelection = borderOnSelection;
	}

	public void setColorOnSelection(final Color colorOnSelection) {
		this.colorOnSelection = colorOnSelection;
	}

	public void setNormalBackground(final Color normalBackground) {
		this.normalBackground = normalBackground;
	}

	public void setNormalBorder(final Border normalBorder) {
		this.normalBorder = normalBorder;
	}

	public void setNormalColor(final Color normalColor) {
		this.normalColor = normalColor;
	}

}
