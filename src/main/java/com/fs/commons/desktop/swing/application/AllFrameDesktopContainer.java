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
package com.fs.commons.desktop.swing.application;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class AllFrameDesktopContainer {
	public class MyAction implements MenuListener {
		@Override
		public void menuCanceled(final MenuEvent me) {
		}

		@Override
		public void menuDeselected(final MenuEvent me) {
		}

		@Override
		public void menuSelected(final MenuEvent me) {
			final int i = AllFrameDesktopContainer.this.desk.getAllFrames().length;
			JOptionPane.showMessageDialog(null, "Total visible internal frames are : " + i, "Roseindia.net", 1);
		}
	}

	public static void main(final String[] args) {
	}

	JDesktopPane desk;

	JInternalFrame iframe;

	JFrame frame;

	public AllFrameDesktopContainer() {
		this.frame = new JFrame("All Frames in a JDesktopPane Container");
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.desk = new JDesktopPane();
		try {
			final String str = JOptionPane.showInputDialog(null, "Enter number of frames :", "Roseindia.net", 1);
			final int i = Integer.parseInt(str);
			for (int j = 1; j <= i; j++) {
				this.iframe = new JInternalFrame("Internal Frame: " + j, true, true, true, true);
				this.iframe.setBounds(j * 20, j * 20, 150, 100);
				this.iframe.setVisible(true);
				this.desk.add(this.iframe);
				this.iframe.setToolTipText("Internal Frame :" + j);
			}
		} catch (final NumberFormatException ne) {
			JOptionPane.showMessageDialog(null, "Please enter number value.", "Roseindia.net", 1);
			System.exit(0);
		}
		final JMenuBar menubar = new JMenuBar();
		final JMenu count = new JMenu("Count Total Frames");
		count.addMenuListener(new MyAction());
		menubar.add(count);
		this.frame.setJMenuBar(menubar);
		this.frame.add(this.desk);
		this.frame.setSize(400, 400);
		this.frame.setVisible(true);
	}
}