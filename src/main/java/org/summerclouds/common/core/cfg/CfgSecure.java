package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.util.SecureString;

public class CfgSecure extends AbstractCfg<SecureString> {

	public CfgSecure(Class<?> owner, String param, SecureString def) {
		super(owner, param, def);
	}

	public CfgSecure(String name, SecureString def) {
		super(name, def);
	}

	public CfgSecure(Class<?> owner, String param, String def) {
		super(owner, param, new SecureString(def));
	}

	public CfgSecure(String name, String def) {
		super(name, new SecureString(def));
	}
	
	@Override
	protected SecureString valueOf(String value) {
		return new SecureString(value);
	}

	public String valueAsString() {
		SecureString v = value();
		return v == null ? null : v.value();
	}


}
