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
package com.fs.commons.desktop.swing.dialogs;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.plaf.LayerUI;

public class DialogUtils {
	private static class DialogBackPanel extends JPanel {
		/**
		 *
		 */
		private static final long serialVersionUID = 4661510795553055144L;
		private static final Paint fill = new Color(0xAAFFFFFF, true);
		private static final ImageIcon shadowImage = new ImageIcon(DialogUtils.class.getResource("dialogShadow.png"));
		private final JComponent cmp;
		private final JLabel title = new JLabel();
		private final JLabel info = new JLabel("Hit 'ESC' to close the dialog");

		public DialogBackPanel(final JDialog dialog) {
			this.cmp = (JComponent) dialog.getContentPane();

			setOpaque(false);
			setLayout(null);
			add(this.cmp);
			add(this.title);
			add(this.info);

			this.cmp.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
			this.title.setFont(new Font("SquareFont", Font.PLAIN, 26));
			this.title.setForeground(Color.WHITE);
			this.info.setForeground(Color.WHITE);

			this.title.setText(dialog.getTitle());
			this.title.setSize(this.title.getPreferredSize());
			this.info.setSize(this.info.getPreferredSize());
			this.cmp.setSize(this.cmp.getPreferredSize());
		}

		@Override
		protected void paintComponent(final Graphics g) {
			super.paintComponent(g);

			final int w = getWidth();
			final int h = getHeight();

			final int shadowX = w / 2 - (this.cmp.getWidth() + 100) / 2;
			final int shadowY = h / 2 - (this.cmp.getHeight() + 100) / 2;
			this.cmp.setLocation(w / 2 - this.cmp.getWidth() / 2, h / 2 - this.cmp.getHeight() / 2);
			this.title.setLocation(w / 2 - this.cmp.getWidth() / 2, h / 2 - this.cmp.getHeight() / 2 - this.title.getHeight());
			this.info.setLocation(w / 2 + this.cmp.getWidth() / 2 - this.info.getWidth(), h / 2 - this.cmp.getHeight() / 2 - this.info.getHeight());

			final Graphics2D gg = (Graphics2D) g.create();
			gg.setPaint(fill);
			gg.fillRect(0, 0, w, h);
			gg.drawImage(shadowImage.getImage(), shadowX, shadowY, this.cmp.getWidth() + 100, this.cmp.getHeight() + 100, null);
			gg.dispose();
		}
	}

	/**
	 * Adds a glass layer to the dialog to intercept all key events. If the
	 * espace key is pressed, the dialog is disposed (either with a fadeout
	 * animation, or directly).
	 */
	public static void addEscapeToCloseSupport(final JDialog dialog, final boolean fadeOnClose) {
		final LayerUI<Container> layerUI = new LayerUI<Container>() {
			/**
			 *
			 */
			private static final long serialVersionUID = -7197890707453641705L;
			private boolean closing = false;

			@Override
			public void eventDispatched(final AWTEvent e, final JLayer<? extends Container> l) {
				if (e instanceof KeyEvent && ((KeyEvent) e).getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (this.closing) {
						return;
					}
					this.closing = true;
					if (fadeOnClose) {
						fadeOut(dialog);
					} else {
						dialog.dispose();
					}
				}
			}

			@Override
			public void installUI(final JComponent c) {
				super.installUI(c);
				((JLayer) c).setLayerEventMask(AWTEvent.KEY_EVENT_MASK);
			}

			@Override
			public void uninstallUI(final JComponent c) {
				super.uninstallUI(c);
				((JLayer) c).setLayerEventMask(0);
			}
		};

		final JLayer<Container> layer = new JLayer<>(dialog.getContentPane(), layerUI);
		dialog.setContentPane(layer);
	}

	/**
	 * Centers the dialog over the given parent component. Also, creates a
	 * semi-transparent panel behind the dialog to mask the parent content. The
	 * title of the dialog is displayed in a custom fashion over the dialog
	 * panel, and a rectangular shadow is placed behind the dialog.
	 */
	public static void createDialogBackPanel(final JDialog dialog, final Container parent) {
		final DialogBackPanel newContentPane = new DialogBackPanel(dialog);
		dialog.setContentPane(newContentPane);
		dialog.setSize(parent.getSize());
		if (parent != null && parent instanceof JFrame) {
			final JFrame frm = (JFrame) parent;
			dialog.setLocation(frm.getContentPane().getLocationOnScreen());
		}
	}

	/**
	 * Creates an animation to fade the dialog opacity from 0 to 1.
	 */
	public static void fadeIn(final JDialog dialog) {
		final Timer timer = new Timer(10, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = 0;

			@Override
			public void actionPerformed(final ActionEvent e) {
				this.opacity += 0.15f;
				dialog.setOpacity(Math.min(this.opacity, 1));
				if (this.opacity >= 1) {
					timer.stop();
				}
			}
		});

		dialog.setOpacity(0);
		timer.start();
		dialog.setVisible(true);
	}

	// -------------------------------------------------------------------------
	// Helpers
	// -------------------------------------------------------------------------

	/**
	 * Creates an animation to fade the dialog opacity from 1 to 0.
	 */
	public static void fadeOut(final JDialog dialog) {
		final Timer timer = new Timer(10, null);
		timer.setRepeats(true);
		timer.addActionListener(new ActionListener() {
			private float opacity = 1;

			@Override
			public void actionPerformed(final ActionEvent e) {
				this.opacity -= 0.15f;
				dialog.setOpacity(Math.max(this.opacity, 0));
				if (this.opacity <= 0) {
					timer.stop();
					dialog.dispose();
				}
			}
		});

		dialog.setOpacity(1);
		timer.start();
	}
}