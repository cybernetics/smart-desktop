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

import java.awt.ComponentOrientation;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.plaf.TabbedPaneUI;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.desktop.swing.jtabbedpaneui.AquaBarTabbedPaneUI;

public class TabbedUITest {

	private static JTabbedPane createTab(final TabbedPaneUI ui) {
		final JTabbedPane p = new JTabbedPane();
		p.setUI(ui);
		for (int i = 0; i < 10; i++) {
			p.addTab("Tab " + i, new JTextField());
		}
		return p;
	}

	public static void main(final String[] args) throws FileNotFoundException, ApplicationException {
		final JPanel pnl = new JPanel();
		pnl.setLayout(new GridLayout(6, 1));
		pnl.add(createTab(new AquaBarTabbedPaneUI()));
		pnl.add(createTab(new javax.swing.plaf.metal.MetalTabbedPaneUI()));
		pnl.add(createTab(new com.jgoodies.looks.plastic.PlasticTabbedPaneUI()));
		pnl.add(createTab(new com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI()));

		pnl.add(createTab(new com.sun.java.swing.plaf.motif.MotifTabbedPaneUI()));

		pnl.add(createTab(new PSTabbedPaneUI()));

		final JFrame f = new JFrame();
		f.add(pnl);
		f.setExtendedState(Frame.MAXIMIZED_BOTH);
		f.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		f.setVisible(true);

	}
}
