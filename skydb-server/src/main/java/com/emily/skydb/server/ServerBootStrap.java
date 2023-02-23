package com.emily.skydb.server;


import com.emily.skydb.server.connection.SkyServerProperties;
import com.emily.skydb.server.manager.SkyServerManager;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        SkyServerProperties properties = new SkyServerProperties();
        SkyServerManager.init(properties);
    }

}
