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
package com.fs.notification.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.apps.templates.dao.TemplateDao;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.notification.bean.Account;
import com.fs.notification.bean.AccountEvent;
import com.fs.notification.bean.Event;
import com.fs.notification.bean.EventGenerationTask;
import com.fs.notification.bean.NotificationType;
import com.fs.notification.bean.Status;
import com.jk.db.dataaccess.plain.JKFinder;
import com.jk.db.dataaccess.plain.JKUpdater;

public class NotificationDao extends TemplateDao {

	// //////////////////////////////////////////////////////////////////////
	public static void main(final String[] args) throws JKRecordNotFoundException, JKDataAccessException {
		final NotificationDao dao = new NotificationDao();
		final ArrayList<AccountEvent> accountEvents = dao.lstEventAccounts(1);
		for (int i = 0; i < accountEvents.size(); i++) {
			System.out.println(accountEvents.get(i).getId());
			System.out.println(accountEvents.get(i).getEvent().getTitle());
			System.out.println(accountEvents.get(i).getStatus().getName());
			System.out.println(accountEvents.get(i).getAccount().getName());
		}
	}

	/////////////////////////////////////////////////////////////////////////////////
	public void addAccount(final Account account) throws JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return "INSERT INTO not_accounts (account_id_str,account_name,email,mobile, active) VALUES(?,?,?,?,?)";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				int counter = 1;
				ps.setString(counter++, account.getNumber());
				ps.setString(counter++, account.getName());
				ps.setString(counter++, account.getEmail());
				ps.setString(counter++, account.getMobile());
				ps.setBoolean(counter++, account.isActive());
			}
		};
		final int accountId = executeUpdate(updater);
		account.setId(accountId);
	}

	//////////////////////////////////////////////////////////////////////////
	public int addAccountEvent(final AccountEvent event) throws JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return "INSERT INTO not_account_events (account_id,event_id,status_id) VALUES(?,?,?)";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, event.getAccount().getId());
				ps.setInt(2, event.getEvent().getId());
				ps.setInt(3, event.getStatus().getId());
			}
		};
		return executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////////////
	public void addEvent(final Event event) throws JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return "insert into not_events(event_title, event_text, status_id,not_type_id) values (?, ?, ?,?)";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				int count = 1;
				ps.setString(count++, event.getTitle());
				ps.setString(count++, event.getText());
				ps.setInt(count++, event.getStatus().getId());
				ps.setInt(count++, event.getNotificationType().getId());
			}
		};
		final int id = executeUpdate(updater);
		event.setId(id);
	}

	// //////////////////////////////////////////////////////////////////////
	public Account findAccount(final int id) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT * FROM not_accounts WHERE account_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return populateAccount(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		};
		return (Account) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public Account findAccount(final String number, final String name) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT * FROM not_accounts WHERE account_id_str = ? AND account_name=?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return populateAccount(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setString(1, number);
				ps.setString(2, name);
			}
		};
		return (Account) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public Event findEvent(final int id) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT * FROM not_events WHERE event_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				final Event event = new Event();
				event.setId(rs.getInt("event_id"));
				event.setTitle(rs.getString("event_title"));
				event.setText(rs.getString("event_text"));
				event.setStatus(findEventStatus(rs.getInt("status_id")));
				return event;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);

			}
		};
		return (Event) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public EventGenerationTask findEventGenerationTask(final int id) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {
			@Override
			public String getQuery() {
				return "SELECT * FROM not_event_generation_task WHERE task_id=?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				final EventGenerationTask task = new EventGenerationTask();
				task.setId(rs.getInt("task_id"));
				task.setName(rs.getString("task_name"));
				task.setTemplate(findTemplate(rs.getInt("template_id")));
				task.setTemplateValues(findQuery(rs.getInt("paramters_query_id")));
				task.setNotType(findNotificationType(rs.getInt("not_type_id")));
				if (rs.getInt("accounts_creation_query") != 0) {
					task.setAppliedAccountsQuery(findQuery(rs.getInt("accounts_creation_query")));
				}
				return task;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		};
		return (EventGenerationTask) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public Status findEventStatus(final int statusId) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT * FROM not_event_status WHERE status_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return populateStatus(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, statusId);
			}
		};
		return (Status) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public NotificationType findNotificationType(final int id) throws JKRecordNotFoundException, JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT * FROM not_notification_types WHERE not_type_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				return populateNotificationType(rs);
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);

			}
		};
		return (NotificationType) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public ArrayList<AccountEvent> lstEventAccounts(final int eventId) throws JKDataAccessException {
		final JKFinder finder = new JKFinder() {

			@Override
			public String getQuery() {
				return "SELECT * FROM not_account_events WHERE event_id = ?";
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, JKRecordNotFoundException, JKDataAccessException {
				final AccountEvent accountEvent = new AccountEvent();
				accountEvent.setId(rs.getInt("account_event_id"));
				accountEvent.setAccount(findAccount(rs.getInt("account_id")));
				accountEvent.setEvent(findEvent(rs.getInt("event_id")));
				accountEvent.setStatus(findEventStatus(rs.getInt("status_id")));
				return accountEvent;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setInt(1, eventId);

			}
		};
		return lstRecords(finder);
	}

	///////////////////////////////////////////////////////////////////////////////
	private Object populateAccount(final ResultSet rs) throws SQLException {
		final Account account = new Account();
		account.setId(rs.getInt("account_id"));
		account.setNumber(rs.getString("account_id_str"));
		account.setName(rs.getString("account_name"));
		account.setMobile(rs.getString("mobile"));
		account.setEmail(rs.getString("email"));

		account.setActive(rs.getBoolean("active"));
		return account;
	}

	// //////////////////////////////////////////////////////////////////////
	private NotificationType populateNotificationType(final ResultSet rs) throws SQLException {
		final NotificationType notificationType = new NotificationType();
		notificationType.setId(rs.getInt("not_type_id"));
		notificationType.setName(rs.getString("not_type_name"));
		notificationType.setImpl(rs.getString("not_type_impl"));
		return notificationType;
	}

	// //////////////////////////////////////////////////////////////////////
	private Status populateStatus(final ResultSet rs) throws SQLException {
		final Status status = new Status();
		status.setId(rs.getInt("status_id"));
		status.setName(rs.getString("status_name"));
		status.setActive(rs.getBoolean("active"));
		return status;
	}

	///////////////////////////////////////////////////////////////////////////////
	public void updateAccount(final Account account) throws JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return "UPDATE not_accounts SET email=? ,mobile=?  , active=? WHERE account_id=?";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				ps.setString(1, account.getEmail());
				ps.setString(2, account.getMobile());
				ps.setBoolean(3, account.isActive());
				ps.setInt(4, account.getId());
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////////////
	// its just update the event status
	public void updateEvent(final Event event) throws JKDataAccessException {
		final JKUpdater updater = new JKUpdater() {

			@Override
			public String getQuery() {
				return "update not_events set status_id = ? WHERE event_id = ?";
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
				int count = 1;
				ps.setInt(count++, event.getStatus().getId());
				ps.setInt(count++, event.getId());

			}
		};
		executeUpdate(updater);
	}

}
