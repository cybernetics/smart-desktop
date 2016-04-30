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
import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.connection.DataSourceFactory;
import com.fs.commons.dao.connection.PoolingDataSource;
import com.fs.commons.dao.dynamic.meta.AbstractTableMetaFactory;
import com.fs.commons.dao.dynamic.meta.FieldMeta;
import com.fs.commons.dao.dynamic.meta.TableMeta;
import com.fs.commons.dao.dynamic.meta.TableMetaFactory;
import com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXMLGenerator;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.JKButton;
import com.fs.commons.desktop.swing.comp.JKComboBox;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.logging.Logger;
import com.fs.commons.util.GeneralUtility;

public class DataBaseAnalyzerFrame extends JFrame {
	private static final long serialVersionUID = 1L;

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

	JTable tblTables = new MyTable(mdlTables);

	JFileChooser chooser = new JFileChooser(".");

	private DataSource connectionManager;

	/**
	 * 
	 * @throws SQLException
	 * @throws DaoException
	 */
	public DataBaseAnalyzerFrame() throws SQLException, DaoException {
		connectionManager = DataSourceFactory.getDefaultDataSource();
		analyzer = connectionManager.getDatabaseAnasyaler();
		init();
		populate();
		loadPath();
		applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		SwingUtility.setDefaultComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	}

	/**
	 * @throws DaoException
	 * 
	 */
	private void init() throws DaoException {
		setExtendedState(MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pnl = new JPanel(new BorderLayout());
		pnl.setPreferredSize(new Dimension(400, 500));

		tblTables.getColumnModel().getColumn(0).setMaxWidth(30);

		JScrollPane pane = new JScrollPane(tblTables);
		pane.setBorder(BorderFactory.createTitledBorder("Database Tables"));
		JKPanel pnlButtons = new JKPanel();
		pnlButtons.setLayout(new GridLayout(7, 1, 5, 10));
		pnlButtons.add(btnDeSelectAll);
		pnlButtons.add(btnSelectAll);
		pnlButtons.add(btnGenerate);
		pnlButtons.add(btnParse);
		pnlButtons.add(btnLoad);
		pnlButtons.add(btnSave);
		pnlButtons.add(btnCompare);

		JKPanel pnl2 = new JKPanel();
		pnl2.add(pnlButtons);

		pnl.add(new JKLabledComponent("Database ", cmbDatabases), BorderLayout.NORTH);
		pnl.add(pane, BorderLayout.CENTER);
		pnl.add(pnl2, BorderLayout.EAST);

		add(pnl, BorderLayout.WEST);
		JScrollPane txtPane = new JScrollPane(txtResult, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		txtPane.setBorder(BorderFactory.createTitledBorder("Result table-meta.xml"));
		add(txtPane, BorderLayout.CENTER);
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleGenerate();

			}
		});
		btnParse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					parseText(txtResult.getText());
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (JKXmlException e1) {
					e1.printStackTrace();
				}
			};
		});
		tblTables.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					handleShowTableMetaPanel(tblTables.getSelectedRow());
				}
			}
		});
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleLoadTableMetaFile();
			}
		});
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll(true);
			}
		});
		btnDeSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectAll(false);
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				handleSave();
			}
		});
		txtResult.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_F) && e.isControlDown()) {
					String input = SwingUtility.showInputDialog("Enter text to find");
					if (input != null) {
						int index = txtResult.getText().substring(txtResult.getCaretPosition()).indexOf(input);
						if (index != -1) {
							txtResult.setCaretPosition(index);
						}
					}
				} else {
					super.keyPressed(e);
				}
			}
		});
		tblTables.addKeyListener(new KeyAdapter() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * java.awt.event.KeyAdapter#keyPressed(java.awt.event.KeyEvent)
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					handleShowTableMetaPanel(tblTables.getSelectedRow());
				}
			}
		});
		btnCompare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					handleCompare();
				} catch (DaoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * @throws DaoException
	 * 
	 */
	protected void handleCompare() throws DaoException {
		try {
			ArrayList<TableMeta> databaseTables = analyzer.getTablesMeta(getCurrentDatabasesName());
			for (int i = 0; i < databaseTables.size(); i++) {
				TableMeta databaseMeta = databaseTables.get(i);
				TableMeta currentTableMeta = tablesHash.get(databaseMeta.getTableName());
				Logger.info("Comparing Table  : " + currentTableMeta.getTableName());
				if (currentTableMeta == null) {
					Logger.info("Table  : " + databaseMeta.getTableName() + " exists in the database and doesnot exist the current meta");
				} else {
					Vector<FieldMeta> databaseTableFields = databaseMeta.getFieldList();
					databaseMeta.getFieldList();
					for (int j = 0; j < databaseTableFields.size(); j++) {
						String fildName = databaseTableFields.get(i).getName();
						FieldMeta field = currentTableMeta.getField(fildName);
						if (field == null) {
							Logger.info("Field : " + fildName + " doesnot exist");
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void handleSave() {
		chooser.showOpenDialog(this);
		File file = chooser.getSelectedFile();
		if (file != null) {
			try {
				GeneralUtility.writeDataToFile(txtResult.getText().getBytes(), file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param checked
	 */
	private void selectAll(boolean checked) {
		for (int i = 0; i < mdlTables.getRowCount(); i++) {
			setChecked(i, checked);
		}
		mdlTables.fireTableDataChanged();
	}

	/**
	 * 
	 */
	protected void handleLoadTableMetaFile() {
		chooser.showOpenDialog(this);
		File file = chooser.getSelectedFile();
		if (file != null) {
			try {
				selectAll(false);
				cachePath(file.getAbsolutePath());
				String text = GeneralUtility.getFileText(file.getAbsolutePath());
				txtResult.setText(text);
				parseText(txtResult.getText());
				this.mdlTables.fireTableDataChanged();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Unable to load file :" + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * @param text
	 * @throws JKXmlException
	 * @throws FileNotFoundException
	 */
	private void parseText(String text) throws JKXmlException, FileNotFoundException {
		getMetaFactory().getTables().clear();
		getMetaFactory().loadMetaFiles(new StringBufferInputStream(text));
		ArrayList<TableMeta> list = getMetaFactory().getTablesAsArrayList();

		for (int i = 0; i < list.size(); i++) {
			TableMeta meta = list.get(i);
			TableMeta old = tablesHash.get(meta.getTableName());
			if (old != null) {
				tablesHash.put(meta.getTableName(), meta);
				int rowNum = getTableMetaRowIndex(meta.getTableName());
				setChecked(rowNum, true);
			}
		}
		mdlTables.fireTableDataChanged();
	}

	/**
	 */
	protected void handleShowTableMetaPanel(int selectedRow) {
		TableMeta meta = tablesHash.get(getTableName(selectedRow));
		TableMetaPanel pnl = new TableMetaPanel(meta, tablesHash);
		SwingUtility.showPanelInDialog(pnl, meta.getTableName());
	}

	/**
	 * 
	 * @param rowId
	 * @return
	 */
	String getTableName(int rowId) {
		return (String) ((Vector) mdlTables.getDataVector().get(rowId)).get(1);
	}

	int getTableMetaRowIndex(String tableName) {
		Vector<Vector> data = mdlTables.getDataVector();
		for (int i = 0; i < data.size(); i++) {
			String name = (String) data.get(i).get(1);
			if (name.equalsIgnoreCase(tableName)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param rowNum
	 */
	void setChecked(int rowNum, boolean checked) {
		Vector data = (Vector) mdlTables.getDataVector().get(rowNum);
		data.remove(0);
		data.add(0, new Boolean(checked));
		// ((Boolean)data.get(0)).
	}

	/**
	 * 
	 */
	protected void handleGenerate() {
		TableMetaXMLGenerator generator = new TableMetaXMLGenerator();
		ArrayList<TableMeta> selectedTablesMeta = getSelectedTablesMeta();
		String result = generator.generateTablesMetaXml(selectedTablesMeta);
		txtResult.setText(result);
	}

	/**
	 * 
	 * @return
	 */
	private ArrayList<TableMeta> getSelectedTablesMeta() {
		Vector<Vector> data = mdlTables.getDataVector();
		ArrayList<TableMeta> selectedTablesMeta = new ArrayList<TableMeta>();
		for (int i = 0; i < data.size(); i++) {
			Boolean selected = (Boolean) data.get(i).get(0);
			String tableName = (String) data.get(i).get(1);
			if (selected) {
				selectedTablesMeta.add(tablesHash.get(tableName));
			}
		}
		return selectedTablesMeta;
	}

	/**
	 * @throws SQLException
	 * @throws DaoException
	 */
	protected void handleDatabaseChanged() throws SQLException, DaoException {
		if (cmbDatabases.getSelectedIndex() != -1) {
			connectionManager.setDatabaseName(getCurrentDatabasesName());
			ArrayList<TableMeta> tables = analyzer.getTablesMeta(getCurrentDatabasesName());
			tablesHash = new Hashtable<String, TableMeta>();
			mdlTables.getDataVector().clear();
			for (int i = 0; i < tables.size(); i++) {
				TableMeta table = tables.get(i);
				tablesHash.put(table.getTableName(), table);
				mdlTables.addRow(new Object[] { new Boolean(false), table.getTableName() });
			}
		}
	}

	/**
	 * @return
	 */
	private String getCurrentDatabasesName() {
		return (String) cmbDatabases.getSelectedItem();
	}

	/**
	 * 
	 * @throws SQLException
	 */
	private void populate() throws SQLException {
		ArrayList<String> databases = analyzer.getDatabasesName();
		cmbDatabases.removeAllItems();
		for (int i = 0; i < databases.size(); i++) {
			cmbDatabases.addItem(new String(databases.get(i)));
		}
		cmbDatabases.setSelectedIndex(-1);
		cmbDatabases.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					handleDatabaseChanged();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (DaoException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		});

	}

	/**
	 * 
	 * @param args
	 * @throws DaoException
	 * @throws SQLException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws ServerDownException
	 */
	public static void main(String[] args) throws DaoException, SQLException, FileNotFoundException, IOException, ServerDownException {
		Properties prop = new Properties();
		if (args.length > 0) {
			prop.load(new FileInputStream(args[0]));
		}
		prop.getProperty("host", "localhost");
		Integer.parseInt(prop.getProperty("port", "3306"));
		prop.getProperty("db-name", "");
		prop.getProperty("user-name", "root");
		prop.getProperty("password", "123456");
		DataSourceFactory.setDefaultDataSource(new PoolingDataSource());// host,
																								// port,
																								// dbName,
																								// userName,
																								// password));
		// Connection con =
		// (ConnectionManagerFactory.getResourceManager().getConnection());
		DataBaseAnalyzerFrame frame = new DataBaseAnalyzerFrame();
		frame.setVisible(true);
	}

	/**
	 * 
	 * @param fileName
	 */
	void cachePath(String fileName) {
		try {
			GeneralUtility.writeDataToFile(fileName.getBytes(), new File("oldpath.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	void loadPath() {
		try {
			String path = GeneralUtility.getFileText("oldpath.txt");
			chooser.setCurrentDirectory(new File(path));
		} catch (IOException e) {
		}
	}

	private TableMetaFactory getMetaFactory() {
		return AbstractTableMetaFactory.getDefaultMetaFactory();
	}
}

class MyTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyTable(TableModel model) {
		super(model);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0;// enabled the checkbox only
	}
}

class MyTableModel extends DefaultTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Class getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Boolean.class;
		}
		return super.getColumnClass(columnIndex);
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 1:
			return "Table name";
		}
		return " ";
	}

}