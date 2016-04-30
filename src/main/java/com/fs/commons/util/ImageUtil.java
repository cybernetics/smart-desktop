/**
 * 
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
//	public static byte[] readImage() throws TwainException, IOException,
//			MorenaException {
//		try {
//			byte[] imageBytes = null;
//			TwainSource source = TwainManager.selectSource(null);
//			source.maskBadValueException(false);
//			source.maskUnsupportedCapabilityException(false);
//			System.err.println("Selected source is " + source);
//			if (source != null) {
//				MorenaImage image = new MorenaImage(source);
//				Image img = Toolkit.getDefaultToolkit().createImage(image);
//				imageBytes = ImageUtil.toPng(img);
//			}
//			return imageBytes;
//		} finally {
//			Morena.close();
//		}
//	}

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
	public static byte[] toJpeg(Image img) throws IOException {
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
	public static byte[] toPng(Image img) throws IOException {
		return toFormat(img, "png");
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
	public static byte[] toFormat(Image img, String format) throws IOException {
		BufferedImage bi = imageToBufferedImage(img);
		Iterator writers = ImageIO.getImageWritersByFormatName(format
				.toLowerCase());
		if (writers == null || !writers.hasNext()) {
			throw new IllegalArgumentException("Unsupported format (" + format
					+ ")");
		}
		ImageWriter writer = (ImageWriter) writers.next();
		IIOImage iioImg = new IIOImage(bi, null, null);
		ImageWriteParam iwparam = writer.getDefaultWriteParam();
		// if JPEG, set image quality parameters
		if ("jpeg".equalsIgnoreCase(format)) {
			iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwparam.setCompressionQuality(1.0f);
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		writer.setOutput(ImageIO.createImageOutputStream(out));
		writer.write(null, iioImg, iwparam);
		return out.toByteArray();
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
	public static BufferedImage loadImage(byte[] imageData) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(imageData);
		BufferedImage bi = ImageIO.read(in);
		in.close();
		return bi;
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
	public static BufferedImage imageToBufferedImage(Image img) {
		// if it's already a buffered image, return it (assume it's fully loaded
		// already)
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// create a new buffered image and draw the specified image on it
		BufferedImage bi = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bi.createGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.dispose();
		return bi;
	}

	/**
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public static BufferedImage getCompatibleImage(int w, int h) {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		BufferedImage image = gc.createCompatibleImage(w, h);
		return image;
	}

	/**
	 * 
	 * @param source
	 * @param compat
	 * @return
	 */
	public static BufferedImage scaleNewerWay(BufferedImage source,
			boolean compat, int width, int height) {
		BufferedImage bi = compat ? getCompatibleImage(width, height)
				: new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bi.createGraphics();
		// assuming width == height for source
		double xScale = (double) width / source.getWidth();
		double yScale = (double) height / source.getHeight();
		AffineTransform at = AffineTransform.getScaleInstance(xScale, yScale);
		g2d.drawRenderedImage(source, at);
		g2d.dispose();
		return bi;
	}
	
//	public static void main(String[] args) throws TwainException, IOException, MorenaException {
//		TwainSource[] listSources = TwainManager.listSources();
//		for (TwainSource twainSource : listSources) {
//			System.out.println(twainSource);
//		}
//	}
}
