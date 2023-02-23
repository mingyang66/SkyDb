package com.emily.skydb.server.example;


import com.emily.skydb.server.connection.SkyServerConnection;
import com.emily.skydb.server.connection.SkyServerProperties;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        SkyServerProperties properties = new SkyServerProperties();
        new SkyServerConnection(properties).startServer();
    }

}
