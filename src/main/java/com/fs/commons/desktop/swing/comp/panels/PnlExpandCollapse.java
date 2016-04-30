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

	public PnlExpandCollapse(JKPanel pnlMain) {
		this.pnlMain = pnlMain;
		init();
	}

	/**
	 * init
	 */
	private void init() {
		setLayout(new BorderLayout(0, 0));
		add(pnlMain, BorderLayout.CENTER);
		add(buildButtonPanel(), BorderLayout.LINE_END);
	}

	/**
	 * buildButtonPanel
	 * 
	 * @return PopupMenu
	 */
	private JKPanel buildButtonPanel() {
		final JKPanel pnl = new JKPanel();
		//pnl.setBorder(OFF_BORDER);
		btnShow.setBorder(null);
		btnHide.setBorder(null);
		btnShow.setPreferredSize(null);
		btnHide.setPreferredSize(null);

		btnShow.setIcon(SwingUtility.isLeftOrientation() ? "collapse.gif" : "expand.gif");
		btnHide.setIcon(SwingUtility.isLeftOrientation() ? "expand.gif" : "collapse.gif");
		btnShow.setVisible(false);

		pnl.add(btnShow);
		pnl.add(btnHide);
		btnShow.setBorder(OFF_BORDER);
		btnHide.setBorder(OFF_BORDER);
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				expand();
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				btnShow.setBorder(ON_BORDER);	
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnShow.setBorder(OFF_BORDER);
			}
		});

		btnHide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				collaps();
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				btnHide.setBorder(ON_BORDER);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				btnHide.setBorder(OFF_BORDER);
			}			
		});
		return pnl;
	}

	public void expand() {
		btnShow.setVisible(false);
		btnHide.setVisible(true);
		pnlMain.setVisible(true);
	}

	public void collaps() {
		btnShow.setVisible(true);
		btnHide.setVisible(false);
		pnlMain.setVisible(false);
	}

}
