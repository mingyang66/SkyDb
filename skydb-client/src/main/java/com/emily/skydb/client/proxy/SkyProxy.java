package com.emily.skydb.client.proxy;

import com.emily.skydb.client.SkyClientPoolManager;
import com.emily.skydb.client.pool.SkyConnection;
import com.emily.skydb.client.pool.SkyObjectPool;
import com.emily.skydb.core.ObjectUtils;
import com.emily.skydb.core.SkyMessage;
import com.emily.skydb.core.SkyRequest;
import com.emily.skydb.core.SkyResponse;

/**
 * @program: SkyDb
 * @description: 创建Netty客户端及自定义处理器
 * @author: Emily
 * @create: 2021/09/17
 */
public class SkyProxy {


    /**
     * 通过连接池发送
     *
     * @return
     */
    public static SkyResponse execute(SkyRequest request) throws Exception {
        SkyMessage message = SkyMessage.build(ObjectUtils.serialize(request));
        //运行线程，发送数据
        SkyObjectPool pool = SkyClientPoolManager.getObjectPool();
        //Channel对象
        SkyConnection connection = null;
        try {
            connection = pool.borrowObject();
            return connection.getClientChannelHandler().send(message);
        } catch (Exception exception) {
            // logger.error(PrintExceptionInfo.printErrorInfo(exception));
            //throw new BasicException(HttpStatusType.EXCEPTION.getStatus(), "Rpc调用异常");
            return null;
        } finally {
            if (connection != null) {
                pool.returnObject(connection);
            }
        }
    }
}
