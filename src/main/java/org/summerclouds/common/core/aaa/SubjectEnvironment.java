/**
 * Copyright (C) 2002 Mike Hummel (mh@mhus.de)
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
package org.summerclouds.common.core.aaa;

import java.io.Closeable;

import javax.security.auth.Subject;

import org.summerclouds.common.core.MSecurity;
import org.summerclouds.common.core.tracing.IScope;

public class SubjectEnvironment implements Closeable {

    private ISubject subject;
    private ISubject predecessor;
    private IScope scope;

    public SubjectEnvironment(Subject subject, Subject predecessor, IScope scope) {
        this.subject = subject;
        this.predecessor = predecessor;
        this.scope = scope;
    }

    public Subject getSubject() {
        return subject;
    }

    @Override
    public void close() {
        if (predecessor == null) ThreadContext.remove();
        else ThreadContext.bind(predecessor);
        if (scope != null)
            try {
                scope.close();
            } catch (Throwable t) {
            }
        scope = null;
    }

    @Override
    public String toString() {
        return MSecurity.toString(subject);
    }
}
