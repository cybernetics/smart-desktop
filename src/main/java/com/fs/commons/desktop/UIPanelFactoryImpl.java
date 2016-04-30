/**
 * 
 */
package com.fs.commons.desktop;

import java.util.ArrayList;
import java.util.Properties;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.application.ui.UIPanelFactory;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.reports.Report;
import com.fs.commons.reports.ReportManager;
import com.fs.commons.reports.ReportUIPanel;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;
import com.fs.commons.util.WebUtil;

/**
 * @author u087
 * 
 */
public class UIPanelFactoryImpl implements UIPanelFactory {
	public static final String DYN_MASTER_DETAIL_CRUDL_PANEL = "com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel";

	public static final String DYN_SINGLE_MASTER_DETAIL_CRUDL_PANEL = "com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel";

	UIPanel instance;

	private final MenuItem menuItem;

	public UIPanelFactoryImpl(MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * @param prop
	 * @return
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createPanel(Properties prop, boolean createNew) throws UIOPanelCreationException {
		try {
			// System.out.println(new Date());
			if (!createNew && instance != null) {
				return instance;
			}
			String value;
			if ((value = prop.getProperty("panel-factory")) != null) {
				instance = createPanelFromPanelFactory(value, createNew, prop);
			} else if ((value = prop.getProperty("report-name")) != null) {
				instance = createReportPanel(value);
			} else if ((value = prop.getProperty("table-meta")) != null) {
				String tableMetaName = value;
				try {
					instance = createMasterDetailWithListPanel(tableMetaName, prop);
				} catch (Exception e) {
					e.printStackTrace();
					throw new UIOPanelCreationException(e);
				}
			} else if ((value = prop.getProperty("panel-class")) != null) {
				try {
					instance = (UIPanel) Class.forName(value).newInstance();
				} catch (Exception e) {
					System.err.println(prop);
					e.printStackTrace();
					throw new UIOPanelCreationException(e);
				}
			} else if ((value = prop.getProperty("executor")) != null) {
				try {
					((Runnable) Class.forName(value).newInstance()).run();
					instance = null;
				} catch (Exception e) {
					e.printStackTrace();
					throw new UIOPanelCreationException(e);
				}
			} else if ((value = prop.getProperty("master-report")) != null) {
				instance = createMasterReportPanel(prop);

			} else if ((value = prop.getProperty("fs-url")) != null) {
				// JKWebBrowser browser = new JKWebBrowser();
				// browser.setHeaders(WebUtil.getDefaultHeaders());
				// browser.setUrl(WebUtil.getFSWebServerUrl(value));
				// instance=browser;
				throw new IllegalStateException("We are working on new mechanism to view webpages inside the app,Please contact Jalal");

			} else {
				instance = new JKPanel();
			}
			return instance;
		} catch (UIOPanelCreationException e) {
			System.err.println("Unable to create panel with properties " + prop);
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
			return null;
		} finally {
			// System.out.println(new Date());
		}
	}

	/**
	 * @param prop
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createMasterReportPanel(Properties prop) throws UIOPanelCreationException {
		String sqlFile = prop.getProperty("sql-file-name");
		String title = prop.getProperty("report-title", "Master Report");
		if (sqlFile == null) {
			throw new RuntimeException("master-report panels should have sql-file-name property set");
		}
		String sql = GeneralUtility.getSqlFile(sqlFile);
		QueryJTable tbl = new QueryJTable(sql, title);
		tbl.setMasterTable();
		// tbl.setAllowExcelExport(true);
		// tbl.setAllowFiltering(true);
		// tbl.allowPrinting(true);
		return tbl;
	}

	/**
	 * @param value
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createReportPanel(String value) throws UIOPanelCreationException {
		try {
			Report report = ReportManager.getReport(SwingUtility.getDefaultLocale() + "_" + value);
			ReportUIPanel pnl = new ReportUIPanel(report);
			return pnl;
		} catch (Exception e) {
			throw new UIOPanelCreationException(e);
		}

	}

	/**
	 * @param value
	 * @param prop
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createPanelFromPanelFactory(String value, boolean createNew, Properties prop) throws UIOPanelCreationException {
		try {
			UIPanelFactory factory = (UIPanelFactory) Class.forName(value).newInstance();
			return factory.createPanel(prop, createNew);
		} catch (Exception e) {
			throw new UIOPanelCreationException(e);
		}
	}

	/**
	 * @param tableMetaName
	 * @param prop
	 * @return
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createMasterDetailWithListPanel(String tableMetaName, Properties prop)
			throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		// TODO : fix me
		TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(getDataSource(), tableMetaName);
		if (prop.getProperty("detail-tables") != null) {
			String[] detailTables = prop.getProperty("detail-tables").split(",");
			String[] detailFields = prop.getProperty("detail_fields").split(",");
			String[] crossTables = null;
			if (prop.getProperty("cross-table") != null) {
				crossTables = prop.getProperty("cross-table").split(",");
			}
			ArrayList<ForiegnKeyFieldMeta> detailedFields = buildDetailFields(detailTables, detailFields, crossTables);
			tableMeta.setDetailFields(detailedFields);
		}
		if (tableMeta.getMaxRecordsCount() == 1 || (prop.getProperty("single-record") != null && prop.getProperty("single-record").equals("true"))) {
			String className = prop.getProperty("panel-class", DYN_SINGLE_MASTER_DETAIL_CRUDL_PANEL);
			return createSingleRecordPanel(className, tableMeta);

		}
		String className = prop.getProperty("panel-class", DYN_MASTER_DETAIL_CRUDL_PANEL);
		boolean allowExcelExport = Boolean.parseBoolean(prop.getProperty("allow-excel-export", "true"));
		DynMasterDetailCRUDLPanel panel = (DynMasterDetailCRUDLPanel) createPanelByClassName(className);
		panel.setTableMeta(tableMeta);
		panel.getTable().setAllowExcelExport(allowExcelExport);
		return panel;
		// return new DynMasterDetailCRUDLPanel();
	}

	/**
	 * @param className
	 * @param tableMeta
	 * @return
	 * @throws DaoException
	 * @throws TableMetaNotFoundException
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createSingleRecordPanel(String className, TableMeta tableMeta)
			throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		// final DynMasterDetailPanel pnl = new DynMasterDetailPanel(tableMeta);
		final DynMasterDetailPanel pnl = (DynMasterDetailPanel) createPanelByClassName(className);
		pnl.setTableMeta(tableMeta);
		pnl.init();
		pnl.getMasterPanel().setAllowClear(false);
		pnl.getMasterPanel().addDynDaoActionListener(new DynDaoActionAdapter() {
			@Override
			public void afterClosePanel() {
				SwingUtility.closePanel(pnl);
			}
		});
		try {
			// Record record =
			// pnl.getPnlMaster().getDao().getFirstRecordInTable();
			Integer id = (Integer) DaoUtil.exeuteSingleOutputQuery(tableMeta.getDataSource(), tableMeta.getReportSql());
			// if (record != null) {
			if (id != null) {
				pnl.getMasterPanel().handleFindRecord(id);
			} else {
				pnl.setMode(DynDaoMode.ADD);
			}
		} catch (RecordNotFoundException e) {
			pnl.setMode(DynDaoMode.ADD);
		}
		return pnl;
	}

	/**
	 * @param className
	 * @return
	 */
	private UIPanel createPanelByClassName(String className) {
		try {
			return (UIPanel) Class.forName(className).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Unable to instaniate class " + className, e);
		}

	}

	/**
	 * @param detailTables
	 * @param crossTables
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	private ArrayList<ForiegnKeyFieldMeta> buildDetailFields(String[] detailTables, String[] detailFields, String[] crossTables)
			throws TableMetaNotFoundException {
		ArrayList<ForiegnKeyFieldMeta> list = new ArrayList<ForiegnKeyFieldMeta>();
		for (int i = 0; i < detailTables.length; i++) {
			// TODO : fix me
			TableMeta detailTable = AbstractTableMetaFactory.getTableMeta(getDataSource(), detailTables[i]);
			if (crossTables != null) {
				detailTable.setCrossTable(Boolean.parseBoolean(crossTables[i]));
			}
			ForiegnKeyFieldMeta fieldMeta = (ForiegnKeyFieldMeta) detailTable.getField(detailFields[i]);
			list.add(fieldMeta);
		}
		return list;
	}

	private DataSource getDataSource() {
		return menuItem.getParentMenu().getParentModule().getDataSource();
	}
}
