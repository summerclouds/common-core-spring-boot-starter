package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class ForbiddenRuntimeException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.FORBIDDEN;
    }

    public ForbiddenRuntimeException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public ForbiddenRuntimeException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public ForbiddenRuntimeException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public ForbiddenRuntimeException(IResult cause) {
        super(cause);
    }

    public ForbiddenRuntimeException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public ForbiddenRuntimeException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

}
