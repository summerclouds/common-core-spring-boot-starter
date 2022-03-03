package org.summerclouds.common.core.crypt;

import java.util.UUID;

public interface KeychainSource {

    /**
     * Return a entry by id or null if not found.
     *
     * @param id
     * @return The id or null
     */
    KeyEntry getEntry(UUID id);

    /**
     * Return a not editable list of current stored entry ids.
     *
     * @return a list of ids.
     */
    Iterable<UUID> getEntryIds();

    /**
     * Return a unique name of the source.
     *
     * @return the name
     */
    String getName();

    /**
     * Return a editable instance or null if not supported
     *
     * @return editable vault source
     */
    MutableKeychainSource getEditable();

    /**
     * Return a entry by name or null if not found. Return the first entry found.
     *
     * @param name
     * @return The id or null
     */
    KeyEntry getEntry(String name);
    
}
