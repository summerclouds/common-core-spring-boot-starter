package org.summerclouds.common.core.cfg;

public class CfgString extends ICfg<String> {

	public CfgString(Class<?> owner, String param, String def) {
		super(owner, param, def);
	}

	public CfgString(String name, String def) {
		super(name, def);
	}

	@Override
	protected String valueOf(String value) {
		return value;
	}


}
