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

import java.awt.AWTEvent;
import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

/**
 * RoundButton - a class that produces a lightweight button.
 *
 * Lightweight components can have "transparent" areas, meaning that you can see
 * the background of the container behind these areas.
 *
 */
public class RoundButton extends Component {

	/**
	 *
	 */
	private static final long serialVersionUID = 2687577445893395660L;
	ActionListener actionListener; // Post action events to listeners
	String label; // The Button's text
	protected boolean pressed = false; // true if the button is detented.

	/**
	 * Constructs a RoundButton with no label.
	 */
	public RoundButton() {
		this("");
	}

	/**
	 * Constructs a RoundButton with the specified label.
	 * 
	 * @param label
	 *            the label of the button
	 */
	public RoundButton(final String label) {
		this.label = label;
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * Adds the specified action listener to receive action events from this
	 * button.
	 * 
	 * @param listener
	 *            the action listener
	 */
	public void addActionListener(final ActionListener listener) {
		this.actionListener = AWTEventMulticaster.add(this.actionListener, listener);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	}

	/**
	 * Determine if click was inside round button.
	 */
	@Override
	public boolean contains(final int x, final int y) {
		final int mx = getSize().width / 2;
		final int my = getSize().height / 2;
		return (mx - x) * (mx - x) + (my - y) * (my - y) <= mx * mx;
	}

	/**
	 * gets the label
	 * 
	 * @see setLabel
	 */
	public String getLabel() {
		return this.label;
	}

	/**
	 * The minimum size of the button.
	 */
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(100, 100);
	}

	/**
	 * The preferred size of the button.
	 */
	@Override
	public Dimension getPreferredSize() {
		final Font f = getFont();
		if (f != null) {
			final FontMetrics fm = getFontMetrics(getFont());
			final int max = Math.max(fm.stringWidth(this.label) + 40, fm.getHeight() + 40);
			return new Dimension(max, max);
		} else {
			return new Dimension(100, 100);
		}
	}

	/**
	 * paints the RoundButton
	 */
	@Override
	public void paint(final Graphics g) {
		final int s = Math.min(getSize().width - 1, getSize().height - 1);

		// paint the interior of the button
		if (this.pressed) {
			g.setColor(getBackground().darker().darker());
		} else {
			g.setColor(getBackground());
		}
		g.fillArc(0, 0, s, s, 0, 360);

		// draw the perimeter of the button
		g.setColor(getBackground().darker().darker().darker());
		g.drawArc(0, 0, s, s, 0, 360);

		// draw the label centered in the button
		final Font f = getFont();
		if (f != null) {
			final FontMetrics fm = getFontMetrics(getFont());
			g.setColor(getForeground());
			g.drawString(this.label, s / 2 - fm.stringWidth(this.label) / 2, s / 2 + fm.getMaxDescent());
		}
	}

	/**
	 * Paints the button and distribute an action event to all listeners.
	 */
	@Override
	public void processMouseEvent(final MouseEvent e) {
		switch (e.getID()) {
		case MouseEvent.MOUSE_PRESSED:
			// render myself inverted....
			this.pressed = true;

			// Repaint might flicker a bit. To avoid this, you can use
			// double buffering (see the Gauge example).
			repaint();
			break;
		case MouseEvent.MOUSE_RELEASED:
			if (this.actionListener != null) {
				this.actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.label));
			}
			// render myself normal again
			if (this.pressed == true) {
				this.pressed = false;

				// Repaint might flicker a bit. To avoid this, you can use
				// double buffering (see the Gauge example).
				repaint();
			}
			break;
		case MouseEvent.MOUSE_ENTERED:

			break;
		case MouseEvent.MOUSE_EXITED:
			if (this.pressed == true) {
				// Cancel! Don't send action event.
				this.pressed = false;

				// Repaint might flicker a bit. To avoid this, you can use
				// double buffering (see the Gauge example).
				repaint();

				// Note: for a more complete button implementation,
				// you wouldn't want to cancel at this point, but
				// rather detect when the mouse re-entered, and
				// re-highlight the button. There are a few state
				// issues that that you need to handle, which we leave
				// this an an excercise for the reader (I always
				// wanted to say that!)
			}
			break;
		}
		super.processMouseEvent(e);
	}

	/**
	 * Removes the specified action listener so it no longer receives action
	 * events from this button.
	 * 
	 * @param listener
	 *            the action listener
	 */
	public void removeActionListener(final ActionListener listener) {
		this.actionListener = AWTEventMulticaster.remove(this.actionListener, listener);
	}

	/**
	 * sets the label
	 * 
	 * @see getLabel
	 */
	public void setLabel(final String label) {
		this.label = label;
		invalidate();
		repaint();
	}

}
