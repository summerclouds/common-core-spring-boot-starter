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

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.summerclouds.common.core.M;
import org.summerclouds.common.core.resource.ClassLoaderResourceProvider;
import org.summerclouds.common.core.resource.MResourceProvider;
import org.summerclouds.common.core.tool.MCast;
import org.summerclouds.common.core.tool.MSystem;

public class MNlsFactory extends MNlsBundle {

    public MNlsFactory() {}

    public MNls load(Class<?> owner) {
        return load(null, owner, null, null);
    }

    public static String toResourceName(Object owner) {
        if (owner == null) return null;
        if (owner instanceof String) return (String) owner;
        return MSystem.getClassName(owner).replace('.', '/');
    }

    public MNls load(Class<?> owner, String locale) {
        return load(null, owner, null, locale == null ? null : MCast.toLocale(locale));
    }

    public MNls load(Class<?> owner, Locale locale) {
        return load(null, owner, null, locale);
    }

    public MNls load(MResourceProvider res, Class<?> owner, String resourceName, Locale locale) {
        return load(res, owner, resourceName, locale, true);
    }

    public MNls load(
            MResourceProvider res,
            Class<?> owner,
            String resourceName,
            Locale locale,
            boolean searchAlternatives) {

        try {

            if (resourceName == null) {
                if (owner.getCanonicalName() != null)
                    resourceName = owner.getCanonicalName().replace('.', '/');
                else resourceName = owner.getEnclosingClass().getCanonicalName().replace('.', '/');
            }

            if (res == null) {
                res = findResourceProvider(owner);
            }

            if (locale == null) locale = Locale.getDefault();

            ResourceBundleMessageSource source = new ResourceBundleMessageSource();
            source.setBasename(resourceName);
            source.setDefaultLocale(locale);

            return new MNls(source, null);
        } catch (Throwable e) {
            log().e(e);
        }

        return new MNls();
    }

    protected String getResourcePrefix() {
        return null;
    }

    protected MResourceProvider findResourceProvider(Class<?> owner) {
        if (owner != null) return new ClassLoaderResourceProvider(owner.getClassLoader());
        else return new ClassLoaderResourceProvider();
    }
    //
    //    public MNls load(InputStream is) {
    //
    //        Properties properties = new Properties();
    //        try {
    //            properties.load(is);
    //        } catch (IOException e) {
    //            log().e(e);
    //        }
    //        return new MNls(properties, "");
    //    }

    public static MNlsFactory lookup(Object owner) {
        return M.l(MNlsFactory.class);
    }

    @Override
    public MNls createNls(String locale) {
        return load(null, null, getPath(), MCast.toLocale(locale), false);
    }
}
