package com.fs.commons.desktop.swing.comp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fs.commons.locale.Lables;

public class JKSmallButton extends JKButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font=new Font("Arial", Font.BOLD, 10);

	/**
	 * 
	 * @param caption
	 *            String
	 */
	public JKSmallButton(String caption) {
		super(Lables.get(caption));
		init();
		setFocusable(false);
	}

	/**
	 * 
	 */
	@Override
	void init() {
		setFont(font);
		setPreferredSize(new Dimension(70, 17));
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					// transferFocus();
					doClick();
				}
			}
		});
	}

}
