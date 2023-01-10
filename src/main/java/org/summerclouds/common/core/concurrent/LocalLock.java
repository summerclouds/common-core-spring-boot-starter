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

import org.springframework.beans.factory.annotation.Autowired;
import org.summerclouds.common.core.M;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.tool.MTracing;
import org.summerclouds.common.core.tracing.IScope;

public class LocalLock extends MLog implements Lock {

    protected volatile Thread lock = null;
    protected String name;
    protected volatile long lockTime = 0;
    protected volatile long cnt = 0;
    protected volatile String stacktrace;

    @Autowired protected LockManager lockManager;

    public LocalLock() {}

    public LocalLock(String name) {
        setName(name);
        register();
    }

    protected void register() {
        if (lockManager == null) // fallback for non spring
        lockManager = M.l(LockManager.class);
        lockManager.register(this);
    }

    @SuppressWarnings("resource")
    @Override
    public Lock lock() {
        IScope scope = null;
        try {
            synchronized (this) {
                while (isLocked()) {
                    if (scope != null) scope = MTracing.enter("waitUntilUnlock", "name", getName());
                    try {
                        wait();
                    } catch (InterruptedException e) {

                    }
                }
                lock = Thread.currentThread();
                stacktrace = MCast.toString("", lock.getStackTrace());
                lockTime = System.currentTimeMillis();
                cnt++;
                lockEvent(true);
            }
        } finally {
            if (scope != null) scope.close();
        }
        return this;
    }

    /**
     * Overwrite this to get the lock events.
     *
     * @param locked
     */
    protected void lockEvent(boolean locked) {}

    @SuppressWarnings("resource")
    @Override
    public boolean lock(long timeout) {
        log().t("lock", name, timeout);
        IScope scope = null;
        try {
            synchronized (this) {
                long start = System.currentTimeMillis();
                while (isLocked()) {
                    if (scope != null) scope = MTracing.enter("waitUntilUnlock", "name", getName());
                    try {
                        wait(timeout);
                    } catch (InterruptedException e) {
                    }
                    if (System.currentTimeMillis() - start >= timeout) {
                        log().t("timeout lock", name);
                        return false;
                    }
                }
                lock = Thread.currentThread();
                stacktrace = MCast.toString("", lock.getStackTrace());
                lockTime = System.currentTimeMillis();
                lockEvent(true);
                log().t("gain lock", name);
                return true;
            }
        } finally {
            if (scope != null) scope.close();
        }
    }

    /**
     * Unlock if the current thread is also the owner.
     *
     * @return true if unlock was successful
     */
    @Override
    public boolean unlock() {
        log().t("unlock", name);
        synchronized (this) {
            if (lock != Thread.currentThread()) return false;
            lockEvent(false);
            lock = null;
            stacktrace = null;
            lockTime = 0;
            notify();
            return true;
        }
    }

    /** Unlock in every case !!! This can break a locked area. */
    @Override
    public void unlockHard() {
        log().d("unlockHard", name);
        synchronized (this) {
            lockEvent(false);
            lock = null;
            stacktrace = null;
            lockTime = 0;
            notify();
        }
    }

    @Override
    public boolean isLocked() {
        return lock != null;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getOwner() {
        return lock == null ? null : lock.getId() + " " + lock.toString();
    }

    @Override
    public String toString() {
        return name + (lock != null ? " " + lock.getName() : "");
    }

    @Override
    public long getLockTime() {
        return lockTime;
    }

    @Override
    public boolean refresh() {
        return isLocked();
    }

    @Override
    public long getCnt() {
        return cnt;
    }

    @Override
    public String getStartStackTrace() {
        return stacktrace;
    }
}
