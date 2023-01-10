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

import org.summerclouds.common.core.activator.DefaultImplementation;

@DefaultImplementation(DummySecurity.class)
public interface ISecurity {

    ISubject getCurrent();

    ISubject getSubject(String username);

    void subjectCleanup();

    ISubjectEnvironment asSubject(String username);

    ISubjectEnvironment asSubject(ISubject subject);

    String getAdminName();

    boolean hasPermission(Class<?> clazz, String action, String instance);

    boolean hasPermission(String ace);

    boolean hasPermission(String clazz, String action, String instance);

    boolean hasPermission(ISubject subject, Class<?> clazz, String action, String instance);

    boolean hasPermission(ISubject subject, String ace);

    boolean hasPermission(ISubject subject, String clazz, String action, String instance);

    boolean hasRole(String role);

    boolean hasRole(ISubject subject, String role);

    boolean isAdmin();

    boolean isAdmin(ISubject subject);

    boolean isAuthenticated();

    void setLocale(Locale locale);

    Locale getLocale();

    Object getSessionAttribute(String key);

    Object getSessionAttribute(String key, Object def);

    void setSessionAttribute(String key, Object value);

    void touch();

    /**
     * Check if the current user has access to the class check by annotations.
     *
     * @param clazz
     * @return
     */
    boolean hasPermission(Class<?> clazz);
}
