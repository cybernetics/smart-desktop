package com.fs.commons.application.config;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.fs.commons.util.ExceptionUtil;

public class UserPreferences {
	private static Preferences systemRoot = Preferences.userRoot();
	private static String keyPrefix="app-";


	/**
	 * 
	 * @param key
	 * @return
	 */
	protected static String fixKey(String key) {
		if(key.startsWith(getKeyPrefix())){
			return key;
		}
		return getKeyPrefix()+"-"+ key;
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#get(java.lang.String, java.lang.String)
	 */
	public static String get(String key, String def) {
		try {
			return systemRoot.get(fixKey(key), def);
		} catch (Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getByteArray(java.lang.String, byte[])
	 */
	public byte[] getByteArray(String key, byte[] def) {
		try {
			return systemRoot.getByteArray(fixKey(key), def);
		} catch (Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putByteArray(java.lang.String, byte[])
	 */
	public void putByteArray(String key, byte[] value) {
		try {
			systemRoot.putByteArray(fixKey(key), value);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getBoolean(java.lang.String, boolean)
	 */
	public static boolean getBoolean(String key, boolean def) {
		try {
			return systemRoot.getBoolean(fixKey(key), def);
		} catch (Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getFloat(java.lang.String, float)
	 */
	public static float getFloat(String key, float def) {
		try {
			return systemRoot.getFloat(fixKey(key), def);
		} catch (Exception e) {
			// just eat the exception to avoid any system
			// crash on system issues
			return def;
		}
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getInt(java.lang.String, int)
	 */
	public static int getInt(String key, int def) {
		try {
			return systemRoot.getInt(fixKey(key), def);
		} catch (Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	/**
	 * @return
	 * @throws BackingStoreException
	 * @see java.util.prefs.Preferences#keys()
	 */
	public static String[] keys() throws BackingStoreException {
		try {
			return systemRoot.keys();
		} catch (Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return new String[0];
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#put(java.lang.String, java.lang.String)
	 */
	public static void put(String key, String value) {
		try {
			systemRoot.put(fixKey(key), value);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putBoolean(java.lang.String, boolean)
	 */
	public static void putBoolean(String key, boolean value) {
		try {
			systemRoot.putBoolean(fixKey(key), value);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putFloat(java.lang.String, float)
	 */
	public static void putFloat(String key, float value) {
		try {
			systemRoot.putFloat(fixKey(key), value);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putInt(java.lang.String, int)
	 */
	public static void putInt(String key, int value) {
		try {
			systemRoot.putInt(fixKey(key), value);
		} catch (Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * 
	 * @param name
	 * @param hash
	 * @return
	 */
	public static Hashtable<String, String> getHashtable(String name) {
		Hashtable<String, String> hash = new Hashtable<String, String>();
		try {
			String configStr = UserPreferences.get(fixKey(name), "");
			if (!configStr.equals("")) {
				String[] rows = configStr.split(";");
				for (String row : rows) {
					String[] split = row.split(":");
					if (split.length == 2) {
						String key = split[0];
						String value = split[1];
						hash.put(key, value);
					}
				}
			}
		} catch (Exception e) {
			// just eat the exception to avoid any system crash on system issues
		}
		return hash;
	}

	/**
	 * 
	 * @param name
	 * @param hash
	 */
	public static void putHashTable(String name, Hashtable hash) {
		Enumeration<String> keys = hash.keys();
		StringBuffer buf = new StringBuffer("");
		while (keys.hasMoreElements()) {
			if (!buf.toString().equals("")) {
				// end the previous record
				buf.append(";");
			}
			String key = keys.nextElement();
			String value = hash.get(key).toString();
			buf.append(key + ":" + value);
		}
		put(fixKey(name), buf.toString());
	}

	public static void clear() throws BackingStoreException {
		try {
			systemRoot.clear();
		} catch (Exception e) {
			System.err.print(e);
		}
	}
	
	public static String getKeyPrefix() {
		return keyPrefix;
	}

	public static void setKeyPrefix(String keyPrefix) {
		UserPreferences.keyPrefix = keyPrefix;
		try {
			systemRoot.sync();
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

}
