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

public class CfgDouble extends AbstractCfg<Double> {

    public CfgDouble(Class<?> owner, String param, Double def) {
        super(owner, param, def);
    }

    public CfgDouble(String name, Double def) {
        super(name, def);
    }

    @Override
    protected Double valueOf(String value) {
        try {
            return Double.valueOf(value);
        } catch (Throwable t) {
            return null;
        }
    }
}
