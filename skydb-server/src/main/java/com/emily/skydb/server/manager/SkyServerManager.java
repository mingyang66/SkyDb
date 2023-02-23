package com.emily.skydb.server.manager;

import com.emily.skydb.server.connection.SkyServerConnection;

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
    public static void init(SkyServerProperties properties) {
        if (properties == null) {
            properties = new SkyServerProperties();
        }
        new SkyServerConnection(properties).start();
    }
}
