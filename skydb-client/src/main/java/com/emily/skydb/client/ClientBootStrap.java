package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.BodyProtocol;
import com.emily.skydb.core.protocol.BaseResponse;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {


    public static void main(String[] args) throws Exception {
        SkyClientProperties properties = new SkyClientProperties();
        LoadBalance loadBalance = new RoundLoadBalance();
        properties.getPool().setMinIdle(1);
        SkyClientManager.initPool(properties, loadBalance);
        BodyProtocol bodyProtocol = new BodyProtocol();
        bodyProtocol.setDbName("account");
        bodyProtocol.setSqlId("123");
        bodyProtocol.getParams().put("username", "田晓霞");
        bodyProtocol.getParams().put("password", "123456");

        for (int i = 0; i < 1000000; i++) {
            //连接netty，并获得一个代理对象
            BaseResponse<String> bean = SkyClientManager.execute(bodyProtocol);
            if (bean != null) {
                System.out.println(bean.getData()+ "-------------" + bean.getData() + "---" + i);
            }
            //Thread.sleep(1000);
        }
    }


}
