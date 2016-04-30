package com.fs.commons.locale;

import java.io.Serializable;

public class Locale implements Serializable {
	public static final Locale ARABIC=new Locale(1,"ar");
	public static final Locale ENGLISH=new Locale(2,"en"); 
	int languageId;

	String languageName;
	
	/**
	 * 
	 * @param langId
	 * @param languageName
	 */
	public Locale(int langId,String languageName) {
		languageId = langId;
		this.languageName = languageName;
	}
	/**
	 * @return the languageId
	 */
	public int getLanguageId() {
		return languageId;
	}

	/**
	 * @param languageId
	 *            the languageId to set
	 */
	public void setLanguageId(int languageId) {
		this.languageId = languageId;
	}

	/**
	 * @return the languageName
	 */
	public String getLanguageName() {
		return languageName;
	}

	/**
	 * @param languageName
	 *            the languageName to set
	 */
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
	
	public static Locale valueOf(String localeString){
		return localeString.equals("ar")?ARABIC:ENGLISH;
	}
	public static Locale valueOf(int languageId) {
		return languageId==1?ARABIC:ENGLISH;
	}

}
