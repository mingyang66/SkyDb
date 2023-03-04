package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.utils.JsonUtils;

import java.io.IOException;

/**
 * @Description :  后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/24 1:34 PM
 */
public interface SkyBusinessHandler {
    /**
     * 自定义处理后置业务
     *
     * @param transBody
     * @return
     * @throws IOException
     */
    default Object handler(DbTransBody reqDbBody) throws IOException {
        //请求协议
        TestEntity entity = new TestEntity();
        entity.password = "1234";
        entity.username = "田晓霞";
        System.out.println(JsonUtils.toJSONString(reqDbBody));
        return entity;
    }
}
