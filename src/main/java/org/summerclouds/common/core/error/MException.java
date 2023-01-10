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
package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.CAUSE;

public class MException extends Exception implements IResult {

    private static final long serialVersionUID = 1L;

    private int rc;

    public MException(RC.STATUS rc, Object... in) {
        this(CAUSE.ENCAPSULATE, rc.rc(), rc.name(), in);
    }

    public MException(RC.CAUSE causeHandling, RC.STATUS rc, Object... in) {
        this(causeHandling, rc.rc(), rc.name(), in);
    }

    public MException(int rc, Throwable cause) {
        this(CAUSE.ENCAPSULATE, rc, cause.getMessage(), cause);
    }

    public MException(IResult cause) {
        super(cause.getMessage(), cause instanceof Throwable ? (Throwable) cause : null);
        setReturnCode(cause.getReturnCode());
    }

    public MException(IResult cause, String msg, Object... parameters) {
        super(
                RC.toMessage(cause.getReturnCode(), cause, msg, parameters, 0),
                cause instanceof Throwable ? (Throwable) cause : null);
        setReturnCode(cause.getReturnCode());
    }

    public MException(int rc, String msg, Object... in) {
        this(CAUSE.ENCAPSULATE, rc, msg, in);
    }

    public MException(RC.CAUSE causeHandling, int rc, String msg, Object... parameters) {
        super(
                RC.toMessage(rc, causeHandling, msg, parameters, 0),
                RC.findCause(causeHandling, parameters));
        setReturnCode(RC.findReturnCode(causeHandling, rc, parameters));
    }

    public MException(int rc) {
        super(RC.toString(rc));
        setReturnCode(rc);
    }

    @Override
    public String toString() {
        return getReturnCode() + " " + super.toString();
    }

    @Override
    public int getReturnCode() {
        return rc;
    }

    private void setReturnCode(int rc) {
        this.rc = rc;
    }
}
