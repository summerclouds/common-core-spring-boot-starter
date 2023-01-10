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

import org.summerclouds.common.core.error.RC.STATUS;

public class TimeoutException extends MException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.TIMEOUT;
    }

    public TimeoutException(Object... in) {
        super(getDefaultStatus(), in);
    }

    public TimeoutException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public TimeoutException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public TimeoutException(IResult cause) {
        super(cause);
    }

    public TimeoutException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public TimeoutException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }
}
