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
package org.summerclouds.common.core.security;

public class DummySubject implements ISubject {

    private String name;

    public DummySubject() {
        this("");
    }

    public DummySubject(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public Object getPrincipal() {
        return name;
    }

    @Override
    public boolean hasRole(String rolename) {
        return true;
    }

    @Override
    public boolean hasPermission(String ace) {
        return true;
    }

    @Override
    public boolean hasPermission(String object, String action, String instance) {
        return true;
    }

    @Override
    public boolean hasPermission(
            ISubject subject, Class<?> object, String action, String instance) {
        return true;
    }
}
