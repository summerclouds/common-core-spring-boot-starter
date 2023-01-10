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

import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.util.Value;

public class CfgBoolean extends AbstractCfg<Boolean> {

    public CfgBoolean(Class<?> owner, String param, Boolean def) {
        super(owner, param, def);
    }

    public CfgBoolean(String name, Boolean def) {
        super(name, def);
    }

    @Override
    protected Boolean valueOf(String value) {
        try {
            Value<Boolean> val = new Value<>();
            MCast.OBJECT_TO_BOOLEAN.toBoolean(value, false, val);
            return val.getValue(); // could be null
        } catch (Throwable t) {
            return null;
        }
    }
}
