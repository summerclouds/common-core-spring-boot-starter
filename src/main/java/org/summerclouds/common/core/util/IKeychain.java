package org.summerclouds.common.core.util;

import java.util.UUID;

import org.summerclouds.common.core.activator.DefaultImplementation;
import org.summerclouds.common.core.tool.MKeychain.KeyEntry;
import org.summerclouds.common.core.tool.MKeychain.KeychainSource;

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
