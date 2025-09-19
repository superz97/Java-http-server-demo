package com.github.superz97.http.server.controller;

import com.github.superz97.http.server.bind.RequestMapping;
import com.github.superz97.http.server.common.HttpHeaders;
import com.github.superz97.http.server.common.HttpMethod;
import com.github.superz97.http.server.common.HttpStatus;
import com.github.superz97.http.server.request.RequestContext;
import com.github.superz97.http.server.response.ResponseContext;

import java.util.Map;

public class ApplicationController {

    @RequestMapping(path = "/", method = HttpMethod.GET)
    public ResponseContext simpleOk(RequestContext context) {
        return ResponseContext.build(HttpStatus.OK);
    }

    @RequestMapping(path = "/echo/{command}", method = HttpMethod.GET)
    public ResponseContext echo(RequestContext context) {
        var responseBody = context.getLastPart();
        return ResponseContext.build(
                HttpStatus.OK, HttpHeaders.fromHeaderMap(Map.of("Content-Type",
                        "text/plain",
                        "Content-Length", String.valueOf(responseBody.getBytes().length))),
                responseBody);
    }

    @RequestMapping(path = "/user-agent", method = HttpMethod.GET)
    public ResponseContext userAgent(RequestContext context) {
        var responseBody = context.getHeaders().getFirst("User-Agent");
        return ResponseContext.build(HttpStatus.OK,
                HttpHeaders.fromHeaderMap(Map.of("Content-Type", "text/plain",
                        "Content-Length", String.valueOf(responseBody.getBytes().length))),
                responseBody);
    }



}
