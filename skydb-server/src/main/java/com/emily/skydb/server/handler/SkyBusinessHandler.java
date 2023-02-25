package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.SkyTransBody;
import com.emily.skydb.core.protocol.SkyTransMessage;
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
     * @param message
     * @return
     */
    default SkyTransResponse handler(SkyTransMessage message) {
        //请求协议
        SkyTransBody transBody = ObjectUtils.deserialize(message.getBody());
        //Rpc响应结果
        return SkyTransResponse.buildResponse(transBody);
    }
}
