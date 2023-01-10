/**
 * Copyright (C) 2022 Mike Hummel (mh@mhus.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
