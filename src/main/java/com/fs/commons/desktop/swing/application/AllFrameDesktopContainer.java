package com.fs.commons.desktop.swing.application;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class AllFrameDesktopContainer {
	JDesktopPane desk;
	JInternalFrame iframe;
	JFrame frame;

	public static void main(String[] args) {
	}

	public AllFrameDesktopContainer() {
		frame = new JFrame("All Frames in a JDesktopPane Container");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		desk = new JDesktopPane();
		try {
			String str = JOptionPane.showInputDialog(null, "Enter number of frames :", "Roseindia.net", 1);
			int i = Integer.parseInt(str);
			for (int j = 1; j <= i; j++) {
				iframe = new JInternalFrame("Internal Frame: " + j, true, true, true, true);
				iframe.setBounds(j * 20, j * 20, 150, 100);
				iframe.setVisible(true);
				desk.add(iframe);
				iframe.setToolTipText("Internal Frame :" + j);
			}
		} catch (NumberFormatException ne) {
			JOptionPane.showMessageDialog(null, "Please enter number value.", "Roseindia.net", 1);
			System.exit(0);
		}
		JMenuBar menubar = new JMenuBar();
		JMenu count = new JMenu("Count Total Frames");
		count.addMenuListener(new MyAction());
		menubar.add(count);
		frame.setJMenuBar(menubar);
		frame.add(desk);
		frame.setSize(400, 400);
		frame.setVisible(true);
	}

	public class MyAction implements MenuListener {
		public void menuSelected(MenuEvent me) {
			int i = desk.getAllFrames().length;
			JOptionPane.showMessageDialog(null, "Total visible internal frames are : " + i, "Roseindia.net", 1);
		}

		public void menuCanceled(MenuEvent me) {
		}

		public void menuDeselected(MenuEvent me) {
		}
	}
}