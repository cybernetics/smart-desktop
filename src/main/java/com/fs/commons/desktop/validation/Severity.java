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
package com.fs.commons.desktop.validation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

/**
 * Indicates the severity of a problem. See Problem.Kind.
 *
 * @author Tim Boudreau
 */
public enum Severity {

	/**
	 * An information message for the user, which should not block them from
	 * proceeding but may provide advice
	 */
	INFO,
	/**
	 * A warning to the user that they should change a value, but which does not
	 * block them from proceeding
	 */
	WARNING,
	/**
	 * A fatal problem with user input which must be corrected
	 */
	FATAL;
	private BufferedImage image;

	BufferedImage badge;

	public BufferedImage badge() {
		if (this.badge == null) {
			String name;
			switch (this) {
			case INFO:
				name = "info-badge.png"; // NOI18N
				break;
			case WARNING:
				name = "warning-badge.png"; // NOI18N
				break;
			case FATAL:
				name = "error-badge.png"; // NOI18N
				break;
			default:
				throw new AssertionError();
			}
			try {
				this.badge = ImageIO.read(Severity.class.getResourceAsStream(name));
			} catch (final IOException ex) {
				throw new IllegalArgumentException(ex);
			}
		}
		return this.badge;
	}

	/**
	 * Get a suitable color for displaying problem text
	 * 
	 * @return A color
	 */
	public Color color() {
		switch (this) {
		case FATAL: {
			Color c = UIManager.getColor("nb.errorForeground"); // NOI18N
			if (c == null) {
				c = Color.RED.darker();
			}
			return c;
		}
		case WARNING:
			return Color.BLUE.darker();
		case INFO:
			return UIManager.getColor("textText");
		default:
			throw new AssertionError();
		}
	}

	/**
	 * Get an icon version of the warning image
	 * 
	 * @return An icon
	 */
	public Icon icon() {
		return new ImageIcon(image());
	}

	/**
	 * Get a warning icon as an image
	 * 
	 * @return An image
	 */
	public synchronized BufferedImage image() {
		if (this.image == null) {
			String name;
			switch (this) {
			case INFO:
				name = "info.png"; // NOI18N
				break;
			case WARNING:
				name = "warning.png"; // NOI18N
				break;
			case FATAL:
				name = "error.png"; // NOI18N
				break;
			default:
				throw new AssertionError();
			}
			try {
				this.image = ImageIO.read(Severity.class.getResourceAsStream(name));
			} catch (final IOException ex) {
				throw new IllegalArgumentException(ex);
			}
		}
		return this.image;
	}
}
