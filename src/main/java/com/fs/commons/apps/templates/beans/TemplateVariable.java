package com.fs.commons.apps.templates.beans;

public class TemplateVariable {

	int tempVarId;
	Template temp;
	int varIndex;
	Variable var;

	// ///////////////////////////////////////////////////////////////
	public int getTempVarId() {
		return tempVarId;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTempVarId(int tempVarId) {
		this.tempVarId = tempVarId;
	}

	// ///////////////////////////////////////////////////////////////
	public Template getTemp() {
		return temp;
	}

	// ///////////////////////////////////////////////////////////////
	public void setTemp(Template temp) {
		this.temp = temp;
	}

	// ///////////////////////////////////////////////////////////////
	public int getVarIndex() {
		return varIndex;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVarIndex(int varIndex) {
		this.varIndex = varIndex;
	}

	// ///////////////////////////////////////////////////////////////
	public Variable getVar() {
		return var;
	}

	// ///////////////////////////////////////////////////////////////
	public void setVar(Variable var) {
		this.var = var;
	}

}
