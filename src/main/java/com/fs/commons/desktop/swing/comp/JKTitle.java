package com.fs.commons.desktop.swing.comp;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.border.EmptyBorder;

import com.fs.commons.desktop.graphics.GraphicsFactory;
import com.fs.commons.locale.Lables;

public class JKTitle extends JKLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

//	public JKTitle(String label) {
//		this(label,true);
//	}

	public JKTitle() {
		this("");
	}
	
	public JKTitle(String label) {
		super(label);
		//setOpaque(opaque);
		this.setText(Lables.get(label,true));
		setBorder(new EmptyBorder(2, 8, 2, 8));
		setAlignmentX(HORIZONTAL);		
		setFont(new Font("Arial",Font.BOLD,16));
		setOpaque(false);
		setPreferredSize(new Dimension(150,30));
	}
	
	@Override
	public void paint(Graphics g) {
		GraphicsFactory.makeGradient(this, g, getBackground());
		super.paint(g);
	}
}
