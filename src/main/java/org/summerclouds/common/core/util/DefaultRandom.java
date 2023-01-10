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
package org.summerclouds.common.core.util;

import java.security.SecureRandom;
import java.util.Random;

import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.tool.MString;

public class DefaultRandom implements IRandom {

    private SecureRandom secureRandom;
    private Random rand;

    @Override
    public byte getByte() {
        return (byte) (random() * 255);
    }

    @Override
    public int getInt() {
        return (int) (random() * Integer.MAX_VALUE); // no negative values!
    }

    @Override
    public double getDouble() {
        return random();
    }

    @Override
    public long getLong() {
        return (long) (random() * Long.MAX_VALUE); // no negative values!
    }

    /**
     * Overwrite this to deliver your own random numbers
     *
     * @return
     */
    protected double random() {
        return Math.random();
    }

    public <T> T adaptTo(Class<? extends T> ifc) {
        return null;
    }

    @Override
    public char getChar() {
        return MString.CHARS_READABLE[getInt() % MString.CHARS_READABLE.length];
    }

    @Override
    public synchronized SecureRandom getSecureRandom() {
        try {
            if (secureRandom == null) secureRandom = new MySecureRandom();
        } catch (Exception e) {
            Log.getLog(DefaultRandom.class).e(e);
        }
        return secureRandom;
    }

    @Override
    public synchronized Random getRandom() {
        if (rand == null) rand = new MyRandom();
        return rand;
    }

    private class MySecureRandom extends SecureRandom {

        private static final long serialVersionUID = 1L;

        @Override
        public synchronized void nextBytes(byte[] bytes) {
            super.nextBytes(bytes);
            byte b = getByte();
            for (int i = 0; i < bytes.length; i++) bytes[i] = (byte) ((bytes[i] + b) & 255);
        }
    }

    private class MyRandom extends Random {
        private static final long serialVersionUID = 1L;

        MyRandom() {
            super(getLong());
        }

        @Override
        protected int next(int bits) {
            setSeed(getLong());
            return super.next(bits);
        }
    }
}
