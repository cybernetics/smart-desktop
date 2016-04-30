/**
 * 
 */
package com.fs.commons.application.ui;

import java.util.Properties;

/**
 * @author u087
 * 
 */
public interface UIPanelFactory {
	public UIPanel createPanel(Properties prop,boolean createNew) throws UIOPanelCreationException;
}
