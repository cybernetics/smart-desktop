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
	public TableMetaFactory(final DataSource connectionManager) throws DaoException {
		this.connectionManager = connectionManager;
		try {
			loadDynamicMeta(connectionManager);
		} catch (final Exception e) {
			throw new DaoException(e);
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void addTablesMeta(final Hashtable<String, TableMeta> newTables) {
		for (final String tableMetaName : newTables.keySet()) {
			if (this.metas.get(tableMetaName) != null) {
				// Logger.fatal(tableMetaName +
				// " Is Overriding please provide a unique ID for it");
			}
		}
		this.metas.putAll(newTables);
		// the following used to be sure that dynamicTabls shuold hold
		// unconfigurable values only
		final Collection<TableMeta> values = newTables.values();
		for (final TableMeta tableMeta : values) {
			this.dynamicTable.remove(tableMeta.getTableId());
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public TableMeta getTableMeta(final String tableName) throws TableMetaNotFoundException {
		TableMeta meta = this.metas.get(tableName);
		if (meta == null) {
			// System.err.println("TableMeta for " + tableName +
			// " not defined , return default meta \n");
			meta = this.dynamicTable.get(tableName);
			if (meta == null) {
				throw new TableMetaNotFoundException("TableMeta : " + tableName + " doesnot exists");
			}
		}
		meta.setDataSource(this.connectionManager);
		return meta;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public Hashtable<String, TableMeta> getTables() {
		return this.metas;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public ArrayList<TableMeta> getTablesAsArrayList() {
		final Enumeration keys = this.metas.keys();
		final ArrayList<TableMeta> list = new ArrayList<TableMeta>();
		while (keys.hasMoreElements()) {
			list.add(this.metas.get(keys.nextElement()));
		}
		return list;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public boolean isMetaExists(final String metaName) {
		return this.metas.get(metaName) != null || this.dynamicTable.get(metaName) != null;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private void loadDynamicMeta(final DataSource connectionManager)
			throws DaoException, SQLException, FileNotFoundException, IOException, JKXmlException {
		if (System.getProperty(CollectionUtil.fixPropertyKey("tablemeta.dynamic.generate"), "true").equals("true")) {
			final File file = new File(GeneralUtility.getUserFolderPath(true) + "meta-" + connectionManager.getDatabaseName() + ".dll");
			try {
				if (file.exists()) {
					System.err.println("Dynamic Meta file is already exist , loading from file.");
					final TableMetaXmlParser parser = new TableMetaXmlParser();
					this.dynamicTable = parser.parse(new FileInputStream(file), "dynamic");
				} else {
					final DataBaseAnaylser analyszer = connectionManager.getDatabaseAnasyaler();
					Logger.printCurrentTime("Generating table meta : " + file);
					this.dynamicTable = toHashTable(analyszer.getTablesMeta(), "dynamic");
					if (System.getProperty("tablemeta.dynamic.save", "true").equals("true")) {
						final TableMetaXMLGenerator generator = new TableMetaXMLGenerator();
						final String metaXml = generator.generateTablesMetaXml(new ArrayList<TableMeta>(this.dynamicTable.values()));
						GeneralUtility.writeDataToFile(metaXml.getBytes(), file);
						Logger.printCurrentTime("Table meta generation done");
					}
				}
			} finally {
				// ile.deleteOnExit();
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void loadMetaFiles(final InputStream in) throws FileNotFoundException, JKXmlException {
		final TableMetaXmlParser parser = new TableMetaXmlParser();
		final Hashtable<String, TableMeta> newTables = parser.parse(in, "user");
		addTablesMeta(newTables);
		// TODO : make the following statement for another location to remove
		// coupling
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	private Hashtable<String, TableMeta> toHashTable(final ArrayList<TableMeta> tablesMeta, final String source) {
		final Hashtable<String, TableMeta> hash = new Hashtable<String, TableMeta>();
		for (final TableMeta tableMeta : tablesMeta) {
			tableMeta.setSource(source);
			hash.put(tableMeta.getTableId(), tableMeta);
		}
		return hash;
	}

	// /////////////////////////////////////////////////////////////////////////////////////
	public void writeDynamicMeta(final java.io.OutputStream out) throws IOException {
		final TableMetaXMLGenerator generator = new TableMetaXMLGenerator();
		final String xml = generator.generateTablesMetaXml(new ArrayList<TableMeta>(this.dynamicTable.values()));
		out.write(xml.getBytes());
		out.close();
	}

}
