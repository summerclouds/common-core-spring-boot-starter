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

import org.summerclouds.common.core.util.SecureString;

public class CfgSecure extends AbstractCfg<SecureString> {

    public CfgSecure(Class<?> owner, String param, SecureString def) {
        super(owner, param, def);
    }

    public CfgSecure(String name, SecureString def) {
        super(name, def);
    }

    public CfgSecure(Class<?> owner, String param, String def) {
        super(owner, param, new SecureString(def));
    }

    public CfgSecure(String name, String def) {
        super(name, new SecureString(def));
    }

    @Override
    protected SecureString valueOf(String value) {
        return new SecureString(value);
    }

    public String valueAsString() {
        SecureString v = value();
        return v == null ? null : v.value();
    }
}
