package com.fs.commons.application.config;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;

public class CommonsConfigFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFileChooser chooser = SwingUtility.getDefaultFileChooser();
	JKButton btnLoad = new JKButton("Load");
	JKButton btnNew = new JKButton("New Config File");
	JKButton btnExit = new JKButton("Exit");
	JKPanel mainPanel;

	/**
	 * 
	 */
	public CommonsConfigFrame() {
		init();
	}

	/**
	 * 
	 */
	private void init() {		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		add(getButtonsPanel(), BorderLayout.SOUTH);
		packWindow();
		btnNew.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleNew();
			}
		});
		btnLoad.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleLoad();
			}
		});
		btnExit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				handleExit();
			}
		});
	}

	/**
	 * 
	 */
	private void packWindow() {
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * 
	 */
	protected void handleExit() {
		System.exit(0);
	}

	/**
	 * 
	 */
	protected void handleLoad() {
		int choice=chooser.showOpenDialog(this);
		if(choice==JFileChooser.APPROVE_OPTION){
			 File file=chooser.getSelectedFile();
			 if(file.isFile()){
				 try {
					CommonsConfigManager configManager=new CommonsConfigManager(file.getAbsolutePath());
					CommonsConfigPanel configPanel=new CommonsConfigPanel(configManager);
					showConfigPanel(configPanel);
				} catch (IOException e) {
					SwingUtility.showErrorDialog(e.getMessage(), e);
				}
			 }else{
				 SwingUtility.showUserErrorDialog("Only normal files are allowed");
			 }
		}
	}

	/**
	 * 
	 * @param configPanel
	 */
	private void showConfigPanel(CommonsConfigPanel configPanel) {
		if(this.mainPanel!=null){
			remove(mainPanel);
		}
		this.mainPanel=configPanel;
		add(configPanel,BorderLayout.CENTER);
		validate();
		repaint();
		packWindow();		
	}

	/**
	 * 
	 */
	protected void handleNew() {		
		CommonsConfigManager configManager=new CommonsConfigManager();
		CommonsConfigPanel configPanel=new CommonsConfigPanel(configManager);
		showConfigPanel(configPanel);		
	}

	/**
	 * 
	 * @return
	 */
	private JKPanel getButtonsPanel() {
		JKPanel pnl = new JKPanel();
		pnl.add(btnNew);
		pnl.add(btnLoad);
		pnl.add(btnExit);
		return pnl;
	}
}
