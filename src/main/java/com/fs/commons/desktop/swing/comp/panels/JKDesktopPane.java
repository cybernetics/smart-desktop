package com.fs.commons.desktop.swing.comp.panels;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import com.fs.commons.desktop.swing.Colors;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.util.ExceptionUtil;

public class JKDesktopPane extends JDesktopPane {

	private Image backGroundImage;

	public JKDesktopPane() {
		super();
		init();
	}

	private void init() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setBackground(Colors.MAIN_PANEL_BG);
		setOpaque(true);
	}

	public void paintComponent(Graphics g) {
		if (backGroundImage == null)
			super.paintComponent(g);
		else {
			Graphics2D g2d = (Graphics2D) g;

			// scale the image to fit the size of the Panel
			double mw = backGroundImage.getWidth(null);
			double mh = backGroundImage.getHeight(null);

			double sw = getWidth() / mw;
			double sh = getHeight() / mh;

			g2d.scale(sw, sh);
			g2d.drawImage(backGroundImage, 0, 0, this);
		}
	}

	@Override
	public Component add(Component comp) {
		Component added = super.add(comp);
		if (comp instanceof JInternalFrame) {
			setPosition((JInternalFrame) comp);
		}
		return added;
	}

	private void setPosition(JInternalFrame frame) {
		Point location = frame.getLocation();
		if (location.getX() == 0 && location.getY() == 0) {
			int length = getAllFrames().length;
			int y = length * 20;
			int x;
			if (SwingUtility.isLeftOrientation()) {
				x = length * 20;
			} else {
				int width = frame.getContentPane().getPreferredSize().width;
				x = (int) (getSize().getWidth() - length * 20) - width;
			}
			System.out.println("X=" + x);
			System.out.println("Y=" + y);
			System.out.println(frame.getContentPane().getPreferredSize());
			frame.setLocation(x, y);
		}
	}

	public void setBackGroundImage(String path) {
		try {
			this.backGroundImage = SwingUtility.getImage(path);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
