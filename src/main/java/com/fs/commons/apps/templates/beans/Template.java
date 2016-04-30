package com.fs.commons.apps.templates.beans;

import java.util.ArrayList;

public class Template {

	int tempId;
	String tempName;
	String tempTitle;
	String tempText;
	private ArrayList<TemplateVariable> variables;

	// ///////////////////////////////////////////////////////////////
	public int getTempId() {
		return tempId;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempId(int tempId) {
		this.tempId = tempId;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTempName() {
		return tempName;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempName(String tempName) {
		this.tempName = tempName;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTempTitle() {
		return tempTitle;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}

	// ///////////////////////////////////////////////////////////////
	public String getTempText() {
		return tempText;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempText(String tempText) {
		this.tempText = tempText;
	}

	public void setVariables(ArrayList<TemplateVariable> variables) {
		this.variables = variables;
	}

	public ArrayList<TemplateVariable> getVariables() {
		return variables;
	}

}
