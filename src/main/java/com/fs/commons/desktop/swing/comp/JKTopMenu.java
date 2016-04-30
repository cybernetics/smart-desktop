package com.fs.commons.desktop.swing.comp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class JKTopMenu extends JMenu{

	private BufferedImage img;

	public JKTopMenu(String caption, String iconName) {
		super(Lables.get(caption));
		try {
			img = javax.imageio.ImageIO.read(GeneralUtility.getIconURL(iconName));
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		}
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
		setPreferredSize(new Dimension(img.getWidth()+8, img.getHeight()+8));
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 1, 1,img.getWidth(),img.getHeight(), this);
		super.paint(g);
		
	}
}
