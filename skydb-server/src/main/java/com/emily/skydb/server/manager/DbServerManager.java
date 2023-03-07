package com.emily.skydb.server.manager;

import com.emily.skydb.server.connection.DbServerConnection;
import com.emily.skydb.server.handler.DbBusinessHandler;

/**
 * @Description :  服务端管理器类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/23 4:17 PM
 */
public class DbServerManager {
    /**
     * 启动服务端
     *
     * @param properties
     */
    public static void bootstrap(DbBusinessHandler handler, DbServerProperties properties) {
        if (properties == null) {
            properties = new DbServerProperties();
        }
        new DbServerConnection(handler, properties).start();
    }
}
