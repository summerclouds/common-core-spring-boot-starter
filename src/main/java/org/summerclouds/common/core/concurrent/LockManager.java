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
package org.summerclouds.common.core.concurrent;

import java.util.function.Function;

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(LockManagerImpl.class)
public interface LockManager {

    Lock getLock(String name);

    /**
     * Return a list of names with current locks.
     *
     * @return List
     */
    Lock[] managedLocks();

    void register(Lock lock);

    Lock[] getRegisteredLocks();

    /**
     * Returns the lock or will create it using the creator.
     *
     * @param name
     * @param creator
     * @return The lock
     */
    Lock getLock(String name, Function<String, Lock> creator);
}
