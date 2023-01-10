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

import java.util.UUID;

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(DummyKeychain.class)
public interface IKeychain {

    /**
     * Register a new source for VaultEntries
     *
     * @param source
     */
    void registerSource(KeychainSource source);
    /**
     * Unregister a registered source
     *
     * @param sourceName
     */
    void unregisterSource(String sourceName);

    /**
     * Return a list of registered sources
     *
     * @return a list of names.
     */
    String[] getSourceNames();

    /**
     * Return a single source or null if not found.
     *
     * @param name
     * @return the source or null.
     */
    KeychainSource getSource(String name);

    /**
     * Return a entry by id or null if not found.
     *
     * @param id
     * @return The entry or null.
     */
    KeyEntry getEntry(UUID id);

    /**
     * Return a entry by name or null if not found. The method will return the first entry found.
     *
     * @param name
     * @return The entry or null.
     */
    KeyEntry getEntry(String name);
}
