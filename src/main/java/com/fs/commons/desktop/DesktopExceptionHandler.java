package com.fs.commons.desktop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.exceptions.DatabaseDownException;
import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.application.exceptions.util.ExceptionHandler;
import com.fs.commons.application.exceptions.util.ExceptionLogging;
import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.apps.instance.InstanceException;
import com.fs.commons.dao.dynamic.constraints.exceptions.ConstraintException;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.DaoValidationException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.locale.Lables;
import com.fs.commons.reports.EmptyReportException;
import com.fs.commons.reports.ReportException;
import com.fs.commons.security.exceptions.InvalidUserException;
import com.fs.commons.security.exceptions.SecurityException;
import com.fs.commons.util.GeneralUtility;
import com.fs.license.LicenseException;

public class DesktopExceptionHandler implements ExceptionHandler {

	private static String LOGS_FOLDER = "logs";

	@Override
	public void handleException(Throwable e) {
		if (e instanceof ServerDownException) {
			showError(e, e.getMessage(), "GeneralDatabaseerror.gif", false);
		}
		if (e instanceof DatabaseDownException) {
			showError(e, e.getMessage(), "GeneralDatabaseerror.gif", false);
			System.exit(0);
		}

		if (e instanceof RecordNotFoundException) {
			showError(e, e.getMessage(), "recordnotfound2.gif", false);
			return;
		}
		if (e instanceof DaoException) {
			DaoException ex = (DaoException) e;
			if(e instanceof DaoValidationException){
				showError(e, e.getMessage(), "GeneralDatabaseerror.gif", false);
				return;
			}
			if (ex.getCause() instanceof ValidationException) {
				handleException((Exception) ex.getCause());
				return;
			}
			if (ex.isInvalidUsernamePassword() || ex.isInvalidDatabaseName()) {
				showError(e, e.getMessage(), "GeneralDatabaseerror.gif", false);
				System.exit(0);
			}else if(e.getCause() instanceof java.net.ConnectException || e.getCause() instanceof java.net.SocketException){
				handleException(new ServerDownException(ex.getCause()));
			} else if (ex.getCause() instanceof ServerDownException) {
					handleException((Exception) ex.getCause());
			}else if(ex.isDuplicateKeyErrorCode()){
				showError(e, "DUPLICATE_RECORD", "GeneralDatabaseerror.gif", false);
			}else {
				showError(e, "DATABASE_ERROR,PLEASE_CHECK_LOG_FILE", "GeneralDatabaseerror.gif", true);
				e.printStackTrace(System.err);
				return;
			}
		}
		if (e instanceof IOException) {
			SwingUtility.showErrorDialog("IO_EXCEPTION", e, SwingUtility.getDefaultMainFrame());
			return;
		}
		if (e instanceof CloneNotSupportedException) {
			SwingUtility.showErrorDialog("CLASS_NOTFOUND_EXCEPTION", e, SwingUtility.getDefaultMainFrame());
			return;
		}
		
		if (e instanceof ReportException) {
			if (e.getCause() instanceof EmptyReportException) {	
				SwingUtility.showErrorDialog("EMPTY_REPORT", e, SwingUtility.getDefaultMainFrame());
				return;
			}
		}

		if (e instanceof TableMetaNotFoundException) {
			SwingUtility.showErrorDialog("TABLEMETA_NOTFOUND_EXCEPTION", e, SwingUtility.getDefaultMainFrame());
			return;
		}
		if (e instanceof UIOPanelCreationException) {
			// showError(e, "PANEL_CREATION_EXCEPTION",
			// "GeneralDatabaseerror.gif", false);
			throw new RuntimeException();
		}
		if (e instanceof SQLException) {
			handleException(new DaoException(e));
			return;
		}
		if (e instanceof ReportException) {
			SwingUtility.showErrorDialog(Lables.get("UNABLE_TO_PRINT_REPORT") + " : " + Lables.get(e.getMessage()), e, SwingUtility
					.getDefaultMainFrame());
			return;
		}
		if (e instanceof LicenseException) {
			LicenseException ex = (LicenseException) e;
			SwingUtility.showUserErrorDialog("License Error : \n\t" + e.getMessage() + " \n Error Code : " + ex.getErrorCode(), false);
			System.exit(0);
		}
		if (e instanceof InvalidUserException) {
			SwingUtility.showUserErrorDialog(e.getMessage(), false);
			return;
		}
		if (e instanceof InstanceException) {
			SwingUtility.showMessageDialog("APPLICATION_LOADED_ERROR", e);
			System.exit(0);
			return;
		}
		if (e instanceof SecurityException) {
			SwingUtility.showMessageDialog("THIS_OPERATION_IS_NOT_ALLOWED", e);
			return;
		}
		if (e instanceof ApplicationException) {
			try {
				if (e.getCause() instanceof InvalidUserException) {
					// just eat the exception since it is already handled in the
					// security framework
				} else if (e.getCause() instanceof LicenseException) {
					handleException((LicenseException) e.getCause());
				} else if (e.getCause() instanceof InstanceException) {
					handleException((InstanceException) e.getCause());
				} else {
					SwingUtility.showErrorDialog(e.getMessage(), e, SwingUtility.getDefaultMainFrame());
				}
			} finally {
				System.exit(0);
			}
		}
		if (e instanceof ValidationException) {
			ValidationException ex = (ValidationException) e;
			if (ex.getComponent() != null) {
				ex.getComponent().requestFocus();
			}
			if (e.getCause() instanceof ConstraintException) {
				showError(e, e.getMessage(), "recordnotfound.gif", false);
			} else if (ex.getField() != null) {
				showError(e, Lables.get(e.getMessage()) + " : " + Lables.get(ex.getField().getFieldName()), "recordnotfound.gif", false);
			} else {
				SwingUtility.showMessageDialog(e.getMessage(), e);
			}
			return;
		}
		//Default exception handling
		SwingUtility.showErrorDialog(Lables.get("EXCEPTION") + " : \n" + Lables.get(e.getMessage()), e, SwingUtility.getDefaultMainFrame());
	}

	/**
	 * 
	 * @param e
	 * @param errorMessage
	 * @param iconName
	 */
	public void showError(Throwable e, String errorMessage, String iconName, boolean throwRunTime) {
		JOptionPane.showMessageDialog(SwingUtility.getDefaultMainFrame(), Lables.get(errorMessage,true), Lables.get("ERROR"), JOptionPane.ERROR_MESSAGE,
				new ImageIcon(GeneralUtility.getIconURL(iconName)));
		if (throwRunTime) {
			throw new RuntimeException(e);
		} else {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws FileNotFoundException
	 */
	public static void initExceptionLogging() throws FileNotFoundException {
		// check if log folder exists
		File file = new File(LOGS_FOLDER);
		if (!file.exists()) {
			file.mkdir();
		}
		System.setErr(new ExceptionLogging(getLogFileName()));
	}

	/**
	 * 
	 * @return
	 */
	public static String getLogFileName() {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		String logFileName = LOGS_FOLDER + "/" + format.format(new Date()) + ".log";
		return logFileName;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public static byte[] getTodayLogFile() throws IOException {
		try {
			byte[] file = GeneralUtility.readFile(new File(getLogFileName()));
			return file;
		} catch (FileNotFoundException e) {
			return new byte[] {};
		}
	}

}
