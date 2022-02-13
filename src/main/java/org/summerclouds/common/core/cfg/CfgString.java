package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;

public class CfgString {

	private String name;
	private String def;
	private String value;

	public CfgString(String name, String def) {
		this.name = name;
		this.def = def;
	}
	
	public String value() {
		if (value != null) return value;
		value = MSpring.getValue(name);
		return value == null ? def : value;
	}

	@Override
	public String toString() {
		return value();
	}
	
}
