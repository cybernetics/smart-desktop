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
package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.RecordTraversePolicy;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.jk.exceptions.handler.ExceptionUtil;

public abstract class AbstractMasterDetail extends JKMainPanel {
	// /////////////////////////////////////////////////////////////////////////////////
	class DetailDaoAdpater extends DynDaoActionAdapter {

		private final DetailPanel pnlDetail;

		// ///////////////////////////////////////////////////////////////////////////////
		public DetailDaoAdpater(final DetailPanel pnlDetail) {
			this.pnlDetail = pnlDetail;
		}

		@Override
		public void afterAddRecord(final Record record) {
			if (this.pnlDetail instanceof DetailOneToOnePanel) {
				try {
					this.pnlDetail.handleFind(record.getIdValue());
					navigateNext();
				} catch (final JKDataAccessException e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	// //////////////////////////////////////////////////////////////////////////
	class MasterDynActionListener extends DynDaoActionAdapter {
		@Override
		public void afterAddRecord(final Record record) {
			handleAfterRecordDuplicated(record);

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						AbstractMasterDetail.this.pnlMaster.handleFindRecord(record.getIdValue());
						navigateNext();
					} catch (final Exception e) {
						ExceptionUtil.handle(e);
					}
				}
			});
		}

		@Override
		public void afterResetComponents() {
			resetDetailPanels();
			navigateToPanelAtIndex(0);
		}

		// ///////////////////////////////////////////////////////////////////////////////
		public void handleAfterRecordDuplicated(final Record newRecord) {
			// Make deep duplicate for the record
			if (AbstractMasterDetail.this.pnlMaster.getMode() == DynDaoMode.DUPLICATE) {
				final DynamicDao dao = AbstractMasterDetail.this.pnlMaster.getDao();
				try {
					dao.cloneDetails(AbstractMasterDetail.this.pnlMaster.getDuplicatedRecord().getIdValue(), newRecord.getIdValue());
				} catch (final JKDataAccessException e) {
					ExceptionUtil.handle(e);
				}
			}
		}

		// ///////////////////////////////////////////////////////////////////////////////
		private void handleFindInDetail(final Record masterRecord) {
			try {
				for (int i = 0; i < AbstractMasterDetail.this.detailPanels.size(); i++) {
					final DetailPanel pnlDetail = AbstractMasterDetail.this.detailPanels.get(i);
					pnlDetail.setMasterIdValue(masterRecord.getIdValue());
				}
			} catch (final JKDataAccessException e) {
				ExceptionUtil.handle(e);
			}
		}

		@Override
		public void onRecordFound(final Record masterRecord) {
			handleFindInDetail(masterRecord);
		}

		@Override
		public void onRecordNotFound(final Object recordId, final JKDataAccessException e) {
			resetDetailPanels();
		}

		// ///////////////////////////////////////////////////////////////////////////////
		private void resetDetailPanels() {
			for (int i = 0; i < AbstractMasterDetail.this.detailPanels.size(); i++) {
				final DetailPanel pnl = AbstractMasterDetail.this.detailPanels.get(i);
				try {
					pnl.setMasterIdValue(null);
				} catch (final JKDataAccessException e) {
					ExceptionUtil.handle(e);
				}
			}
		}
	}

	/**
	 *
	 */
	private static final long serialVersionUID = 698408470223965950L;

	// ///////////////////////////////////////////////////////////////////////////////
	private TableMeta tableMeta;

	private DynDaoPanel pnlMaster;

	ArrayList<DetailPanel> detailPanels = new ArrayList<DetailPanel>();

	// ///////////////////////////////////////////////////////////////////////////////
	public AbstractMasterDetail() {
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public AbstractMasterDetail(final TableMeta tableMeta) throws TableMetaNotFoundException, JKDataAccessException, UIOPanelCreationException {
		setTableMeta(tableMeta);
	}

	public abstract void addComponent(JComponent comp);

	// /////////////////////////////////////////////////////////////////
	public void addDetailDaoActionListener(final DynDaoActionListener listener) {
		for (int i = 0; i < this.detailPanels.size(); i++) {
			this.detailPanels.get(i).addDynDaoActionListener(listener);
		}
	}

	// /////////////////////////////////////////////////////////////////
	public void addMasterDaoActionListener(final DynDaoActionListener listener) {
		getMasterPanel().addDynDaoActionListener(listener);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void addPanelsToContainer() {
		addPanelToView(getTableMeta().getCaption(), getTableMeta().getIconName(), this.pnlMaster);
		for (int i = 0; i < this.detailPanels.size(); i++) {
			final TableMeta detailTable = getDetailFields().get(i).getParentTable();
			// try {
			// JKSecurityManager.checkAllowedPrivilige(detailTable.getPriviligeId());
			addPanelToView(detailTable.getCaption(), detailTable.getIconName(), (JKPanel) this.detailPanels.get(i));
			// } catch (NotAllowedOperationException e) {
			// // its safe to eat this exception
			// } catch (SecurityException e) {
			// ExceptionUtil.handle(e);
			// }
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract void addPanelToView(String title, String icon, JKPanel pnl);

	// ///////////////////////////////////////////////////////////////////////////////
	protected DetailPanel createDetailPanel(final ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws JKDataAccessException, UIOPanelCreationException {
		final DetailPanel pnlDetail = DetailPanelFactory.createDetailPanel(foriegnKeyFieldMeta);
		pnlDetail.addDynDaoActionListener(new DetailDaoAdpater(pnlDetail));
		return pnlDetail;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected DynDaoPanel createMasterPanel(final TableMeta tableMeta) throws JKDataAccessException {
		final DynDaoPanel dynDaoPanel = new DynDaoPanel(tableMeta);
		dynDaoPanel.addDynDaoActionListener(new MasterDynActionListener());
		dynDaoPanel.setAllowDuplicate(true);
		return dynDaoPanel;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract int getCurrentPanelIndex();

	// ///////////////////////////////////////////////////////////////////////////////
	protected ArrayList<ForiegnKeyFieldMeta> getDetailFields() {
		return this.tableMeta.getDetailFields();
	}

	public DetailPanel getDetailPanel(final String name) {
		for (final DetailPanel detialPanel : this.detailPanels) {
			if (detialPanel.getName().equals(name)) {
				return detialPanel;
			}
		}
		return null;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public int getIdFieldValueAsInteger() {
		return getMasterPanel().getIdValueAsInteger();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public DynDaoPanel getMasterPanel() {
		return this.pnlMaster;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public DynDaoMode getMode() {
		return getMasterPanel().getMode();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta() {
		return this.tableMeta;
	}

	/**
	 *
	 */
	public void handleClosePanel() {
		SwingUtility.closePanelWindow(this);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void handleFind(final Object masterId) throws JKDataAccessException {
		getMasterPanel().handleFind(masterId.toString(), true);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void init() throws JKDataAccessException, TableMetaNotFoundException, UIOPanelCreationException {
		initPanel();
		initUI();
		addPanelsToContainer();
		navigateToPanelAtIndex(0);
		setMode(DynDaoMode.ADD);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void initPanel() throws JKDataAccessException, UIOPanelCreationException {
		this.pnlMaster = createMasterPanel(this.tableMeta);
		for (int i = 0; i < getDetailFields().size(); i++) {
			final ForiegnKeyFieldMeta foriegnKeyFieldMeta = getDetailFields().get(i);
			final DetailPanel pnlDetail = createDetailPanel(foriegnKeyFieldMeta);
			pnlDetail.setName(foriegnKeyFieldMeta.getParentTable().getTableId());
			this.detailPanels.add(pnlDetail);
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract void initUI();

	// ///////////////////////////////////////////////////////////////////////////////
	public void navigateNext() {
		navigateToPanelAtIndex(getCurrentPanelIndex() + 1);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract void navigateToPanelAtIndex(int panelIndex);

	@Override
	public void requestFocus() {
		navigateToPanelAtIndex(0);
		getMasterPanel().requestFocusInWindow();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void setMode(final DynDaoMode mode) throws JKDataAccessException {
		this.pnlMaster.setMode(mode);
	}

	public void setRecordTraversePolicy(final RecordTraversePolicy traversePolicy) {
		this.pnlMaster.setTraversePolicy(traversePolicy);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void setTableMeta(final TableMeta tableMeta) throws TableMetaNotFoundException, JKDataAccessException, UIOPanelCreationException {
		this.tableMeta = tableMeta;
		// init();
	}
}
