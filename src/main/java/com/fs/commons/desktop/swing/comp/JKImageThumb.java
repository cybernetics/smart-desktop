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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.fs.commons.desktop.swing.SwingPrintUtility;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;

/**
 * @author u087
 *
 */
public class JKImageThumb extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JPanel pnlImage;

	byte[] imageData;

	/**
	 *
	 */
	public JKImageThumb(final byte[] image) {
		this.imageData = image;
		this.pnlImage = SwingUtility.buildImagePanel(image, ImagePanel.SCALED);
		init();
	}

	/**
	 *
	 */
	protected void handleShowImage() {
		final JKPanel pnl = new JKPanel(new BorderLayout());
		final JPanel pnlImage = SwingUtility.buildImagePanel(this.imageData, ImagePanel.SCALED);
		pnlImage.setPreferredSize(new Dimension(450, 600));
		final JKPanel pnlButtons = new JKPanel();
		final JKButton btnPrint = new JKButton("Print");
		final JKButton btnClose = new JKButton("Close");
		btnPrint.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileprint.png")));
		btnClose.setIcon(new ImageIcon(GeneralUtility.getIconURL("cancel.png")));

		pnlButtons.add(btnPrint);
		pnlButtons.add(btnClose);
		pnlImage.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
		pnl.add(pnlImage, BorderLayout.CENTER);
		pnl.add(pnlButtons, BorderLayout.SOUTH);

		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SwingUtility.closePanelWindow(pnl);
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SwingPrintUtility.printComponent(pnlImage);
			}
		});
		SwingUtility.showPanelInDialog(pnl, "Preview");
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		this.pnlImage.setPreferredSize(new Dimension(100, 100));
		setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		add(this.pnlImage, BorderLayout.CENTER);

		this.pnlImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 1) {
					handleShowImage();
				}
			}
		});
	}
}
