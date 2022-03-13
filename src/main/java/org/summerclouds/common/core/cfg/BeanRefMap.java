package org.summerclouds.common.core.cfg;

import java.util.Collections;
import java.util.Map;

import org.summerclouds.common.core.tool.MSpring;

public class BeanRefMap<T> {

	private Class<T> clazz;
	private Map<String,T> inst;

	public BeanRefMap(Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Return a map of registered beans or null if spring is not ready.
	 * @return
	 */
	public Map<String,T> beans() {
		if (!MSpring.isStarted())
			return MSpring.getBeansOfType(clazz);
		if (inst == null)
			inst = Collections.unmodifiableMap(MSpring.getBeansOfType(clazz));
		return inst;
	}

}
