package com.fs.commons.util;

import java.io.File;
import java.util.List;

public class TextUtil {

	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isDouble(String txt) {
		try{
			Double.parseDouble(txt);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static String removeExtension(String fileName) {
		String separator = System.getProperty("file.separator");
	    String filename;
	    // Remove the path upto the filename.
	    int lastSeparatorIndex = fileName.lastIndexOf(separator);
	    if (lastSeparatorIndex == -1) {
	        filename = fileName;
	    } else {
	        filename = fileName.substring(lastSeparatorIndex + 1);
	    }

	    // Remove the extension.
	    int extensionIndex = filename.lastIndexOf(".");
	    if (extensionIndex == -1){
	        return filename;
	    }
	    fileName = fileName.substring(0,lastSeparatorIndex);
	    return fileName + File.separator + filename.substring(0, extensionIndex);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static String getExtension(String fileName, boolean withPoint) {
		int lastIndexOf = fileName.lastIndexOf(".");
		if(!withPoint){
			return fileName.substring(lastIndexOf+1);	
		}
		return fileName.substring(lastIndexOf);
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isFloat(String txt) {
		try{
			Float.parseFloat(txt);
			return true;
		}catch (Exception e) {
			return false;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isInteger(String txt) {
		try{
			Integer.parseInt(txt);
			return true;
		}catch (NumberFormatException e) {
			return false;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static String toString(List<?> list, String separtor) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			Object object = list.get(i);
			buf.append(object.toString());
			if (i < list.size() - 1) {
				buf.append(separtor);
			}
		}
		return buf.toString();
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static String fixPropertyName(String name) {
		// captialize every char after the underscore , and remove underscores
		char[] charArray = name.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			if (charArray[i] == '_') {
				charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);
			}
		}
		name = new String(charArray).replaceAll("_", "");
		return name;
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isUpperCase(String txt){
		boolean upper = true;
		for (char c : txt.toCharArray()) {
		    if (Character.isLowerCase(c)) {
		        upper = false;
		        break;
		    }
		}
		return upper;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static String toCamelCase(String s) {
		String[] parts = s.split("_");
		String camelCaseString = "";
		for (String part : parts) {
			camelCaseString = camelCaseString + toProperCase(part) +" " ;
		}
		return camelCaseString;
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	private static String toProperCase(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static String escpeString(String value) {
		return value.replace("'", "");
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isInt(String str) {
		try{
			Integer.parseInt(str);
			return true;
		}catch (NumberFormatException e) {
			return false;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static StringBuilder removeLast(StringBuilder builder, String string) {
		int lastIndexOf = builder.lastIndexOf(string);
		if(lastIndexOf == -1 ){
			return builder;
		}
		return new StringBuilder(builder.substring(0, lastIndexOf));
	}

	////////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isBoolean(String param) {
		if(isEmpty(param)){
			return false;
		}
		return param.equalsIgnoreCase("true")||param.equalsIgnoreCase("false");
	}

	public static String getFirstLine(String message) {
		if(isEmpty(message)){
			return message;
		}
		if(message.contains("\n")){
			return message.split("\n")[0];
		}
		return message;
	}

}
