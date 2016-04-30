package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class JKStatusBar extends JKPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Dimension PREFERRED_SIZE = new Dimension(800,25);
	JKLabel lblMessage=new JKLabel("",false);
	
	
	public JKStatusBar() {
		init();
	}

	private void init() {
		lblMessage.setCaptilize(true);
		setLayout(new BorderLayout());
		add(lblMessage);
		setFocusable(false);
		setBorder(BorderFactory.createLoweredBevelBorder());
	}
	
	/**
	 * 
	 * @param text
	 * @return
	 */
	public void setText(final String text){
		Runnable runnable=new Runnable(){
			public void run(){		
				lblMessage.setText(text);
			}
		};
		SwingUtilities.invokeLater(runnable);
	}
	
	public String getText(){
		return lblMessage.getText();
	}
}
