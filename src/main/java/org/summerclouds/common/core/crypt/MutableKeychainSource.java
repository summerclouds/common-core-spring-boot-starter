package org.summerclouds.common.core.crypt;

import java.io.IOException;
import java.util.UUID;

import org.summerclouds.common.core.error.MException;

public interface MutableKeychainSource extends KeychainSource {

    void addEntry(KeyEntry entry) throws MException;

    void removeEntry(UUID id) throws MException;

    void doLoad() throws IOException;

    void doSave() throws IOException;

    /**
     * Return true if load and save is needed to persist changed data.
     *
     * @return true if storage is in memory
     */
    boolean isMemoryBased();

    void updateEntry(KeyEntry entry) throws MException;

}
