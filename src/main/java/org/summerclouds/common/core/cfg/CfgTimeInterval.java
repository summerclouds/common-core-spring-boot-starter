package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MPeriod;

public class CfgTimeInterval extends AbstractCfg<String> {

	public CfgTimeInterval(Class<?> owner, String param, String def) {
		super(owner, param, def);
	}

	public CfgTimeInterval(String name, String def) {
		super(name, def);
	}

	@Override
	protected String valueOf(String value) {
		return value;
	}

	public long interval() {
		return MPeriod.toTime(value(), -1);
	}
	

}
