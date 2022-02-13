package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.M;

public class BeanRef<T> {

	private Class<T> clazz;
	private Class<? extends T> def;

	public BeanRef(Class<T> clazz, Class<? extends T> def) {
		this.clazz = clazz;
		this.def = def;
	}
	
	public T bean() {
		return M.l(clazz, def);
	}
	
}
