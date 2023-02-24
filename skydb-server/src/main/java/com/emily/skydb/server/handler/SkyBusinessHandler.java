package com.emily.skydb.server.handler;

import com.emily.skydb.core.entity.SkyTransMessage;
import com.emily.skydb.core.entity.SkyRequest;
import com.emily.skydb.core.entity.SkyResponse;
import com.emily.skydb.core.utils.ObjectUtils;

/**
 * @Description :  后置业务处理
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/2/24 1:34 PM
 */
public interface SkyBusinessHandler {
    /**
     * 后置业务
     * @param message
     * @return
     */
    default SkyResponse handler(SkyTransMessage message){
        //请求协议
        SkyRequest request = ObjectUtils.deserialize(message.getBody());
        //Rpc响应结果
        return SkyResponse.buildResponse(request);
    }
}
