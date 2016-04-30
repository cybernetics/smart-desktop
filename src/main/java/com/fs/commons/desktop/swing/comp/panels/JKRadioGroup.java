package com.fs.commons.desktop.swing.comp.panels;


import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;

import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.IdValueRecord;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKRadioButton;
import com.fs.commons.util.ExceptionUtil;

public class JKRadioGroup extends JKPanel<Object> {
	private String sql;
	ArrayList<JKRadioButton> radios = new ArrayList<JKRadioButton>();
	private ButtonGroup group;
	private List<IdValueRecord> records;

	/**
	 * 
	 * @param meta
	 */
	public JKRadioGroup(TableMeta meta) {
		this(meta.getListSql(), meta.getCaption());
	}

	/**
	 * 
	 * @param sql
	 * @param string
	 */
	private JKRadioGroup(String sql, String title) {
		setSql(sql);
		setTitle(title);
	}
	
	
//	private JKRadioGroup(int[]ids, Object values[]) {
//		setRecords(IdValueRecord.createList(ids,values));
//	}



	public JKRadioGroup(Object[] values) {		
		setRecords(IdValueRecord.createList(values));
	}


	public JKRadioGroup(Object[] values, String title) {
		this(values);
		setTitle(title);
	}

	/**
	 * @param sql
	 *            the sql to set
	 * @throws DaoException
	 */
	private void setSql(String sql) {
		this.sql = sql;
		radios.clear();
		try {
			setRecords(DaoUtil.createRecordsFromSQL(sql));
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}

	}

	/**
	 * @param vector 
	 * 
	 */
	private void setRecords(List<IdValueRecord> records) {
		this.records=records;
		group = new ButtonGroup();
		for (IdValueRecord idValueRecord : records) {
			JKRadioButton button = new JKRadioButton(idValueRecord.getValue().toString());
			group.add(button);
			radios.add(button);
		}
		removeAll();
		init();
		repaint();
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new java.awt.GridLayout(1, radios.size()));
		for (JKRadioButton rd : radios) {
			add(rd);
		}
		if (radios.size() > 0) {
			radios.get(0).setSelected(true);
		}
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	public int getSelectedIndex() {
		for (int i = 0; i < radios.size(); i++) {
			JKRadioButton rd = radios.get(i);
			if (rd.isSelected()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Integer getValue() {
		int index = getSelectedIndex();
		if (index == -1) {
			return -1;
		}
		return records.get(index).getIdAsInteger();
	}

	@Override
	public void setValue(Object value) {
		if(value==null){
			value=-1;
		}
		for (int i = 0; i < records.size(); i++) {
			IdValueRecord idValueRecord = records.get(i);
			JKRadioButton radioButton = radios.get(i);
			if (idValueRecord.getIdAsInteger() == Integer.parseInt(value.toString())) {				
				radioButton.setSelected(true);
				break;
			}else{
				radioButton.setSelected(false);
			}
		}
	}

	/**
	 * @param listener
	 */
	public void addActionListener(ActionListener listener) {
		for (JKRadioButton btn : radios) {
			btn.addActionListener(listener);
		}
	}

	public void setTitle(String title) {
		setBorder(SwingUtility.createTitledBorder(title));
	}

	public int getItemsCount() {
		return radios.size();
	}

	public void setSelectedItem(int index) {
		radios.get(index).setSelected(true);
	}

	public Object getSelectedItem() {
		return records.get(getSelectedIndex()).getValue();
	}

	// @Override
	// public void requestFocus() {
	// SwingUtilities.invokeLater(new Runnable(){
	//
	// @Override
	// public void run() {
	// radios.get(getSelectedIndex()).requestFocus();
	// }
	// });
	//		
	// }
}
