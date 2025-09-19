package com.github.superz97.http.server.interceptor;

import com.github.superz97.http.server.exception.InterceptorException;
import com.github.superz97.http.server.request.RequestContext;
import com.github.superz97.http.server.response.ResponseContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.GZIPOutputStream;

public class EncodeInterceptor implements Interceptor {

    @Override
    public void beforeSendResponse(RequestContext context, ResponseContext responseContext) {
        var acceptEncoding = responseContext.getHeaders().getFirst("Accept-Encoding");
        if (acceptEncoding != null && !acceptEncoding.isBlank() && responseContext.getResponseBody() != null) {
            var parts = acceptEncoding.split(",");
            Arrays.stream(parts)
                    .filter(it -> it.trim().equalsIgnoreCase("gzip"))
                    .findFirst()
                    .ifPresent(gzipString -> {
                        byte[] responseBody = compressResponseBody(responseContext.getResponseBody());
                        responseContext.getHeaders().set("Content-Length", String.valueOf(responseBody.length));
                        responseContext.getHeaders().set("Content-Encoding", "gzip");
                        responseContext.setResponseBody(responseBody);
                    });
        }
    }

    private byte[] compressResponseBody(byte[] responseBody) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream)) {
                gzipOutputStream.write(responseBody);
            }
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new InterceptorException("Exception while compressing response body" + new String(responseBody));
        }
    }

}
