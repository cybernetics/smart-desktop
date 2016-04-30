package com.fs.commons.desktop.swing.application;

import java.awt.ComponentOrientation;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.plaf.TabbedPaneUI;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.desktop.swing.jtabbedpaneui.AquaBarTabbedPaneUI;

public class TabbedUITest {

	public static void main(String[] args) throws FileNotFoundException, ApplicationException {
		JPanel pnl=new JPanel();
		pnl.setLayout(new GridLayout(6,1));
		pnl.add(createTab(new AquaBarTabbedPaneUI()));
		pnl.add(createTab(new javax.swing.plaf.metal.MetalTabbedPaneUI()));
		pnl.add(createTab(new com.jgoodies.looks.plastic.PlasticTabbedPaneUI()));
		pnl.add(createTab(new com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI()));
		
		pnl.add(createTab(new com.sun.java.swing.plaf.motif.MotifTabbedPaneUI()));
		
		pnl.add(createTab(new PSTabbedPaneUI()));

		JFrame f=new JFrame();
		f.add(pnl);
		f.setExtendedState(Frame.MAXIMIZED_BOTH);
		f.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		f.setVisible(true);
		
	}

	private static JTabbedPane createTab(TabbedPaneUI ui) {
		JTabbedPane p=new JTabbedPane();
		p.setUI(ui);
		for(int i=0;i<10;i++){
			p.addTab("Tab "+i, new JTextField());
		}
		return p;
	}
}
