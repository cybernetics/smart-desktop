package com.fs.commons.dao.dynamic.meta.generator;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class PnlTriggers extends JKPanel<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Collection<String> triggersNames;
	ArrayList<JKTextField> cmbTriggers = new ArrayList<JKTextField>();
	private JKPanel<?> pnlTriggers;
	
	JKButton btnAdd=new JKButton("add");
	JKButton btnSave=new JKButton("save");
	JKButton btnClose=new JKButton("close");

	/**
	 * 
	 * @param triggers
	 */
	public PnlTriggers(Collection<String> triggers) {
		this.triggersNames = triggers;
		init();
	}

	/**
	 * 
	 */
	private void init() {
		setLayout(new BorderLayout());
		add(getTriggersPanel(), BorderLayout.CENTER);
		add(getButtonsPanel(),BorderLayout.SOUTH);
		btnAdd.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			  handleAdd();	
			}
		});
		btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			  handleSave();	
			}
		});
		btnClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			  handleClose();	
			}
		});
	}

	/**
	 * 
	 */
	protected void handleAdd() {
		String triggerName= SwingUtility.showInputDialog("Enter trigger Name");
		addTrigger(triggerName);
		pnlTriggers.validate();
		pnlTriggers.repaint();
		SwingUtility.packWindow(this);
	}

	/**
	 * 
	 */
	protected void handleSave() {
		triggersNames.clear();
		for (int i = 0; i < cmbTriggers.size(); i++) {
			String triggerName=cmbTriggers.get(i).getText().trim();
			if(!triggerName.equals("")){
				triggersNames.add(triggerName);
			}
		}
		handleClose();
	}

	/**
	 * 
	 */
	protected void handleClose() {
		SwingUtility.closePanelDialog(this);
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getButtonsPanel() {
		JKPanel<?> pnlButtons=new JKPanel<Object>();
		pnlButtons.add(btnAdd);
		pnlButtons.add(btnSave);
		pnlButtons.add(btnClose);
		return pnlButtons;
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel<?> getTriggersPanel() {
		if (pnlTriggers == null) {
			pnlTriggers = new JKPanel<Object>();
			pnlTriggers.setLayout(new BoxLayout(pnlTriggers, BoxLayout.Y_AXIS));
			for (String triggersName : triggersNames) {
				addTrigger(triggersName);
			}
		}
		return pnlTriggers;
	}

	/**
	 * 
	 * @param name
	 */
	private void addTrigger(String name) {
		JKTextField txt=new JKTextField();
		txt.setText(name);
		cmbTriggers.add(txt);
		pnlTriggers.add(txt);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> triggers = new ArrayList<String>();
		PnlTriggers pnl=new PnlTriggers(triggers);
		SwingUtility.showPanelInDialog(pnl, "");
		System.out.println(triggers);
	}
}
