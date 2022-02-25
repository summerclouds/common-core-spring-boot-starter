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
