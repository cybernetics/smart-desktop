package com.fs.security.ui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKPasswordField;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.documents.TextDocument;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dialogs.JKDialog;
import com.fs.commons.security.User;
import com.fs.commons.security.exceptions.InvalidUserException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;
import com.fs.security.facade.SecurityFacade;

/**
 * @author u087
 */
public class AuthenicationDialog extends JKDialog {

	private static final long serialVersionUID = 1L;

	JKTextField txtUserName = new JKTextField(new TextDocument(14), 15);

	JKPasswordField txtPassword = new JKPasswordField(14, 15);

	JKButton btnAuthenticate = new JKButton("AUTHENTICATE");

	JKButton btnCancel = new JKButton("CLOSE");

	private User user;

	boolean cancelled;

	/**
	 * @return
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @param cancelled
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @param parent
	 * @param title
	 * @return
	 * @throws SecurityException
	 */
	public static User authenticateUser(JFrame parent, String title, int maxRetries) throws InvalidUserException {
		AuthenicationDialog dlg = new AuthenicationDialog(parent, title);
		for (int i = 0; i < maxRetries; i++) {
			dlg.reset();
			dlg.setVisible(true);
			if (dlg.getUser() != null) {
				return dlg.getUser();
			} else if (dlg.isCancelled()) {
				break;
			} else {
				SwingUtility.showUserErrorDialog("INVALID_USER_NAME_OR_PASSWORD", false);
			}
		}
		throw new InvalidUserException("INVALID_USER");
	}

	/**
	 * 
	 */
	private void reset() {
		txtUserName.setText("");
		txtPassword.setText("");
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 */
	public AuthenicationDialog(Dialog parent, String title) {
		super(parent, title);
		initUI();
	}

	/**
	 * 
	 * @param parent
	 */
	public AuthenicationDialog(Dialog parent) {
		super(parent, "AUTHINECATION");
		initUI();
	}

	/**
	 * 
	 * @param parent
	 * @param title
	 */
	public AuthenicationDialog(Frame parent, String title) {
		super(parent, title);
		initUI();
	}

	/**
	 * 
	 * @param parent
	 */
	public AuthenicationDialog(Frame parent) {
		this(parent, "AUTHINECATION");
		initUI();
	}

	/**
	 * 
	 */
	private void initUI() {
		setComponentOrientation(SwingUtility.getDefaultComponentOrientation());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		JKPanel<?> pnlInfo = new JKPanel<Object>(new FlowLayout(FlowLayout.LEADING));
		pnlInfo.setLayout(new GridLayout(2, 1, 5, 2));
		pnlInfo.setBorder(BorderFactory.createTitledBorder("USER_CRED"));
		pnlInfo.add(new JKLabledComponent("USER_NAME", 85, txtUserName));
		pnlInfo.add(new JKLabledComponent("PASSWORD", 85, txtPassword));

		JKPanel<?> pnlButtons = new JKPanel<Object>(new FlowLayout(FlowLayout.CENTER));

		btnAuthenticate.setIcon("unlock.png");
		btnCancel.setIcon("cancel.png");
		pnlButtons.add(btnAuthenticate);
		pnlButtons.add(btnCancel);
		JKPanel<?> container = new JKMainPanel(new BorderLayout());

		container.add(pnlInfo, BorderLayout.CENTER);
		container.add(pnlButtons, BorderLayout.SOUTH);
		add(container);
		pack();
		setLocationRelativeTo(null);

		txtUserName.grabFocus();
		btnAuthenticate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAuthenticate();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleClose();
			}

		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				handleClose();
			}
		});
		txtUserName.requestFocusInWindow();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				handleClose();
			}
		});
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnAuthenticate.doClick();
				}
			}
		});
	}

	/**
	 * 
	 */
	private void handleClose() {
		setCancelled(true);
		dispose();
	}

	public static void main(String[] args) throws SecurityException {
		User info = authenticateUser(null, "Test", 3);
		System.out.println(info);

	}

	private void handleAuthenticate() {
		try {
			validateInput();
			User user = viewToModel();
			SecurityFacade facade = new SecurityFacade();
			try {
				if (facade.isValidUser(user)) {
					if (user.isDisabled()) {
						SwingUtility.showUserErrorDialog("DISABLED_USER");
					} else {
						// facade.addUserLoginAudit();
						this.user = user;
						dispose();
					}
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							txtUserName.requestFocus();
						}
					});
					SwingUtility.showUserErrorDialog("INVAILD_USER_NAME_OR_PASSWORD", false);
				}
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		} catch (ValidationException e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private User viewToModel() {
		User user = new User();
		user.setUserId(txtUserName.getText());
		user.setPassword(GeneralUtility.encode(txtPassword.getText()));
		return user;
	}

	/**
	 * @throws ValidationException
	 * 
	 */
	private void validateInput() throws ValidationException {
		SwingValidator.checkEmpty(txtUserName);
		SwingValidator.checkEmpty(txtPassword);
	}

	/**
	 * @return the authenticationInfo
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param authenticationInfo
	 *            the authenticationInfo to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
