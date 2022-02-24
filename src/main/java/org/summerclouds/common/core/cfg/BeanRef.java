package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;

public class BeanRef<T> {

	private Class<T> clazz;
	private T inst;

	public BeanRef(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T bean() {
		if (!MSpring.isStarted())
			return MSpring.lookup(clazz);
		if (inst == null)
			inst = MSpring.lookup(clazz);
		return inst;
	}

}
