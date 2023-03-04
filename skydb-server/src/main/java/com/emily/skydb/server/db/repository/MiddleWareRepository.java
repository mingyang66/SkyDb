package com.emily.skydb.server.db.repository;

import com.emily.skydb.server.db.entity.MiddleWare;

import java.util.Map;

/**
 * @Description :  数据库中间件仓储接口
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/4 3:29 PM
 */
public interface MiddleWareRepository {

    Map<String, MiddleWare> queryMiddleWare();
}
