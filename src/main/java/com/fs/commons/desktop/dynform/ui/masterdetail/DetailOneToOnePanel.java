/**
 * 
 */
package com.fs.commons.desktop.dynform.ui.masterdetail;

import java.awt.BorderLayout;
import java.util.ArrayList;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel;
import com.fs.commons.desktop.dynform.ui.DynDaoPanel.DynDaoMode;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionListener;
import com.fs.commons.desktop.swing.comp.panels.JKMainPanel;
import com.fs.commons.util.ExceptionUtil;

/**
 * @author u087
 * 
 */
public class DetailOneToOnePanel extends JKMainPanel implements DetailPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ForiegnKeyFieldMeta foriegnKeyFieldMeta;
	DynDaoPanel pnlDetail;
	private Object masterIdValue;

	/**
	 * @throws TableMetaNotFoundException
	 * @throws DaoException
	 * 
	 */
	public DetailOneToOnePanel(ForiegnKeyFieldMeta foriegnKeyFieldMeta) throws TableMetaNotFoundException, DaoException {
		this.foriegnKeyFieldMeta = foriegnKeyFieldMeta;
		this.foriegnKeyFieldMeta.setEnabled(false);
		pnlDetail = new DynDaoPanel(getDetailTableMeta());
		pnlDetail.setAllowClose(false);
		pnlDetail.setAllowClear(false);
		init();		
		setMasterIdValue(null);
	}

	/**
	 * 
	 */
	private void init() {
		addDynDaoActionListener(new DynDaoActionAdapter(){
			@Override
			public void afterAddRecord(Record record) {
				try {
					setMasterIdValue(masterIdValue);
					//change to find mode
				} catch (DaoException e) {
					ExceptionUtil.handleException(e);
				}
			}
			@Override
			public void afterDeleteRecord(Record record) {
				//without the following call , the dynPanel will lost the old master value
				try {
					setMasterIdValue(masterIdValue);
				} catch (DaoException e) {
					ExceptionUtil.handleException(e);
				}
			}
		});
		add(pnlDetail, BorderLayout.NORTH);
	}

	/**
	 * 
	 */
	public void setMasterIdValue(Object masterIdValue) throws DaoException {
		if(masterIdValue==null || masterIdValue.toString().trim().equals("")){
			this.masterIdValue=null;
			pnlDetail.resetComponents();
			setMode(DynDaoMode.ADD);
			setEnabled(false);
		}else{
			this.masterIdValue=masterIdValue;
			//we enable it , the internal enable disable fnctioanlity will manged locally inside that class
			//pnlDetail.setEnabled(true);
			setEnabled(true);
			try{				
				Record record= findByMasterId(masterIdValue);
				handleFind(record.getIdValue());
			}catch(RecordNotFoundException e){				
				setMode(DynDaoMode.ADD);
				pnlDetail.setComponentValue(foriegnKeyFieldMeta.getName(), masterIdValue);
			}			
		}
	}

	/**
	 * @param masterIdValue2
	 * @return 
	 * @throws DaoException 
	 */
	private Record findByMasterId(Object masterIdValue) throws RecordNotFoundException,DaoException {
		DynamicDao dao=new DynamicDao(getDetailTableMeta());
		ArrayList<Record> records=dao.findByFieldValue(foriegnKeyFieldMeta.getName(),masterIdValue);
		if(records.size()==0){
			throw new RecordNotFoundException();
		}
		//Guaranteed to be only one record because it is OneToOne relation , right?
		return records.get(0);
	}

	/**
	 * 
	 */
	public void setMode(DynDaoMode mode) {
		pnlDetail.setMode(mode);
	}

	/**
	 * 
	 */
	public void addDynDaoActionListener(DynDaoActionListener listener) {
		pnlDetail.addDynDaoActionListener(listener);

	}
	
	/**
	 * 
	 */
	public void handleFind(Object idValue) throws DaoException {
		pnlDetail.handleFindRecord(idValue);
		pnlDetail.setMode(DynDaoMode.VIEW);//TODO : check the purpose of this statement
	}

	/**
	 * 
	 */
	@Override
	public void resetComponents() throws DaoException {
		setMasterIdValue(null);
	}

	/**
	 * @return the pnlDetail
	 */
	public DynDaoPanel getDetailPanel() {
		return this.pnlDetail;
	}
	
	/**
	 * 
	 * @return
	 */
	private TableMeta getDetailTableMeta() {
		return foriegnKeyFieldMeta.getParentTable();
	}

	public void setShowButtons(boolean show) {
		pnlDetail.setShowButtons(show);
	}
	
}
