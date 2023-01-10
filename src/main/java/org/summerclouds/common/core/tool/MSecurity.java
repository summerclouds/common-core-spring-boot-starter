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
package org.summerclouds.common.core.tool;

import java.util.List;

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.error.ConflictRuntimeException;
import org.summerclouds.common.core.log.Log;
import org.summerclouds.common.core.security.ISecurity;
import org.summerclouds.common.core.security.ISubject;
import org.summerclouds.common.core.security.ISubjectEnvironment;

public class MSecurity {

    // default rights
    public static final String READ = "read";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String VIEW = "view";
    public static final String ADMIN = "admin";
    public static final String GUEST = "guest";
    public static final String EXECUTE = "execute";

    public static final String WILDCARD_TOKEN = "*";
    public static final String PART_DIVIDER_TOKEN = ":";
    public static final String SUBPART_DIVIDER_TOKEN = ",";
    public static final String ACE_DIVIDER = ";";

    private MSecurity() {};

    private static final Log log = Log.getLog(MSecurity.class);

    private static BeanRef<ISecurity> instance = new BeanRef<>(ISecurity.class);

    public static ISecurity get() {
        return instance.bean();
    }

    public static ISubject getCurrent() {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.getCurrent();
    }

    public static ISubject getSubject(String username) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.getSubject(username);
    }

    public static void subjectCleanup() {
        ISecurity sec = get();
        if (sec == null) return;
        sec.subjectCleanup();
    }

    public static ISubjectEnvironment asSubject(String username) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.asSubject(username);
    }

    public static ISubjectEnvironment asSubject(ISubject subject) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.asSubject(subject);
    }

    public static ISubjectEnvironment asAdmin() {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.asSubject(sec.getAdminName());
    }

    public static String getAdminName() {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.getAdminName();
    }

    public static boolean hasPermission(Class<?> clazz, String action, String instance) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(clazz, action, instance);
    }

    public static boolean hasPermission(String ace) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(ace);
    }

    public static boolean hasPermission(String clazz, String action, String instance) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(clazz, action, instance);
    }

    public static boolean hasPermission(
            ISubject subject, Class<?> clazz, String action, String instance) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(subject, clazz, action, instance);
    }

    public static boolean hasPermission(ISubject subject, String ace) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(subject, ace);
    }

    public static boolean hasPermission(
            ISubject subject, String clazz, String action, String instance) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(subject, clazz, action, instance);
    }

    public static boolean hasPermissionByList(
            List<String> map, ISubject account, String objectIdent) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");

        boolean access = false;
        String principal = account.getName();
        for (String g : map) {

            g = g.trim();
            if (g.length() == 0) continue;

            if (g.startsWith("policy:")) {
                access = MCast.toboolean(g.substring(7), access);
            } else if (g.startsWith("user:")) {
                if (g.substring(5).equals(principal)) {
                    log.d("access granted", objectIdent, g);
                    access = true;
                    break;
                }
            } else if (g.startsWith("notuser:")) {
                if (g.substring(8).equals(principal)) {
                    log.d("access denied", objectIdent, g);
                    access = false;
                    break;
                }
            } else if (g.startsWith("not:")) {
                if (account.hasRole(g.substring(4))) {
                    log.d("access denied", objectIdent, g);
                    access = false;
                    break;
                }
            } else if (g.equals("*")) {
                log.d("access granted", objectIdent, g);
                access = true;
                break;
            } else if (account.hasRole(g)) {
                log.d("access granted", objectIdent, g);
                access = true;
                break;
            }
            ;
        }
        return access;
    }

    public static boolean hasPermissionByList(String list, ISubject account, String objectIdent) {
        List<String> map = MCollection.toList(list.split(";"));
        return hasPermissionByList(map, account, objectIdent);
    }

    public static boolean hasRole(String role) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasRole(role);
    }

    public static boolean hasRole(ISubject subject, String role) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasRole(subject, role);
    }

    public static boolean isAdmin() {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.isAdmin();
    }

    public static boolean isAdmin(ISubject subject) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.isAdmin(subject);
    }

    public static boolean isAuthenticated() {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.isAuthenticated();
    }

    /**
     * Check if the current user has access to the class check by annotations.
     *
     * @param clazz
     * @return
     */
    public static boolean hasPermission(Class<?> clazz) {
        ISecurity sec = get();
        if (sec == null) throw new ConflictRuntimeException("security bean not found");
        return sec.hasPermission(clazz);
    }
}
