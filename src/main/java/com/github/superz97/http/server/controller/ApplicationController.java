package com.github.superz97.http.server.controller;

import com.github.superz97.http.server.bind.RequestMapping;
import com.github.superz97.http.server.common.ApplicationParameters;
import com.github.superz97.http.server.common.HttpHeaders;
import com.github.superz97.http.server.common.HttpMethod;
import com.github.superz97.http.server.common.HttpStatus;
import com.github.superz97.http.server.request.RequestContext;
import com.github.superz97.http.server.response.ResponseContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @RequestMapping(path = "/files/{file}", method = HttpMethod.POST)
    public ResponseContext saveFile(RequestContext context) {
        if (!ApplicationParameters.getInstance().isDirectoryExists()) {
            return ResponseContext.build(HttpStatus.NOT_FOUND);
        }
        var directory =  ApplicationParameters.getInstance().getFileDirectory();
        directory = directory.endsWith("/") ? directory : directory + '/';
        var fileName = context.getLastPart();
        saveFile(String.format("%s%s", directory, fileName), context.getBody());
        return ResponseContext.build(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/files/{file}", method = HttpMethod.GET)
    public ResponseContext readFileByName(RequestContext context) {
        if (!ApplicationParameters.getInstance().isDirectoryExists()) {
            return ResponseContext.build(HttpStatus.NOT_FOUND);
        }
        var directory = ApplicationParameters.getInstance().getFileDirectory();
        directory = directory.endsWith("/") ? directory : directory + '/';
        var fileName = context.getLastPart();
        var file = new File(String.format("%s%s", directory, fileName));
        if (!file.exists() || !file.isFile()) {
            return ResponseContext.build(HttpStatus.NOT_FOUND);
        }
        String fileContent = readFile(file);
        return ResponseContext.build(
                HttpStatus.OK,
                HttpHeaders.fromHeaderMap(Map.of("Content-Type", "application/octet-stream",
                        "Content-Length", String.valueOf(fileContent.getBytes().length))),
                fileContent
        );
    }

    private String readFile(File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            fileInputStream.read(bytes);
            return new String(bytes);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading file " + file.getAbsolutePath(), ex);
        }
    }

    private void saveFile(String fullPath, String content) {
        try {
            Path path = Paths.get(fullPath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.write(path, content.getBytes());
        } catch (IOException ex) {
            throw new RuntimeException("Exception trying to save file", ex);
        }
    }

}
