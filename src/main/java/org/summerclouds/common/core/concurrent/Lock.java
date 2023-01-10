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

import java.io.Closeable;

import org.summerclouds.common.core.error.TimeoutException;
import org.summerclouds.common.core.error.TimeoutRuntimeException;
import org.summerclouds.common.core.tool.MTracing;
import org.summerclouds.common.core.tracing.IScope;

public interface Lock extends Closeable {

    Lock lock();

    /**
     * The method will throw an exception after the timeout is reached.
     *
     * @param timeout
     * @return The lock itself
     * @throws TimeoutException If timeout is reached
     */
    default Lock lockWithException(long timeout) throws TimeoutException {
        if (lock(timeout)) return this;
        throw new TimeoutException(getName());
    }

    /**
     * The method will unlock the resource automatically and try to gain access after timeout is
     * reached.
     *
     * @param timeout
     * @return The lock itself
     */
    default Lock lockWithUnlock(long timeout) {
        while (true) {
            if (lock(timeout)) return this;
            unlockHard();
        }
    }

    /**
     * Try to lock the resource or return false if tmeout is reached.
     *
     * @param timeout
     * @return false if timeout is reached. true if locked.
     */
    boolean lock(long timeout);

    /**
     * Run the given task in a locked environment.
     *
     * @param task
     */
    default void lock(Runnable task) {
        try (Lock lock = lock()) {
            task.run();
        }
    }

    /**
     * Run the given task in a locked environment.
     *
     * @param task
     * @param timeout
     * @throws TimeoutException
     */
    default void lock(Runnable task, long timeout) throws TimeoutException {
        try (Lock lock = lockWithException(timeout)) {
            task.run();
        }
    }

    /**
     * Unlock if the current thread is also the owner.
     *
     * @return true if unlock was successful
     */
    boolean unlock();

    /**
     * A synonym to unlock()
     *
     * @return true if unlock was successful
     */
    default boolean release() {
        return unlock();
    }

    /** Unlock in every case !!! This can break a locked area. */
    void unlockHard();

    /** Sleeps until the lock is released. */
    default void waitUntilUnlock() {
        synchronized (this) {
            while (isLocked()) {
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }
        }
    }

    default void waitWithException(long timeout) {
        if (waitUntilUnlock(timeout)) return;
        throw new TimeoutRuntimeException(getName());
    }

    @SuppressWarnings("resource")
    default boolean waitUntilUnlock(long timeout) {
        IScope scope = null;
        try {
            synchronized (this) {
                while (isLocked()) {
                    long start = System.currentTimeMillis();
                    if (scope != null) scope = MTracing.enter("waitUntilUnlock", "name", getName());
                    try {
                        wait(timeout);
                    } catch (InterruptedException e) {
                    }
                    if (System.currentTimeMillis() - start >= timeout) return false;
                }
                return true;
            }
        } finally {
            if (scope != null) scope.close();
        }
    }

    boolean isLocked();

    String getName();

    String getOwner();

    long getLockTime();

    @Override
    default void close() {
        unlock();
    }

    /**
     * Some locks need to be refreshed.
     *
     * @return True if locked
     */
    boolean refresh();

    long getCnt();

    String getStartStackTrace();
}
