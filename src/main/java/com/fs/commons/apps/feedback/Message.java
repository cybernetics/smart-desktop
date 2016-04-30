/**
 * Modification history
 * ====================================================
 * Version    Date         Developer        Purpose 
 * ====================================================
 * 1.1      03/07/2008     Jamil Shreet    -Add the following class : 
 */
package com.fs.commons.apps.feedback;

/**
 * @1.1
 * @author ASUS
 * 
 */
public class Message {

	private String panelName;
	private String errorDesc;
	private String errorScenario;

	/**
	 * @return the panelName
	 */
	public String getPanelName() {
		return panelName;
	}

	/**
	 * @param panelName
	 *            the panelName to set
	 */
	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	/**
	 * @return the errorDesc
	 */
	public String getErrorDesc() {
		return errorDesc;
	}

	/**
	 * @param errorDesc
	 *            the errorDesc to set
	 */
	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	/**
	 * @return the errorScenario
	 */
	public String getErrorScenario() {
		return errorScenario;
	}

	/**
	 * @param errorScenario
	 *            the errorScenario to set
	 */
	public void setErrorScenario(String errorScenario) {
		this.errorScenario = errorScenario;
	}

}
