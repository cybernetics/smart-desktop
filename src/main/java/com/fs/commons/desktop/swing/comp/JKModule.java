package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.SwingConstants;

import com.fs.commons.desktop.swing.Colors;

public class JKModule extends JKButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Color forColor = Colors.MODULE_BUTTON_FC;

	static Color backColor = Colors.MODULE_BUTTON_BG;

	// static Color selectedBackColor = new Color(22, 125, 219);

	// static Border FOCUS_BORDER =
	// BorderFactory.createLineBorder(Color.orange);

	// Border DEFAULT_BORDER = getBorder();

	static Dimension dim = new Dimension(140, 35);

	public JKModule(String str) {
		super(str);
		init();
	}

	@Override
	void init() {
		setFocusable(false);
		setBorder(null);
		setOpaque(false);
		setHorizontalAlignment(CENTER);
		setBackground(backColor);
		setForeground(forColor);
		super.init();
		setPreferredSize(dim);
		// setFont(new Font(getFontName(),Font.BOLD,18));
		// addMouseMotionListener(new MouseMotionAdapter() {
		//
		// @Override
		// public void mouseMoved(MouseEvent e) {
		// }
		// });
		// }

		// @Override
		// public void setSelected(boolean selected) {
		// super.setSelected(selected);
		// if (selected) {
		// setForeground(Color.black);
		// // setBorder(FOCUS_BORDER);
		// } else {
		// setForeground(forColor);
		// // setBorder(DEFAULT_BORDER);
		// }
	}

	@Override
	public void setIcon(Icon defaultIcon) {
		setVerticalTextPosition(SwingConstants.BOTTOM);
		setHorizontalTextPosition(SwingConstants.CENTER);
	}
}
