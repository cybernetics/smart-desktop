/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 *  1.1      21/08/2008     ahmad ali       - add this class
 *  1.2      29/12/2008     Jamil Shreet    -Add icons to the buttons
 */
package com.fs.security.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKPasswordField;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.security.SecurityManager;
import com.fs.commons.security.User;
import com.fs.commons.util.ExceptionUtil;
import com.fs.security.facade.SecurityFacade;

public class PnlResetPassword extends JKPanel<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JKTextField txtUserName = new JKTextField(12, 20,false);
	private JKPasswordField txtOldPassword = new JKPasswordField(20,20);
	private JKPasswordField txtNewPassword = new JKPasswordField(12,20);
	private JKPasswordField txtConfirmPassword = new JKPasswordField(12,20);
	private JKButton btnSave = new JKButton("SAVE");
	private JKButton btnCancel = new JKButton("CLOSE_PANEL");
	private User user;
	
	/**
	 * 
	 */
	public PnlResetPassword() {
		this(SecurityManager.getCurrentUser());
	}

	/**
	 * 
	 */
	public PnlResetPassword(User user) {
		this.user = user;
		init();
		modelToView();
	}

	/**
	 * @1.2
	 */
	private void init() {
		JKPanel<?> container=new JKPanel<Object>(new BorderLayout());
		container.setBorder(SwingUtility.createTitledBorder(""));
		container.add(getUserInfoPanel(),BorderLayout.CENTER);
		container.add(getButtonsPanel(),BorderLayout.SOUTH);
		btnSave.setIcon("small_filesave.png");
		btnCancel.setIcon("close_x_red_commons_model_icon.png");
		add(container);
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});
		btnCancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleCancel();
			}
		});
	}

	/**
	 * 
	 */
	protected void handleCancel() {
		SwingUtility.closePanel(this);
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getButtonsPanel() {
		JKPanel<?> panel=new JKPanel<Object>();
		panel.add(btnSave);
		panel.add(btnCancel);
		return panel;
	}

	/**
	 * @return
	 */
	private JKPanel<?> getUserInfoPanel() {
		JKPanel<?> pnlInfo = new JKPanel<Object>(new GridLayout(4,1,3,2));
		pnlInfo.setBorder(SwingUtility.createTitledBorder("USER_INFO"));
		//pnlInfo.setLayout(new BoxLayout(pnlInfo, BoxLayout.Y_AXIS));
		pnlInfo.add(new JKLabledComponent("USER_NAME", txtUserName));
		pnlInfo.add(new JKLabledComponent("OLD_PASSWORD", txtOldPassword));
		pnlInfo.add(new JKLabledComponent("NEW_PASSWORD", txtNewPassword));
		pnlInfo.add(new JKLabledComponent("_CONFIRM_PASSWORD",txtConfirmPassword));
		return pnlInfo;
	}
	
	/**
	 * @param employee
	 * @throws DaoException
	 */
	public void modelToView()  {
		txtUserName.setText(user.getUserId());
	}
	/**
	 * @return
	 */
	protected void checkValues()throws ValidationException{
		txtUserName.checkEmpty();
		txtOldPassword.checkEmpty();
		txtNewPassword.checkEmpty();
		txtConfirmPassword.checkEmpty();
		
		if(!txtOldPassword.getText().equals(user.getPassword())){
			throw new ValidationException("OLD_PASSWORD_INCORRECT",txtOldPassword);
		}
		if(!txtNewPassword.getText().equals(txtConfirmPassword.getText())){
			throw new ValidationException("CONFIRM_PASSWORD_MISMATCH",txtNewPassword);
		}
	}
	
	/**
	 * @throws DaoException
	 */
	protected void handleSave(){
		try {
			checkValues();
			User user=viewToModel();
			SecurityFacade facade=new SecurityFacade();
			facade.updateUser(user);
			SwingUtility.showSuccessDialog("PASSWORD_CHANGED_SUCC");
			SwingUtility.closePanel(this);
		} catch (ValidationException e) {
			ExceptionUtil.handleException(e);
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private User viewToModel() {		
		user.setPassword(txtNewPassword.getText());
		return user;
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		User user=new User();
//		user.setUserRecordId(1);
//		user.setUserId("admin");
//		user.setFullName("admin");
//		user.setPassword("admin");
//		user.setStatus(0);
//			
//	  //  user =SecurityManager.getCurrentUser();
//		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
//		SwingUtility.showPanelInDialog(new PnlResetPassword(user),"TEST");
//	}
}

