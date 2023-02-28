package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.BodyProtocol;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {


    public static void main(String[] args) {
        SkyClientProperties properties = new SkyClientProperties();
        LoadBalance loadBalance = new RoundLoadBalance();
        properties.getPool().setMinIdle(1);
        SkyClientManager.initPool(properties, loadBalance);
        BodyProtocol bodyProtocol = new BodyProtocol();
        bodyProtocol.dbName = "account";
        bodyProtocol.sqlId = "123";
        bodyProtocol.params.put("username", "孙少安|田晓霞|孙少平|田晓霞|孙少平||田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平");
        bodyProtocol.params.put("password", "123456");

        for (int i = 0; i < 1000000; i++) {
            try {

                //连接netty，并获得一个代理对象
                TestUser bean = SkyClientManager.invoke(bodyProtocol, TestUser.class);
                if (bean != null) {
                    System.out.println(bean.username + "--------" + bean.password);
                }
                //Thread.sleep(1000);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}
