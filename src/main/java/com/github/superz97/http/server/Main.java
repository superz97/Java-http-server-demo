package com.github.superz97.http.server;

import com.github.superz97.http.server.common.ApplicationParameters;

public class Main {

    public static void main(String[] args) {
        ApplicationParameters.getInstance().setFileDirectory(args);
        var server = new Server(4221);
        server.start();
    }

}
