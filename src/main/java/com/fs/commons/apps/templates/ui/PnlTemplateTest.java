/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.jk.exceptions.handler.ExceptionUtil;

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

	public PnlTemplateTest() throws JKDataAccessException {
		init();
		handleTemplateChanged();
	}

	// ////////////////////////////////////////////////////
	private void buildVariablesPanel() {
		this.pnlVariablesPanel.removeAll();
		if (this.template != null) {
			final ArrayList<TemplateVariable> variables = this.template.getVariables();
			this.pnlVariablesPanel.setLayout(new BoxLayout(this.pnlVariablesPanel, BoxLayout.Y_AXIS));
			this.variablesList = new ArrayList<JKTextField>();
			for (final TemplateVariable templateVariable : variables) {
				final JKTextField comp = new JKTextField(10);
				this.variablesList.add(comp);
				this.pnlVariablesPanel.add(new JKLabledComponent(templateVariable.getVar().getVarName(), comp));

			}
		}
		validate();
		repaint();
	}

	// ////////////////////////////////////////////////////
	protected void handleTemplateChanged() {
		final int templateId = this.cmbTemplates.getSelectedIdValueAsInteger();
		if (templateId != -1) {
			final TemplateFacade facade = new TemplateFacade();
			try {
				this.template = facade.findTemplate(templateId);
			} catch (final Exception e) {
				ExceptionUtil.handle(e);
			}
		} else {
			this.template = null;
		}
		buildVariablesPanel();
	}

	// ////////////////////////////////////////////////////
	protected void handleTest() {
		try {
			if (this.template != null) {
				// txtId.checkEmpty();
				final Object[] variableValues = new Object[this.variablesList.size()];
				for (int i = 0; i < this.variablesList.size(); i++) {
					final JKTextField txt = this.variablesList.get(i);
					variableValues[i] = txt.getValue();
				}
				final String compiled = TemplateManager.compile(this.template, variableValues);
				// int recordId = txtId.getTextAsInteger();
				JOptionPane.showMessageDialog(SwingUtility.getDefaultMainFrame(), compiled);
				// System.out.println(compiled);
			}
		} catch (final Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void init() {
		final JKPanel<?> container = new JKPanel<Object>();

		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		container.add(new JKLabledComponent("TEMPLATE", this.cmbTemplates));
		// add(new JKLabledComponent("RECORD_ID", txtId));
		container.add(this.pnlVariablesPanel);
		container.add(this.btnTest);

		add(container);
		container.setBorder(SwingUtility.createTitledBorder(""));
		this.pnlVariablesPanel.setBorder(SwingUtility.createTitledBorder("VARIABLES"));

		this.cmbTemplates.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleTemplateChanged();
			}
		});
		this.btnTest.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				handleTest();
			}
		});
	}

}
