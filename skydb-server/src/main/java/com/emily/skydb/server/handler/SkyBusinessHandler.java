package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.BaseResponse;
import com.emily.skydb.core.protocol.BodyProtocol;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.utils.SerializeUtils;

/**
 * @Description :  后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/24 1:34 PM
 */
public interface SkyBusinessHandler {
    /**
     * 后置业务
     *
     * @param packet
     * @return
     */
    default BaseResponse handler(DataPacket packet) {
        //请求协议
        BodyProtocol bodyProtocol = SerializeUtils.deserialize(packet.body);
        TestEntity entity = new TestEntity();
        entity.password = "1234";
        entity.username = "田晓霞";
        try {
            //Rpc响应结果
            return BaseResponse.buildResponse(bodyProtocol);
        } catch (Exception exception) {
            return BaseResponse.buildResponse("发生错误了" + exception.getMessage());
        }
    }
}
