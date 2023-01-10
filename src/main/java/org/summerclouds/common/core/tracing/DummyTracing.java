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
package org.summerclouds.common.core.tracing;

public class DummyTracing implements ITracing {

    static final ISpan DUMMY_SPAN = new DummySpan();
    static final IScope DUMMY_SCOPE = new DummyScope();

    @Override
    public ISpan current() {
        return DUMMY_SPAN;
    }

    @Override
    public IScope enter(ISpan span, String name, Object... keyValue) {
        return DUMMY_SCOPE;
    }

    @Override
    public void cleanup() {}

    @Override
    public IScope enter(String name, Object... keyValue) {
        return DUMMY_SCOPE;
    }

    @Override
    public String getTraceId() {
        return "";
    }

    @Override
    public String getSpanId() {
        return "";
    }

    @Override
    public void inject(Setter<String> getter) {}

    @Override
    public IScope extract(Getter<String> getter) {
        return DUMMY_SCOPE;
    }
}
