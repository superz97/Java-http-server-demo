package com.github.superz97.http.server.response;

import com.github.superz97.http.server.common.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ResponseContext {

    private HttpStatus status;

    public static ResponseContext build(HttpStatus status) {
        var context = new ResponseContext();
        context.setStatus(status);
        return context;
    }

    public byte[] getResponseAsBytes() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(("HTTP/1.1 " + status.getCode() + " " + status.getReasonPhrase() + "\r\n").getBytes());
            outputStream.write("\r\n".getBytes());
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new RuntimeException("Exception while getting response bytes", ex);
        }
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
