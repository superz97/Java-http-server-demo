package com.github.superz97.http.server.interceptor;

import com.github.superz97.http.server.request.RequestContext;
import com.github.superz97.http.server.response.ResponseContext;

public interface Interceptor {

    void beforeSendResponse(RequestContext context, ResponseContext responseContext);

}
