package com.fs.commons.configuration.beans;

public class Language {
	private int langId;
	private String langName;
	private String langShortName;
	private LangDirection LangDirection;

	public int getLangId() {
		return langId;
	}

	public void setLangId(int langId) {
		this.langId = langId;
	}

	public String getLangName() {
		return langName;
	}

	public void setLangName(String langName) {
		this.langName = langName;
	}

	public String getLangShortName() {
		return langShortName;
	}

	public void setLangShortName(String langShortName) {
		this.langShortName = langShortName;
	}

	public LangDirection getLangDirection() {
		return LangDirection;
	}

	public void setLangDirection(LangDirection langDirection) {
		LangDirection = langDirection;
	}

}
