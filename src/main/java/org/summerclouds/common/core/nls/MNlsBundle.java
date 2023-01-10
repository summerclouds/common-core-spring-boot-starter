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
package org.summerclouds.common.core.nls;

import java.util.HashMap;
import java.util.Locale;

import org.summerclouds.common.core.M;
import org.summerclouds.common.core.log.MLog;

// @DefaultImplementation(MNlsFactory.class)
public abstract class MNlsBundle extends MLog {

    private String path;
    private MNls defaultNls;
    private HashMap<String, Object> cache = new HashMap<>();

    public MNlsBundle() {}

    public MNlsBundle(String path) {
        setPath(path);
    }

    public MNlsBundle(Object owner) {
        setPath(MNlsFactory.toResourceName(owner));
    }

    public MNls getNls(Locale locale) {
        if (locale == null) return getDefaultNls();
        String tag = locale.toLanguageTag();
        MNls out = getNls(tag);
        if (out != null) return out;
        tag = locale.getLanguage();
        out = getNls(tag);
        if (out != null) return out;
        return getDefaultNls();
    }

    public synchronized MNls getDefaultNls() {
        if (defaultNls != null) return defaultNls;
        defaultNls = getNls("");
        if (defaultNls == null) defaultNls = new MNls();
        return defaultNls;
    }

    public synchronized MNls getNls(String locale) {
        Object out = cache.get(locale);
        if (out != null) {
            if (out instanceof MNls) return (MNls) out;
            return null;
        }
        out = createNls(locale);
        if (out == null || !(out instanceof MNls)) out = new Object(); // set dummy
        cache.put(locale, out);
        if (out instanceof MNls) return (MNls) out;
        return null;
    }

    /**
     * Create a NLS for the given path and locale or return null.
     *
     * @param locale
     * @return the nls object
     */
    public abstract MNls createNls(String locale);

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MNlsBundle setOwner(Object owner) {
        setPath(MNlsFactory.toResourceName(owner));
        return this;
    }

    public static MNlsBundle lookup(Object owner) {
        MNlsBundleFactory factory = M.l(MNlsBundleFactory.class);
        MNlsBundle nlsBundle = factory.create(owner);
        return nlsBundle;
    }
}
