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

	public VerticalExpandCollapse(JKPanel pnlMain) {
		this(pnlMain, null);
	}

	public VerticalExpandCollapse(JKPanel panel, String name) {
		this.pnlMain = panel;
		init();
	}

	/**
	 * init
	 */
	private void init() {
		setLayout(new BorderLayout());
//		setBorder(BorderFactory.createRaisedBevelBorder());		
		add(buildButtonPanel(), BorderLayout.SOUTH);		
		add(pnlMain, BorderLayout.CENTER);
	}

	/**
	 * buildButtonPanel
	 * 
	 * @return PopupMenu
	 */
	private JKPanel buildButtonPanel() {
		if (pnlButtons == null) {
			 pnlButtons = new JKPanel();			 
			btnShow.setBorder(null);
			btnHide.setBorder(null);
			btnShow.setPreferredSize(null);
			btnHide.setPreferredSize(null);
			
			btnShow.setIcon("down_arrow.gif");
			btnHide.setIcon("up_arrow.gif");
//			if (name != null) {
//				btnShow = new JKMenuSection(name);
//				btnHide = new JKMenuSection(name);
//			}
			btnShow.setVisible(false);
			pnlButtons.add(btnShow);
			pnlButtons.add(btnHide);
			btnShow.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					expand();
				}
			});

			btnHide.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					collaps();
				}
			});
		}
		return pnlButtons;
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
