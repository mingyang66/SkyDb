package com.emily.infrastructure.client.example;

import com.emily.infrastructure.client.proxy.SkyProxy;
import com.emily.infrastructure.core.SkyRequest;
import com.emily.infrastructure.core.SkyResponse;

import java.util.UUID;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {


    public static void main(String[] args) throws Exception {
        SkyRequest request = new SkyRequest();
        request.setDbName("account");
        request.setTraceId(UUID.randomUUID().toString());
        request.setSqlId("123");
        request.getParams().put("username", "田晓霞");
        request.getParams().put("password", "123456");

        for(int i=0;i<10000;i++) {
            //连接netty，并获得一个代理对象
            SkyResponse<SkyRequest> bean = SkyProxy.execute(request);
            System.out.println((bean.getData().getDbName()));
            System.out.println((bean.getData().getSqlId()));
            Thread.sleep(1000);
        }
    }


}
