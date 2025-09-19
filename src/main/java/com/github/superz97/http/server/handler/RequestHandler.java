package com.github.superz97.http.server.handler;

import com.github.superz97.http.server.common.HttpStatus;
import com.github.superz97.http.server.request.RequestContext;
import com.github.superz97.http.server.response.ResponseContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class RequestHandler implements Runnable{

    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            var inputStream = clientSocket.getInputStream();
            var bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            var context = RequestContext.buildContext(bufferedReader);
            if (context == null) {
                System.out.println("Context is null");
                return;
            }
            var os = clientSocket.getOutputStream();
            if (context.pathIsEqualsTo("/")) {
                os.write(ResponseContext.build(HttpStatus.OK).getResponseAsBytes());
                os.flush();
            } else {
                os.write(ResponseContext.build(HttpStatus.NOT_FOUND).getResponseAsBytes());
                os.flush();
            }
        } catch (IOException ex) {
            throw new RuntimeException("Handler exception", ex);
        } finally {
            try {
                clientSocket.close();
                System.out.println("Socket closed");
            } catch (IOException ex) {
                System.out.println("Exception while closing socket");
            }
        }
    }

}
