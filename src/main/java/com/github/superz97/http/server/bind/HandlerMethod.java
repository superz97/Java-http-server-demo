package com.github.superz97.http.server.bind;

import com.github.superz97.http.server.common.HttpMethod;
import com.github.superz97.http.server.exception.HandlerException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object handlerObject;
    private final HttpMethod method;
    private final String path;
    private final Method handler;

    public HandlerMethod(Object handlerObject, HttpMethod method, String path, Method handler) {
        this.handlerObject = handlerObject;
        this.method = method;
        this.path = path;
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    public <T> T invoke(Object... args) {
        try {
            return (T) handler.invoke(handlerObject, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new HandlerException(e);
        }
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

}
