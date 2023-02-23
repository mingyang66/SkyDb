package com.emily.infrastructure.server.example;


import com.emily.infrastructure.server.connection.SkyServerConnection;
import com.emily.infrastructure.server.connection.SkyServerProperties;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        SkyServerProperties properties = new SkyServerProperties();
        new SkyServerConnection(properties).startServer();
    }

}
