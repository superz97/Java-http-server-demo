package com.github.superz97.http.server.request;

import com.github.superz97.http.server.common.HttpHeaders;
import com.github.superz97.http.server.common.HttpMethod;
import com.github.superz97.http.server.exception.RequestContextException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestContext {

    private final HttpMethod method;

    private final String path;

    private final HttpHeaders headers;

    private String body;

    private final List<String> pathParts;

    public RequestContext(HttpMethod method, String path, HttpHeaders headers) {
        this.method = method;
        this.path = path;
        this.headers = headers;
        this.pathParts = Arrays.stream(path.split("/")).toList();
    }

    public static RequestContext buildContext(BufferedReader reader) {
        try {
            var requestLine = reader.readLine();
            if (requestLine == null || requestLine.isBlank()) {
                return null;
            }
            var methodWithPath = extractMethodAndPass(requestLine);
            List<String> headers = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null &&  !line.isBlank()) {
                headers.add(line);
            }
            var httpHeaders = HttpHeaders.fromHeaderList(headers);
            System.out.println("HTTP request headers: " + headers);
            var requestContext = new RequestContext(methodWithPath.getKey(), methodWithPath.getValue(), httpHeaders);
            var contentLength = httpHeaders.getFirst("Content-Length");
            if (contentLength != null && !contentLength.isBlank()) {
                int bodySize = Integer.parseInt(contentLength);
                char[] bodyBuffer = new char[bodySize];
                reader.read(bodyBuffer);
                requestContext.setBody(new String(bodyBuffer));
            }
            return requestContext;
        } catch (IOException ex) {
            System.out.println("Exception trying to build rquest context: " + ex.getMessage());
            throw new RequestContextException(ex);
        }
    }

    private static AbstractMap.SimpleEntry<HttpMethod, String> extractMethodAndPass(String requestLine) {
        var parts = requestLine.split(" ");
        return new AbstractMap.SimpleEntry<>(HttpMethod.fromType(parts[0]), parts[1]);
    }

    public boolean hasPath() {
        return path != null && !path.isEmpty();
    }

    public String getPart(int index) {
        if (pathParts.size() < index) {
            return null;
        }
        return pathParts.get(index);
    }

    public boolean pathIsEqualsTo(String actualPath) {
        return hasPath() && path.equals(actualPath);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getPathParts() {
        return pathParts;
    }

    public String getLastPart() {
        return getPart(pathParts.size() - 1);
    }

    @Override
    public String toString() {
        return "RequestContext{" +
                "method=" + method +
                ", path='" + path + '\'' +
                '}';
    }
}
