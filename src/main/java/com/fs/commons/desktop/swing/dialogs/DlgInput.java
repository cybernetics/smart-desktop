package com.fs.commons.desktop.swing.dialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.FloatDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

public class DlgInput extends JKPanel<Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final JKTextField txtValue = new JKTextField(new FloatDocument(5), 20);
	JKButton btnOk=new JKButton("OK");
	JKButton btnCancel=new JKButton("CANCEL");
	private Float amount;
	private final String title;
	private final Float rangeTo;
	private final Float rangeFrom;
	
	/**
	 * 
	 * @return
	 */
	public static Float showInputDialog(String title){
		return showInputDialog(title,"");
	}

	/**
	 * 
	 * @param string
	 * @param string2
	 */
	public static Float showInputDialog(String title, String defaultValue) {
		return showInputDialog(title,defaultValue,null,null);
	}
	
	/*p
	 * 
	 */
	public static float showInputDialog(String title, String defaultValue, Float rangeFrom, Float rangeTo) {
		DlgInput pnl=new DlgInput(defaultValue,title,rangeFrom,rangeTo);
		SwingUtility.showPanelInDialog(pnl, "INPUT_DIALOG");
		return pnl.amount;
	}

	
	/**
	 * @param defaultValue 
	 * @param title 
	 * 
	 */
	private DlgInput(String defaultValue, String title, Float rangeFrom, Float rangeTo) {
		this.title = title;
		this.rangeFrom = rangeFrom;
		this.rangeTo = rangeTo;
		init();
		txtValue.setText(defaultValue);
	}
	
	/**
	 * 
	 */
	void init(){
		setLayout(new BorderLayout());
		JKPanel<?> pnlInfo=new JKPanel<Object>();
		pnlInfo.setBorder(SwingUtility.createTitledBorder("SET_VALUE"));
		pnlInfo.add(new JKLabledComponent(title, txtValue));
		
		JKPanel<?> pnlButtons=new JKPanel<Object>();
		pnlButtons.add(btnOk);
		pnlButtons.add(btnCancel);
		
		add(pnlInfo,BorderLayout.CENTER);
		add(pnlButtons,BorderLayout.SOUTH);
		
		btnOk.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleOk();
			}
		});
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				amount=null;
				SwingUtility.closePanelDialog(DlgInput.this);
			}
		});		
		txtValue.addKeyListener(new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					btnOk.doClick();
				}
			}
		});
	}

	/**
	 * 
	 */
	protected void handleOk() {
		try {
			SwingValidator.checkEmpty(txtValue);
			if(rangeFrom!=null && rangeTo!=null){
				SwingValidator.checkValidRange(txtValue, rangeFrom, rangeTo);
			}
			amount=txtValue.getTextAsFloat();
			SwingUtility.closePanelDialog(this);
		} catch (ValidationException e) {
			ExceptionUtil.handleException(e);
		}		
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		showInputDialog("Fees");
	}

}