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

public class PlainValues {

    public static long getLong(String name, long def) {
        String value = getString(name, null);
        return MCast.tolong(value, def);
    }

    public static int getInt(String name, int def) {
        String value = getString(name, null);
        return MCast.toint(value, def);
    }

    public static boolean getBoolean(String name, boolean def) {
        String value = getString(name, null);
        return MCast.toboolean(value, def);
    }

    public static String getString(String name, String def) {
        String value = System.getenv("app." + name);
        if (value == null) {
            value = System.getProperty("app." + name);
        }
        return value == null ? def : value;
    }
}
