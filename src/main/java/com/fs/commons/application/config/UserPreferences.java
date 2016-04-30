/*
 * Copyright 2002-2016 Jalal Kiswani.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fs.commons.application.config;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.fs.commons.util.ExceptionUtil;

public class UserPreferences {
	private static Preferences systemRoot = Preferences.userRoot();
	private static String keyPrefix = "app-";

	public static void clear() throws BackingStoreException {
		try {
			systemRoot.clear();
		} catch (final Exception e) {
			System.err.print(e);
		}
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	protected static String fixKey(final String key) {
		if (key.startsWith(getKeyPrefix())) {
			return key;
		}
		return getKeyPrefix() + "-" + key;
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#get(java.lang.String, java.lang.String)
	 */
	public static String get(final String key, final String def) {
		try {
			return systemRoot.get(fixKey(key), def);
		} catch (final Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getBoolean(java.lang.String, boolean)
	 */
	public static boolean getBoolean(final String key, final boolean def) {
		try {
			return systemRoot.getBoolean(fixKey(key), def);
		} catch (final Exception e) {
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
	public static float getFloat(final String key, final float def) {
		try {
			return systemRoot.getFloat(fixKey(key), def);
		} catch (final Exception e) {
			// just eat the exception to avoid any system
			// crash on system issues
			return def;
		}
	}

	/**
	 *
	 * @param name
	 * @param hash
	 * @return
	 */
	public static Hashtable<String, String> getHashtable(final String name) {
		final Hashtable<String, String> hash = new Hashtable<String, String>();
		try {
			final String configStr = UserPreferences.get(fixKey(name), "");
			if (!configStr.equals("")) {
				final String[] rows = configStr.split(";");
				for (final String row : rows) {
					final String[] split = row.split(":");
					if (split.length == 2) {
						final String key = split[0];
						final String value = split[1];
						hash.put(key, value);
					}
				}
			}
		} catch (final Exception e) {
			// just eat the exception to avoid any system crash on system issues
		}
		return hash;
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getInt(java.lang.String, int)
	 */
	public static int getInt(final String key, final int def) {
		try {
			return systemRoot.getInt(fixKey(key), def);
		} catch (final Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	public static String getKeyPrefix() {
		return keyPrefix;
	}

	/**
	 * @return
	 * @throws BackingStoreException
	 * @see java.util.prefs.Preferences#keys()
	 */
	public static String[] keys() throws BackingStoreException {
		try {
			return systemRoot.keys();
		} catch (final Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return new String[0];
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#put(java.lang.String, java.lang.String)
	 */
	public static void put(final String key, final String value) {
		try {
			systemRoot.put(fixKey(key), value);
		} catch (final Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putBoolean(java.lang.String, boolean)
	 */
	public static void putBoolean(final String key, final boolean value) {
		try {
			systemRoot.putBoolean(fixKey(key), value);
		} catch (final Exception e) {
			System.err.print(e);
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putFloat(java.lang.String, float)
	 */
	public static void putFloat(final String key, final float value) {
		try {
			systemRoot.putFloat(fixKey(key), value);
		} catch (final Exception e) {
			System.err.print(e);
		}
	}

	/**
	 *
	 * @param name
	 * @param hash
	 */
	public static void putHashTable(final String name, final Hashtable hash) {
		final Enumeration<String> keys = hash.keys();
		final StringBuffer buf = new StringBuffer("");
		while (keys.hasMoreElements()) {
			if (!buf.toString().equals("")) {
				// end the previous record
				buf.append(";");
			}
			final String key = keys.nextElement();
			final String value = hash.get(key).toString();
			buf.append(key + ":" + value);
		}
		put(fixKey(name), buf.toString());
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putInt(java.lang.String, int)
	 */
	public static void putInt(final String key, final int value) {
		try {
			systemRoot.putInt(fixKey(key), value);
		} catch (final Exception e) {
			System.err.print(e);
		}
	}

	public static void setKeyPrefix(final String keyPrefix) {
		UserPreferences.keyPrefix = keyPrefix;
		try {
			systemRoot.sync();
		} catch (final Exception e) {
			ExceptionUtil.handleException(e);
		}
	}

	/**
	 * @param key
	 * @param def
	 * @return
	 * @see java.util.prefs.Preferences#getByteArray(java.lang.String, byte[])
	 */
	public byte[] getByteArray(final String key, final byte[] def) {
		try {
			return systemRoot.getByteArray(fixKey(key), def);
		} catch (final Exception e) {
			// just eat the exception to avoid any system crash on system issues
			return def;
		}
	}

	/**
	 * @param key
	 * @param value
	 * @see java.util.prefs.Preferences#putByteArray(java.lang.String, byte[])
	 */
	public void putByteArray(final String key, final byte[] value) {
		try {
			systemRoot.putByteArray(fixKey(key), value);
		} catch (final Exception e) {
			System.err.print(e);
		}
	}

}
