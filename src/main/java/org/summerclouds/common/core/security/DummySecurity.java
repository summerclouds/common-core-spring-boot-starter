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

import java.util.Locale;

public class DummySecurity implements ISecurity {

    public static final ISubject SUBJECT = new DummySubject();
    public static final ISubjectEnvironment SUBJECT_ENVIRONMENT = new DummySubjectEnvironment();

    @Override
    public ISubject getCurrent() {
        return SUBJECT;
    }

    @Override
    public void subjectCleanup() {}

    @Override
    public ISubjectEnvironment asSubject(String username) {
        return SUBJECT_ENVIRONMENT;
    }

    @Override
    public ISubjectEnvironment asSubject(ISubject subject) {
        return SUBJECT_ENVIRONMENT;
    }

    @Override
    public String getAdminName() {
        return "admin";
    }

    @Override
    public boolean hasPermission(Class<?> clazz, String action, String instance) {
        return true;
    }

    @Override
    public boolean hasPermission(String ace) {
        return true;
    }

    @Override
    public boolean hasPermission(String clazz, String action, String instance) {
        return true;
    }

    @Override
    public boolean hasPermission(ISubject subject, Class<?> clazz, String action, String instance) {
        return true;
    }

    @Override
    public boolean hasPermission(ISubject subject, String ace) {
        return true;
    }

    @Override
    public boolean hasPermission(ISubject subject, String clazz, String action, String instance) {
        return true;
    }

    @Override
    public boolean hasRole(String role) {
        return true;
    }

    @Override
    public boolean hasRole(ISubject subject, String role) {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public boolean isAdmin(ISubject subject) {
        return false;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setLocale(Locale locale) {}

    @Override
    public Locale getLocale() {
        return Locale.getDefault();
    }

    @Override
    public Object getSessionAttribute(String key) {
        return null;
    }

    @Override
    public Object getSessionAttribute(String key, Object def) {
        return def;
    }

    @Override
    public void setSessionAttribute(String key, Object value) {}

    @Override
    public void touch() {}

    @Override
    public ISubject getSubject(String username) {
        return null;
    }

    @Override
    public boolean hasPermission(Class<?> clazz) {
        return true;
    }
}
