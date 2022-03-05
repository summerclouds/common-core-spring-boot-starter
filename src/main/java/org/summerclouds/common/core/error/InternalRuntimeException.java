package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class InternalRuntimeException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.INTERNAL_ERROR;
    }

    public InternalRuntimeException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public InternalRuntimeException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public InternalRuntimeException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public InternalRuntimeException(IResult cause) {
        super(cause);
    }

    public InternalRuntimeException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public InternalRuntimeException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

}
