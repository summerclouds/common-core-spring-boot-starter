package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class InternalException extends MException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.INTERNAL_ERROR;
    }

    public InternalException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public InternalException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public InternalException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public InternalException(IResult cause) {
        super(cause);
    }

    public InternalException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public InternalException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

    public InternalException(int rc) {
        super(getDefaultStatus().rc());
    }
}
