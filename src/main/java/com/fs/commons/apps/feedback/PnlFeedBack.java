/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      03/07/2008     ahmad  ali    -modify handleSend 
 */
package com.fs.commons.apps.feedback;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;

import org.apache.commons.mail.EmailException;

import com.fs.commons.application.config.CommonsConfigManager;
import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.apps.backup.CompressionException;
import com.fs.commons.apps.backup.DatabaseInfo;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKCheckBox;
import com.fs.commons.desktop.swing.comp.JKScrollPane;
import com.fs.commons.desktop.swing.comp.JKTextArea;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

public class PnlFeedBack extends JKPanel<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SUPPORT_EMAIL = System.getProperty("mail-to","Bug-Report@final-solutions.net");
	JKTextField txtScreenName = new JKTextField(50);
	JKTextArea txtDescription = new JKTextArea(10, 50);
	JKTextArea txtScenario = new JKTextArea(10, 50);
	JKButton btnSend = new JKButton("ERR_SEND");
	JKCheckBox chkInlcudeDatabase=new JKCheckBox("INCLUDE_DATABASE");

	/**
	 * 
	 */
	public PnlFeedBack() {
		init();
	}

	/**
	 * 
	 */
	private void init() {
	 	JKPanel<?> container = new JKPanel<Object>();
		container.setBorder(SwingUtility.createTitledBorder("FEED_BACK_INFO"));
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(new JKLabledComponent("ERROR_SCREEN_NAME", txtScreenName));
		container.add(Box.createVerticalStrut(3));
		container.add(new JKLabledComponent("ERROR_DESCRIPTION", new JKScrollPane(txtDescription)));
		container.add(Box.createVerticalStrut(3));
		container.add(new JKLabledComponent("ERROR_SCENARIO", new JKScrollPane(txtScenario)));
		container.add(chkInlcudeDatabase);
		container.add(Box.createVerticalStrut(3));
		container.add(getButtonsPanel());
		btnSend.setIcon("send_feed_back.png");
		add(container);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSend();
			}
		});
	}
	/**
	 * @1.1
	 */
	
	protected void handleSend() {
		try {			
			txtScreenName.checkEmpty();
			SwingValidator.checkEmpty(txtDescription);
			SwingValidator.checkEmpty(txtScenario);
			DatabaseInfo info = new DatabaseInfo();
 			info.setDatabaseHost( System.getProperty("db-host","localhost"));
			info.setDatabaseName( System.getProperty("db-name","db"));
			info.setDatabasePort(Integer.parseInt( System.getProperty("db-port","3306")));
			info.setDatabaseUser( CommonsConfigManager.decode(System.getProperty("db-user")));
			info.setDatabasePassword(CommonsConfigManager.decode(System.getProperty("db-password")));	
			
			Message msg = new Message();
			msg.setPanelName(txtScreenName.getText().trim());
			msg.setErrorDesc(txtDescription.getText().trim());
			msg.setErrorScenario(txtScenario.getText().trim());
		
			FeedbackManager feedbackManager = new FeedbackManager();
			feedbackManager.sendFeddback(SUPPORT_EMAIL, info, msg,isSendDatabaseBackup());
			SwingUtility.showSuccessDialog("FEED_BACK_SENT_SUCCESSFULLY");
			SwingUtility.closePanel(this);
		} catch (ValidationException e) {
			ExceptionUtil.handleException(e);
		} catch (IOException e) {
			ExceptionUtil.handleException(e);
		} catch (CompressionException e) {
			ExceptionUtil.handleException(e);
		} catch (EmailException e) {
			ExceptionUtil.handleException(e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean isSendDatabaseBackup() {
		return chkInlcudeDatabase.isSelected();
	}

	/**
	 * 
	 * @return 
	 */
	private JKPanel<?> getButtonsPanel() {
		JKPanel<?> pnl = new JKPanel<Object>();
		pnl.add(btnSend);
		btnSend.setShowProgress(true);
		return pnl;				
	}

	/**
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {
	  SwingUtility.testPanel(new PnlFeedBack());
	}

}
