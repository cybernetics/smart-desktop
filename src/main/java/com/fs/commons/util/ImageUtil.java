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
package com.fs.commons.util;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

//import SK.gnome.morena.Morena;
//import SK.gnome.morena.MorenaException;
//import SK.gnome.morena.MorenaImage;
//import SK.gnome.twain.TwainException;
//import SK.gnome.twain.TwainManager;
//import SK.gnome.twain.TwainSource;

/**
 * @author u087
 *
 */
public class ImageUtil {
	// public static byte[] readImage() throws TwainException, IOException,
	// MorenaException {
	// try {
	// byte[] imageBytes = null;
	// TwainSource source = TwainManager.selectSource(null);
	// source.maskBadValueException(false);
	// source.maskUnsupportedCapabilityException(false);
	// System.err.println("Selected source is " + source);
	// if (source != null) {
	// MorenaImage image = new MorenaImage(source);
	// Image img = Toolkit.getDefaultToolkit().createImage(image);
	// imageBytes = ImageUtil.toPng(img);
	// }
	// return imageBytes;
	// } finally {
	// Morena.close();
	// }
	// }

	/**
	 *
	 * @param w
	 * @param h
	 * @return
	 */
	public static BufferedImage getCompatibleImage(final int w, final int h) {
		final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice gd = ge.getDefaultScreenDevice();
		final GraphicsConfiguration gc = gd.getDefaultConfiguration();
		final BufferedImage image = gc.createCompatibleImage(w, h);
		return image;
	}

	/**
	 * Converts the specified image to a
	 * <code>java.awt.image.BuffededImage</code>. If the image is already a
	 * buffered image, it is cast and returned. Otherwise, the image is drawn
	 * onto a new buffered image.
	 *
	 * @param img
	 *            the image
	 * @return a buffered image
	 */
	public static BufferedImage imageToBufferedImage(final Image img) {
		// if it's already a buffered image, return it (assume it's fully loaded
		// already)
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// create a new buffered image and draw the specified image on it
		final BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();
		return bi;
	}

	/**
	 * Uses <code>javax.imageio.ImageIO</code> to read the specified data into a
	 * <code>java.awt.image.BuffededImage</code>.
	 *
	 * @param imageData
	 *            the image data
	 * @return a buffered image
	 * @throws IOException
	 *             on I/O errors
	 */
	public static BufferedImage loadImage(final byte[] imageData) throws IOException {
		final ByteArrayInputStream in = new ByteArrayInputStream(imageData);
		final BufferedImage bi = ImageIO.read(in);
		in.close();
		return bi;
	}

	/**
	 *
	 * @param source
	 * @param compat
	 * @return
	 */
	public static BufferedImage scaleNewerWay(final BufferedImage source, final boolean compat, final int width, final int height) {
		final BufferedImage bi = compat ? getCompatibleImage(width, height) : new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = bi.createGraphics();
		// assuming width == height for source
		final double xScale = (double) width / source.getWidth();
		final double yScale = (double) height / source.getHeight();
		final AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
		g2d.drawRenderedImage(source, at);
		g2d.dispose();
		return bi;
	}

	/**
	 * Converts the specified image to a byte array which is an image file of
	 * the specified format. The formats that can be used are whatever formats
	 * are supported by the Java Image I/O package.
	 *
	 * @param img
	 *            the image
	 * @param format
	 *            the image format (jpeg, png, etc)
	 * @return the bytes of the image file
	 * @throws IOException
	 *             on I/O errors
	 */
	public static byte[] toFormat(final Image img, final String format) throws IOException {
		final BufferedImage bi = imageToBufferedImage(img);
		final Iterator writers = ImageIO.getImageWritersByFormatName(format.toLowerCase());
		if (writers == null || !writers.hasNext()) {
			throw new IllegalArgumentException("Unsupported format (" + format + ")");
		}
		final ImageWriter writer = (ImageWriter) writers.next();
		final IIOImage iioImg = new IIOImage(bi, null, null);
		final ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// if JPEG, set image quality parameters
		if ("jpeg".equalsIgnoreCase(format)) {
			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwparam.setCompressionQuality(1.0f);
		}
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		writer.setOutput(ImageIO.createImageOutputStream(out));
		writer.write(null, iioImg, iwparam);
		return out.toByteArray();
	}

	/**
	 * Converts the specified image to a byte array which is a JPEG formatted
	 * image file.
	 *
	 * @param img
	 *            the image
	 * @return the bytes of the image file
	 * @throws IOException
	 *             on I/O errors
	 */
	public static byte[] toJpeg(final Image img) throws IOException {
		return toFormat(img, "jpeg");
	}

	/**
	 * Converts the specified image to a byte array which is a PNG formatted
	 * image file.
	 *
	 * @param img
	 *            the image
	 * @return the bytes of the image file
	 * @throws IOException
	 *             on I/O errors
	 */
	public static byte[] toPng(final Image img) throws IOException {
		return toFormat(img, "png");
	}

	// public static void main(String[] args) throws TwainException,
	// IOException, MorenaException {
	// TwainSource[] listSources = TwainManager.listSources();
	// for (TwainSource twainSource : listSources) {
	// System.out.println(twainSource);
	// }
	// }
}
