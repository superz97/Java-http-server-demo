package com.github.superz97.http.server.request;

import com.github.superz97.http.server.common.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestContext {

    private final HttpMethod method;

    private final String path;

    private final List<String> pathParts;

    public RequestContext(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
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
            System.out.println("HTTP request headers: " + headers);
            var requestContext = new RequestContext(methodWithPath.getKey(), methodWithPath.getValue());
            return requestContext;
        } catch (IOException ex) {
            System.out.println("Exception trying to build rquest context: " + ex.getMessage());
            throw new RuntimeException(ex);
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

    @Override
    public String toString() {
        return "RequestContext{" +
                "method=" + method +
                ", path='" + path + '\'' +
                '}';
    }
}
