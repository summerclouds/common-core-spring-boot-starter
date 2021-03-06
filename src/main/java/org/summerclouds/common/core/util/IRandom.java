package org.summerclouds.common.core.util;

import java.security.SecureRandom;
import java.util.Random;

import org.summerclouds.common.core.activator.DefaultImplementation;
import org.summerclouds.common.core.error.NotSupportedException;

@DefaultImplementation(DefaultRandom.class)
public interface IRandom {

    /**
     * Return a random byte from -127 to 128
     *
     * @return a random yte
     */
    byte getByte();

    /**
     * Return a random integer from 0 to INTEGER MAX.
     *
     * @return a random integer
     */
    int getInt();

    /**
     * Return a random double from 0 to 1
     *
     * @return random double
     */
    double getDouble();

    /**
     * Return a random long from 0 to LONG MAX.
     *
     * @return random long
     */
    long getLong();

    /**
     * Return an adaption of an random if available. e.g. java.util.Random and java.security.Random
     * should be supported.
     *
     * @param ifc Requested Interface or Class
     * @return The instance of Ifc
     * @throws NotSupportedException If adaption was not possible.
     */
    <T> T adaptTo(Class<? extends T> ifc)
            throws NotSupportedException; // adaptTo java.util.Random or java.secure.Random if
    // available

    /**
     * Return a random readable character
     *
     * @return random character
     */
    char getChar();

	SecureRandom getSecureRandom();

	Random getRandom();

}
