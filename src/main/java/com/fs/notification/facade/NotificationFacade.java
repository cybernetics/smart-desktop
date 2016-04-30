package com.fs.notification.facade;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;



import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.apps.templates.TemplateManager;
import com.fs.commons.apps.templates.beans.Query;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.Session;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.notification.bean.Account;
import com.fs.notification.bean.AccountEvent;
import com.fs.notification.bean.Event;
import com.fs.notification.bean.EventGenerationTask;
import com.fs.notification.bean.Status;
import com.fs.notification.dao.NotificationDao;

public class NotificationFacade {
	NotificationDao dao = new NotificationDao();

	public void generateEvent(int specId) throws RecordNotFoundException, DaoException {
		boolean commit = false;
		Session session = getSession();
		try {
			EventGenerationTask spec = dao.findEventGenerationTask(specId);
			String[] group = TemplateManager.compileTemplateGroup(spec.getTemplate(), spec.getTemplateValues().getQueryText());

			ArrayList<Account> accounts = syncAccounts(spec.getAppliedAccountsQuery());
			if(group.length!=accounts.size()){
				throw new IllegalStateException("TEMPLATE_GROUP_COUNT_DOESNOT_MATCH_ACCOUNTS_COUNT"+" -->("+group.length+","+accounts.size()+")");
			}
			
			for (int i=0;i<group.length;i++) {
				String compileTemplateText =group[i];
				
				Event event = new Event();
				event.setStatus(new Status(1));
				event.setTitle(spec.getTemplate().getTempTitle());
				event.setText(compileTemplateText);
				event.setNotificationType(spec.getNotType());
				
				Account account=accounts.get(i);
				dao.addEvent(event);
				dao.addAccountEvent(new AccountEvent(event,account));
								
			}
			commit = true;
		} finally {
			session.close(commit);
		}
	}

	/**
	 * create if accounts doesnot exists
	 * @param query
	 * @return
	 * @throws DaoException
	 */
	private ArrayList<Account> loadAccounts(Query query) throws DaoException {
		Object[] records = DaoUtil.executeQueryAsArray(query.getQueryText());
		ArrayList<Account> accounts = new ArrayList<Account>();
		for (int i = 0; i < records.length; i++) {
			Object[] row = (Object[]) records[i];
			if (row.length != 4) {
				System.out.println(Arrays.toString(row));
				throw new IllegalStateException("GENERATION_ACCOUNTS_QUERY_SHOULW_CONTAINS(NUMBER,NAME,MOBILE,EMAIL) : "+row.length);
			}
			Account account = new Account();
			account.setNumber(row[0].toString());
			account.setName(row[1].toString());
			account.setActive(true);
			
			if(row[2]!=null){
				account.setMobile(row[2].toString());
			}
			if(row[3]!=null && validateEmail(row[3])){
				account.setEmail(row[3].toString());
			}
			accounts.add(account);
		}
		return accounts;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	private boolean validateEmail(Object object) {
		return true;
	}

	///////////////////////////////////////////////////////////////////////////////
	private Account syncAccount(Account account) throws DaoException {		
		try {
			account= dao.findAccount(account.getNumber(), account.getName());
			//account.setId(account1.getId());
			//dao.updateAccount(account);
		} catch (RecordNotFoundException e) {
			dao.addAccount(account);
		}
		return account;
	}

	///////////////////////////////////////////////////////////////////////////////
	private Session getSession() throws DaoException {
		Session session = DataSourceFactory.getDefaultDataSource().createSession();
		dao.setSession(session);
		return session;
	}

	///////////////////////////////////////////////////////////////////////////////
	public ArrayList<Account> syncAccounts(int queryId) throws RecordNotFoundException, DaoException {
		Query query = dao.findQuery(queryId);
		return syncAccounts(query);
	}

	///////////////////////////////////////////////////////////////////////////////
	/**
	 * Load accounts from query
	 * Build array list of accounts
	 * for each accout , check if not exists in the database , add it
	 */
	private ArrayList<Account> syncAccounts(Query query) throws DaoException {
		ArrayList<Account> accounts = loadAccounts(query);
		for (int i=0;i<accounts.size();i++) {
			 Account account = accounts.get(i);
			 accounts.set(i,syncAccount(account));
		}
		return accounts;
	}

	///////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws FileNotFoundException, ApplicationException, RecordNotFoundException, DaoException {
		ApplicationManager.getInstance().init();
		NotificationFacade f=new NotificationFacade();
		System.out.println(f.syncAccounts(6).size());
	}
}
