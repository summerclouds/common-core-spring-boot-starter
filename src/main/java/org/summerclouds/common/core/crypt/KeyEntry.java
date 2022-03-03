package org.summerclouds.common.core.crypt;

import java.util.UUID;

import org.summerclouds.common.core.util.SecureString;

public interface KeyEntry {

    /**
     * Returns the unique id of the entry.
     *
     * @return The unique id
     */
    UUID getId();

    /**
     * Returns the type of the entry as string. A list of default types is defined in MVault.
     *
     * @return The type of the entry, never null.
     */
    String getType();

    /**
     * Return a readable description describe the key and/or the usage.
     *
     * @return description
     */
    String getDescription();

    /**
     * Return the value of the entry as text.
     *
     * @return The entry as text.
     */
    SecureString getValue();

    /**
     * Return a technical name of the entry.
     *
     * @return The name
     */
    String getName();
    
}
