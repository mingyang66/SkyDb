package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.SkyTransBody;
import com.emily.skydb.core.protocol.SkyTransResponse;

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
        SkyTransBody transBody = new SkyTransBody();
        transBody.setDbName("account");
        transBody.setSqlId("123");
        transBody.getParams().put("username", "田晓霞");
        transBody.getParams().put("password", "123456");

        for (int i = 0; i < 1000000; i++) {
            //连接netty，并获得一个代理对象
            SkyTransResponse<SkyTransBody> bean = SkyClientManager.execute(transBody);
            if (bean != null) {
                System.out.println((bean.getData().getDbName()) + "-------------" + (bean.getData().getSqlId()) + "---" + i);
            }
            //Thread.sleep(1000);
        }
    }


}
