package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class ConflictRuntimeException extends MRuntimeException {

    private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.CONFLICT;
    }

    public ConflictRuntimeException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public ConflictRuntimeException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public ConflictRuntimeException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public ConflictRuntimeException(IResult cause) {
        super(cause);
    }

    public ConflictRuntimeException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public ConflictRuntimeException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }


}
