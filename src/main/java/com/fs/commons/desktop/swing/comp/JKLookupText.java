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
package com.fs.commons.desktop.swing.comp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.fs.commons.bean.binding.BindingComponent;
import com.fs.commons.dao.AbstractDao;
import com.fs.commons.dao.DaoFinder;
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.DaoFactory;
import com.fs.commons.dao.dynamic.DynamicDao;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.Record;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.dao.exception.RecordNotFoundException;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.dialogs.QueryDialog;
import com.fs.commons.desktop.validation.builtin.FSValidators;
import com.fs.commons.locale.Lables;
import com.fs.commons.util.ExceptionUtil;

public class JKLookupText extends JKPanel implements BindingComponent, DaoComponent {
	/**
	 *
	 */
	private static final long serialVersionUID = 4727657760094348803L;
	private static final int DEFAULT_HIEGHT = 25;
	// private FSAbstractComponent fsWrapper = new FSAbstractComponent(this);
	private String query;
	JKTextField txtNum = new JKTextField();
	JKTextField txtName = new JKTextField();
	private DataSource datasource;
	private Object defaultValue;
	// String tableName;
	// String idFieldName;
	// String numberFieldName;
	// String textFieldName;
	private Object recordId;
	private TableMeta tableMeta;
	private FieldMeta idField;
	private FieldMeta numberField;
	private Vector<FieldMeta> summaryFields = new Vector<FieldMeta>();
	private boolean required;

	// ////////////////////////////////////////////////////////////
	public JKLookupText() {
		this(null);
	}

	// ////////////////////////////////////////////////////////////
	public JKLookupText(final String query) {
		setQuery(query);
		init();
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void clear() {
		setValue(null);
	}

	// ////////////////////////////////////////////////////////////
	private void clearFields(final boolean clearNumber) {
		this.recordId = null;
		if (clearNumber) {
			this.txtNum.setText("");
		}
		this.txtName.setText("");
	}

	// //////////////////////////////////////////////////////////////////////////////////
	private DynamicDao getDao() {
		return DaoFactory.createDynamicDao(getTableMeta());
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public DataSource getDataSource() {
		return this.datasource;
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public Object getDefaultValue() {
		return this.defaultValue;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public Object getFieldValue(final String fieldName) throws RecordNotFoundException, DaoException {
		if (getValue() == null) {
			return null;
		}
		final DynamicDao dao = getDao();
		final Record record = dao.findRecord(getValue());
		return record.getFieldValue(fieldName);
	}

	/**
	 *
	 * @return
	 */
	public int getIdAsInteger() {
		if (this.recordId == null) {
			return -1;
		}
		return new Integer(this.recordId.toString());
	}

	public String getIdFieldName() {
		return this.idField.getName();
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public int getNameWidth() {
		return this.txtName.getPreferredSize().width;
	}

	// /////////////////////////////////////////////////////////////////
	public String getNumberFieldName() {
		return this.numberField.getName();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public Object getNumberValue() {
		return this.txtNum.getValue();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public Object getNumberValueAsInteger() {
		return this.txtNum.getTextAsInteger();
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public int getNumberWidth() {
		return this.txtNum.getPreferredSize().width;
	}

	// ////////////////////////////////////////////////////////////
	public String getQuery() {
		if (this.query != null && !this.query.trim().equals("")) {
			return this.query;
		}
		String query = "SELECT ";
		query += getIdFieldName() + ",";
		query += getNumberFieldName() + ",";
		query += getTextFieldName() + "\n";
		query += " FROM " + getTableName();
		query += " WHERE 1=1";
		return query;
	}

	// // ////////////////////////////////////////////////////////////
	// @Override
	// public void addValidator(Validator validator) {
	// fsWrapper.addValidator(validator);
	// }

	// // ////////////////////////////////////////////////////////////
	// @Override
	// public void validateValue() throws ValidationException {
	// fsWrapper.validateValue();
	// }

	// ///////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta() {
		if (this.tableMeta == null) {
			throw new IllegalStateException("Please set tableMeta on FSLookUpText");
		}

		return this.tableMeta;
	}

	public String getTableName() {
		return getTableMeta().getTableName();
	}

	// /////////////////////////////////////////////////////////////////
	public String getTextFieldName() {
		final StringBuffer buf = new StringBuffer();
		int counter = 0;
		for (final FieldMeta field : this.summaryFields) {
			if (counter++ != 0) {
				buf.append(",");
			}
			buf.append(field.getName());
		}
		return buf.toString();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public String getTextValue() {
		return this.txtName.getText();
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public Object getValue() {
		return this.recordId;
	}

	/**
	 *
	 */
	protected void handleFindValue() {
		final Object id = QueryDialog.showQueryDialog(this.tableMeta.getShortReportSql(), "SEARCH", 0);
		if (id != null) {
			setValue(id.toString());
		}
	}

	// ////////////////////////////////////////////////////////////
	private void handleIdChanged() {
		if (this.recordId == null || this.recordId.toString().trim().equals("")) {
			clearFields(true);
			return;
		}
		String query = getQuery();
		query += " AND " + getIdFieldName() + "=" + this.recordId;
		try {
			loadQuery(query);
		} catch (final RecordNotFoundException e) {
			clearFields(true);
		} catch (final DaoException e) {
			System.err.println("Error while trying  to extecute: " + query);
			ExceptionUtil.handleException(e);
		}
	}

	// /////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////
	private void handleNumberChanged() {
		final Object old = getValue();
		final String number = this.txtNum.getText();
		if (number.trim().equals("")) {
			clearFields(true);
			return;
		}
		String query = getQuery();
		query += " AND " + getNumberFieldName() + "='" + number + "'";
		try {
			loadQuery(query);
		} catch (final RecordNotFoundException e) {
			clearFields(false);
			this.requestFocus();
			this.txtName.setText(Lables.get("N/A"));
		} catch (final DaoException e) {
			ExceptionUtil.handleException(e);
		}
		this.fsWrapper.fireValueChangeListener(old, getValue());
	}

	// /////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////
	private void init() {
		// setPreferredSize(FSSwingConstants.DEFAULT_TEXT_SIZE);
		setLayout(new BorderLayout());
		this.txtNum.setPreferredSize(new Dimension(50, DEFAULT_HIEGHT));
		this.txtName.setPreferredSize(new Dimension(140, DEFAULT_HIEGHT));
		add(this.txtNum, BorderLayout.LINE_START);
		add(this.txtName, BorderLayout.CENTER);
		this.txtName.setEnabled(false);
		// txtNum.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyReleased(KeyEvent e) {
		// handleNumberChanged();
		// }
		// });
		this.txtNum.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(final FocusEvent e) {

			}

			@Override
			public void focusLost(final FocusEvent e) {
				handleNumberChanged();
			}
		});
		this.txtNum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
					handleFindValue();
				}

			}
		});
	}

	// ///////////////////////////////////////////////////////////////////////////
	public boolean isRequired() {
		return this.required;
	}

	// /////////////////////////////////////////////////////////////
	private void loadQuery(final String query) throws RecordNotFoundException, DaoException {
		final AbstractDao dao = getDao();
		dao.findRecord(new DaoFinder() {

			@Override
			public String getFinderSql() {
				return query;
			}

			@Override
			public Object populate(final ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				final int columnCount = rs.getMetaData().getColumnCount();
				if (columnCount < 3) {
					throw new IllegalStateException("Query " + query + " should return at least three cols only");
				}
				JKLookupText.this.recordId = rs.getObject(1);
				JKLookupText.this.txtNum.setText(rs.getString(2));
				final StringBuffer buf = new StringBuffer();
				for (int i = 3; i <= columnCount; i++) {
					buf.append(rs.getString(i) + " ");
				}
				JKLookupText.this.txtName.setText(buf.toString());
				return null;
			}

			@Override
			public void setParamters(final PreparedStatement ps) throws SQLException {
			}
		});
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void reset() {
		setValue(getDefaultValue());
	}

	// ///////////////////////////////////////////////////////////////////////////
	@Override
	public void setDataSource(final DataSource manager) {
		this.datasource = manager;
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void setDefaultValue(final Object defaultValue) {
		this.defaultValue = defaultValue;

	}

	// ///////////////////////////////////////////////////////////////////////////
	@Override
	public void setEnabled(final boolean enabled) {
		this.txtNum.setEnabled(enabled);
	}

	public void setIdFieldName(final String idFieldName) {
		this.idField = getTableMeta().getField(idFieldName, true);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public void setNameWidth(final int width) {
		this.txtName.setPreferredSize(new Dimension(width, DEFAULT_HIEGHT));
	}

	// /////////////////////////////////////////////////////////////////
	public void setNumberFieldName(final String numberFieldName) {
		this.numberField = getTableMeta().getField(numberFieldName, true);
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public void setNumberWidth(final int width) {
		// txtNum.getPreferredSize().width=width;
		this.txtNum.setPreferredSize(new Dimension(width, DEFAULT_HIEGHT));
	}

	// ////////////////////////////////////////////////////////////
	public void setQuery(final String query) {
		this.query = query;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public void setRequired(final boolean required) {
		if (required) {
			addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		} else {
			this.fsWrapper.removeValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		}
		this.required = required;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public void setTableMeta(final TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		setIdFieldName(tableMeta.getIdField().getName());
		setNumberFieldName(tableMeta.getLookupNumberField().getName());
		this.summaryFields = tableMeta.lstSummaryFields();
	}

	// /////////////////////////////////////////////////////////////////
	public void setTableName(final String tableName) {
		setTableMeta(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	// /////////////////////////////////////////////////////////////////
	public void setTextFieldName(final String textFieldName) {
		final String[] fieldNames = textFieldName.split(",");
		this.summaryFields.clear();
		for (final String fieldName : fieldNames) {
			this.summaryFields.add(getTableMeta().getField(fieldName));
		}
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void setValue(final Object value) {
		final Object oldId = this.recordId;
		this.recordId = value;
		handleIdChanged();
		this.fsWrapper.fireValueChangeListener(oldId, value);
	}
}
