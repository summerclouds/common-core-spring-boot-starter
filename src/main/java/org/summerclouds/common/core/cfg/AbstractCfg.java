package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public abstract class AbstractCfg<T> {

	private T value;
	private String name;
	private T def;
	
	public AbstractCfg(String name, T def) {
		this.name = name;
		this.def = def;
	}

	public AbstractCfg(Class<?> owner, String param, T def) {
		this.name = MSystem.getOwnerName(owner) + "." + name;
		this.def = def;
	}

	public T value() {
		if (value != null) return value;
		value = valueOf(MSpring.getValue(name));
		if (value == null) {
			if(MSpring.isStarted()) {
				value = def;
			} else {
				String appName = "app." + name;
				String strValue = System.getenv().get(appName);
				if (strValue == null) {
					strValue = System.getProperty(appName);
					if (strValue == null)
						return def;
				}
				T v = valueOf(strValue);
				return v == null ? def : v;
			}
		}
		return value;
		
	}
	
	@Override
	public String toString() {
		return String.valueOf(value());
	}

	protected abstract T valueOf(String value);
	
	public T getDefault() {
		return def;
	}
	
	public void set(T value) {
		this.value = value;
	}
	
}
