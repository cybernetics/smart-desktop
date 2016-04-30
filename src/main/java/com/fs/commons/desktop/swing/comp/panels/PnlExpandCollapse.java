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

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.fs.commons.desktop.swing.SwingUtility;
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
public class PnlExpandCollapse extends JKPanel {
	private static final Border OFF_BORDER = BorderFactory.createRaisedBevelBorder();
	private static final Border ON_BORDER = BorderFactory.createLoweredBevelBorder();

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	JKPanel pnlMain;

	final JKLabel btnShow = new JKLabel("");

	final JKLabel btnHide = new JKLabel("");

	public PnlExpandCollapse(final JKPanel pnlMain) {
		this.pnlMain = pnlMain;
		init();
	}

	/**
	 * buildButtonPanel
	 *
	 * @return PopupMenu
	 */
	private JKPanel buildButtonPanel() {
		final JKPanel pnl = new JKPanel();
		// pnl.setBorder(OFF_BORDER);
		this.btnShow.setBorder(null);
		this.btnHide.setBorder(null);
		this.btnShow.setPreferredSize(null);
		this.btnHide.setPreferredSize(null);

		this.btnShow.setIcon(SwingUtility.isLeftOrientation() ? "collapse.gif" : "expand.gif");
		this.btnHide.setIcon(SwingUtility.isLeftOrientation() ? "expand.gif" : "collapse.gif");
		this.btnShow.setVisible(false);

		pnl.add(this.btnShow);
		pnl.add(this.btnHide);
		this.btnShow.setBorder(OFF_BORDER);
		this.btnHide.setBorder(OFF_BORDER);
		this.btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				expand();
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				PnlExpandCollapse.this.btnShow.setBorder(ON_BORDER);
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				PnlExpandCollapse.this.btnShow.setBorder(OFF_BORDER);
			}
		});

		this.btnHide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				collaps();
			}

			@Override
			public void mouseEntered(final MouseEvent e) {
				PnlExpandCollapse.this.btnHide.setBorder(ON_BORDER);
			}

			@Override
			public void mouseExited(final MouseEvent e) {
				PnlExpandCollapse.this.btnHide.setBorder(OFF_BORDER);
			}
		});
		return pnl;
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
		setLayout(new BorderLayout(0, 0));
		add(this.pnlMain, BorderLayout.CENTER);
		add(buildButtonPanel(), BorderLayout.LINE_END);
	}

}
