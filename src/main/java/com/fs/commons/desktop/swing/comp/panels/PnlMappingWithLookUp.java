package com.fs.commons.desktop.swing.comp.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.fs.commons.application.ApplicationManager;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Field;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.ExceptionUtil;

/**
 * 
 * @author mkiswani
 *
 */
public abstract class PnlMappingWithLookUp extends PnlMappingFields {
	protected abstract String getLookupTableName() ;

	protected abstract String getTargetFildName() ;
	private TableMeta meta = AbstractTableMetaFactory.getTableMeta(getLookupTableName());
	private DaoComboBox cmd = new DaoComboBox(meta);
	private JKButton addBtn = new JKButton("ADD");
	public PnlMappingWithLookUp() throws DaoException{
		
	}
	
	@Override
	protected void addBtnsListeners() {
		SwingUtility.addActionListener(addAsNewBtn, this, "processAdd");
		SwingUtility.addActionListener(removeRecordBtn, this, "handleRemove");
		SwingUtility.addActionListener(autoMapBtn, this, "handleAutoMap");
		SwingUtility.addActionListener(mapBtn, this, "handleMap");
		SwingUtility.addActionListener(removeBtn, this, "handleRemoveMapping");
	}
	
	
	protected void processAdd() {
		SwingUtility.showPanelInDialog(getLookupPnl(), "PLEASE_SELECT"+meta.getCaption() , true);		
	}
	
	private void handleAddWithLookUp() {
		try {
			source.checkEmptySelection();
			cmd.checkEmpty();
			for (String id : source.getSelectedIds()) {
				Record sourceRecord = sourceDao.findRecord(id);
				Record newRecord = targetMeta.createEmptyRecord();
				Field field = newRecord.getField(getTargetMainFieldName());
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				field = newRecord.getField(MAPPING_FIELD_NAME);
				field.setValue(sourceRecord.getFieldValue(getSourceFieldName()));
				field = newRecord.getField(getTargetFildName());
				field.setValue(cmd.getSelectedIdValue());
				
				targetDao.insertRecord(newRecord);
			}
			
			target.reloadData();
			source.reloadData();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	private JKPanel getLookupPnl() {
		JKPanel panel = new JKPanel();
		panel.add(new JKLabledComponent("DEFALUT_"+meta.getField(getTargetFildName(),true).getName() ,cmd));
		panel.add(addBtn);
		addBtn.setIcon("db_add.png");
		addBtn.addActionListener(getAddWithLookupListener());
		return panel;
		
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	protected ActionListener getAddWithLookupListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleAddWithLookUp();
			}
		};
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) {
		ApplicationManager instance = ApplicationManager.getInstance();
		try {
			instance.init();
			SwingUtility.testPanel(new PnlMappingWithLookUp(){
				@Override
				protected String getLookupTableName() {
					return "conf_countries";
				}

				@Override
				protected String getTargetFildName() {
					return "country_id";
				}

				@Override
				protected String getSourceTableMetaName() {
					return "tmp_cities";
				}

				@Override
				protected String getTargetTableMetaName() {
					return "conf_cities";
				}

				@Override
				protected String getSourceFieldName() {
					return "city";
				}

				@Override
				protected String getTargetMainFieldName() {
					return "city_name";
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
