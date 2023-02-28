package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.BaseResponse;
import com.emily.skydb.core.protocol.BodyProtocol;

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
        bodyProtocol.dbName = "account";
        bodyProtocol.sqlId = "123";
        bodyProtocol.params.put("username", "孙少安|田晓霞|孙少平|田晓霞|孙少平||田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平|田晓霞|孙少平");
        bodyProtocol.params.put("password", "123456");

        for (int i = 0; i < 1000000; i++) {
            //连接netty，并获得一个代理对象
            BaseResponse<BodyProtocol> bean = SkyClientManager.invoke(bodyProtocol);
            if (bean != null) {
                System.out.println(bean.getData().dbName + "-------------" + bean.getData().params.get("username") + "---" + i);
            }
            //Thread.sleep(1000);
        }
    }


}
