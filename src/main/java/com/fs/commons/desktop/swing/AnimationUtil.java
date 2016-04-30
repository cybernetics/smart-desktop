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
			public void actionPerformed(ActionEvent e) {
				opacity += OPCICITY_INC_DEC;
				window.setOpacity(Math.min(opacity, 1));
				if (opacity >= 1)
					timer.stop();
			}
		});
		window.setOpacity(0);
		timer.start();
	}

	private static void disableParentOfModalDialog(Window window) {
		if (window instanceof JDialog && window.getParent() instanceof JFrame) {
			if (((JDialog) window).isModal()) {
				disable((JFrame) (window.getParent()), null);
			}
		}
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
			public void actionPerformed(ActionEvent e) {
				opacity -= OPCICITY_INC_DEC;
				window.setOpacity(Math.max(opacity, 0));
				if (opacity <= 0) {
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

	/**
	 * 
	 * @param window
	 */
	private static void enableParentOfModalDialog(Window window) {
		if (window instanceof JDialog && window.getParent() instanceof JFrame) {
			if (((JDialog) window).isModal()) {
				enable((JFrame) (window.getParent()));
			}
		}
	}

	/**
	 * 
	 * @param window
	 */
	public static void open(final Window window) {
		final Timer timer = new Timer(FRAME_TIMER_TIMEOUT, null);
		timer.setRepeats(true);
		final Dimension originalSize = SwingUtility.getWindowActualSize(window);
//		final double ratio = originalSize.getWidth() / originalSize.getHeight();

		timer.addActionListener(new ActionListener() {
			private double width=originalSize.getWidth();

			private int height;

			@Override
			public void actionPerformed(ActionEvent e) {
//				width += originalSize.getWidth() / 20;// to be two seconds
//				double height = width / ratio;	
				height+=originalSize.getHeight()/10;

				// System.out.println("width : " + width + "  , height : " +
				// height);
				int newWidth = (int) Math.min(originalSize.getWidth(), width);
				int newHight = (int) Math.min(originalSize.getHeight(), height);
				window.setSize(newWidth, newHight);
//				window.setLocationRelativeTo(null);
				if (originalSize.getWidth() <= width && originalSize.getHeight() <= height) {
					timer.stop();
					SwingUtility.maximumizBoth(window);
				}
			}
		});
		window.setSize((int)originalSize.getWidth(), 1);
		timer.start();
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
			public void actionPerformed(ActionEvent e) {
				width -= originalSize.getWidth() / 20;
				double height = width / ratio;

				// System.out.println("width : " + width + "  , height : " +
				// height);
				int newWidth = (int) Math.max(0, width);
				int newHight = (int) Math.min(0, height);
				window.setSize(newWidth, newHight);
				window.setLocationRelativeTo(null);
				if (width <= 0 && height <= 0) {
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
	public static void disable(JFrame frame, String msg) {
		frame.setGlassPane(new DisabledGlassPane(msg));
		frame.getGlassPane().setVisible(true);
	}

	/**
	 * 
	 * @param frame
	 */
	public static void enable(JFrame frame) {
		frame.setGlassPane(new JPanel());
		frame.getGlassPane().setVisible(false);
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
			public void actionPerformed(ActionEvent e) {
				opacity += 10;
				// Color color=new c
				Color newColor = cloneColor(orginalBackGroundColor, Math.min(opacity, 255));
				panel.setBackground(newColor);
				System.out.println(newColor + " , " + opacity);
				if (opacity >= 255) {
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

	private static Color cloneColor(final Color orginalBackGroundColor, int alpha) {
		int red = orginalBackGroundColor.getRed();
		int green = orginalBackGroundColor.getGreen();
		int blue = orginalBackGroundColor.getBlue();

		return new Color(red, green, blue, alpha);
	}

	public static void main(String[] args) {
		// System.out.println(new Color(255, 255, 255, 1));
		float x = 700;
		float y = 500;
		float ratio = x / y;

		int x2 = 500;
		System.out.println(x2 / ratio);

	}
}

class DisabledGlassPane extends JComponent {
	private final String msg;

	public DisabledGlassPane(String msg) {
		this.msg = msg;
	}

	@Override
	protected void paintComponent(Graphics g) {
		Rectangle clip = g.getClipBounds();
		Color alphaWhite = new Color(1.0f, 1.0f, 1.0f, 0.65f);
		g.setColor(alphaWhite);
		g.fillRect(clip.x, clip.y, clip.width, clip.height);
		if (msg != null) {
			g.setColor(Colors.MENU_PANEL_BG);
			g.setFont(g.getFont().deriveFont(50.0f));
			g.drawString(msg, getWidth() / 2, getHeight() / 4);
		}
	}
}