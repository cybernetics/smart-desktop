package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

import com.fs.commons.application.ui.UIOPanelCreationException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.dynform.ui.masterdetail.DynMasterDetailCRUDLPanel;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.ExceptionUtil;
import com.fs.commons.util.GeneralUtility;

public class DaoComboWithManagePanel extends JKPanel<Object>  {

	private static final long serialVersionUID = 1L;

	private TableMeta tableMeta;

	private DaoComboBox combo;

	JKButton btnEdit = new JKButton("");
	
	public DaoComboWithManagePanel() {
	}

	/**
	 * @param tableMeta
	 * @throws DaoException
	 */
	public DaoComboWithManagePanel(TableMeta tableMeta) throws DaoException {
		this.tableMeta = tableMeta;
		combo = new DaoComboBox(tableMeta);
		init();
	}

	public DaoComboWithManagePanel(ForiegnKeyFieldMeta meta) throws DaoException {
		this(AbstractTableMetaFactory.getTableMeta(meta.getParentTable().getDataSource(), meta.getReferenceTable()));
		combo.setName(meta.getName());
	}

	/**
	 *
	 */
	private void init() {
		setLayout(new BorderLayout());
		add(combo, BorderLayout.CENTER);
		if (tableMeta.isAllowManage()) {
			add(btnEdit, BorderLayout.LINE_END);
		}
		btnEdit.setIcon(new ImageIcon(GeneralUtility.getIconURL("edit.png")));
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleEdit();
			}
		});
		btnEdit.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					handleEdit();
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnEdit.transferFocus();
				}
			}
		});
	}

	/**
	 * 
	 */
	protected void handleEdit() {
		try {
			JKPanel pnl = createManagePanel();
			SwingUtility.showPanelInDialog(pnl, tableMeta.getCaption());
			reloadData();
			combo.requestFocus();
			if (combo.getItemCount() > 0) {
				combo.setSelectedIndex(0);
			}
			SwingUtility.packWindow(this);
			btnEdit.transferFocus();
		} catch (TableMetaNotFoundException e) {
			ExceptionUtil.handleException(e);
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		} catch (UIOPanelCreationException e) {
			ExceptionUtil.handleException(e);
		}

	}

	/**
	 * @return
	 * @throws DaoException
	 * @throws UIOPanelCreationException
	 */
	private JKPanel createManagePanel() throws DaoException, UIOPanelCreationException {
		DynMasterDetailCRUDLPanel pnl=new DynMasterDetailCRUDLPanel(tableMeta);
		return pnl;
	}

	/**
	 * 
	 */
	public String getValue() {
		if (combo.getValue() != null) {
			return combo.getValue().toString();
		}
		return null;
	}

	public void setValue(Object value) {
		if (value != null) {
			combo.setValue(value.toString());
		} else {
			combo.setSelectedIndex(-1);
		}
	}

	public void reloadData() throws DaoException {
		combo.forceReload();
	}

	@Override
	public void setEnabled(boolean enabled) {
		combo.setEnabled(enabled);
		btnEdit.setVisible(enabled);
	}

	/**
	 * @return the combo
	 */
	public DaoComboBox getCombo() {
		return this.combo;
	}

	/**
	 * @param combo
	 *            the combo to set
	 */
	public void setCombo(DaoComboBox combo) {
		this.combo = combo;
	}

	/**
	 * @return
	 */
	public int getSelectedIndex() {
		return combo.getSelectedIndex();
	}

	@Override
	public void requestFocus() {
		combo.requestFocus();
	}

	@Override
	public void setDefaultValue(Object defaultValue) {
		combo.setDefaultValue(defaultValue);
	}
	
	@Override
	public Object getDefaultValue() {
		return combo.getDefaultValue();
	}
	
	@Override
	public void clear() {
		combo.clear();
	}
	
	@Override
	public void reset() {
		combo.reset();
	}

	public int getItemCount() {
		return combo.getItemCount();
	}

}
