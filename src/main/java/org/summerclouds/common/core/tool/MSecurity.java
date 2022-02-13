package org.summerclouds.common.core.tool;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.security.DummySecurity;
import org.summerclouds.common.core.security.ISecurity;

public class MSecurity {

	
	private static BeanRef<ISecurity> instance = new BeanRef<>(ISecurity.class, DummySecurity.class);
	
	public static ISecurity get() {
		return instance.bean();
	}

}
