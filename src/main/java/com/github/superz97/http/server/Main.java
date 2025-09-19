package com.github.superz97.http.server;

public class Main {

    public static void main(String[] args) {
        var server = new Server(4221);
        server.start();
    }

}
