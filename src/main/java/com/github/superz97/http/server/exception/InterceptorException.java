package com.github.superz97.http.server.exception;

public class InterceptorException extends RuntimeException {
    public InterceptorException(String message) {
        super(message);
    }

    public InterceptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterceptorException(Throwable cause) {
        super(cause);
    }

    public InterceptorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public InterceptorException() {
    }
}
