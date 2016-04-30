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
package com.fs.commons.desktop.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class GraphicsFactory {
	public enum GradientType {
		VERTICAL, HORIZENTAL, DIAGNOLE, VERTICAL_LINEAR, HORIZENTAL_LINEAR;
	}

	public static Color createGradientColor(final Color color) {
		final int inc = 30;
		final int red = color.getRed() + inc;
		final int green = color.getGreen() + inc;
		final int blue = color.getBlue() + inc;
		Color gredientColor = new Color(red > 255 ? 255 : red, green > 255 ? 255 : green, blue > 255 ? 255 : blue);
		if (gredientColor.equals(Color.white)) {
			gredientColor = new Color(color.getRed() - inc, color.getGreen() - inc, color.getBlue() - inc);
		}
		return gredientColor;
	}

	/**
	 *
	 * @return
	 */
	private static Rectangle2D getRectangle(final int width, final int height) {
		final Rectangle2D rec = new Rectangle2D.Double(0, 0, width, height);
		return rec;
	}

	/**
	 *
	 * @param comp
	 * @param g
	 * @param fromColor
	 */
	public static void makeGradient(final Component comp, final Graphics g, final Color fromColor) {
		makeGradient(comp, g, fromColor, createGradientColor(fromColor), GradientType.HORIZENTAL);
	}

	/**
	 *
	 */
	public static void makeGradient(final Component comp, final Graphics g, final Color fromColor, final Color toColor, final GradientType type) {
		// int height = comp.getHeight();
		// int width = comp.getWidth();
		// int endWidth = 0;
		// int endHeight = 0;
		// boolean cycle=true;
		// if (type == GradientType.HORIZENTAL) {
		// endHeight = height / 2;
		// } else if (type == GradientType.VERTICAL) {
		// endWidth = width / 2;
		// } else if (type == GradientType.DIAGNOLE) {
		// endWidth = width / 2;
		// endHeight = height / 2;
		// } else if (type == GradientType.HORIZENTAL_LINEAR) {
		// endWidth = width;
		// cycle=false;
		// } else if (type == GradientType.VERTICAL_LINEAR) {
		// endHeight = height;
		// cycle=false;
		// }
		// GradientPaint gradient = new GradientPaint(0, 0, fromColor, endWidth,
		// endHeight, toColor, cycle);
		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setPaint(gradient);
		// g2d.fill(getRectangle(width, height));
	}
}
