/**
 * 
 */
package com.fs.commons.desktop.dynform.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.dynform.ui.action.DynDaoActionAdapter;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.SwingValidator;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dialogs.QueryDialog;
import com.fs.commons.logging.Logger;
import com.fs.commons.util.ExceptionUtil;

/**
 * @author u087
 */
public class FieldPanelWithFilter extends JKPanel<Object> implements DaoComponent, ManageSupport {
	private static final long serialVersionUID = 1L;
	Record record;
	JKTextField txtView = new JKTextField(20);
	JKButton btnSelect = new JKButton("SELECT", "F12");
	JKButton btnAdd = new JKButton("ADD");
	private  TableMeta tableMeta;
	private Object defaultValue;
	private DataSource manager;
	private boolean allowManage;
	
	public FieldPanelWithFilter() {
	}

	// ///////////////////////////////////////////////////////////////
	public FieldPanelWithFilter(String tableMeta) {
		this(AbstractTableMetaFactory.getTableMeta(tableMeta));
	}

	// ///////////////////////////////////////////////////////////////
	public FieldPanelWithFilter(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		init();
		setAllowManage(false);
	}

	// ///////////////////////////////////////////////////////////////
	public void setAllowManage(boolean allowManage) {
		this.allowManage = allowManage;
		btnAdd.setVisible(allowManage);
	}

	public boolean isAllowManage() {
		btnAdd.setVisible(allowManage);
		return allowManage;
	}

	// ///////////////////////////////////////////////////////////////
	private void init() {
		setLayout(new BorderLayout());
		add(txtView, BorderLayout.CENTER);

		JKPanel pnlButtons = new JKPanel();
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnSelect);
		add(pnlButtons, BorderLayout.LINE_END);

		txtView.setEditable(false);
		btnSelect.setIcon("find_commons_mod_icon.gif");
		// btnSelect.setShortcut("F12", "f12");
		btnAdd.setIcon("edit.png");
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				handleSelect();
			}
		});
		btnSelect.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == e.VK_ENTER) {
					btnSelect.transferFocus();
				}
				if (e.getKeyCode() == e.VK_SPACE) {
					handleSelect();
				}
			}

		});
		txtView.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// if(e.getKeyCode()==e.VK_ENTER){
				// btnSelect.doClick();
				// }
				if (e.getKeyCode() == KeyEvent.VK_DELETE) {
					setValue(null);
				}
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleEdit();
			}
		});

	}

	// //////////////////////////////////////////////////////////////////////
	protected void handleEdit() {
		try {
			DynMasterDetailCRUDLPanel pnl = new DynMasterDetailCRUDLPanel(tableMeta);
			pnl.addMasterDaoActionListener(new DynDaoActionAdapter() {
				@Override
				public void afterAddRecord(Record record) throws DaoException {
					setValue(record.getIdValue());
				}
			});
			SwingUtility.showPanelInDialog(pnl, tableMeta.getTableName());
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	// //////////////////////////////////////////////////////////////////////
	private void handleSelect() {
		Object id = QueryDialog.showQueryDialog(tableMeta);
		if (id != null) {
			setValue(id);
		}
		SwingUtility.pressKey(KeyEvent.VK_TAB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		btnSelect.setVisible(enabled);
		btnSelect.setEnabled(enabled);
		btnAdd.setVisible(enabled && isAllowManage());
		txtView.setEnabled(enabled);

	}

	/**
	 * 
	 * @throws ValidationException
	 */
	public void checkEmpty() throws ValidationException {
		SwingValidator.checkEmpty(this);
	}

	@Override
	public void requestFocus() {
		if (btnSelect.isVisible() && btnSelect.isEnabled()) {
			btnSelect.requestFocus();
			// btnSelect.doClick();
		} else {
			super.requestFocus();
		}
	}

	@Override
	public void reset() {
		setValue(defaultValue);
	}

	@Override
	public void clear() {
		record = null;
		txtView.clear();
	}

	// ///////////////////////////////////////////////////////////////
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	// ///////////////////////////////////////////////////////////////
	public Object getValue() {
		if (record != null) {
			return record.getIdValue();
		}
		return null;
	}

	@Override
	public void setValue(Object value) {
		Object oldValue = getValue();
		if (value != null && !value.toString().trim().equals("") && !value.toString().toLowerCase().equals("null")) {
			try {
				DynamicDao dao = new DynamicDao(tableMeta);
				record = dao.findRecord(value);
				txtView.setValue(record.getSummaryValue());
			} catch (RecordNotFoundException e) {
				Logger.fatal("Record not found for field :" + tableMeta.getTableName() + " for id : " + value);
				setValue(null);// recursion
			} catch (DaoException e) {
				ExceptionUtil.handleException(e);
			}
		} else {
			clear();
		}
		Object newValue = getValue();
		fsWrapper.fireValueChangeListener(oldValue, newValue);
	}

	@Override
	public DataSource getDataSource() {
		return manager;
	}

	@Override
	public void setDataSource(DataSource manager) {
		this.manager = manager;
	}

	public int getValueAsInteger() {
		if (record == null) {
			return -1;
		}
		return record.getIdValueAsInteger();
	}
}
