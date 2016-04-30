package com.fs.security.ui.generator;




import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.NumberDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp.panels.JKRadioGroup;
import com.fs.commons.security.User;
import com.fs.commons.util.PasswordGenerator;
/////////////////////////////////////////////////////////////////////////////////////////
// Author : Mohamed Kiswani
// Since  : 5-2-2010
///////////////////////////////////////////////////////////////////////////////////////
public class PnlGeneratePassword extends JKPanel {
	/////////////////////////////////////////////////////////////////////////////////////
	public enum GenerationType{
		ALPH_NEMRIC,ALPHA,NUMERIC;
	}
	/////////////////////////////////////////////////////////////////////////////////////
	private static final long serialVersionUID = 1L;
	private JKTextField txtNewPassword = new JKTextField(9,20);
	private JKTextField txtPasswordLength = new JKTextField(new NumberDocument(1),20);
	private JKRadioGroup rbgPasswordType=null;
	private JKButton btnCancel = new JKButton("CLOSE_PANEL");
	
	
	/////////////////////////////////////////////////////////////////////////////////////
	public PnlGeneratePassword() {
		init();
	}
	/////////////////////////////////////////////////////////////////////////////////////
	private void init() {
		rbgPasswordType=new JKRadioGroup(GenerationType.values());
		JKPanel container=new JKPanel(new BorderLayout());
		container.setBorder(SwingUtility.createTitledBorder(""));
		container.add(getMainPanel(),BorderLayout.CENTER);
		container.add(getButtonsPanel(),BorderLayout.SOUTH);
		txtNewPassword.setEnabled(false);
		txtPasswordLength.setEnabled(true);
		btnCancel.setIcon("close.png");
		add(container);
		
		rbgPasswordType.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleGeneratePassword();
			}
		});
		
		txtPasswordLength.addKeyListener(new KeyAdapter(){
			public void keyReleased(KeyEvent e) {
				handleGeneratePassword();
			}
						
		}) ;
		
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleCancel();
			}
		});
	}
	/////////////////////////////////////////////////////////////////////////////////////
	private int getPasswordLength() {
		if(!txtPasswordLength.getText().equals("")){
			return Integer.parseInt(txtPasswordLength.getText());
		}else{
			return 4;
		}
	}	
	/////////////////////////////////////////////////////////////////////////////////////
	protected void handleGeneratePassword() {
		GenerationType type= (GenerationType) rbgPasswordType.getSelectedItem();
		switch (type) {
		case NUMERIC:
			txtNewPassword.setText(PasswordGenerator.generateNumricPassword(getPasswordLength()));	
			break;
		case ALPHA:
			txtNewPassword.setText(PasswordGenerator.getAlphapticPassowrds(getPasswordLength(), false));	
			break;
		case ALPH_NEMRIC:
			txtNewPassword.setText(PasswordGenerator.generateMixPassword(getPasswordLength()));	
			break;

		}
				
	}
	/////////////////////////////////////////////////////////////////////////////////////
	protected void handleCancel() {
		SwingUtility.closePanel(this);
	}

	/////////////////////////////////////////////////////////////////////////////////////
	private JKPanel getButtonsPanel() {
		JKPanel panel=new JKPanel();
		panel.add(btnCancel);
		return panel;
	}

	/**
	 * @return
	 */
	private JKPanel getMainPanel() {
		JKPanel pnlInfo = new JKPanel(new GridLayout(4,1,3,2));
		pnlInfo.setBorder(SwingUtility.createTitledBorder("USER_INFO"));
		pnlInfo.add(new JKLabledComponent("NEW_PASSWORD", txtNewPassword));
		pnlInfo.add(new JKLabledComponent("PASSWORD_LENGTH", txtPasswordLength));
		pnlInfo.add(rbgPasswordType);
		return pnlInfo;
	}
	
	/**
	 * @throws DaoException
	 */
	protected void handleGenerate(){
		
	}
	
	public static void main(String[] args) {
		User user=new User();
		user.setUserRecordId(1);
		user.setUserId("admin");
		user.setFullName("admin");
		user.setPassword("admin");
		user.setStatus(0);
			
	  //  user =SecurityManager.getCurrentUser();
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.showPanelInDialog(new PnlGeneratePassword(),"TEST");
	}
}

