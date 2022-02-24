package org.summerclouds.common.core.cfg;

public class CfgFloat extends AbstractCfg<Float> {

	public CfgFloat(Class<?> owner, String param, Float def) {
		super(owner, param, def);
	}

	public CfgFloat(String name, Float def) {
		super(name, def);
	}

	@Override
	protected Float valueOf(String value) {
		try {
			return Float.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}

}
