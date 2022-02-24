package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public abstract class ICfg<T> {

	private T value;
	private String name;
	private T def;
	
	public ICfg(String name, T def) {
		this.name = name;
		this.def = def;
	}

	public ICfg(Class<?> owner, String param, T def) {
		this.name = MSystem.getOwnerName(owner).replace(".", "_") + "." + name;
		this.def = def;
	}

	public T value() {
		if (value != null) return value;
		value = valueOf(MSpring.getValue(name));
		if (value == null) {
			if(MSpring.isStarted())
				value = def;
			else {
				String strValue = System.getenv().getOrDefault("app." + name, null);
				if (strValue == null) return def;
				T v = valueOf( strValue);
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
}
