package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class ForbiddenException extends MException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.FORBIDDEN;
    }

    public ForbiddenException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public ForbiddenException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public ForbiddenException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public ForbiddenException(IResult cause) {
        super(cause);
    }

    public ForbiddenException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public ForbiddenException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

}
