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
package org.summerclouds.common.core.activator;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.summerclouds.common.core.log.PlainLog;
import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.tool.MSystem;

public class Activator extends ClassLoader {

    protected Map<String, Object> registry = Collections.synchronizedMap(new HashMap<>());

    public Activator(ClassLoader parent) {
        super(parent);
    }

    public Activator() {
        this(MSpring.getDefaultClassLoader());
    }

    public Object getObject(String name) {
        Object obj = registry.get(name);
        if (obj != null) return obj;
        obj = createObject(name);
        if (obj == null) return null;
        registry.put(name, obj);
        return obj;
    }

    public Object createObject(String obj) {
        try {
            Class<?> clazz = MSystem.getClass(obj);

            DefaultImplementation defaultImplementation =
                    clazz.getAnnotation(DefaultImplementation.class);
            if (defaultImplementation != null) {
                clazz = defaultImplementation.value();
            }

            Object out = null;

            DefaultFactory factoryClazz = clazz.getAnnotation(DefaultFactory.class);
            if (factoryClazz != null) {
                ObjectFactory inst =
                        (ObjectFactory) getObject(factoryClazz.value().getCanonicalName());
                if (inst != null) {
                    out = inst.create(clazz);
                }
            }

            if (out == null) out = MSystem.createObject(clazz);

            return out;
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException
                | SecurityException e) {
            PlainLog.w("failed to create object for class {1}", obj, e);
        }
        return null;
    }
}
