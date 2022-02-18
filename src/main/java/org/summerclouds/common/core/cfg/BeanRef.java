package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;

public class BeanRef<T> {

	private Class<T> clazz;
	private Class<? extends T> def;
	private T inst;

	public BeanRef(Class<T> clazz, Class<? extends T> def) {
		this.clazz = clazz;
		this.def = def;
	}

	public T bean() {
		if (!MSpring.isStarted())
			return MSpring.lookup(clazz, def);
		if (inst == null)
			inst = MSpring.lookup(clazz, def);
		return inst;
	}

}
