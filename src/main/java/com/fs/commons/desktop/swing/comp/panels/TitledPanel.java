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
package com.fs.commons.desktop.swing.comp.panels;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;

import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.desktop.graphics.GraphicsFactory.GradientType;
import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKFrame;
import com.fs.commons.desktop.swing.comp.JKLabel;
import com.fs.commons.locale.Lables;

class JKTitleButton extends JKButton {

	/**
	 *
	 */
	private static final long serialVersionUID = -331979725257583100L;

	public JKTitleButton(final String string) {
		super(string);
		setFocusable(false);
		setOpaque(false);
		setBorder(null);
		setPreferredSize(new Dimension(24, 24));
		setLightView(true);
	}

}

public class TitledPanel extends JKMainPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final String title;
	private final String icon;
	protected UIPanel panel;

	JKMainPanel pnlTitleBar = new JKMainPanel(new GridLayout(1, 2, 2, 2));
	JKPanel<?> pnlTitle = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));
	JKPanel<?> pnlButtons = new JKPanel<Object>(new FlowLayout(FlowLayout.TRAILING));

	JKButton btnAddToFavorites = new JKTitleButton("");
	JKButton btnShowInFrame = new JKTitleButton("");
	JKButton btnReload = new JKTitleButton("");
	JKButton btnClose = new JKTitleButton("");

	JKButton btnNext = new JKTitleButton(">");
	JKButton btnPrevious = new JKTitleButton("<");

	JKLabel lbl;

	/**
	 *
	 * @param title
	 * @param panelFactory
	 * @param panelProp
	 */
	public TitledPanel(final String title, final UIPanel panel) {
		this(title, panel, null);
	}

	/**
	 *
	 * @param title
	 * @param panelFactory
	 * @param panelProp
	 * @param icon
	 * @param cached
	 */
	public TitledPanel(final String title, final UIPanel panel, final String icon) {
		this.title = title;
		this.panel = panel;
		this.icon = icon;
		init();
		showPanel();
	}

	/**
	 *
	 * @return
	 */
	private JKPanel<?> getTitleBar() {
		initComponenets();
		this.pnlTitleBar.setGradientType(GradientType.HORIZENTAL);
		this.btnClose.setShortcut("control X", "");
		this.btnReload.setShortcut("control R", "");
		this.pnlTitle.add(this.lbl);

		this.pnlButtons.add(this.btnAddToFavorites);
		this.pnlButtons.add(this.btnShowInFrame);
		this.pnlButtons.add(this.btnReload);
		this.pnlButtons.add(this.btnPrevious);
		this.pnlButtons.add(this.btnNext);
		this.pnlButtons.add(this.btnClose);

		this.pnlTitleBar.add(this.pnlTitle);
		this.pnlTitleBar.add(this.pnlButtons);

		this.btnAddToFavorites.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleAddToFavorites();
			}
		});
		this.btnShowInFrame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleShowInFrame();
			}
		});
		this.btnReload.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleReload();
			}
		});

		this.btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				SwingUtility.getDefaultMainFrame().showHomePanel();
			}
		});
		this.btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleNext();
			}
		});
		this.btnPrevious.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handlePreviouse();
			}
		});
		return this.pnlTitleBar;
	}

	/**
	 *
	 */
	protected void handleAddToFavorites() {
		// by default empty implementation
	}

	protected void handleNext() {
		// TODO Auto-generated method stub

	}

	protected void handlePreviouse() {
		// TODO Auto-generated method stub

	}

	/**
	 *
	 */
	protected void handleReload() {
		// by default empty implementation
	}

	/**
	 *
	 */
	protected void handleShowInFrame() {
		final JKFrame frame = SwingUtility.showPanelFrame(this, this.title);
		frame.setExtendedState(JKFrame.NORMAL);
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		final JKPanel<?> titleBar = getTitleBar();

		add(titleBar, BorderLayout.NORTH);
		applyComponentOrientation(SwingUtility.getDefaultComponentOrientation());

	}

	/**
	 *
	 */
	private void initComponenets() {
		// pnlTitleBar.setBorder(BorderFactory.createLineBorder(new Color(200,
		// 200, 200)));
		this.pnlTitleBar.setBackground(Colors.JK_TITLE_BAR_BG);// new Color(191,
																// 215,
																// 255));
		this.pnlTitleBar.setPreferredSize(new Dimension(800, 35));
		this.lbl = new JKLabel();
		this.lbl.setText(Lables.get(this.title, true));
		this.lbl.setOpaque(false);
		this.lbl.setForeground(Colors.JK_TITLE_BAR_FG);// new Color(22, 125,
														// 219));
		this.lbl.setPreferredSize(null);
		this.lbl.setFont(SwingUtility.getDefaultTitleFont());
		this.lbl.setVerticalAlignment(SwingConstants.TOP);
		if (this.icon != null) {
			this.lbl.setIcon(this.icon);
		}
		this.pnlTitle.setOpaque(false);

		// Border emptyBorder = BorderFactory.createEmptyBorder(2,5,2,5);
		// btnAddToFavorites.setBorder(emptyBorder );
		// btnClose.setBorder(emptyBorder );
		// btnNext.setBorder(emptyBorder );
		// btnPrevious.setBorder(emptyBorder );
		// btnReload.setBorder(emptyBorder );
		// btnShowInFrame.setBorder(emptyBorder );

		this.btnShowInFrame.setToolTipText("POP_OUT_WINDOW");
		// btnReload.setBorder(null);
		// btnClose.setBorder(null);

		this.btnReload.setIcon("small_reload_2.png");
		this.btnClose.setIcon("smal_close.png");
		this.btnAddToFavorites.setIcon("1463807492_keditbookmarks.png");
		this.btnShowInFrame.setIcon("show_courses.png");
		this.btnClose.setToolTipText("CLOSE_PANEL");
		this.btnReload.setToolTipText("RELOAD");
		this.btnClose.setToolTipText("CLOSE");
		this.btnAddToFavorites.setToolTipText("ADD_TO_FAVORITES");
		this.btnNext.setToolTipText("NEXT_WINDOW");
		this.btnPrevious.setToolTipText("PREVIOUSE_WINDOW");
	}

	/**
	 *
	 */
	protected void showPanel() {
		add((Container) this.panel, BorderLayout.CENTER);
		validate();
		repaint();
		if (this.panel instanceof JInternalFrame) {
			final JInternalFrame f = (JInternalFrame) this.panel;
			final javax.swing.plaf.InternalFrameUI ifu = f.getUI();
			((javax.swing.plaf.basic.BasicInternalFrameUI) ifu).setNorthPane(null);
			f.setVisible(true);
		}

	}
}
