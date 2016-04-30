package com.fs.notification.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.ExceptionUtil;
import com.fs.notification.facade.NotificationFacade;

public class PnlEventGenerator extends JKPanel{
	DaoComboBox cmbTask=new DaoComboBox(AbstractTableMetaFactory.getTableMeta("not_event_generation_task"));
	JKButton btnGenerateEvent=new JKButton("GENERATE_EVENTS");
	
	/**
	 * @throws DaoException
	 */
	public PnlEventGenerator() throws DaoException{
		init();
	}

	private void init() {
		JKPanel container=new JKPanel();
		container.setLayout(new BorderLayout());
		container.add(getInfoPanel(),BorderLayout.CENTER);
		container.add(getButtonsPanel(),BorderLayout.SOUTH);
		add(container);		
		btnGenerateEvent.setIcon("gen_event.png");
		btnGenerateEvent.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				handleGenerate();
			}
		});
	}

	/**
	 * 
	 */
	protected void handleGenerate() {
		try {
			cmbTask.checkEmpty();
			NotificationFacade facade=new NotificationFacade();
			facade.generateEvent(cmbTask.getSelectedIdValueAsInteger());
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private Component getButtonsPanel() {
		JKPanel pnl=new JKPanel();
		pnl.add(btnGenerateEvent);
		btnGenerateEvent.setShowProgress(true);
		return pnl;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getInfoPanel() {
		JKPanel pnl=new JKPanel();
		pnl.add(new JKLabledComponent("TASK_GENERATOR", cmbTask));
		return pnl;
	}
	
	public static void main(String[] args) throws FileNotFoundException, ApplicationException, DaoException {
		ApplicationManager.getInstance().init();
		SwingUtility.testPanel(new PnlEventGenerator());
	}
}
