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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.fs.commons.desktop.swing.comp.JKLabel;

/**
 * <p>
 * Title:
 * </p>
 *
 * <p>
 * Description:
 * </p>
 *
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 *
 * <p>
 * Company:
 * </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class VerticalExpandCollapse extends JKPanel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JKPanel pnlMain;

	JKLabel btnShow = new JKLabel("");

	JKLabel btnHide = new JKLabel("");

	private JKPanel pnlButtons;

	public VerticalExpandCollapse(final JKPanel pnlMain) {
		this(pnlMain, null);
	}

	public VerticalExpandCollapse(final JKPanel panel, final String name) {
		this.pnlMain = panel;
		init();
	}

	/**
	 * buildButtonPanel
	 *
	 * @return PopupMenu
	 */
	private JKPanel buildButtonPanel() {
		if (this.pnlButtons == null) {
			this.pnlButtons = new JKPanel();
			this.btnShow.setBorder(null);
			this.btnHide.setBorder(null);
			this.btnShow.setPreferredSize(null);
			this.btnHide.setPreferredSize(null);

			this.btnShow.setIcon("down_arrow.gif");
			this.btnHide.setIcon("up_arrow.gif");
			// if (name != null) {
			// btnShow = new JKMenuSection(name);
			// btnHide = new JKMenuSection(name);
			// }
			this.btnShow.setVisible(false);
			this.pnlButtons.add(this.btnShow);
			this.pnlButtons.add(this.btnHide);
			this.btnShow.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					expand();
				}
			});

			this.btnHide.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					collaps();
				}
			});
		}
		return this.pnlButtons;
	}

	public void collaps() {
		this.btnShow.setVisible(true);
		this.btnHide.setVisible(false);
		this.pnlMain.setVisible(false);
	}

	public void expand() {
		this.btnShow.setVisible(false);
		this.btnHide.setVisible(true);
		this.pnlMain.setVisible(true);
	}

	/**
	 * init
	 */
	private void init() {
		setLayout(new BorderLayout());
		// setBorder(BorderFactory.createRaisedBevelBorder());
		add(buildButtonPanel(), BorderLayout.SOUTH);
		add(this.pnlMain, BorderLayout.CENTER);
	}

}
