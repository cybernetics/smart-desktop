package com.fs.commons.dao.dynamic.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;

import com.fs.commons.dao.connection.DataSource;
import com.fs.commons.dao.dynamic.meta.generator.DataBaseAnaylser;
import com.fs.commons.dao.dynamic.meta.xml.JKXmlException;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXMLGenerator;
import com.fs.commons.dao.dynamic.meta.xml.TableMetaXmlParser;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.logging.Logger;
import com.fs.commons.util.CollectionUtil;
import com.fs.commons.util.GeneralUtility;

public class TableMetaFactory {
	Hashtable<String, TableMeta> metas = new Hashtable<String, TableMeta>();
	Hashtable<String, TableMeta> dynamicTable = new Hashtable<String, TableMeta>();
	private final DataSource connectionManager;

	// /////////////////////////////////////////////////////////////////////////////////////
	public TableMetaFactory(DataSource connectionManager) throws DaoException {
		this.connectionManager = connectionManager;
		try {
			loadDynamicMeta(connectionManager);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private void loadDynamicMeta(DataSource connectionManager) throws DaoException, SQLException, FileNotFoundException, IOException, JKXmlException {
		if (System.getProperty(CollectionUtil.fixPropertyKey("tablemeta.dynamic.generate"), "true").equals("true")) {
			final File file = new File(GeneralUtility.getUserFolderPath(true) + "meta-" + connectionManager.getDatabaseName() + ".dll");
			try {
				if (file.exists()) {
					System.err.println("Dynamic Meta file is already exist , loading from file.");
					TableMetaXmlParser parser = new TableMetaXmlParser();
					this.dynamicTable = parser.parse(new FileInputStream(file), "dynamic");
				} else {
					DataBaseAnaylser analyszer = connectionManager.getDatabaseAnasyaler();
					Logger.printCurrentTime("Generating table meta : " + file);
					dynamicTable = toHashTable(analyszer.getTablesMeta(),"dynamic");
					if (System.getProperty("tablemeta.dynamic.save", "true").equals("true")) {
						TableMetaXMLGenerator generator = new TableMetaXMLGenerator();
						String metaXml = generator.generateTablesMetaXml(new ArrayList<TableMeta>(dynamicTable.values()));
						GeneralUtility.writeDataToFile(metaXml.getBytes(), file);
						Logger.printCurrentTime("Table meta generation done");
					}
				}
			} finally {
				 //ile.deleteOnExit();
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private Hashtable<String, TableMeta> toHashTable(ArrayList<TableMeta> tablesMeta, String source) {
		Hashtable<String, TableMeta> hash = new Hashtable<String, TableMeta>();
		for (TableMeta tableMeta : tablesMeta) {
			tableMeta.setSource(source);
			hash.put(tableMeta.getTableId(), tableMeta);
		}
		return hash;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta(String tableName) throws TableMetaNotFoundException {
		TableMeta meta = metas.get(tableName);
		if (meta == null) {
			// System.err.println("TableMeta for " + tableName +
			// " not defined , return default meta \n");
			meta = dynamicTable.get(tableName);
			if (meta == null) {
				throw new TableMetaNotFoundException("TableMeta : " + tableName + " doesnot exists");
			}
		}
		meta.setDataSource(connectionManager);
		return meta;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void writeDynamicMeta(java.io.OutputStream out) throws IOException {
		TableMetaXMLGenerator generator = new TableMetaXMLGenerator();
		String xml = generator.generateTablesMetaXml(new ArrayList<TableMeta>(dynamicTable.values()));
		out.write(xml.getBytes());
		out.close();
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void loadMetaFiles(InputStream in) throws FileNotFoundException, JKXmlException {
		TableMetaXmlParser parser = new TableMetaXmlParser();
		Hashtable<String, TableMeta> newTables = parser.parse(in, "user");
		addTablesMeta(newTables);
		// TODO : make the following statement for another location to remove
		// coupling
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void addTablesMeta(Hashtable<String, TableMeta> newTables) {
		for (String tableMetaName : newTables.keySet()) {
			if (metas.get(tableMetaName) != null) {
				// Logger.fatal(tableMetaName +
				// " Is Overriding please provide a unique ID for it");
			}
		}
		metas.putAll(newTables);
		// the following used to be sure that dynamicTabls shuold hold
		// unconfigurable values only
		Collection<TableMeta> values = newTables.values();
		for (TableMeta tableMeta : values) {
			dynamicTable.remove(tableMeta.getTableId());
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public Hashtable<String, TableMeta> getTables() {
		return metas;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<TableMeta> getTablesAsArrayList() {
		Enumeration keys = metas.keys();
		ArrayList<TableMeta> list = new ArrayList<TableMeta>();
		while (keys.hasMoreElements()) {
			list.add(metas.get(keys.nextElement()));
		}
		return list;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public boolean isMetaExists(String metaName) {
		return metas.get(metaName) != null || dynamicTable.get(metaName) != null;
	}

}
