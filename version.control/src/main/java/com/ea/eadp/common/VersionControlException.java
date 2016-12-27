package com.ea.eadp.common;

/**
 * Created by chriskang on 12/27/2016.
 */
public class VersionControlException extends RuntimeException {
    public VersionControlException() {
    }

    public VersionControlException(String message) {
        super(message);
    }

    public VersionControlException(String message, Throwable cause) {
        super(message, cause);
    }

    public VersionControlException(Throwable cause) {
        super(cause);
    }

    public VersionControlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
