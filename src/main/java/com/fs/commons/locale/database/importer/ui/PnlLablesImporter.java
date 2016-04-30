package com.fs.commons.locale.database.importer.ui;

import java.io.FileNotFoundException;

import javax.swing.BoxLayout;

import com.fs.commons.application.ApplicationException;
import com.fs.commons.application.ApplicationManager;
import com.fs.commons.application.exceptions.ValidationException;
import com.fs.commons.dao.exception.DaoException;
import com.fs.commons.desktop.swing.SwingUtility;
import com.fs.commons.desktop.swing.comp.panels.JKLabledComponent;
import com.fs.commons.desktop.swing.comp.panels.JKPanel;
import com.fs.commons.desktop.swing.comp.panels.PnlImport;
import com.fs.commons.desktop.swing.dao.DaoComboBox;
import com.fs.commons.importers.ImportListenerAdapter;
import com.fs.commons.importers.Importer;

/**
 * 
 * @author mkiswani
 * 
 */
public class PnlLablesImporter extends PnlImport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ////////////////////////////////////////////////////////////////////////////////////
	// Declared by static initilaizor because these components called by
	// getCenterPnl method which is used in the super class
	// before getting the constructor of this class
	static DaoComboBox modules;
	static DaoComboBox languages;
	static {
		modules = new DaoComboBox("select * from conf_modules");
		languages = new DaoComboBox("select * from conf_languages");
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public PnlLablesImporter() {
		super("LABLES_IMPOTRER", LablesImporter.class, "properties");
		addListener(new ParametersSetter());
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected JKPanel<?> getCenterPnl() {
		JKPanel<?> pnl = new JKPanel<Object>();
		pnl.setLayout(new BoxLayout(pnl, BoxLayout.PAGE_AXIS));
		JKPanel<?> pnl2 = new JKPanel<Object>();
		pnl2.setLayout(new BoxLayout(pnl2, BoxLayout.LINE_AXIS));
		pnl2.add(new JKLabledComponent("LABLES_FILE", getFilePanel()));
		pnl2.add(btnImport);
		pnl.add(pnl2);
		pnl.add(new JKLabledComponent("MODULE", modules));
		pnl.add(new JKLabledComponent("LANG", languages));
		return pnl;
	}

	// ///////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void validateFields() throws ValidationException {
		super.validateFields();
		modules.checkEmpty();
		languages.checkEmpty();
	}

	class ParametersSetter extends ImportListenerAdapter {
		@Override
		public void fireStartImport(Importer importer) {
			LablesImporter lablesImporter = (LablesImporter) importer;
			lablesImporter.setModuleId(modules.getSelectedIdValueAsInteger());
			lablesImporter.setLangId(languages.getSelectedIdValueAsInteger());
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) throws FileNotFoundException, ApplicationException, DaoException {
		ApplicationManager instance = ApplicationManager.getInstance();
		instance.init();
		SwingUtility.testPanel(new PnlLablesImporter());
	}

}
