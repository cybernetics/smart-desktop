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
package com.fs.commons.desktop;

import java.util.ArrayList;
import java.util.Properties;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.application.ui.UIPanel;
import com.fs.commons.application.ui.UIPanelFactory;
import com.fs.commons.application.ui.menu.MenuItem;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.JKRecordNotFoundException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.reports.JKReport;
import com.fs.commons.reports.JKReportManager;
import com.fs.commons.reports.ReportUIPanel;
import com.fs.commons.util.GeneralUtility;
import com.jk.exceptions.handler.ExceptionUtil;

/**
 * @author u087
 *
 */
public class UIPanelFactoryImpl implements UIPanelFactory {
	public static final String DYN_MASTER_DETAIL_CRUDL_PANEL = "com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel";

	public static final String DYN_SINGLE_MASTER_DETAIL_CRUDL_PANEL = "com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailPanel";

	UIPanel instance;

	private final MenuItem menuItem;

	public UIPanelFactoryImpl(final MenuItem menuItem) {
		this.menuItem = menuItem;
	}

	/**
	 * @param detailTables
	 * @param crossTables
	 * @return
	 * @throws TableMetaNotFoundException
	 */
	private ArrayList<ForiegnKeyFieldMeta> buildDetailFields(final String[] detailTables, final String[] detailFields, final String[] crossTables)
			throws TableMetaNotFoundException {
		final ArrayList<ForiegnKeyFieldMeta> list = new ArrayList<ForiegnKeyFieldMeta>();
		for (int i = 0; i < detailTables.length; i++) {
			// TODO : fix me
			final TableMeta detailTable = AbstractTableMetaFactory.getTableMeta(getDataSource(), detailTables[i]);
			if (crossTables != null) {
				detailTable.setCrossTable(Boolean.parseBoolean(crossTables[i]));
			}
			final ForiegnKeyFieldMeta fieldMeta = (ForiegnKeyFieldMeta) detailTable.getField(detailFields[i]);
			list.add(fieldMeta);
		}
		return list;
	}

	/**
	 * @param tableMetaName
	 * @param prop
	 * @return
	 * @throws TableMetaNotFoundException
	 * @throws JKDataAccessException
	 * @throws UIOPanelCreationException
	 */
	public UIPanel createMasterDetailWithListPanel(final String tableMetaName, final Properties prop)
			throws TableMetaNotFoundException, JKDataAccessException, UIOPanelCreationException {
		// TODO : fix me
		final TableMeta tableMeta = AbstractTableMetaFactory.getTableMeta(getDataSource(), tableMetaName);
		if (prop.getProperty("detail-tables") != null) {
			final String[] detailTables = prop.getProperty("detail-tables").split(",");
			final String[] detailFields = prop.getProperty("detail_fields").split(",");
			String[] crossTables = null;
			if (prop.getProperty("cross-table") != null) {
				crossTables = prop.getProperty("cross-table").split(",");
			}
			final ArrayList<ForiegnKeyFieldMeta> detailedFields = buildDetailFields(detailTables, detailFields, crossTables);
			tableMeta.setDetailFields(detailedFields);
		}
		if (tableMeta.getMaxRecordsCount() == 1 || prop.getProperty("single-record") != null && prop.getProperty("single-record").equals("true")) {
			final String className = prop.getProperty("panel-class", DYN_SINGLE_MASTER_DETAIL_CRUDL_PANEL);
			return createSingleRecordPanel(className, tableMeta);

		}
		final String className = prop.getProperty("panel-class", DYN_MASTER_DETAIL_CRUDL_PANEL);
		final boolean allowExcelExport = Boolean.parseBoolean(prop.getProperty("allow-excel-export", "true"));
		final DynMasterDetailCRUDLPanel panel = (DynMasterDetailCRUDLPanel) createPanelByClassName(className);
		panel.setTableMeta(tableMeta);
		panel.getTable().setAllowExcelExport(allowExcelExport);
		return panel;
		// return new DynMasterDetailCRUDLPanel();
	}

	/**
	 * @param prop
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createMasterReportPanel(final Properties prop) throws UIOPanelCreationException {
		final String sqlFile = prop.getProperty("sql-file-name");
		final String title = prop.getProperty("report-title", "Master Report");
		if (sqlFile == null) {
			throw new RuntimeException("master-report panels should have sql-file-name property set");
		}
		final String sql = GeneralUtility.getSqlFile(sqlFile);
		final QueryJTable tbl = new QueryJTable(sql, title);
		tbl.setMasterTable();
		// tbl.setAllowExcelExport(true);
		// tbl.setAllowFiltering(true);
		// tbl.allowPrinting(true);
		return tbl;
	}

	/**
	 * @param prop
	 * @return
	 * @throws UIOPanelCreationException
	 */
	@Override
	public UIPanel createPanel(final Properties prop, final boolean createNew) throws UIOPanelCreationException {
		try {
			// System.out.println(new Date());
			if (!createNew && this.instance != null) {
				return this.instance;
			}
			String value;
			if ((value = prop.getProperty("panel-factory")) != null) {
				this.instance = createPanelFromPanelFactory(value, createNew, prop);
			} else if ((value = prop.getProperty("report-name")) != null) {
				this.instance = createReportPanel(value);
			} else if ((value = prop.getProperty("table-meta")) != null) {
				final String tableMetaName = value;
				try {
					this.instance = createMasterDetailWithListPanel(tableMetaName, prop);
				} catch (final Exception e) {
					e.printStackTrace();
					throw new UIOPanelCreationException(e);
				}
			} else if ((value = prop.getProperty("panel-class")) != null) {
				try {
					this.instance = (UIPanel) Class.forName(value).newInstance();
				} catch (final Exception e) {
					System.err.println(prop);
					e.printStackTrace();
					throw new UIOPanelCreationException(e);
				}
			} else if ((value = prop.getProperty("executor")) != null) {
				try {
					((Runnable) Class.forName(value).newInstance()).run();
					this.instance = null;
				} catch (final Exception e) {
					e.printStackTrace();
					throw new UIOPanelCreationException(e);
				}
			} else if ((value = prop.getProperty("master-report")) != null) {
				this.instance = createMasterReportPanel(prop);

			} else if ((value = prop.getProperty("fs-url")) != null) {
				// JKWebBrowser browser = new JKWebBrowser();
				// browser.setHeaders(WebUtil.getDefaultHeaders());
				// browser.setUrl(WebUtil.getFSWebServerUrl(value));
				// instance=browser;
				throw new IllegalStateException("We are working on new mechanism to view webpages inside the app,Please contact Jalal");

			} else {
				this.instance = new JKPanel();
			}
			return this.instance;
		} catch (final UIOPanelCreationException e) {
			System.err.println("Unable to create panel with properties " + prop);
			e.printStackTrace();
			throw e;
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
			return null;
		} finally {
			// System.out.println(new Date());
		}
	}

	/**
	 * @param className
	 * @return
	 */
	private UIPanel createPanelByClassName(final String className) {
		try {
			return (UIPanel) Class.forName(className).newInstance();
		} catch (final Exception e) {
			throw new RuntimeException("Unable to instaniate class " + className, e);
		}

	}

	/**
	 * @param value
	 * @param prop
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createPanelFromPanelFactory(final String value, final boolean createNew, final Properties prop) throws UIOPanelCreationException {
		try {
			final UIPanelFactory factory = (UIPanelFactory) Class.forName(value).newInstance();
			return factory.createPanel(prop, createNew);
		} catch (final Exception e) {
			throw new UIOPanelCreationException(e);
		}
	}

	/**
	 * @param value
	 * @return
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createReportPanel(final String value) throws UIOPanelCreationException {
		try {
			final JKReport report = JKReportManager.getReport(SwingUtility.getDefaultLocale() + "_" + value);
			final ReportUIPanel pnl = new ReportUIPanel(report);
			return pnl;
		} catch (final Exception e) {
			throw new UIOPanelCreationException(e);
		}

	}

	/**
	 * @param className
	 * @param tableMeta
	 * @return
	 * @throws JKDataAccessException
	 * @throws TableMetaNotFoundException
	 * @throws UIOPanelCreationException
	 */
	private UIPanel createSingleRecordPanel(final String className, final TableMeta tableMeta)
			throws TableMetaNotFoundException, JKDataAccessException, UIOPanelCreationException {
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
			final Integer id = (Integer) DaoUtil.exeuteSingleOutputQuery(tableMeta.getDataSource(), tableMeta.getReportSql());
			// if (record != null) {
			if (id != null) {
				pnl.getMasterPanel().handleFindRecord(id);
			} else {
				pnl.setMode(DynDaoMode.ADD);
			}
		} catch (final JKRecordNotFoundException e) {
			pnl.setMode(DynDaoMode.ADD);
		}
		return pnl;
	}

	private JKDataSource getDataSource() {
		return this.menuItem.getParentMenu().getParentModule().getDataSource();
	}
}
