package com.github.superz97.http.server.exception;

public class RequestContextException extends RuntimeException {
    public RequestContextException(String message) {
        super(message);
    }

  public RequestContextException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestContextException(Throwable cause) {
    super(cause);
  }

  public RequestContextException() {
  }
}
