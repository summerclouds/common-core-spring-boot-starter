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
package org.summerclouds.common.core.cast;

import java.util.UUID;

public class ObjectToUUID implements Caster<Object, UUID> {

    @Override
    public Class<? extends UUID> getToClass() {
        return UUID.class;
    }

    @Override
    public Class<? extends Object> getFromClass() {
        return Object.class;
    }

    @Override
    public UUID cast(Object in, UUID def) {
        if (in == null) return def;
        try {
            return UUID.fromString(String.valueOf(in));
        } catch (Exception t) {
            return def;
        }
    }
}
