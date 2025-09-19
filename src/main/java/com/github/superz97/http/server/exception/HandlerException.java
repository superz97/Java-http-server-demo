package com.github.superz97.http.server.exception;

public class HandlerException extends RuntimeException {
    public HandlerException(String message) {
        super(message);
    }

    public HandlerException(String message, Throwable cause) {
      super(message, cause);
    }

    public HandlerException(Throwable cause) {
      super(cause);
    }

    public HandlerException() {
    }
}
