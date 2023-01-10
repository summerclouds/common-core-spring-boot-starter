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
package org.summerclouds.common.core.cfg;

import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public abstract class AbstractCfg<T> {

    private T value;
    private String name;
    private T def;

    public AbstractCfg(String name, T def) {
        this.name = name;
        this.def = def;
    }

    public AbstractCfg(Class<?> owner, String param, T def) {
        this.name = MSystem.getOwnerName(owner) + "." + name;
        this.def = def;
    }

    public T value() {
        if (value != null) return value;
        value = valueOf(MSpring.getValue(name));
        if (value == null) {
            if (MSpring.isStarted()) {
                value = def;
            } else {
                String appName = "app." + name;
                String strValue = System.getenv().get(appName);
                if (strValue == null) {
                    strValue = System.getProperty(appName);
                    if (strValue == null) return def;
                }
                T v = valueOf(strValue);
                return v == null ? def : v;
            }
        }
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }

    protected abstract T valueOf(String value);

    public T getDefault() {
        return def;
    }

    public void set(T value) {
        this.value = value;
    }
}
