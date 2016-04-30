package com.fs.commons.desktop.swing.comp.panels;

import java.awt.GridLayout;
import java.util.ArrayList;

import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.dao.QueryJTable;
import com.fs.commons.util.ExceptionUtil;

/**
 * 
 * @author Mohamed Kiswani
 *
 */
public abstract class PnlMappingFields extends JKPanel{
	
	public static final String MAPPING_FIELD_NAME = "mapping_name";
	////////////////////////////////////////////////////////////////////////////////////
	protected abstract String getSourceTableMetaName() ;
	protected abstract String getTargetTableMetaName() ;
	protected abstract String getSourceFieldName() ;
	protected abstract String getTargetMainFieldName() ;
	
	////////////////////////////////////////////////////////////////////////////////////
	protected QueryJTable source = null ; 
	protected QueryJTable target = null;
	
	protected JKButton addAsNewBtn = new JKButton("ADD_AS_NEW");
	protected JKButton removeRecordBtn = new JKButton("REMOVE");
	protected JKButton autoMapBtn= new JKButton("AUTO_MAP");
	protected JKButton mapBtn = new JKButton("MAP");
	protected JKButton removeBtn = new JKButton("REMOVE_MAPPING");
	
	protected TableMeta sourceMeta;
	protected TableMeta targetMeta;
	
	protected DynamicDao sourceDao;
	protected DynamicDao targetDao;
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public PnlMappingFields(){
		sourceMeta = AbstractTableMetaFactory.getTableMeta(getSourceTableMetaName());
		targetMeta = AbstractTableMetaFactory.getTableMeta(getTargetTableMetaName());
		source = new QueryJTable(sourceMeta,"EXCEL_TABLE", false);
		sourceDao = new DynamicDao(sourceMeta);
		targetDao = new DynamicDao(targetMeta);
		target = new QueryJTable(targetMeta,"FS_TABLE", false);
		init();
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected final void init() {
		source.setPreferredSize(450,250);
		target.setPreferredSize(450,250);
		JKPanel continer = new JKPanel();
		continer.add(getCenterPnl());
		add(continer);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected final JKPanel getCenterPnl() {
		JKPanel pnl = new JKPanel();
		source.setPreferredSize(370,300);
		target.setPreferredSize(370,300);
		pnl.add(source);
		pnl.add(getBtnsPnl());
		pnl.add(target);
		return pnl;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected final JKPanel getBtnsPnl() {
		addBtnsListeners();
		JKPanel container = new JKPanel();
		JKPanel pnl = new JKPanel(); 
		pnl.setLayout(new GridLayout(5,1));
		pnl.add(addAsNewBtn);
		addAsNewBtn.setIcon("db_add.png");
//		pnl.add(removeRecordBtn);
		pnl.add(autoMapBtn);
		autoMapBtn.setIcon("automatic_map.png");
		pnl.add(mapBtn);
		mapBtn.setIcon("map.png");
		pnl.add(removeBtn);
		removeBtn.setIcon("remove_mapping.png");
		container.add(pnl);
		return container;
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void addBtnsListeners() {
		SwingUtility.addActionListener(addAsNewBtn, this, "handleAdd");
		SwingUtility.addActionListener(removeRecordBtn, this, "handleRemove");
		SwingUtility.addActionListener(autoMapBtn, this, "handleAutoMap");
		SwingUtility.addActionListener(mapBtn, this, "handleMap");
		SwingUtility.addActionListener(removeBtn, this, "handleRemoveMapping");
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//this method is called by reflection by SwingUtility.addActionListener  
	protected void handleAdd() {
		try {
			source.checkEmptySelection();
			for (String id : source.getSelectedIds()) {
				Record sourceRecord = sourceDao.findRecord(id);
				Record newRecord = targetMeta.createEmptyRecord();
				Field field = newRecord.getField(getTargetMainFieldName());
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				field = newRecord.getField(MAPPING_FIELD_NAME);
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				setAdditionalValues(sourceRecord , newRecord);
				targetDao.insertRecord(newRecord);
			}
			
			target.reloadData();
			source.reloadData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}

	}
	
	/**
	 * 
	 * @param newRecord 
	 * @param newRecord2 
	 * @return
	 */
	protected void setAdditionalValues(Record source, Record target) {
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//this method is called by reflection by SwingUtility.addActionListener
	protected void handleRemove() {
		try {
			target.checkEmptySelection();
			for (String id : target.getSelectedIds()) {
				targetDao.deleteRecord(id);
			}
			target.reloadData();
		source.reloadData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//this method is called by reflection by SwingUtility.addActionListener
	protected void handleAutoMap() {
		try {
			ArrayList<Record> sourceRecords = sourceDao.lstRecordsByReportSql();
			for (Record record : sourceRecords) {
				Object sourceFieldValue = record.getFieldValue(getSourceFieldName());
				ArrayList<Record> targetRecords = targetDao.findByFieldValue(getTargetMainFieldName(), sourceFieldValue);
				for (Record targetRecord : targetRecords) {
					targetRecord.getField(MAPPING_FIELD_NAME).setValue(record.getFieldValue(getSourceFieldName()));
					targetDao.updateRecord(targetRecord);
				}
			}
			target.reloadData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//this method is called by reflection by SwingUtility.addActionListener
	protected void handleMap() {
		try {
			source.checkEmptySelection();
			target.checkEmptySelection();
			Record sourceRecord = sourceDao.findRecord(source.getSelectedIdAsInteger());
			Record targetRecord = targetDao.findRecord(target.getSelectedIdAsInteger());
			targetRecord.getField(MAPPING_FIELD_NAME).setValue(sourceRecord.getFieldValue(getSourceFieldName()));
			targetDao.updateRecord(targetRecord);
			target.reloadData();
			source.reloadData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	//this method is called by reflection by SwingUtility.addActionListener
	protected void handleRemoveMapping() {
		try {
			for (String id : target.getSelectedIds()) {
				Record targetRecord = targetDao.findRecord(id);
				targetRecord.getField(MAPPING_FIELD_NAME).setValue("");
				targetDao.updateRecord(targetRecord);
			}
			target.reloadData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////
	
}
