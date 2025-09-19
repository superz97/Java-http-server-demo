package com.github.superz97.http.server.service;

import com.github.superz97.http.server.bind.HandlerHolder;
import com.github.superz97.http.server.bind.HandlerMethod;
import com.github.superz97.http.server.request.RequestContext;

public class HandlerMethodResolver {

    public HandlerMethod resolve(RequestContext context) {
        return HandlerHolder.getInstance().getHandlerMethodList()
                .stream()
                .filter(it -> context.getMethod() == it.getMethod() &&
                        PathPattern.path(it.getPath()).match(context.getPath()))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Handler by context not found: " + context);
                    return null;
                });
    }

}
