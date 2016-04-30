package com.fs.commons.desktop.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;

import com.fs.commons.logging.Logger;

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author not attributable
 * @version 1.0
 */
public class SwingPrintUtility implements Printable {
	private final Component componentToBePrinted;

	/**
	 * 
	 * @param c
	 *            Component
	 */
	public static void printComponent(Component c) {
		new SwingPrintUtility(c).print();
	}

	/**
	 * 
	 * @param componentToBePrinted
	 *            Component
	 */
	public SwingPrintUtility(Component componentToBePrinted) {
		this.componentToBePrinted = componentToBePrinted;
	}

	/**
	 * 
	 */
	public void print() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog()) {
			try {
				printJob.print();
			} catch (PrinterException pe) {
				Logger.fatal("Error printing: " + pe);
				pe.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param g
	 *            Graphics
	 * @param pageFormat
	 *            PageFormat
	 * @param pageIndex
	 *            int
	 * @return int
	 */
	public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
		if (pageIndex > 0) {
			return (NO_SUCH_PAGE);
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		disableDoubleBuffering(componentToBePrinted);
		componentToBePrinted.paint(g2d);
		enableDoubleBuffering(componentToBePrinted);
		return (PAGE_EXISTS);
	}

	public static void disableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	public static void enableDoubleBuffering(Component c) {
		RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}
}
