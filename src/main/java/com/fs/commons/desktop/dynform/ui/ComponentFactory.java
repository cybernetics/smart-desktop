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
package com.fs.commons.desktop.dynform.ui;

import java.sql.Types;
import java.util.Hashtable;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.DaoUtil;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.FSTypes;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta;
import com.fs.commons.dao.dynamic.meta.ForiegnKeyFieldMeta.ViewMode;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaNotFoundException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swing.comp.DaoComboWithManagePanel;
import com.fs.commons.desktop.swing.comp.DaoComponent;
import com.fs.commons.desktop.swing.comp.JKCheckBox;
import com.fs.commons.desktop.swing.comp.JKDate;
import com.fs.commons.desktop.swing.comp.JKPasswordField;
import com.fs.commons.desktop.swing.comp.JKTextArea;
import com.fs.commons.desktop.swing.comp.JKTextField;
import com.fs.commons.desktop.swing.comp.JKTime;
import com.fs.commons.desktop.swing.comp.documents.AlphaDocument;
import com.fs.commons.desktop.swing.comp.documents.FSTextDocument;
import com.fs.commons.desktop.swing.comp.documents.MarkDocument;
import com.fs.commons.desktop.swing.comp.panels.JKBlobPanel;
import com.fs.commons.desktop.swing.comp2.FSLookupText;
import com.fs.commons.desktop.swing.dao.DaoComboBox;

public class ComponentFactory {
	static Hashtable<FieldMeta, BindingComponent> componentsCache = new Hashtable();

	// //////////////////////////////////////////////////////////////////////////////////
	public static BindingComponent buildForeignKeyComponent(final ForiegnKeyFieldMeta meta) throws TableMetaNotFoundException, DaoException {
		final TableMeta masterTable = AbstractTableMetaFactory.getTableMeta(meta.getParentTable().getDataSource(), meta.getReferenceTable());
		BindingComponent comp = null;
		if (!meta.isVisible()) {
			final JKTextField txt = new JKTextField();
			comp = txt;
		} else {
			if (meta.getViewMode() == ViewMode.COMBO) {
				if (masterTable.isAllowManage()) {
					final DaoComboWithManagePanel combo = new DaoComboWithManagePanel(meta);
					// if (combo.getItemCount() > 20) {
					// System.err.println("DaoComboBox with name : " +
					// meta.getParentTable().getTableName() + "." +
					// meta.getName()
					// + " contains data with more than 20 items (" +
					// combo.getItemCount()
					// + "), it recommanded to use FSLookup or
					// FSFieldPanelWithFilter instead");
					// }
					comp = combo;
				} else {
					final DaoComboBox combo = new DaoComboBox(meta);
					// if (combo.getItemCount() > 20) {
					// System.err.println("DaoComboBox with name : " +
					// meta.getParentTable().getTableName() + "." +
					// meta.getName()
					// + " contains data with more than 20 items (" +
					// combo.getItemCount()
					// + "), it recommanded to use FSLookup or
					// FSFieldPanelWithFilter instead");
					// }
					comp = combo;
				}
			}
			if (meta.getViewMode() == ViewMode.DIALOG) {
				final FieldPanelWithFilter panelWithFilter = new FieldPanelWithFilter(masterTable);
				panelWithFilter.setAllowManage(masterTable.isAllowManage());
				comp = panelWithFilter;
			}
			if (meta.getViewMode() == ViewMode.LOOKUP) {
				final FSLookupText lookup = new FSLookupText();
				lookup.setTableMeta(masterTable);
				comp = lookup;
			}
		}
		return comp;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	private static Object calculateDefaultValue(final Object defaultValue) throws DaoException {
		if (defaultValue == null || defaultValue != null && defaultValue.equals("-1") || defaultValue.toString().trim().equals("")) {
			return null;
		}
		if (defaultValue.toString().toUpperCase().startsWith("SELECT")) {
			try {
				final Object obj = DaoUtil.exeuteSingleOutputQuery(defaultValue.toString());
				return obj;
			} catch (final RecordNotFoundException e) {
				return null;
			}
		}
		return defaultValue;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public static BindingComponent createComponent(final FieldMeta field, final boolean createNew) throws TableMetaNotFoundException, DaoException {
		BindingComponent component = componentsCache.get(field);
		if (createNew || componentsCache.get(field) == null) {
			if (field.getOptionsQuery() != null) {
				component = createOptionsComponent(field);
			} else if (field instanceof ForiegnKeyFieldMeta) {
				component = buildForeignKeyComponent((ForiegnKeyFieldMeta) field);
			} else {
				final int type = field.getType();
				switch (type) {
				case Types.NUMERIC:
				case Types.INTEGER:
					component = new JKTextField(new com.fs.commons.desktop.swing.comp.documents.NumberDocument(field.getMaxLength()),
							field.getWidth());
					break;
				case Types.FLOAT:
				case Types.REAL:
				case Types.DOUBLE:
				case Types.DECIMAL:
					component = new JKTextField(new com.fs.commons.desktop.swing.comp.documents.FloatDocument(field.getMaxLength()),
							field.getWidth());
					break;
				case Types.VARCHAR:
					component = new JKTextField(new FSTextDocument(field.getMaxLength()), field.getWidth());
					break;
				case Types.DATE:
					component = new JKDate();
					break;
				case Types.TIME:
					component = new JKTime(field.getName());
					break;
				case FSTypes.PASSWORD:
					component = new JKPasswordField(field.getMaxLength(), field.getWidth());
					break;
				case Types.BINARY:
				case Types.LONGVARBINARY:
					component = new JKBlobPanel(field.getCaption());
					break;
				case FSTypes.MARK:
					component = new JKTextField(new MarkDocument(), field.getWidth());
					break;
				case Types.CHAR:
					component = new JKTextField(new AlphaDocument(field.getMaxLength()), field.getWidth());
					break;
				case Types.LONGVARCHAR:
					final JKTextArea txt = new JKTextArea();
					// txt.setPreferredSize(new Dimension(400, 100));
					component = txt;
					break;
				case Types.BOOLEAN:
				case Types.BIT:
					component = new JKCheckBox("");
					break;
				case Types.TINYINT:
					if (field.getMaxLength() == 1) {
						component = new JKCheckBox("");
						break;
					}
				default:
					component = new JKTextField(field.getWidth());
				}
			}
			final Object defaultValue = calculateDefaultValue(field.getDefaultValue());
			if (defaultValue != null) {
				component.setDefaultValue(defaultValue);
			}
			if (component instanceof DaoComponent) {
				((DaoComponent) component).setDataSource(field.getParentTable().getDataSource());
			}
			if (componentsCache.size() < Integer.parseInt(System.getProperty("component-cache", "0"))) {
				componentsCache.put(field, component);
			}
		}
		component.setName(field.getName());
		return component;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	private static BindingComponent createOptionsComponent(final FieldMeta field) throws DaoException {
		final DaoComboBox cmb = new DaoComboBox(field.getOptionsQuery());
		return cmb;
	}
}
