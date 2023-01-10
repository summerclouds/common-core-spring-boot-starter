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
