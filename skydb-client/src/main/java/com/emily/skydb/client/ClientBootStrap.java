package com.emily.skydb.client;

import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.core.SkyRequest;
import com.emily.skydb.core.SkyResponse;

import java.util.UUID;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {


    public static void main(String[] args) throws Exception {
        SkyClientManager.initPool(new SkyClientProperties());
        SkyRequest request = new SkyRequest();
        request.setDbName("account");
        request.setTraceId(UUID.randomUUID().toString());
        request.setSqlId("123");
        request.getParams().put("username", "田晓霞");
        request.getParams().put("password", "123456");

        for (int i = 0; i < 10000; i++) {
            //连接netty，并获得一个代理对象
            SkyResponse<SkyRequest> bean = SkyClientManager.execute(request);
            //System.out.println((bean.getData().getDbName()));
            //System.out.println((bean.getData().getSqlId()));
            Thread.sleep(1000);
        }
    }


}
