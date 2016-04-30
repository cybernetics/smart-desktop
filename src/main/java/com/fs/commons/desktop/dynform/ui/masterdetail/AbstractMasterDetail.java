package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.RecordTraversePolicy;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.ExceptionUtil;

public abstract class AbstractMasterDetail extends JKMainPanel {
	// /////////////////////////////////////////////////////////////////////////////////
	class DetailDaoAdpater extends DynDaoActionAdapter {

		private final DetailPanel pnlDetail;

		// ///////////////////////////////////////////////////////////////////////////////
		public DetailDaoAdpater(DetailPanel pnlDetail) {
			this.pnlDetail = pnlDetail;
		}

		@Override
		public void afterAddRecord(Record record) {
			if (pnlDetail instanceof DetailOneToOnePanel) {
				try {
					pnlDetail.handleFind(record.getIdValue());
					navigateNext();
				} catch (DaoException e) {
					ExceptionUtil.handleException(e);
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
						pnlMaster.handleFindRecord(record.getIdValue());
						navigateNext();
					} catch (Exception e) {
						ExceptionUtil.handleException(e);
					}
				}
			});
		}

		// ///////////////////////////////////////////////////////////////////////////////
		public void handleAfterRecordDuplicated(Record newRecord) {
			// Make deep duplicate for the record
			if (pnlMaster.getMode() == DynDaoMode.DUPLICATE) {
				DynamicDao dao = pnlMaster.getDao();
				try {
					dao.cloneDetails(pnlMaster.getDuplicatedRecord().getIdValue(), newRecord.getIdValue());
				} catch (DaoException e) {
					ExceptionUtil.handleException(e);
				}
			}
		}

		@Override
		public void afterResetComponents() {
			resetDetailPanels();
			navigateToPanelAtIndex(0);
		}

		@Override
		public void onRecordFound(final Record masterRecord) {
			handleFindInDetail(masterRecord);
		}

		@Override
		public void onRecordNotFound(Object recordId, DaoException e) {
			resetDetailPanels();
		}

		// ///////////////////////////////////////////////////////////////////////////////
		private void handleFindInDetail(Record masterRecord) {
			try {
				for (int i = 0; i < detailPanels.size(); i++) {
					DetailPanel pnlDetail = detailPanels.get(i);
					pnlDetail.setMasterIdValue(masterRecord.getIdValue());
				}
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		}

		// ///////////////////////////////////////////////////////////////////////////////
		private void resetDetailPanels() {
			for (int i = 0; i < detailPanels.size(); i++) {
				DetailPanel pnl = (DetailPanel) detailPanels.get(i);
				try {
					pnl.setMasterIdValue(null);
				} catch (DaoException e) {
					ExceptionUtil.handleException(e);
				}
			}
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	private TableMeta tableMeta;

	private DynDaoPanel pnlMaster;

	ArrayList<DetailPanel> detailPanels = new ArrayList<DetailPanel>();

	// ///////////////////////////////////////////////////////////////////////////////
	public AbstractMasterDetail() {
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public AbstractMasterDetail(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		setTableMeta(tableMeta);
	}

	// /////////////////////////////////////////////////////////////////
	public void addDetailDaoActionListener(DynDaoActionListener listener) {
		for (int i = 0; i < detailPanels.size(); i++) {
			detailPanels.get(i).addDynDaoActionListener(listener);
		}
	}

	// /////////////////////////////////////////////////////////////////
	public void addMasterDaoActionListener(DynDaoActionListener listener) {
		getMasterPanel().addDynDaoActionListener(listener);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public int getIdFieldValueAsInteger() {
		return getMasterPanel().getIdValueAsInteger();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public DynDaoPanel getMasterPanel() {
		return pnlMaster;
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
	public void handleFind(Object masterId) throws DaoException {
		getMasterPanel().handleFind(masterId.toString(), true);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void navigateNext() {
		navigateToPanelAtIndex(getCurrentPanelIndex() + 1);
	}

	@Override
	public void requestFocus() {
		navigateToPanelAtIndex(0);
		getMasterPanel().requestFocusInWindow();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void setMode(DynDaoMode mode) throws DaoException {
		pnlMaster.setMode(mode);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void setTableMeta(TableMeta tableMeta) throws TableMetaNotFoundException, DaoException, UIOPanelCreationException {
		this.tableMeta = tableMeta;
		// init();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract void addPanelToView(String title, String icon, JKPanel pnl);

	// ///////////////////////////////////////////////////////////////////////////////
	protected DetailPanel createDetailPanel(ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws DaoException, UIOPanelCreationException {
		DetailPanel pnlDetail = DetailPanelFactory.createDetailPanel(foriegnKeyFieldMeta);
		pnlDetail.addDynDaoActionListener(new DetailDaoAdpater(pnlDetail));
		return pnlDetail;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected DynDaoPanel createMasterPanel(TableMeta tableMeta) throws DaoException {
		DynDaoPanel dynDaoPanel = new DynDaoPanel(tableMeta);
		dynDaoPanel.addDynDaoActionListener(new MasterDynActionListener());
		dynDaoPanel.setAllowDuplicate(true);
		return dynDaoPanel;
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract int getCurrentPanelIndex();

	// ///////////////////////////////////////////////////////////////////////////////
	protected ArrayList<ForiegnKeyFieldMeta> getDetailFields() {
		return tableMeta.getDetailFields();
	}

	// ///////////////////////////////////////////////////////////////////////////////
	public void init() throws DaoException, TableMetaNotFoundException, UIOPanelCreationException {
		initPanel();
		initUI();
		addPanelsToContainer();
		navigateToPanelAtIndex(0);
		setMode(DynDaoMode.ADD);
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void addPanelsToContainer() {
		addPanelToView(getTableMeta().getCaption(), getTableMeta().getIconName(), pnlMaster);
		for (int i = 0; i < detailPanels.size(); i++) {
			TableMeta detailTable = getDetailFields().get(i).getParentTable();
			// try {
			// SecurityManager.checkAllowedPrivilige(detailTable.getPriviligeId());
			addPanelToView(detailTable.getCaption(), detailTable.getIconName(), (JKPanel) detailPanels.get(i));
			// } catch (NotAllowedOperationException e) {
			// // its safe to eat this exception
			// } catch (SecurityException e) {
			// ExceptionUtil.handleException(e);
			// }
		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected void initPanel() throws DaoException, UIOPanelCreationException {
		pnlMaster = createMasterPanel(tableMeta);
		for (int i = 0; i < getDetailFields().size(); i++) {
			ForiegnKeyFieldMeta foriegnKeyFieldMeta = getDetailFields().get(i);
			DetailPanel pnlDetail = createDetailPanel(foriegnKeyFieldMeta);
			pnlDetail.setName(foriegnKeyFieldMeta.getParentTable().getTableId());
			detailPanels.add(pnlDetail);
		}
	}
	
	public DetailPanel getDetailPanel(String name){
		for(DetailPanel detialPanel:detailPanels){
			if(detialPanel.getName().equals(name)){
				return detialPanel;
			}
		}
		return null;		
	}

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract void initUI();

	// ///////////////////////////////////////////////////////////////////////////////
	protected abstract void navigateToPanelAtIndex(int panelIndex);

	public abstract void addComponent(JComponent comp);

	public void setRecordTraversePolicy(RecordTraversePolicy traversePolicy) {
		pnlMaster.setTraversePolicy(traversePolicy);
	}
}
