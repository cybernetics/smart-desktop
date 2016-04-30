package com.fs.commons.importers;

public interface ImportListener {
	void fireStartImport(Importer importer);
	void fireEndImport(Importer importer);
	void onException(Exception e, Importer importer);
}
