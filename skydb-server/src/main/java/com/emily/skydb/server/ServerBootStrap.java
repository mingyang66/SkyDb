package com.emily.skydb.server;


import com.emily.skydb.server.manager.SkyServerManager;
import com.emily.skydb.server.manager.SkyServerProperties;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        SkyServerProperties properties = new SkyServerProperties();
        SkyServerManager.init(properties);
    }

}
