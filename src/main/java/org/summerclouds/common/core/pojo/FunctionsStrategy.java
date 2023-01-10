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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.summerclouds.common.core.log.MLog;
import org.summerclouds.common.core.tool.MCollection;
import org.summerclouds.common.core.tool.MSystem;

public class FunctionsStrategy extends MLog implements PojoStrategy {

    private boolean embedded;
    private boolean toLower = true;
    private String embedGlue;
    private boolean actionsOnly;
    private Class<? extends Annotation>[] annotationMarker;
    private boolean allowPublic = true;

    @SuppressWarnings("unchecked")
    public FunctionsStrategy() {
        this(true, true, ".", false);
    }

    @SuppressWarnings("unchecked")
    public FunctionsStrategy(boolean actionsOnly) {
        this(true, true, ".", actionsOnly);
    }

    public FunctionsStrategy(
            boolean embedded,
            boolean toLower,
            String embedGlue,
            boolean actionsOnly,
            @SuppressWarnings("unchecked") Class<? extends Annotation>... annotationMarker) {
        this.embedded = embedded;
        this.toLower = toLower;
        this.embedGlue = embedGlue;
        this.actionsOnly = actionsOnly;
        this.annotationMarker = annotationMarker;
    }

    @Override
    public void parse(PojoParser parser, Class<?> clazz, PojoModelImpl model) {
        parse("", null, parser, clazz, model, 0);
    }

    @SuppressWarnings("unchecked")
    protected void parse(
            String prefix,
            FunctionAttribute<Object> parent,
            PojoParser parser,
            Class<?> clazz,
            PojoModelImpl model,
            int level) {

        if (level > 10) return; // logging ?

        for (Method m : MSystem.getMethods(clazz)) {

            // ignore static methods
            if (Modifier.isStatic(m.getModifiers())) continue;

            try {
                String mName = m.getName();
                Public desc = m.getAnnotation(Public.class);
                if (!allowPublic) desc = null;
                String s = (toLower ? mName.toLowerCase() : mName);
                if (s.startsWith("get") || s.startsWith("set")) s = s.substring(3);
                else if (s.startsWith("is")) s = s.substring(2);
                if (desc != null && desc.name().length() > 0) {
                    s = desc.name();
                    s = (toLower ? s.toLowerCase() : s);
                }
                String name = prefix + s;
                Method getter = null;
                Method setter = null;
                if (mName.startsWith("get") && m.getParameterCount() == 0) {
                    mName = mName.substring(3);
                    getter = m;
                    try {
                        setter = clazz.getMethod("set" + mName, getter.getReturnType());
                    } catch (NoSuchMethodException nsme) {
                    }
                } else if (mName.startsWith("set") && m.getParameterCount() == 1) {
                    mName = mName.substring(3);
                    setter = m;
                    try {
                        getter = clazz.getMethod("get" + mName);
                    } catch (NoSuchMethodException nsme) {
                        try {
                            getter = clazz.getMethod("is" + mName);
                        } catch (NoSuchMethodException nsme2) {
                        }
                    }
                } else if (mName.startsWith("is") && m.getParameterCount() == 0) {
                    mName = mName.substring(2);
                    getter = m;
                    try {
                        setter = clazz.getMethod("set" + mName, getter.getReturnType());
                    } catch (NoSuchMethodException nsme) {
                    }
                } else {
                    //					log().d("field is not a getter/setter", mName);
                    // it's an action
                    FunctionAction action = new FunctionAction(clazz, m, name, parent);
                    model.addAction(action);
                    continue;
                }

                //				if (getter == null) {
                //					log().d("getter not found",mName);
                //					continue;
                //				}

                //				Class<?> ret = getter.getReturnType();
                //				if (ret == void.class) {
                //					log().d("Value type is void - ignore");
                //					continue;
                //				}

                if (!actionsOnly && (isEmbedded(getter, setter) || isMarker(getter, setter))) {

                    if (desc != null) {
                        if (!desc.writable()) setter = null;
                        if (!desc.readable()) getter = null;
                    }
                    @SuppressWarnings({"rawtypes"})
                    FunctionAttribute attr =
                            new FunctionAttribute(clazz, getter, setter, name, parent);
                    if (isEmbedded(getter, setter)) {
                        parse(
                                prefix + name + embedGlue,
                                attr,
                                parser,
                                attr.getType(),
                                model,
                                level + 1);
                    } else {
                        if (!model.hasAttribute(name)) model.addAttribute(attr);
                    }
                }
            } catch (Exception e) {
                log().d(e);
            }
        }
    }

    private boolean isEmbedded(Method getter, Method setter) {
        if (!embedded) return false;
        if (getter != null) {
            if (getter.isAnnotationPresent(Embedded.class)) return true;
        }
        return false;
    }

    private boolean isMarker(Method getter, Method setter) {
        if (MCollection.isEmpty(annotationMarker)) return true;
        if (getter != null) {
            for (Class<? extends Annotation> a : annotationMarker)
                if (getter.isAnnotationPresent(a)) return true;
        }
        if (setter != null) {
            for (Class<? extends Annotation> a : annotationMarker)
                if (setter.isAnnotationPresent(a)) return true;
        }
        return false;
    }

    @Override
    public void parseObject(PojoParser parser, Object pojo, PojoModelImpl model) {
        Class<?> clazz = pojo.getClass();
        parse(parser, clazz, model);
    }

    public boolean isAllowPublic() {
        return allowPublic;
    }

    public FunctionsStrategy setAllowPublic(boolean allowPublic) {
        this.allowPublic = allowPublic;
        return this;
    }
}
