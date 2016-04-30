package com.fs.commons.desktop.swing.comp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import com.fs.commons.desktop.swing.Colors;

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
public class JKMenu extends JKButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// static Color bgColor=new Color(206,222,239);
	static Color forColor = Colors.MENU_BUTTON_FC;//new Color(22, 125, 219);

	// static Color backColor = new Color(191, 215, 255);
	static Color backColor = Colors.MENU_BUTTON_BG;//new Color(229, 238, 255);

	//static Color selectedBackColor = new Color(229, 238, 255);

	//static Border FOCUS_BORDER = BorderFactory.createLineBorder(Color.orange);

	//Border DEFAULT_BORDER = getBorder();

	// static Color selectedBackColor = new Color(255, 211, 73);

	static Dimension dim = new Dimension(140, 35);

	public JKMenu(String str) {
		super(str);
		init();

		//setSelectedBGColor(selectedBackColor);
		// setFont(new Font("Tahoma", Font.PLAIN, 12));
	}

	@Override
	void init() {
		setFocusable(false);
		super.init();
		setBackground(backColor);
		setForeground(forColor);
		 setPreferredSize(dim);		 
		// setBackground(bgColor);
		
		// setBorder(BorderFactory.createLineBorder(forColor));
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// if(!hasFocus()){
				// requestFocus();
				// doClick();
				// }
			}
		});
	}

	@Override
	public void setSelected(boolean selected) {
		super.setSelected(selected);
		if (selected) {
			setForeground(Color.black);
			// setBorder(FOCUS_BORDER);
		} else {
			setForeground(forColor);
			// setBorder(DEFAULT_BORDER);
		}
	}
	
}
