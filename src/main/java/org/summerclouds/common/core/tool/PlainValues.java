package org.summerclouds.common.core.tool;

public class PlainValues {

	public static long getLong(String name, long def) {
		String value = getString(name, null);
		return MCast.tolong(value, def);
	}

	public static int getInt(String name, int def) {
		String value = getString(name, null);
		return MCast.toint(value, def);
	}

	public static boolean getBoolean(String name, boolean def) {
		String value = getString(name, null);
		return MCast.toboolean(value, def);
	}

	public static String getString(String name, String def) {
		String value = System.getenv("app." + name);
		if (value == null) {
			value = System.getProperty("app." + name);
		}
		return value == null ? def : value;
	}

}
