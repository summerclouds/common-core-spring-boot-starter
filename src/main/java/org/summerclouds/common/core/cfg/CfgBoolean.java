package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.util.Value;

public class CfgBoolean extends AbstractCfg<Boolean> {

	public CfgBoolean(Class<?> owner, String param, Boolean def) {
		super(owner, param, def);
	}

	public CfgBoolean(String name, Boolean def) {
		super(name, def);
	}

	@Override
	protected Boolean valueOf(String value) {
		try {
			Value<Boolean> val = new Value<>();
			MCast.OBJECT_TO_BOOLEAN.toBoolean(value, false, val);
			return val.getValue(); // could be null
		} catch (Throwable t) {
			return null;
		}
	}

}
