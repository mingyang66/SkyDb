package com.emily.skydb.client;

import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.entity.SkyRequest;
import com.emily.skydb.core.entity.SkyResponse;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {


    public static void main(String[] args) throws Exception {
        SkyClientProperties properties = new SkyClientProperties();
        properties.getPool().setMinIdle(1);
        SkyClientManager.initPool(properties);
        SkyRequest request = new SkyRequest();
        request.setDbName("account");
        request.setSqlId("123");
        request.getParams().put("username", "田晓霞");
        request.getParams().put("password", "123456");

        for (int i = 0; i < 10000; i++) {
            //连接netty，并获得一个代理对象
            SkyResponse<SkyRequest> bean = SkyClientManager.execute(request);
            if (bean != null) {
                System.out.println((bean.getData().getDbName()) + "-------------" + (bean.getData().getDbName()));
            }
            Thread.sleep(1000);
        }
    }


}
