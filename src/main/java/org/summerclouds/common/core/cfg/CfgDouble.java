package org.summerclouds.common.core.cfg;

public class CfgDouble extends AbstractCfg<Double> {

	public CfgDouble(Class<?> owner, String param, Double def) {
		super(owner, param, def);
	}

	public CfgDouble(String name, Double def) {
		super(name, def);
	}

	@Override
	protected Double valueOf(String value) {
		try {
			return Double.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}

}
