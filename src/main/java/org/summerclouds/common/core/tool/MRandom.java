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
package org.summerclouds.common.core.tool;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.util.IRandom;

public class MRandom {

    private MRandom() {};

    private static BeanRef<IRandom> instance = new BeanRef<>(IRandom.class);

    public static IRandom get() {
        return instance.bean();
    }

    /**
     * Return a random byte from -127 to 128
     *
     * @return a random byte
     */
    public static byte getByte() {
        return get().getByte();
    }

    /**
     * Return a random integer from 0 to INTEGER MAX.
     *
     * @return a random integer
     */
    public static int getInt() {
        return get().getInt();
    }

    /**
     * Return a random double from 0 to 1
     *
     * @return random double
     */
    public static double getDouble() {
        return get().getDouble();
    }

    /**
     * Return a random long from 0 to LONG MAX.
     *
     * @return random long
     */
    public static long getLong() {
        return get().getLong();
    }

    /**
     * Return a random readable character
     *
     * @return random character
     */
    public static char getChar() {
        return get().getChar();
    }
}
