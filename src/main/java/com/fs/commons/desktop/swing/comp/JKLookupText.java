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
import com.fs.commons.dao.dynamic.meta.IdFieldMeta;
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
	public JKLookupText(String query) {
		setQuery(query);
		init();
	}

	// ////////////////////////////////////////////////////////////
	private void init() {
		// setPreferredSize(FSSwingConstants.DEFAULT_TEXT_SIZE);
		setLayout(new BorderLayout());
		txtNum.setPreferredSize(new Dimension(50, DEFAULT_HIEGHT));
		txtName.setPreferredSize(new Dimension(140, DEFAULT_HIEGHT));
		add(txtNum,BorderLayout.LINE_START);
		add(txtName,BorderLayout.CENTER);
		txtName.setEnabled(false);
		// txtNum.addKeyListener(new KeyAdapter() {
		// @Override
		// public void keyReleased(KeyEvent e) {
		// handleNumberChanged();
		// }
		// });
		txtNum.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				handleNumberChanged();
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});
		txtNum.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
					handleFindValue();
				}

			}
		});
	}

	/**
	 * 
	 */
	protected void handleFindValue() {
		Object id = QueryDialog.showQueryDialog(tableMeta.getShortReportSql(), "SEARCH", 0);
		if (id != null) {
			setValue(id.toString());
		}
	}

	// ////////////////////////////////////////////////////////////
	public void setQuery(String query) {
		this.query = query;
	}

	// ////////////////////////////////////////////////////////////
	public String getQuery() {
		if (query != null && !query.trim().equals("")) {
			return query;
		}
		String query = "SELECT ";
		query += getIdFieldName() + ",";
		query += getNumberFieldName() + ",";
		query += getTextFieldName() + "\n";
		query += " FROM " + getTableName();
		query += " WHERE 1=1";
		return query;
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void setValue(Object value) {
		Object oldId = this.recordId;
		this.recordId = value;
		handleIdChanged();
		fsWrapper.fireValueChangeListener(oldId, value);
	}

	// ////////////////////////////////////////////////////////////
	private void handleIdChanged() {
		if (recordId == null || recordId.toString().trim().equals("")) {
			clearFields(true);
			return;
		}
		String query = getQuery();
		query += " AND " + getIdFieldName() + "=" + recordId;
		try {
			loadQuery(query);
		} catch (RecordNotFoundException e) {
			clearFields(true);
		} catch (DaoException e) {
			System.err.println("Error while trying  to extecute: " + query);
			ExceptionUtil.handleException(e);
		}
	}

	// ////////////////////////////////////////////////////////////
	private void clearFields(boolean clearNumber) {
		recordId = null;
		if (clearNumber) {
			txtNum.setText("");
		}
		txtName.setText("");
	}

	// ////////////////////////////////////////////////////////////
	private void handleNumberChanged() {
		Object old = getValue();
		String number = txtNum.getText();
		if (number.trim().equals("")) {
			clearFields(true);
			return;
		}
		String query = getQuery();
		query += " AND " + getNumberFieldName() + "='" + number + "'";
		try {
			loadQuery(query);
		} catch (RecordNotFoundException e) {
			clearFields(false);
			this.requestFocus();
			txtName.setText(Lables.get("N/A"));
		} catch (DaoException e) {
			ExceptionUtil.handleException(e);
		}
		fsWrapper.fireValueChangeListener(old, getValue());
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public Object getValue() {
		return recordId;
	}

	/**
	 * 
	 * @return
	 */
	public int getIdAsInteger() {
		if (recordId == null) {
			return -1;
		}
		return new Integer(recordId.toString());
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;

	}

	// ////////////////////////////////////////////////////////////
	@Override
	public Object getDefaultValue() {
		return defaultValue;
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void reset() {
		setValue(getDefaultValue());
	}

	// ////////////////////////////////////////////////////////////
	@Override
	public void clear() {
		setValue(null);
	}

//	// ////////////////////////////////////////////////////////////
//	@Override
//	public void addValidator(Validator validator) {
//		fsWrapper.addValidator(validator);
//	}

//	// ////////////////////////////////////////////////////////////
//	@Override
//	public void validateValue() throws ValidationException {
//		fsWrapper.validateValue();
//	}

	// ////////////////////////////////////////////////////////////
	@Override
	public DataSource getDataSource() {
		return datasource;
	}

	public String getTableName() {
		return getTableMeta().getTableName();
	}

	// /////////////////////////////////////////////////////////////////
	public void setTableName(String tableName) {
		setTableMeta(AbstractTableMetaFactory.getTableMeta(tableName));
	}

	// /////////////////////////////////////////////////////////////////
	public String getNumberFieldName() {
		return numberField.getName();
	}

	// /////////////////////////////////////////////////////////////////
	public void setNumberFieldName(String numberFieldName) {
		this.numberField = getTableMeta().getField(numberFieldName, true);
	}

	// /////////////////////////////////////////////////////////////////
	public String getTextFieldName() {
		StringBuffer buf = new StringBuffer();
		int counter = 0;
		for (FieldMeta field : summaryFields) {
			if (counter++ != 0) {
				buf.append(",");
			}
			buf.append(field.getName());
		}
		return buf.toString();
	}

	// /////////////////////////////////////////////////////////////////
	public void setTextFieldName(String textFieldName) {
		String[] fieldNames = textFieldName.split(",");
		summaryFields.clear();
		for (String fieldName : fieldNames) {
			summaryFields.add(getTableMeta().getField(fieldName));
		}
	}

	// /////////////////////////////////////////////////////////////

	public String getIdFieldName() {
		return idField.getName();
	}

	// /////////////////////////////////////////////////////////////

	public void setIdFieldName(String idFieldName) {
		this.idField = (IdFieldMeta) getTableMeta().getField(idFieldName, true);
	}

	// /////////////////////////////////////////////////////////////
	private void loadQuery(final String query) throws RecordNotFoundException, DaoException {
		AbstractDao dao = getDao();
		dao.findRecord(new DaoFinder() {

			@Override
			public void setParamters(PreparedStatement ps) throws SQLException {
			}

			@Override
			public Object populate(ResultSet rs) throws SQLException, RecordNotFoundException, DaoException {
				int columnCount = rs.getMetaData().getColumnCount();
				if (columnCount < 3) {
					throw new IllegalStateException("Query " + query + " should return at least three cols only");
				}
				recordId = rs.getObject(1);
				txtNum.setText(rs.getString(2));
				StringBuffer buf = new StringBuffer();
				for (int i = 3; i <= columnCount; i++) {
					buf.append(rs.getString(i) + " ");
				}
				txtName.setText(buf.toString());
				return null;
			}

			@Override
			public String getFinderSql() {
				return query;
			}
		});
	}

	// //////////////////////////////////////////////////////////////////////////////////
	private DynamicDao getDao() {
		return DaoFactory.createDynamicDao(getTableMeta());
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public void setNumberWidth(int width) {
		// txtNum.getPreferredSize().width=width;
		txtNum.setPreferredSize(new Dimension(width, DEFAULT_HIEGHT));
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public int getNumberWidth() {
		return txtNum.getPreferredSize().width;
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public void setNameWidth(int width) {
		txtName.setPreferredSize(new Dimension(width, DEFAULT_HIEGHT));
	}

	// //////////////////////////////////////////////////////////////////////////////////
	public int getNameWidth() {
		return txtName.getPreferredSize().width;
	}

	// ///////////////////////////////////////////////////////////////////////////
	@Override
	public void setEnabled(boolean enabled) {
		txtNum.setEnabled(enabled);
	}

	// ///////////////////////////////////////////////////////////////////////////
	@Override
	public void setDataSource(DataSource manager) {
		this.datasource = manager;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public void setTableMeta(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
		this.setIdFieldName(tableMeta.getIdField().getName());
		this.setNumberFieldName(tableMeta.getLookupNumberField().getName());
		this.summaryFields = tableMeta.lstSummaryFields();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta() {
		if (tableMeta == null) {
			throw new IllegalStateException("Please set tableMeta on FSLookUpText");
		}

		return tableMeta;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public Object getNumberValue() {
		return txtNum.getValue();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public Object getNumberValueAsInteger() {
		return txtNum.getTextAsInteger();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public String getTextValue() {
		return txtName.getText();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public void setRequired(boolean required) {
		if (required) {
			addValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		} else {
			fsWrapper.removeValidator(FSValidators.REQUIRE_NON_EMPTY_STRING);
		}
		this.required = required;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public boolean isRequired() {
		return required;
	}

	// ///////////////////////////////////////////////////////////////////////////
	public Object getFieldValue(String fieldName) throws RecordNotFoundException, DaoException {
		if (getValue() == null) {
			return null;
		}
		DynamicDao dao = getDao();
		Record record = dao.findRecord(getValue());
		return record.getFieldValue(fieldName);
	}
}
