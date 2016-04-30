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
	public static void disableDoubleBuffering(final Component c) {
		final RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(false);
	}

	public static void enableDoubleBuffering(final Component c) {
		final RepaintManager currentManager = RepaintManager.currentManager(c);
		currentManager.setDoubleBufferingEnabled(true);
	}

	/**
	 *
	 * @param c
	 *            Component
	 */
	public static void printComponent(final Component c) {
		new SwingPrintUtility(c).print();
	}

	private final Component componentToBePrinted;

	/**
	 *
	 * @param componentToBePrinted
	 *            Component
	 */
	public SwingPrintUtility(final Component componentToBePrinted) {
		this.componentToBePrinted = componentToBePrinted;
	}

	/**
	 *
	 */
	public void print() {
		final PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable(this);
		if (printJob.printDialog()) {
			try {
				printJob.print();
			} catch (final PrinterException pe) {
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
	@Override
	public int print(final Graphics g, final PageFormat pageFormat, final int pageIndex) {
		if (pageIndex > 0) {
			return NO_SUCH_PAGE;
		}

		final Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		disableDoubleBuffering(this.componentToBePrinted);
		this.componentToBePrinted.paint(g2d);
		enableDoubleBuffering(this.componentToBePrinted);
		return PAGE_EXISTS;
	}
}
