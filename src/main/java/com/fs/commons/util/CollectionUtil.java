package com.fs.commons.util;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

public class CollectionUtil {
	/**
	 * Unify the objects in the list according to the hash code IMPORTANT :
	 * hashcode usage here is different from the Java Spec , IT SHOULD BE
	 * UNIUQUE FOR EACH OBJECT WHICH SHOULD HOLD SOMTHING LIKE THE DATABASE
	 * PRIMARY KEY
	 * 
	 * @param config
	 */
	public static void unifyReferences(Hashtable hash, List list) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				Object itemAtList = list.get(i);
				Object unifiedReferences = unifyReferences(hash, itemAtList);
				list.set(i, unifiedReferences);
			}
		}
	}

	// //////////////////////////////////////////////////////////
	/**
	 * return unified reference
	 */
	public static Object unifyReferences(Hashtable hash, Object object) {
		Object itemAtHash = hash.get(object.hashCode());
		if (itemAtHash == null) {
			hash.put(object.hashCode(), object);
		} else {
			object = itemAtHash;
		}
		return object;
	}

	// //////////////////////////////////////////////////////////
	public static Object gatRandomItem(List items) {
		if (items == null || items.size() == 0) {
			return null;
		}
		int itemIndex = (int) (Math.random() * items.size());
		return items.get(itemIndex);
	}

	public static String fixPropertyKey(String name) {
		return name.toLowerCase().replace("_", "-");
	}

	public static void fixPropertiesKeys(Properties prop) {
		Enumeration<Object> keys = prop.keys();
		while(keys.hasMoreElements()){
			String currentKey=(String) keys.nextElement();
			String fixedKey=fixPropertyKey(currentKey);
			String value=prop.getProperty(currentKey);
			prop.remove(currentKey);
			prop.setProperty(fixedKey, value);
		}
	}
}
