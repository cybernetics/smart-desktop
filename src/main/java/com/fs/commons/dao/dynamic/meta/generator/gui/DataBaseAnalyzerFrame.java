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
package com.fs.commons.dao.dynamic.meta.generator.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.fs.commons.application.exceptions.ServerDownException;
import com.fs.commons.dao.JKDataAccessException;
import com.fs.commons.dao.connection.JKDataSource;
import com.fs.commons.dao.connection.JKDataSourceFactory;
import com.fs.commons.dao.connection.JKPoolingDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaFactory;
import com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXMLGenerator;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKComboBox;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.util.GeneralUtility;
import com.jk.logging.JKLogger;
import com.jk.logging.JKLoggerFactory;

public class DataBaseAnalyzerFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 * @param args
	 * @throws JKDataAccessException
	 * @throws SQLException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ServerDownException
	 */
	public static void main(final String[] args) throws JKDataAccessException, SQLException, FileNotFoundException, IOException, ServerDownException {
		final Properties prop = new Properties();
		if (args.length > 0) {
			prop.load(new FileInputStream(args[0]));
		}
		prop.getProperty("host", "localhost");
		Integer.parseInt(prop.getProperty("port", "3306"));
		prop.getProperty("db-name", "");
		prop.getProperty("user-name", "root");
		prop.getProperty("password", "123456");
		JKDataSourceFactory.setDefaultDataSource(new JKPoolingDataSource());// host,
																		// port,
																		// dbName,
																		// userName,
																		// password));
		// Connection con =
		// (ConnectionManagerFactory.getResourceManager().getConnection());
		final DataBaseAnalyzerFrame frame = new DataBaseAnalyzerFrame();
		frame.setVisible(true);
	}

	private final DataBaseAnaylser analyzer;

	private Hashtable<String, TableMeta> tablesHash;

	JKComboBox cmbDatabases = new JKComboBox();

	JKButton btnSelectAll = new JKButton("Select ALL    ");

	JKButton btnDeSelectAll = new JKButton("DeSelect ALL");

	JKButton btnGenerate = new JKButton("Generate XML");

	JKButton btnParse = new JKButton("Parse XML");

	JKButton btnLoad = new JKButton("Load XML");

	JKButton btnSave = new JKButton("Save");

	JKButton btnCompare = new JKButton("Compare");

	JTextArea txtResult = new JTextArea(30, 30);

	MyTableModel mdlTables = new MyTableModel();

	JTable tblTables = new MyTable(this.mdlTables);

	JFileChooser chooser = new JFileChooser(".");

	private final JKDataSource connectionManager;

	private JKLogger logger=JKLoggerFactory.getLogger(getClass());

	/**
	 *
	 * @throws SQLException
	 * @throws JKDataAccessException
	 */
	public DataBaseAnalyzerFrame() throws SQLException, JKDataAccessException {
		this.connectionManager = JKDataSourceFactory.getDefaultDataSource();
		this.analyzer = this.connectionManager.getDatabaseAnasyaler();
		init();
		populate();
		loadPath();
		applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	}

	/**
	 *
	 * @param fileName
	 */
	void cachePath(final String fileName) {
		try {
			GeneralUtility.writeDataToFile(fileName.getBytes(), new File("oldpath.txt"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private String getCurrentDatabasesName() {
		return (String) this.cmbDatabases.getSelectedItem();
	}

	private TableMetaFactory getMetaFactory() {
		return AbstractTableMetaFactory.getDefaultMetaFactory();
	}

	/**
	 *
	 * @return
	 */
	private ArrayList<TableMeta> getSelectedTablesMeta() {
		final Vector<Vector> data = this.mdlTables.getDataVector();
		final ArrayList<TableMeta> selectedTablesMeta = new ArrayList<TableMeta>();
		for (int i = 0; i < data.size(); i++) {
			final Boolean selected = (Boolean) data.get(i).get(0);
			final String tableName = (String) data.get(i).get(1);
			if (selected) {
				selectedTablesMeta.add(this.tablesHash.get(tableName));
			}
		}
		return selectedTablesMeta;
	}

	int getTableMetaRowIndex(final String tableName) {
		final Vector<Vector> data = this.mdlTables.getDataVector();
		for (int i = 0; i < data.size(); i++) {
			final String name = (String) data.get(i).get(1);
			if (name.equalsIgnoreCase(tableName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 *
	 * @param rowId
	 * @return
	 */
	String getTableName(final int rowId) {
		return (String) ((Vector) this.mdlTables.getDataVector().get(rowId)).get(1);
	}

	/**
	 * @throws JKDataAccessException
	 *
	 */
	protected void handleCompare() throws JKDataAccessException {
		try {
			final ArrayList<TableMeta> databaseTables = this.analyzer.getTablesMeta(getCurrentDatabasesName());
			for (int i = 0; i < databaseTables.size(); i++) {
				final TableMeta databaseMeta = databaseTables.get(i);
				final TableMeta currentTableMeta = this.tablesHash.get(databaseMeta.getTableName());
				logger.info("Comparing Table  : " + currentTableMeta.getTableName());
				if (currentTableMeta == null) {
					logger.info("Table  : " + databaseMeta.getTableName() + " exists in the database and doesnot exist the current meta");
				} else {
					final Vector<FieldMeta> databaseTableFields = databaseMeta.getFieldList();
					databaseMeta.getFieldList();
					for (int j = 0; j < databaseTableFields.size(); j++) {
						final String fildName = databaseTableFields.get(i).getName();
						final FieldMeta field = currentTableMeta.getField(fildName);
						if (field == null) {
							logger.info("Field : " + fildName + " doesnot exist");
						}
					}
				}
			}
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @throws SQLException
	 * @throws JKDataAccessException
	 */
	protected void handleDatabaseChanged() throws SQLException, JKDataAccessException {
		if (this.cmbDatabases.getSelectedIndex() != -1) {
			this.connectionManager.setDatabaseName(getCurrentDatabasesName());
			final ArrayList<TableMeta> tables = this.analyzer.getTablesMeta(getCurrentDatabasesName());
			this.tablesHash = new Hashtable<String, TableMeta>();
			this.mdlTables.getDataVector().clear();
			for (int i = 0; i < tables.size(); i++) {
				final TableMeta table = tables.get(i);
				this.tablesHash.put(table.getTableName(), table);
				this.mdlTables.addRow(new Object[] { new Boolean(false), table.getTableName() });
			}
		}
	}

	/**
	 *
	 */
	protected void handleGenerate() {
		final TableMetaXMLGenerator generator = new TableMetaXMLGenerator();
		final ArrayList<TableMeta> selectedTablesMeta = getSelectedTablesMeta();
		final String result = generator.generateTablesMetaXml(selectedTablesMeta);
		this.txtResult.setText(result);
	}

	/**
	 *
	 */
	protected void handleLoadTableMetaFile() {
		this.chooser.showOpenDialog(this);
		final File file = this.chooser.getSelectedFile();
		if (file != null) {
			try {
				selectAll(false);
				cachePath(file.getAbsolutePath());
				final String text = GeneralUtility.getFileText(file.getAbsolutePath());
				this.txtResult.setText(text);
				parseText(this.txtResult.getText());
				this.mdlTables.fireTableDataChanged();
			} catch (final Exception e) {
				JOptionPane.showMessageDialog(this, "Unable to load file :" + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 *
	 */
	protected void handleSave() {
		this.chooser.showOpenDialog(this);
		final File file = this.chooser.getSelectedFile();
		if (file != null) {
			try {
				GeneralUtility.writeDataToFile(this.txtResult.getText().getBytes(), file);
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 */
	protected void handleShowTableMetaPanel(final int selectedRow) {
		final TableMeta meta = this.tablesHash.get(getTableName(selectedRow));
		final TableMetaPanel pnl = new TableMetaPanel(meta, this.tablesHash);
		SwingUtility.showPanelInDialog(pnl, meta.getTableName());
	}

	/**
	 * @throws JKDataAccessException
	 *
	 */
	private void init() throws JKDataAccessException {
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final JPanel pnl = new JPanel(new BorderLayout());
		pnl.setPreferredSize(new Dimension(400, 500));

		this.tblTables.getColumnModel().getColumn(0).setMaxWidth(30);

		final JScrollPane pane = new JScrollPane(this.tblTables);
		pane.setBorder(BorderFactory.createTitledBorder("Database Tables"));
		final JKPanel pnlButtons = new JKPanel();
		pnlButtons.setLayout(new GridLayout(7, 1, 5, 10));
		pnlButtons.add(this.btnDeSelectAll);
		pnlButtons.add(this.btnSelectAll);
		pnlButtons.add(this.btnGenerate);
		pnlButtons.add(this.btnParse);
		pnlButtons.add(this.btnLoad);
		pnlButtons.add(this.btnSave);
		pnlButtons.add(this.btnCompare);

		final JKPanel pnl2 = new JKPanel();
		pnl2.add(pnlButtons);

		pnl.add(new JKLabledComponent("Database ", this.cmbDatabases), BorderLayout.NORTH);
		pnl.add(pane, BorderLayout.CENTER);
		pnl.add(pnl2, BorderLayout.EAST);

		add(pnl, BorderLayout.WEST);
		final JScrollPane txtPane = new JScrollPane(this.txtResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		txtPane.setBorder(BorderFactory.createTitledBorder("Result table-meta.xml"));
		add(txtPane, BorderLayout.CENTER);
		this.btnGenerate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleGenerate();

			}
		});
		this.btnParse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					parseText(DataBaseAnalyzerFrame.this.txtResult.getText());
				} catch (final FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (final JKXmlException e1) {
					e1.printStackTrace();
				}
			};
		});
		this.tblTables.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent e) {
				if (e.getClickCount() == 2) {
					handleShowTableMetaPanel(DataBaseAnalyzerFrame.this.tblTables.getSelectedRow());
				}
			}
		});
		this.btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleLoadTableMetaFile();
			}
		});
		this.btnSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				selectAll(true);
			}
		});
		this.btnDeSelectAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				selectAll(false);
			}
		});
		this.btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				handleSave();
			}
		});
		this.txtResult.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
					final String input = SwingUtility.showInputDialog("Enter text to find");
					if (input != null) {
						final int index = DataBaseAnalyzerFrame.this.txtResult.getText()
								.substring(DataBaseAnalyzerFrame.this.txtResult.getCaretPosition()).indexOf(input);
						if (index != -1) {
							DataBaseAnalyzerFrame.this.txtResult.setCaretPosition(index);
						}
					}
				} else {
					super.keyPressed(e);
				}
			}
		});
		this.tblTables.addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(final KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					handleShowTableMetaPanel(DataBaseAnalyzerFrame.this.tblTables.getSelectedRow());
				}
			}
		});
		this.btnCompare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					handleCompare();
				} catch (final JKDataAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 *
	 * @return
	 */
	void loadPath() {
		try {
			final String path = GeneralUtility.getFileText("oldpath.txt");
			this.chooser.setCurrentDirectory(new File(path));
		} catch (final IOException e) {
		}
	}

	/**
	 *
	 * @param text
	 * @throws JKXmlException
	 * @throws FileNotFoundException
	 */
	private void parseText(final String text) throws JKXmlException, FileNotFoundException {
		getMetaFactory().getTables().clear();
		getMetaFactory().loadMetaFiles(new StringBufferInputStream(text));
		final ArrayList<TableMeta> list = getMetaFactory().getTablesAsArrayList();

		for (int i = 0; i < list.size(); i++) {
			final TableMeta meta = list.get(i);
			final TableMeta old = this.tablesHash.get(meta.getTableName());
			if (old != null) {
				this.tablesHash.put(meta.getTableName(), meta);
				final int rowNum = getTableMetaRowIndex(meta.getTableName());
				setChecked(rowNum, true);
			}
		}
		this.mdlTables.fireTableDataChanged();
	}

	/**
	 *
	 * @throws SQLException
	 */
	private void populate() throws SQLException {
		final ArrayList<String> databases = this.analyzer.getDatabasesName();
		this.cmbDatabases.removeAllItems();
		for (int i = 0; i < databases.size(); i++) {
			this.cmbDatabases.addItem(new String(databases.get(i)));
		}
		this.cmbDatabases.setSelectedIndex(-1);
		this.cmbDatabases.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				try {
					handleDatabaseChanged();
				} catch (final SQLException e1) {
					e1.printStackTrace();
				} catch (final JKDataAccessException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});

	}

	/**
	 *
	 * @param checked
	 */
	private void selectAll(final boolean checked) {
		for (int i = 0; i < this.mdlTables.getRowCount(); i++) {
			setChecked(i, checked);
		}
		this.mdlTables.fireTableDataChanged();
	}

	/**
	 *
	 * @param rowNum
	 */
	void setChecked(final int rowNum, final boolean checked) {
		final Vector data = (Vector) this.mdlTables.getDataVector().get(rowNum);
		data.remove(0);
		data.add(0, new Boolean(checked));
		// ((Boolean)data.get(0)).
	}
}

class MyTable extends JTable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public MyTable(final TableModel model) {
		super(model);
	}

	@Override
	public boolean isCellEditable(final int row, final int column) {
		return column == 0;// enabled the checkbox only
	}
}

class MyTableModel extends DefaultTableModel {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Class getColumnClass(final int columnIndex) {
		if (columnIndex == 0) {
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(final int column) {
		switch (column) {
		case 1:
			return "Table name";
		}
		return " ";
	}

}