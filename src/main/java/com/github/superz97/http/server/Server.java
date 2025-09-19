package com.github.superz97.http.server;

import com.github.superz97.http.server.handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final int port;

    private final ExecutorService executorService;

    public Server(int port) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void start() {
        try (var serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                var clientSocket = serverSocket.accept();
                System.out.println("Accepted connection");
                executorService.execute(new RequestHandler(clientSocket));
            }
        } catch (IOException ex) {
            System.out.println("IOException" + ex.getMessage());
        }
    }

}
