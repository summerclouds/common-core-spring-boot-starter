package org.summerclouds.common.core.cfg;

public class CfgInt extends ICfg<Integer> {

	public CfgInt(Class<?> owner, String param, Integer def) {
		super(owner, param, def);
	}

	public CfgInt(String name, Integer def) {
		super(name, def);
	}

	@Override
	protected Integer valueOf(String value) {
		try {
			return Integer.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}

}
