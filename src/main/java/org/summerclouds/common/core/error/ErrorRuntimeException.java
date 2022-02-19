package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class ErrorRuntimeException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.ERROR;
    }

    public ErrorRuntimeException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public ErrorRuntimeException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public ErrorRuntimeException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public ErrorRuntimeException(IResult cause) {
        super(cause);
    }

    public ErrorRuntimeException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public ErrorRuntimeException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }
    
}
