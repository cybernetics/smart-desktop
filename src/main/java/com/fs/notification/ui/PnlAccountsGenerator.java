package com.fs.notification.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.ExceptionUtil;
import com.fs.notification.facade.NotificationFacade;

public class PnlAccountsGenerator extends JKPanel{
	DaoComboBox cmbAccountsCreationQuery=new DaoComboBox(AbstractTableMetaFactory.getTableMeta("vi_accont_creation_query"));
	JKButton btnGenerateAccounts=new JKButton("GENERATE_ACCOUNTS");
	
	/**
	 * @throws DaoException
	 */
	public PnlAccountsGenerator() throws DaoException{
		init();
	}

	private void init() {
		JKPanel container=new JKPanel();
		container.setLayout(new BorderLayout());
		container.add(getInfoPanel(),BorderLayout.CENTER);
		container.add(getButtonsPanel(),BorderLayout.SOUTH);
		add(container);		
		btnGenerateAccounts.setIcon("generate_accounts.png");
		btnGenerateAccounts.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleGenerate();
			}
		});
	}

	/**
	 * 
	 */
	protected void handleGenerate() {
		try {
			cmbAccountsCreationQuery.checkEmpty();
			NotificationFacade facade=new NotificationFacade();
			facade.syncAccounts(cmbAccountsCreationQuery.getSelectedIdValueAsInteger());
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private Component getButtonsPanel() {
		JKPanel pnl=new JKPanel();
		pnl.add(btnGenerateAccounts);
		btnGenerateAccounts.setShowProgress(true);
		return pnl;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getInfoPanel() {
		JKPanel pnl=new JKPanel();
		pnl.add(new JKLabledComponent("ACCOUNT_GENERATION_QUERY", cmbAccountsCreationQuery));
		return pnl;
	}
	
	public static void main(String[] args) throws FileNotFoundException, ApplicationException, DaoException {
		ApplicationManager.getInstance().init();
		SwingUtility.testPanel(new PnlAccountsGenerator());
	}
}
