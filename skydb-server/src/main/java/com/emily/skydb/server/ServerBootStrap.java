package com.emily.skydb.server;


import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.server.handler.SkyBusinessHandler;
import com.emily.skydb.server.manager.SkyServerManager;
import com.emily.skydb.server.manager.SkyServerProperties;

import java.io.IOException;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        SkyServerProperties properties = new SkyServerProperties();
        SkyBusinessHandler handler = new SkyBusinessHandler() {
            @Override
            public String handler(DataPacket packet) throws IOException {
                return SkyBusinessHandler.super.handler(packet);
            }
        };
        SkyServerManager.init(handler, properties);
    }

}
