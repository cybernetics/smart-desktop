package com.fs.commons.desktop.swing.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JPanel;

import com.fs.commons.desktop.swing.AnimationUtil;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.util.GeneralUtility;
import com.sun.awt.AWTUtilities;

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
public class Splash extends Window {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String imageName;

	/**
	 * p
	 * 
	 * @param imageName
	 */
	public Splash(String imageName) {
		super(new Frame());
		this.imageName = imageName;
		init();
	}

	/**
	 * init
	 */
	private void init() {
		setSize(new Dimension(640, 400));
		AWTUtilities.setWindowOpaque(this, false);
		JPanel pnl = SwingUtility.buildImagePanel(GeneralUtility.getURL(imageName), ImagePanel.SCALED);
		setLocationRelativeTo(null);
		add(pnl, BorderLayout.CENTER);
	}

	@Override
	public void setVisible(boolean show) {
		if (show) {
			AnimationUtil.fadeIn(this);
			super.setVisible(true);
		} else {
//			if (getWidth() <= 1) {
				super.setVisible(false);
//			} else {
//				AnimationUtil.fadeOut(this, false);
//			}

		}
	}

	@Override
	public void dispose() {
//		if (getWidth() <= 1) {
			super.dispose();
//		}else{
//			AnimationUtil.fadeOut(this, true);
//		}
	}

}
