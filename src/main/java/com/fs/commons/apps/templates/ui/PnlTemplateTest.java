package com.fs.commons.apps.templates.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import com.fs.commons.apps.templates.TemplateManager;
import com.fs.commons.apps.templates.beans.Template;
import com.fs.commons.apps.templates.beans.TemplateVariable;
import com.fs.commons.apps.templates.facade.TemplateFacade;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.util.ExceptionUtil;

public class PnlTemplateTest extends JKPanel<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DaoComboBox cmbTemplates = new DaoComboBox(AbstractTableMetaFactory.getTableMeta("conf_templates"));
	// JKTextField txtId=new JKTextField(new NumberDocument(5),20);
	JKPanel<?> pnlVariablesPanel = new JKPanel<Object>();
	JKButton btnTest = new JKButton("TEST_TEMPLATE");
	private Template template;
	private ArrayList<JKTextField> variablesList;

	public PnlTemplateTest() throws DaoException {
		init();
		handleTemplateChanged();
	}

	private void init() {
		JKPanel<?> container = new JKPanel<Object>();
		
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(new JKLabledComponent("TEMPLATE", cmbTemplates));
		// add(new JKLabledComponent("RECORD_ID", txtId));
		container.add(pnlVariablesPanel);
		container.add(btnTest);

		add(container);
		container.setBorder(SwingUtility.createTitledBorder(""));
		pnlVariablesPanel.setBorder(SwingUtility.createTitledBorder("VARIABLES"));
		
		cmbTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleTemplateChanged();
			}
		});
		btnTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				handleTest();
			}
		});
	}

	// ////////////////////////////////////////////////////
	protected void handleTemplateChanged() {
		int templateId = cmbTemplates.getSelectedIdValueAsInteger();
		if (templateId != -1) {
			TemplateFacade facade = new TemplateFacade();
			try {
				template = facade.findTemplate(templateId);
			} catch (Exception e) {
				ExceptionUtil.handleException(e);
			}
		} else {
			template = null;
		}
		buildVariablesPanel();
	}

	// ////////////////////////////////////////////////////
	private void buildVariablesPanel() {
		pnlVariablesPanel.removeAll();
		if (template != null) {
			ArrayList<TemplateVariable> variables = template.getVariables();
			pnlVariablesPanel.setLayout(new BoxLayout(pnlVariablesPanel, BoxLayout.Y_AXIS));
			variablesList = new ArrayList<JKTextField>();
			for (TemplateVariable templateVariable : variables) {
				JKTextField comp = new JKTextField(10);
				variablesList.add(comp);
				pnlVariablesPanel.add(new JKLabledComponent(templateVariable.getVar().getVarName(), comp));

			}
		}
		validate();
		repaint();
	}

	// ////////////////////////////////////////////////////
	protected void handleTest() {
		try {
			if (template != null) {
				// txtId.checkEmpty();
				Object[] variableValues=new Object[variablesList.size()];
				for(int i=0;i<variablesList.size();i++){
					JKTextField txt=variablesList.get(i);
					variableValues[i]=txt.getValue();
				}
				String compiled = TemplateManager.compile(template, variableValues);
				// int recordId = txtId.getTextAsInteger();
				JOptionPane.showMessageDialog(SwingUtility.getDefaultMainFrame(), compiled);
				// System.out.println(compiled);
			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
