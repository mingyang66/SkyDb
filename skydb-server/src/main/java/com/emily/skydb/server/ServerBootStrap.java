package com.emily.skydb.server;


import com.emily.skydb.core.protocol.BaseResponse;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.server.handler.SkyBusinessHandler;
import com.emily.skydb.server.manager.SkyServerManager;
import com.emily.skydb.server.manager.SkyServerProperties;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        SkyServerProperties properties = new SkyServerProperties();
        SkyBusinessHandler handler = new SkyBusinessHandler() {
            @Override
            public BaseResponse handler(DataPacket message) {
                return SkyBusinessHandler.super.handler(message);
            }
        };
        SkyServerManager.init(handler, properties);
    }

}
