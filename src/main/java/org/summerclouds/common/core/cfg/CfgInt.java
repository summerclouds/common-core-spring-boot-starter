package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;

public class CfgInt {

	private String name;
	private int def;
	private int value;
	private boolean set = false;

	public CfgInt(String name, int def) {
		this.name = name;
		this.def = def;
	}
	
	public int value() {
		if (set) return value;
		Integer v = MSpring.getValueInt(name);
		if (v != null) {
			value = v;
			set = true;
			return value;
		} else {
			if (MSpring.isStarted()) {
				set = true;
				value = def;
			}
			return def;
		}
	}

	@Override
	public String toString() {
		return String.valueOf(value());
	}

}
