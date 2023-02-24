package com.emily.skydb.server.manager;

import com.emily.skydb.server.connection.SkyServerConnection;
import com.emily.skydb.server.handler.SkyBusinessHandler;

/**
 * @Description :  服务端管理器类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/23 4:17 PM
 */
public class SkyServerManager {
    /**
     * 启动服务端
     *
     * @param properties
     */
    public static void init(SkyBusinessHandler handler, SkyServerProperties properties) {
        if (properties == null) {
            properties = new SkyServerProperties();
        }
        new SkyServerConnection(handler, properties).start();
    }
}
