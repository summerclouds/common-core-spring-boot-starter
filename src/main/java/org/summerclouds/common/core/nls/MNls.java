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
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.summerclouds.common.core.tool.MSystem;

public class MNls {

    protected String prefix = "";
    private MessageSource source;

    public MNls() {
        this(new ResourceBundleMessageSource(), "");
    }

    public MNls(MessageSource source, String prefix) {
        this.source = source;
        this.prefix = prefix == null || "".equals(prefix) ? "" : prefix + ".";
    }

    public String find(String in, String... strings) {
        if (strings == null || strings.length == 0) return find(in, (Map<String, Object>) null);
        HashMap<String, Object> attr = new HashMap<String, Object>();
        for (int i = 0; i < strings.length; i++) attr.put(String.valueOf(i), strings[i]);
        return find(in, attr);
    }

    public static String find(MNlsProvider provider, String in, Object... args) {
        return find(provider == null ? null : provider.getNls(), in, args);
    }

    public static String find(MNls nls, String in, Object... args) {
        return nls.find(in, args);
    }

    public String find(String name, Object... args) {
        return source.getMessage(prefix + name, args, null);
    }

    @Override
    public String toString() {
        return MSystem.toString(this, source);
    }

    public MNls createSubstitute(String prefix) {
        if (prefix == null) return this;
        return new MNls(source, this.prefix + prefix);
    }

    public static MNls lookup(Object owner) {
        MNlsFactory factory = MNlsFactory.lookup(owner);
        if (factory != null) return factory.load(owner.getClass());
        return null;
    }

    public static MNls lookup(Object owner, Locale locale) {
        MNlsFactory factory = MNlsFactory.lookup(owner);
        if (factory != null) return factory.load(owner.getClass(), locale);
        return null;
    }
}
