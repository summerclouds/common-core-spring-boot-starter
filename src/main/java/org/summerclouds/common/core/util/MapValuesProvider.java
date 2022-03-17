package org.summerclouds.common.core.util;

import java.util.Map;

public class MapValuesProvider implements IValuesProvider {

	private Map<String, Object> map;

	public MapValuesProvider(Map<String,Object> map) {
		this.map = map;
	}
	
	@Override
	public Object get(String key) {
		return map.get(key);
	}

	@Override
	public String toString() {
		return String.valueOf(map);
	}

	public Map<String, Object> getMap() {
		return map;
	}

}
