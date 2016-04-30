package com.fs.commons.support;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;

public class PnlEncodeDecode extends JKPanel<Object>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JKTextField txtSource=new JKTextField(20);
	JKTextField txtResult=new JKTextField(20);
	JKButton btnEncode=new JKButton("Encode");
	JKButton btnDecode=new JKButton("Decode");
	
	public PnlEncodeDecode() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
		add(txtSource);
		add(btnDecode);
		add(btnEncode);
		add(txtResult);
		btnDecode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				handleDecode();
			}
		});
		btnEncode.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				handleEncode();
			}
		});
	}
	
	void handleDecode(){
		txtSource.setText(GeneralUtility.decode(txtResult.getText()));
	}
	
	void handleEncode(){
		txtResult.setText(GeneralUtility.encode(txtSource.getText()));
	}
}
