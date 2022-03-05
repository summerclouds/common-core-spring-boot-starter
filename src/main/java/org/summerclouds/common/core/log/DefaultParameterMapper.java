/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package org.summerclouds.common.core.log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.summerclouds.common.core.tool.MSpring;
import org.summerclouds.common.core.util.SoftHashMap;

public class DefaultParameterMapper extends AbstractParameterMapper
        implements IMutableParameterMapper {

    private HashMap<String, ParameterEntryMapper> mapping;
    private SoftHashMap<String, ParameterEntryMapper> cache = new SoftHashMap<>();
    private ParameterEntryMapper noMapper =
            new ParameterEntryMapper() {

                @Override
                public String map(Object in) {
                    return null;
                }
            };

    @Override
    protected Object map(Object o) {
    	init();
        if (o == null || mapping.size() == 0) return null;
        Class<?> c = o.getClass();
        if (c.isPrimitive()) return null;
        String name = c.getCanonicalName();
        if (name == null || name.startsWith("java.lang")) return null;

        synchronized (this) {
            ParameterEntryMapper mapper = cache.get(name);
            if (mapper != null) return mapper.map(o);
        }

        while (c != null) {

            String n = c.getCanonicalName();
            ParameterEntryMapper mapper = mapping.get(n);
            if (mapper != null) {
                synchronized (this) {
                    cache.put(name, mapper);
                    return mapper.map(o);
                }
            }

            List<Class<?>> l = new LinkedList<>();
            findInterfaces(c, l);
            for (Class<?> i : l) {

                n = i.getCanonicalName();
                mapper = mapping.get(n);

                if (mapper != null) {
                    synchronized (this) {
                        cache.put(name, mapper);
                        return mapper.map(o);
                    }
                }
            }

            c = c.getSuperclass();
        }

        synchronized (this) {
            cache.put(name, noMapper);
        }
        return null;
    }

    protected synchronized void init() {
		if (mapping != null) return;
		mapping = new HashMap<>();
		Map<String, ParameterEntryMapper> map = MSpring.getBeansOfType( ParameterEntryMapper.class );
		if (map != null)
			mapping.putAll(map);
	}

	private void findInterfaces(Class<?> c, List<Class<?>> list) {
        for (Class<?> i : c.getInterfaces()) {
            list.add(i);
            findInterfaces(i, list);
        }
    }

    @Override
    public void clear() {
        synchronized (this) {
            mapping = null;
            cache.clear();
        }
    }

    @Override
    public void put(String clazz, ParameterEntryMapper mapper) {
    	init();
        synchronized (this) {
            mapping.put(clazz, mapper);
            cache.remove(clazz);
        }
    }
}
