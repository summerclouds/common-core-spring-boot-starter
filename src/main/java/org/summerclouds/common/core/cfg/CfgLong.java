package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;

public class CfgLong {

	private String name;
	private long def;
	private long value;
	private boolean set = false;

	public CfgLong(String name, long def) {
		this.name = name;
		this.def = def;
	}
	
	public long value() {
		if (set) return value;
		Long v = MSpring.getValueLong(name);
		if (v != null) {
			value = v;
			set = true;
			return value;
		} else {
			return def;
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf(value());
	}

}
