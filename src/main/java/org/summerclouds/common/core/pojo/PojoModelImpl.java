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
package org.summerclouds.common.core.pojo;

import java.util.HashMap;
import java.util.Iterator;

public class PojoModelImpl implements PojoModel {

    @SuppressWarnings("rawtypes")
    private HashMap<String, PojoAttribute> attributes = new HashMap<String, PojoAttribute>();

    private HashMap<String, PojoAction> actions = new HashMap<String, PojoAction>();
    private Class<?> clazz;

    public PojoModelImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void addAttribute(@SuppressWarnings("rawtypes") PojoAttribute attr) {
        attributes.put(attr.getName(), attr);
    }

    public void addAction(PojoAction attr) {
        actions.put(attr.getName(), attr);
    }

    public void addAction(String name, PojoAction attr) {
        actions.put(name, attr);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Iterator<PojoAttribute> iterator() {
        return attributes.values().iterator();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public PojoAttribute getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public String[] getAttributeNames() {
        return attributes.keySet().toArray(new String[attributes.size()]);
    }

    @Override
    public Class<?> getManagedClass() {
        return clazz;
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void removeAction(String name) {
        actions.remove(name);
    }

    @Override
    public boolean hasAction(String name) {
        return actions.containsKey(name);
    }

    @Override
    public PojoAction getAction(String name) {
        return actions.get(name);
    }

    @Override
    public String[] getActionNames() {
        return actions.keySet().toArray(new String[actions.size()]);
    }
}
