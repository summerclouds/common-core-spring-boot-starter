package org.summerclouds.common.core.cfg;

public class CfgLong extends AbstractCfg<Long> {

	public CfgLong(Class<?> owner, String param, Long def) {
		super(owner, param, def);
	}

	public CfgLong(String name, Long def) {
		super(name, def);
	}

	@Override
	protected Long valueOf(String value) {
		try {
			return Long.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}

}
