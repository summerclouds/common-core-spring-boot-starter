package org.summerclouds.common.core.error;

import org.summerclouds.common.core.error.RC.STATUS;

public class AuthorizationException extends MRuntimeException {

	private static final long serialVersionUID = 1L;

    public static STATUS getDefaultStatus() {
        return RC.STATUS.FORBIDDEN;
    }

    public AuthorizationException(Object... in) {
        super(getDefaultStatus(),in);
    }

    public AuthorizationException(RC.CAUSE causeHandling, Object... in) {
        super(causeHandling, getDefaultStatus(), in);
    }

    public AuthorizationException(Throwable cause) {
        super(getDefaultStatus().rc(), cause);
    }

    public AuthorizationException(IResult cause) {
        super(cause);
    }

    public AuthorizationException(String msg, Object... in) {
        super(getDefaultStatus().rc(), msg, in);
    }

    public AuthorizationException(RC.CAUSE causeHandling, String msg, Object... parameters) {
        super(causeHandling, getDefaultStatus().rc(), msg, parameters);
    }

    public AuthorizationException(int rc) {
        super(getDefaultStatus().rc());
    }
}
