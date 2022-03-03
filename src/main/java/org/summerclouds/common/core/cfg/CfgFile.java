package org.summerclouds.common.core.cfg;

import java.io.File;

public class CfgFile extends AbstractCfg<File> {

	public CfgFile(Class<?> owner, String param, File def) {
		super(owner, param, def);
	}

	public CfgFile(String name, File def) {
		super(name, def);
	}

	public CfgFile(Class<?> owner, String param, String def) {
		super(owner, param, new File(def));
	}

	public CfgFile(String name, String def) {
		super(name, new File(def));
	}
	
	@Override
	protected File valueOf(String value) {
		if (value == null) return null;
		return new File(value);
	}


}
