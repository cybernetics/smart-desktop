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
package com.fs.commons.desktop.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimationUtil {
	private static final int FRAME_TIMER_TIMEOUT = 50;
	private static final float OPCICITY_INC_DEC = 0.10f;
	private static final int FADE_TIMER_DURATION = 3;

	private static Color cloneColor(final Color orginalBackGroundColor, final int alpha) {
		final int red = orginalBackGroundColor.getRed();
		final int green = orginalBackGroundColor.getGreen();
		final int blue = orginalBackGroundColor.getBlue();

		return new Color(red, green, blue, alpha);
	}

	/**
	 *
	 * @param window
	 * @param dispose
	 */
	public static void close(final Window window, final boolean dispose) {
		final Timer timer = new Timer(FRAME_TIMER_TIMEOUT, null);
		timer.setRepeats(true);
		final Dimension originalSize = SwingUtility.getWindowActualSize(window);
		final double ratio = originalSize.getWidth() / originalSize.getHeight();

		timer.addActionListener(new ActionListener() {
			private double width = originalSize.getWidth();

			// private int height;

			@Override
			public void actionPerformed(final ActionEvent e) {
				this.width -= originalSize.getWidth() / 20;
				final double height = this.width / ratio;

				// System.out.println("width : " + width + " , height : " +
				// height);
				final int newWidth = (int) Math.max(0, this.width);
				final int newHight = (int) Math.min(0, height);
				window.setSize(newWidth, newHight);
				window.setLocationRelativeTo(null);
				if (this.width <= 0 && height <= 0) {
					timer.stop();
					if (dispose) {
						window.dispose();
					} else {
						window.setVisible(false);
					}

				}
			}
		});
		timer.start();
	}

	/**
	 *
	 * @param frame
	 * @param msg
	 */
	public static void disable(final JFrame frame, final String msg) {
		frame.setGlassPane(new DisabledGlassPane(msg));
		frame.getGlassPane().setVisible(true);
	}

	private static void disableParentOfModalDialog(final Window window) {
		if (window instanceof JDialog && window.getParent() instanceof JFrame) {
			if (((JDialog) window).isModal()) {
				disable((JFrame) window.getParent(), null);
			}
		}
	}

	/**
	 *
	 * @param frame
	 */
	public static void enable(final JFrame frame) {
		frame.setGlassPane(new JPanel());
		frame.getGlassPane().setVisible(false);
	}

	/**
	 *
	 * @param window
	 */
	private static void enableParentOfModalDialog(final Window window) {
		if (window instanceof JDialog && window.getParent() instanceof JFrame) {
			if (((JDialog) window).isModal()) {
				enable((JFrame) window.getParent());
			}
		}
	}

	/**
	 *
	 * @param panel
	 */
	public static void fadeIn(final JPanel panel) {
		final Color orginalBackGroundColor = panel.getBackground();
		final boolean origonalOpaque = panel.isOpaque();
		final Timer timer = new Timer(FADE_TIMER_DURATION, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private int opacity = 0;

			@Override
			public void actionPerformed(final ActionEvent e) {
				this.opacity += 10;
				// Color color=new c
				final Color newColor = cloneColor(orginalBackGroundColor, Math.min(this.opacity, 255));
				panel.setBackground(newColor);
				System.out.println(newColor + " , " + this.opacity);
				if (this.opacity >= 255) {
					timer.stop();
					panel.setOpaque(origonalOpaque);
					// panel.setBackground(orginalBackGroundColor);
				}
			}
		});
		panel.setOpaque(false);
		panel.setBackground(cloneColor(orginalBackGroundColor, 0));
		timer.start();

	}

	/**
	 *
	 */
	public static void fadeIn(final Window window) {
		disableParentOfModalDialog(window);
		final Timer timer = new Timer(FADE_TIMER_DURATION, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = 0;

			@Override
			public void actionPerformed(final ActionEvent e) {
				this.opacity += OPCICITY_INC_DEC;
				window.setOpacity(Math.min(this.opacity, 1));
				if (this.opacity >= 1) {
					timer.stop();
				}
			}
		});
		window.setOpacity(0);
		timer.start();
	}

	/**
	 *
	 */
	public static void fadeOut(final Window window, final boolean dispose) {
		enableParentOfModalDialog(window);
		final Timer timer = new Timer(FADE_TIMER_DURATION, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = 1;

			@Override
			public void actionPerformed(final ActionEvent e) {
				this.opacity -= OPCICITY_INC_DEC;
				window.setOpacity(Math.max(this.opacity, 0));
				if (this.opacity <= 0) {
					timer.stop();
					if (dispose) {
						window.dispose();
					} else {
						window.setVisible(false);
					}
				}
			}
		});
		window.setOpacity(1);
		timer.start();
	}

	public static void main(final String[] args) {
		// System.out.println(new Color(255, 255, 255, 1));
		final float x = 700;
		final float y = 500;
		final float ratio = x / y;

		final int x2 = 500;
		System.out.println(x2 / ratio);

	}

	/**
	 *
	 * @param window
	 */
	public static void open(final Window window) {
		final Timer timer = new Timer(FRAME_TIMER_TIMEOUT, null);
		timer.setRepeats(true);
		final Dimension originalSize = SwingUtility.getWindowActualSize(window);
		// final double ratio = originalSize.getWidth() /
		// originalSize.getHeight();

		timer.addActionListener(new ActionListener() {
			private final double width = originalSize.getWidth();

			private int height;

			@Override
			public void actionPerformed(final ActionEvent e) {
				// width += originalSize.getWidth() / 20;// to be two seconds
				// double height = width / ratio;
				this.height += originalSize.getHeight() / 10;

				// System.out.println("width : " + width + " , height : " +
				// height);
				final int newWidth = (int) Math.min(originalSize.getWidth(), this.width);
				final int newHight = (int) Math.min(originalSize.getHeight(), this.height);
				window.setSize(newWidth, newHight);
				// window.setLocationRelativeTo(null);
				if (originalSize.getWidth() <= this.width && originalSize.getHeight() <= this.height) {
					timer.stop();
					SwingUtility.maximumizBoth(window);
				}
			}
		});
		window.setSize((int) originalSize.getWidth(), 1);
		timer.start();
	}
}

class DisabledGlassPane extends JComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = 5305694456356793873L;
	private final String msg;

	public DisabledGlassPane(final String msg) {
		this.msg = msg;
	}

	@Override
	protected void paintComponent(final Graphics g) {
		final Rectangle clip = g.getClipBounds();
		final Color alphaWhite = new Color(1.0f, 1.0f, 1.0f, 0.65f);
		g.setColor(alphaWhite);
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		if (this.msg != null) {
			g.setColor(Colors.MENU_PANEL_BG);
			g.setFont(g.getFont().deriveFont(50.0f));
			g.drawString(this.msg, getWidth() / 2, getHeight() / 4);
		}
	}
}