/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package org.summerclouds.common.core.util;

import java.util.Vector;

import org.summerclouds.common.core.cfg.CfgLong;
import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.tool.MThread;
import org.summerclouds.common.core.util.ThreadPool.ThreadContainer;

public class ThreadPoolManager extends MLog {

    public static CfgLong CFG_SLEEP_TIME = new CfgLong("org.summerclouds.common.core.util.ThreadPoolManager.sleepTime", 1000l * 60 * 10);
    public static CfgLong CFG_PENDING_TIME = new CfgLong("org.summerclouds.common.core.util.ThreadPoolManager.pendingTime", 1000l * 60);
    private Vector<ThreadContainer> pool = new Vector<ThreadContainer>();
    private ThreadGroup group = new ThreadGroup("MThreadPool");
    private Thread housekeeper;

    private class ThreadHousekeeper implements Runnable {

        @Override
        public void run() {
        	while (true) {
        		MThread.sleepForSure(CFG_SLEEP_TIME.value());
        		if (housekeeper == null) {
    	            log().t("EXIT Housekeeper");
        			return;
        		}
	            log().t("Housekeeper");
	            poolClean(CFG_PENDING_TIME.value());
	            try {
	                ThreadPoolDaemon.poolClean(CFG_PENDING_TIME.value());
	            } catch (NoClassDefFoundError e) {
	            	log().e(e);
	            }
	        }
        }
    }

    ThreadContainer start(ThreadPool _task, String _name) {

        ThreadContainer tc = null;
        synchronized (pool) {
            if (housekeeper == null) {
                housekeeper = new Thread( new ThreadHousekeeper(), "ThreadPoolManager.housekeeper" );
                housekeeper.setDaemon(true);
                housekeeper.start();
            }
            // search free thread

            for (int i = 0; i < pool.size(); i++)
                if (!pool.elementAt(i).isWorking()) {
                    tc = pool.elementAt(i);
                    break;
                }

            if (tc == null) {
                tc = new ThreadContainer(group, "AT" + pool.size());
                tc.start();
                pool.addElement(tc);
            }

            log().t("###: NEW THREAD@POOL", tc.getId());
            tc.setName(_name);
            tc.newWork(_task);
        }

        return tc;
    }

    public void poolClean(long pendingTime) {
        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (int i = 0; i < list.length; i++) {
                long sleep = list[i].getSleepTime();
                if (sleep != 0 && sleep <= pendingTime) {
                    pool.remove(list[i]);
                    list[i].stopRunning();
                }
            }
        }
    }

    public void poolClean() {

        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (int i = 0; i < list.length; i++) {
                if (!list[i].isWorking()) {
                    pool.remove(list[i]);
                    list[i].stopRunning();
                }
            }
        }
    }

    public int poolSize() {
        synchronized (pool) {
            return pool.size();
        }
    }

    public int poolWorkingSize() {
        int size = 0;
        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (int i = 0; i < list.length; i++) {
                if (list[i].isWorking()) size++;
            }
        }
        return size;
    }

    @Override
    protected void finalize() {
        log().t("finalize");
        housekeeper = null;
        synchronized (pool) {
            ThreadContainer[] list = pool.toArray(new ThreadContainer[pool.size()]);
            for (ThreadContainer tc : list) {
                tc.stopRunning();
            }
        }
    }
}
