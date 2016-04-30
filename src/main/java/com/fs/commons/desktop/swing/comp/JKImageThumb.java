/**
 * 
 */
package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.fs.commons.desktop.swing.SwingPrintUtility;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.ImagePanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;

/**
 * @author u087
 * 
 */
public class JKImageThumb extends JKPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel pnlImage;

	byte[] imageData;

	/**
	 * 
	 */
	public JKImageThumb(byte[] image) {
		this.imageData = image;
		pnlImage = SwingUtility.buildImagePanel(image, ImagePanel.SCALED);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout());
		pnlImage.setPreferredSize(new Dimension(100, 100));
		setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		add(pnlImage, BorderLayout.CENTER);

		pnlImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					handleShowImage();
				}
			}
		});
	}

	/**
	 * 
	 */
	protected void handleShowImage() {
		final JKPanel pnl = new JKPanel(new BorderLayout());
		final JPanel pnlImage = SwingUtility.buildImagePanel(imageData, ImagePanel.SCALED);
		pnlImage.setPreferredSize(new Dimension(450, 600));
		JKPanel pnlButtons = new JKPanel();
		JKButton btnPrint = new JKButton("Print");
		JKButton btnClose = new JKButton("Close");
		btnPrint.setIcon(new ImageIcon(GeneralUtility.getIconURL("fileprint.png")));
		btnClose.setIcon(new ImageIcon(GeneralUtility.getIconURL("cancel.png")));

		pnlButtons.add(btnPrint);
		pnlButtons.add(btnClose);
		pnlImage.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
		pnl.add(pnlImage, BorderLayout.CENTER);
		pnl.add(pnlButtons, BorderLayout.SOUTH);

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingUtility.closePanelWindow(pnl);
			}
		});
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SwingPrintUtility.printComponent(pnlImage);
			}
		});
		SwingUtility.showPanelInDialog(pnl, "Preview");
	}
}
