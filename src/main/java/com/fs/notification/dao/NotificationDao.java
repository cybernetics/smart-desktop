package com.fs.notification.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fs.commons.apps.templates.dao.TemplateDao;
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.DaoUpdater;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.notification.bean.Account;
import com.fs.notification.bean.AccountEvent;
import com.fs.notification.bean.Event;
import com.fs.notification.bean.EventGenerationTask;
import com.fs.notification.bean.NotificationType;
import com.fs.notification.bean.Status;

public class NotificationDao extends TemplateDao {


	// //////////////////////////////////////////////////////////////////////
	public NotificationType findNotificationType(final int id) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);

			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateNotificationType(rs);
			}

			@Override
			public String getFinderSql() {
				return "SELECT * FROM not_notification_types WHERE not_type_id = ?";
			}
		};
		return (NotificationType) findRecord(finder);
	}
	// //////////////////////////////////////////////////////////////////////
	public Account findAccount(final int id) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateAccount(rs);
			}

			@Override
			public String getFinderSql() {
				return "SELECT * FROM not_accounts WHERE account_id = ?";
			}
		};
		return (Account) findRecord(finder);
	}
	// //////////////////////////////////////////////////////////////////////
	public Status findEventStatus(final int statusId) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, statusId);
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				return populateStatus(rs);
			}

			@Override
			public String getFinderSql() {
				return "SELECT * FROM not_event_status WHERE status_id = ?";
			}
		};
		return (Status) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	public void addEvent(final Event event) throws DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public String getUpdateSql() {
				return "insert into not_events(event_title, event_text, status_id,not_type_id) values (?, ?, ?,?)";
			}

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int count = 1;
				ps.setString(count++, event.getTitle());
				ps.setString(count++, event.getText());
				ps.setInt(count++, event.getStatus().getId());
				ps.setInt(count++, event.getNotificationType().getId());
			}
		};
		int id = executeUpdate(updater);
		event.setId(id);
	}

	// //////////////////////////////////////////////////////////////////////
	public Event findEvent(final int id) throws RecordNotFoundException, DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);

			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				Event event = new Event();
				event.setId(rs.getInt("event_id"));
				event.setTitle(rs.getString("event_title"));
				event.setText(rs.getString("event_text"));
				event.setStatus(findEventStatus(rs.getInt("status_id")));
				return event;
			}

			@Override
			public String getFinderSql() {
				return "SELECT * FROM not_events WHERE event_id = ?";
			}
		};
		return (Event) findRecord(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	// its just update the event status
	public void updateEvent(final Event event) throws DaoException {
		DaoUpdater updater = new DaoUpdater() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int count = 1;
				ps.setInt(count++, event.getStatus().getId());
				ps.setInt(count++, event.getId());

			}

			@Override
			public String getUpdateSql() {
				return "update not_events set status_id = ? WHERE event_id = ?";
			}
		};
		executeUpdate(updater);
	}

	// //////////////////////////////////////////////////////////////////////
	public ArrayList<AccountEvent> lstEventAccounts(final int eventId) throws DaoException {
		DaoFinder finder = new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, eventId);

			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				AccountEvent accountEvent = new AccountEvent();
				accountEvent.setId(rs.getInt("account_event_id"));
				accountEvent.setAccount(findAccount(rs.getInt("account_id")));
				accountEvent.setEvent(findEvent(rs.getInt("event_id")));
				accountEvent.setStatus(findEventStatus(rs.getInt("status_id")));
				return accountEvent;
			}

			@Override
			public String getFinderSql() {
				return "SELECT * FROM not_account_events WHERE event_id = ?";
			}
		};
		return lstRecords(finder);
	}

	// //////////////////////////////////////////////////////////////////////
	private NotificationType populateNotificationType(ResultSet rs) throws SQLException {
		NotificationType notificationType = new NotificationType();
		notificationType.setId(rs.getInt("not_type_id"));
		notificationType.setName(rs.getString("not_type_name"));
		notificationType.setImpl(rs.getString("not_type_impl"));
		return notificationType;
	}

	// //////////////////////////////////////////////////////////////////////
	private Status populateStatus(ResultSet rs) throws SQLException {
		Status status = new Status();
		status.setId(rs.getInt("status_id"));
		status.setName(rs.getString("status_name"));
		status.setActive(rs.getBoolean("active"));
		return status;
	}

	// //////////////////////////////////////////////////////////////////////
	public EventGenerationTask findEventGenerationTask(final int id) throws RecordNotFoundException, DaoException{
		DaoFinder finder=new  DaoFinder() {			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
			
			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				EventGenerationTask task=new EventGenerationTask();
				task.setId(rs.getInt("task_id"));
				task.setName(rs.getString("task_name"));
				task.setTemplate(findTemplate(rs.getInt("template_id")));
				task.setTemplateValues(findQuery(rs.getInt("paramters_query_id")));
				task.setNotType(findNotificationType(rs.getInt("not_type_id")));
				if(rs.getInt("accounts_creation_query")!=0){
					task.setAppliedAccountsQuery(findQuery(rs.getInt("accounts_creation_query")));
				}
				return task;
			}
			
			@Override
			public String getFinderSql() {
				return "SELECT * FROM not_event_generation_task WHERE task_id=?";
			}
		};
		return (EventGenerationTask) findRecord(finder);
	}
	
	// //////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws RecordNotFoundException, DaoException {
		NotificationDao dao = new NotificationDao();
		ArrayList<AccountEvent> accountEvents = dao.lstEventAccounts(1);
		for (int i = 0; i < accountEvents.size(); i++) {
			System.out.println(accountEvents.get(i).getId());
			System.out.println(accountEvents.get(i).getEvent().getTitle());
			System.out.println(accountEvents.get(i).getStatus().getName());
			System.out.println(accountEvents.get(i).getAccount().getName());
		}
	}
	// //////////////////////////////////////////////////////////////////////	
	public Account findAccount(final String number, final String name)throws RecordNotFoundException, DaoException {
			DaoFinder finder = new DaoFinder() {

				@Override
				public void setParamters(PreparedStatement ps) throws SQLException {
					ps.setString(1, number);
					ps.setString(2, name);
				}

				@Override
				public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
					return populateAccount(rs);
				}

				@Override
				public String getFinderSql() {
					return "SELECT * FROM not_accounts WHERE account_id_str = ? AND account_name=?";
				}
			};
			return (Account) findRecord(finder);	
	}

	///////////////////////////////////////////////////////////////////////////////
	private Object populateAccount(ResultSet rs) throws SQLException {
		Account account = new Account();
		account.setId(rs.getInt("account_id"));
		account.setNumber(rs.getString("account_id_str"));
		account.setName(rs.getString("account_name"));
		account.setMobile(rs.getString("mobile"));
		account.setEmail(rs.getString("email"));
		
		account.setActive(rs.getBoolean("active"));
		return account;
	}
	
	///////////////////////////////////////////////////////////////////////////////
	public void updateAccount(final Account account) throws DaoException {
		DaoUpdater updater=new DaoUpdater() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setString(1, account.getEmail());
				ps.setString(2, account.getMobile());
				ps.setBoolean(3, account.isActive());
				ps.setInt(4, account.getId());
			}
			
			@Override
			public String getUpdateSql() {
				return "UPDATE not_accounts SET email=? ,mobile=?  , active=? WHERE account_id=?";
			}
		};
		executeUpdate(updater);
	}
	
	/////////////////////////////////////////////////////////////////////////////////
	public void addAccount(final Account account) throws DaoException {
		DaoUpdater updater=new DaoUpdater() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				int counter=1;
				ps.setString(counter++, account.getNumber());
				ps.setString(counter++, account.getName());
				ps.setString(counter++, account.getEmail());
				ps.setString(counter++, account.getMobile());
				ps.setBoolean(counter++, account.isActive());
			}
			
			@Override
			public String getUpdateSql() {
				return "INSERT INTO not_accounts (account_id_str,account_name,email,mobile, active) VALUES(?,?,?,?,?)";
			}
		};
		int accountId = executeUpdate(updater);
		account.setId(accountId);
	}
	
	//////////////////////////////////////////////////////////////////////////
	public int addAccountEvent(final AccountEvent event) throws DaoException {
		DaoUpdater updater=new DaoUpdater() {
			
			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
				ps.setInt(1, event.getAccount().getId());
				ps.setInt(2, event.getEvent().getId());
				ps.setInt(3, event.getStatus().getId());
			}
			
			@Override
			public String getUpdateSql() {				
				return "INSERT INTO not_account_events (account_id,event_id,status_id) VALUES(?,?,?)";
			}
		};
		return executeUpdate(updater);
	}

	

}
