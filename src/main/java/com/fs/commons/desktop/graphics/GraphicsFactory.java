package com.fs.commons.desktop.graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class GraphicsFactory {
	public enum GradientType {
		VERTICAL, HORIZENTAL, DIAGNOLE, VERTICAL_LINEAR, HORIZENTAL_LINEAR;
	}

	/**
	 * 
	 * @param comp
	 * @param g
	 * @param fromColor
	 */
	public static void makeGradient(Component comp, Graphics g, Color fromColor) {
		makeGradient(comp, g, fromColor,createGradientColor(fromColor),GradientType.HORIZENTAL);
	}
	
	/**
	 * 
	 */
	public static void makeGradient(Component comp, Graphics g, Color fromColor, Color toColor, GradientType type) {
//		int height = comp.getHeight();
//		int width = comp.getWidth();
//		int endWidth = 0;
//		int endHeight = 0;
//		boolean cycle=true;
//		if (type == GradientType.HORIZENTAL) {
//			endHeight = height / 2;
//		} else if (type == GradientType.VERTICAL) {
//			endWidth = width / 2;
//		} else if (type == GradientType.DIAGNOLE) {
//			endWidth = width / 2;
//			endHeight = height / 2;
//		} else if (type == GradientType.HORIZENTAL_LINEAR) {
//			endWidth = width;
//			cycle=false;
//		} else if (type == GradientType.VERTICAL_LINEAR) {
//			endHeight = height;
//			cycle=false;
//		}
//		GradientPaint gradient = new GradientPaint(0, 0, fromColor, endWidth, endHeight, toColor, cycle);
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setPaint(gradient);
//		g2d.fill(getRectangle(width, height));
	}

	/**
	 * 
	 * @return
	 */
	private static Rectangle2D getRectangle(int width, int height) {
		Rectangle2D rec = new Rectangle2D.Double(0, 0, width, height);
		return rec;
	}

	public static Color createGradientColor(Color color) {
		int inc = 30;
		int red = color.getRed() + inc;
		int green = color.getGreen() + inc;
		int blue = color.getBlue() + inc;
		Color gredientColor = new Color(red > 255 ? 255 : red, green > 255 ? 255 : green, blue > 255 ? 255 : blue);
		if(gredientColor.equals(Color.white)){
			gredientColor = new Color(color.getRed()-inc, color.getGreen()-inc, color.getBlue()-inc);
		}
		return gredientColor;
	}
}
