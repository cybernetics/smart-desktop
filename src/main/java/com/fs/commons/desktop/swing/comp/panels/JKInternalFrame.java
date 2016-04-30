//package com.fs.commons.desktop.swing.comp.panels;
//
//import java.awt.Component;
//import java.awt.Dimension;
//import java.beans.PropertyVetoException;
//
//import javax.swing.JComponent;
//import javax.swing.JInternalFrame;
//import javax.swing.JScrollPane;
//import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
//import javax.swing.plaf.basic.BasicInternalFrameUI;
//
//import com.fs.commons.desktop.swing.Colors;
//import com.fs.commons.desktop.swing.SwingUtility;
//import com.fs.commons.desktop.swing.comp.JKScrollPane;
//import com.fs.commons.locale.Lables;
//import com.fs.commons.util.ExceptionUtil;
//
//public class JKInternalFrame extends JInternalFrame {
//
//	private static final int TITLE_HEIGHT = 35;
//
//	public JKInternalFrame() {
//		super();
//		init();
//	}
//
//	public JKInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
//		super(title, resizable, closable, maximizable, iconifiable);
//		init();
//	}
//
//	public JKInternalFrame(String title, boolean resizable, boolean closable, boolean maximizable) {
//		super(title, resizable, closable, maximizable);
//		init();
//	}
//
//	public JKInternalFrame(String title, boolean resizable, boolean closable) {
//		super(title, resizable, closable);
//		init();
//	}
//
//	public JKInternalFrame(String title, boolean resizable) {
//		super(title, resizable);
//		init();
//	}
//
//	public JKInternalFrame(String title) {
//		super(title);
//		init();
//	}
//
//	@Override
//	public void setTitle(String title) {
//		super.setTitle(Lables.get(title));
//	}
//
//	private void init() {
//		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
//		getContentPane().setBackground(Colors.MAIN_PANEL_BG);
//		JComponent c = (BasicInternalFrameTitlePane)((BasicInternalFrameUI) getUI()).getNorthPane();  
//		c.setPreferredSize(new Dimension(c.getPreferredSize().width, TITLE_HEIGHT));
//	}
//	
////	@Override
////	public Component add(Component comp) {
////		Component add = super.add(comp);
////		return add;
////	}
//
//}
