/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.notification.facade;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.apps.templates.TemplateManager;
import com.fs.commons.apps.templates.beans.Query;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.JKSession;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.notification.bean.Account;
import com.fs.notification.bean.AccountEvent;
import com.fs.notification.bean.Event;
import com.fs.notification.bean.EventGenerationTask;
import com.fs.notification.bean.Status;
import com.fs.notification.dao.NotificationDao;

public class NotificationFacade {
	///////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws FileNotFoundException, ApplicationException, JKRecordNotFoundException, JKDataAccessException {
		ApplicationManager.getInstance().init();
		final NotificationFacade f = new NotificationFacade();
		System.out.println(f.syncAccounts(6).size());
	}

	NotificationDao dao = new NotificationDao();

	public void generateEvent(final int specId) throws JKRecordNotFoundException, JKDataAccessException {
		boolean commit = false;
		final JKSession session = getSession();
		try {
			final EventGenerationTask spec = this.dao.findEventGenerationTask(specId);
			final String[] group = TemplateManager.compileTemplateGroup(spec.getTemplate(), spec.getTemplateValues().getQueryText());

			final ArrayList<Account> accounts = syncAccounts(spec.getAppliedAccountsQuery());
			if (group.length != accounts.size()) {
				throw new IllegalStateException(
						"TEMPLATE_GROUP_COUNT_DOESNOT_MATCH_ACCOUNTS_COUNT" + " -->(" + group.length + "," + accounts.size() + ")");
			}

			for (int i = 0; i < group.length; i++) {
				final String compileTemplateText = group[i];

				final Event event = new Event();
				event.setStatus(new Status(1));
				event.setTitle(spec.getTemplate().getTempTitle());
				event.setText(compileTemplateText);
				event.setNotificationType(spec.getNotType());

				final Account account = accounts.get(i);
				this.dao.addEvent(event);
				this.dao.addAccountEvent(new AccountEvent(event, account));

			}
			commit = true;
		} finally {
			session.close(commit);
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	private JKSession getSession() throws JKDataAccessException {
		final JKSession session = JKDataSourceFactory.getDefaultDataSource().createSession();
		this.dao.setSession(session);
		return session;
	}

	/**
	 * create if accounts doesnot exists
	 * 
	 * @param query
	 * @return
	 * @throws JKDataAccessException
	 */
	private ArrayList<Account> loadAccounts(final Query query) throws JKDataAccessException {
		final Object[] records = DaoUtil.executeQueryAsArray(query.getQueryText());
		final ArrayList<Account> accounts = new ArrayList<Account>();
		for (final Object record : records) {
			final Object[] row = (Object[]) record;
			if (row.length != 4) {
				System.out.println(Arrays.toString(row));
				throw new IllegalStateException("GENERATION_ACCOUNTS_QUERY_SHOULW_CONTAINS(NUMBER,NAME,MOBILE,EMAIL) : " + row.length);
			}
			final Account account = new Account();
			account.setNumber(row[0].toString());
			account.setName(row[1].toString());
			account.setActive(true);

			if (row[2] != null) {
				account.setMobile(row[2].toString());
			}
			if (row[3] != null && validateEmail(row[3])) {
				account.setEmail(row[3].toString());
			}
			accounts.add(account);
		}
		return accounts;
	}

	///////////////////////////////////////////////////////////////////////////////
	private Account syncAccount(Account account) throws JKDataAccessException {
		try {
			account = this.dao.findAccount(account.getNumber(), account.getName());
			// account.setId(account1.getId());
			// dao.updateAccount(account);
		} catch (final JKRecordNotFoundException e) {
			this.dao.addAccount(account);
		}
		return account;
	}

	///////////////////////////////////////////////////////////////////////////////
	public ArrayList<Account> syncAccounts(final int queryId) throws JKRecordNotFoundException, JKDataAccessException {
		final Query query = this.dao.findQuery(queryId);
		return syncAccounts(query);
	}

	///////////////////////////////////////////////////////////////////////////////
	/**
	 * Load accounts from query Build array list of accounts for each accout ,
	 * check if not exists in the database , add it
	 */
	private ArrayList<Account> syncAccounts(final Query query) throws JKDataAccessException {
		final ArrayList<Account> accounts = loadAccounts(query);
		for (int i = 0; i < accounts.size(); i++) {
			final Account account = accounts.get(i);
			accounts.set(i, syncAccount(account));
		}
		return accounts;
	}

	/**
	 *
	 * @param object
	 * @return
	 */
	private boolean validateEmail(final Object object) {
		return true;
	}
}
