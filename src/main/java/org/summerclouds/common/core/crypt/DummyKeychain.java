package org.summerclouds.common.core.crypt;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DummyKeychain implements IKeychain {

	private Map<String, KeychainSource> sources = new HashMap<>();
	
	@Override
	public void registerSource(KeychainSource source) {
		sources.put(source.getName(), source);
	}

	@Override
	public void unregisterSource(String sourceName) {
		sources.remove(sourceName);
	}

	@Override
	public String[] getSourceNames() {
		return sources.keySet().toArray(new String[0]);
	}

	@Override
	public KeychainSource getSource(String name) {
		return sources.get(name);
	}

	@Override
	public KeyEntry getEntry(UUID id) {
		for (KeychainSource source : sources.values()) {
			KeyEntry entry = source.getEntry(id);
			if (entry != null) return entry;
		}
		return null;
	}

	@Override
	public KeyEntry getEntry(String name) {
		for (KeychainSource source : sources.values()) {
			KeyEntry entry = source.getEntry(name);
			if (entry != null) return entry;
		}
		return null;
	}

}
