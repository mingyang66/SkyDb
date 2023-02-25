package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.SkyTransBody;
import com.emily.skydb.core.protocol.SkyTransResponse;
import com.emily.skydb.core.utils.ObjectUtils;

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
    default SkyTransResponse handler(DataPacket packet) {
        //请求协议
        SkyTransBody transBody = ObjectUtils.deserialize(packet.getBody());
        //Rpc响应结果
        return SkyTransResponse.buildResponse(transBody);
    }
}
