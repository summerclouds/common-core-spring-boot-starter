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

import org.summerclouds.common.core.cfg.BeanRef;
import org.summerclouds.common.core.error.ConflictRuntimeException;
import org.summerclouds.common.core.tracing.IScope;
import org.summerclouds.common.core.tracing.ISpan;
import org.summerclouds.common.core.tracing.ITracing;

public class MTracing {

    private static BeanRef<ITracing> instance = new BeanRef<>(ITracing.class);

    public static ITracing get() {
        return instance.bean();
    }

    public static ISpan current() {
        ITracing inst = get();
        if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
        return inst.current();
    }

    public static IScope enter(ISpan span, String name, Object... keyValue) {
        ITracing inst = get();
        if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
        return inst.enter(span, name, keyValue);
    }

    public static IScope enter(ISpan span, String name) {
        ITracing inst = get();
        if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
        return inst.enter(span, name);
    }

    public static IScope enter(String name, Object... keyValue) {
        ITracing inst = get();
        if (inst == null) throw new ConflictRuntimeException("tracing bean not found");
        return inst.enter(name, keyValue);
    }

    public static String getTraceId() {
        ITracing inst = get();
        if (inst == null) return "";
        return inst.getTraceId();
    }

    public static String getSpanId() {
        ITracing inst = get();
        if (inst == null) return "";
        return inst.getSpanId();
    }
}
