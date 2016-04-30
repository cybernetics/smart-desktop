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
package com.fs.commons.desktop.swing.comp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JMenu;

import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class JKIconButton extends JMenu {

	/**
	 *
	 */
	private static final long serialVersionUID = 6523353223396276484L;
	private BufferedImage img;

	public JKIconButton(final String caption, final String iconName) {
		super(caption);
		try {
			this.img = javax.imageio.ImageIO.read(GeneralUtility.getIconURL(iconName));
		} catch (final IOException e) {
			ExceptionUtil.handleException(e);
		}
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		setPreferredSize(new Dimension(this.img.getWidth() + 8, this.img.getHeight() + 8));
	}

	@Override
	public void paint(final Graphics g) {
		g.drawImage(this.img, 1, 1, this.img.getWidth(), this.img.getHeight(), this);
		super.paint(g);

	}

}
