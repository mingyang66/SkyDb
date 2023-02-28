package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.BodyProtocol;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.utils.JsonUtils;
import com.emily.skydb.core.utils.MessagePackUtils;

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
     * @param packet
     * @return
     * @throws IOException
     */
    default String handler(DataPacket packet) throws IOException {
        //请求协议
        BodyProtocol bodyProtocol = MessagePackUtils.deSerialize(packet.body, BodyProtocol.class);
        TestEntity entity = new TestEntity();
        entity.password = "1234";
        entity.username = "田晓霞";
        try {
            //Rpc响应结果
            return JsonUtils.toJSONString(bodyProtocol);
        } catch (Exception exception) {
            return "异常";
        }
    }
}
