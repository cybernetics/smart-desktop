package com.fs.commons.application.config;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class CommonsConfigPanel extends JKPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// key : property name
	Hashtable<String, BindingComponent<String>> components = new Hashtable<String, BindingComponent<String>>();
	private final CommonsConfigManager configManager;

	JKButton btnSave = new JKButton("Save");
	JKButton btnReload = new JKButton("Reload");
	JKButton btnAddProperty=new JKButton("Add Property");
	JFileChooser chooser=SwingUtility.getDefaultFileChooser();
	private JKPanel mainPanel;

	/**
	 * 
	 */
	public CommonsConfigPanel(CommonsConfigManager configManager) {
		this.configManager = configManager;
		init();
		populateData();
	}

	/**
	 * 
	 */
	private void init() {
		setBorder(BorderFactory.createRaisedBevelBorder());
		JKPanel containerPanel = new JKPanel();
		containerPanel.setBorder(SwingUtility.createTitledBorder("Config Properties"));
		containerPanel.setLayout(new BorderLayout());
		mainPanel = new JKPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		Properties prop = configManager.getProperties();
		Enumeration keys = prop.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			prop.getProperty(key);
			buildPropertyPanel(mainPanel, key);
		}
		containerPanel.add(mainPanel, BorderLayout.CENTER);
		containerPanel.add(getButtonsPanel(), BorderLayout.SOUTH);

		add(containerPanel);

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});
		btnReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateData();
			}
		});
		btnAddProperty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleAddProperyt();
			}
		});
	}

	/**
	 * 
	 */
	protected void handleAddProperyt() {		
		String property=SwingUtility.showInputDialog("Enter new property name");
		if(property!=null && !property.equals("")){
			buildPropertyPanel(mainPanel, property);
			SwingUtility.packJFrameWindow(this);
		}
	}

	/**
	 * 
	 * @param mainPanel
	 * @param key
	 * @return
	 */
	private JKTextField buildPropertyPanel(JKPanel mainPanel, String key) {
		JKPanel pnl = new JKPanel();

		final JKTextField txt = new JKTextField(20);
		JKButton btnEncode = new JKButton("Encode");
		JKButton btnDecode = new JKButton("Decode");

		pnl.add(new JKLabledComponent(key, txt));
		pnl.add(btnEncode);
		pnl.add(btnDecode);
		mainPanel.add(pnl);
		mainPanel.add(Box.createVerticalStrut(3));
		components.put(key, txt);		
		btnEncode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = CommonsConfigManager.encode(txt.getText());
				txt.setText(result);
			}
		});

		btnDecode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String result = CommonsConfigManager.decode(txt.getText());
				txt.setText(result);
			}
		});
		return txt;
	}

	/**
	 * 
	 */
	protected void populateData() {
		Enumeration keys = components.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String originalValue = configManager.getProperty(key, "");
			components.get(key).setValue(originalValue);
		}
	}

	/**
	 * 
	 */
	protected void handleSave() {
		Enumeration keys = components.keys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value=components.get(key).getValue();
			configManager.setProperty(key, value);			
		}
		try {
			int choice=chooser.showSaveDialog(null);
			if(choice==JFileChooser.APPROVE_OPTION){
				String fileName=chooser.getSelectedFile().getAbsolutePath();
				configManager.storeToXML(fileName);
				SwingUtility.showSuccessDialog("File has been saved succesfully");				
			}
		} catch (Exception e) {			
			SwingUtility.showUserErrorDialog("Error saving file:"+e.getMessage(),e);
		}
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		JKPanel panel = new JKPanel();
		panel.add(btnSave);
		panel.add(btnReload);
		panel.add(btnAddProperty);
		return panel;
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	// private JKPanel createComponent(String key, String value) {
	// JKPanel pnl=new JKPanel();
	// pnl.add(new JKLabledComponent(key,new JKTextField(20)));
	// return pnl;
	// }
}
