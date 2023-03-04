package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.DbParamItem;
import com.emily.skydb.core.protocol.DbReqBody;
import com.emily.skydb.core.protocol.DbType;
import com.fasterxml.jackson.core.type.TypeReference;

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
        DbReqBody reqDbBody = new DbReqBody();
        reqDbBody.dbName = "account";
        reqDbBody.id = "insert_test";
        reqDbBody.params.add(new DbParamItem("name", "田晓霞"));
        reqDbBody.params.add(new DbParamItem("color", "女"));
        reqDbBody.params.add(new DbParamItem("age", "18", DbType.Int32));
        reqDbBody.params.add(new DbParamItem("year", "2023", DbType.Year));
        reqDbBody.params.add(new DbParamItem("price", "6183.26", DbType.Decimal));
        reqDbBody.params.add(new DbParamItem("updateTime", "2023-03-03 17:23:56", DbType.DateTime));
        reqDbBody.params.add(new DbParamItem("insertTime", "2023-03-03 17:23:56", DbType.DateTime));

        for (int i = 0; i < 1; i++) {
            try {

                //连接netty，并获得一个代理对象
               /* List<TestUser> bean = SkyClientManager.invoke(reqDbBody, new TypeReference<>() {
                });
                if (bean != null) {
                    System.out.println(bean.get(0).name + "--------" + bean.get(0).colour);
                }*/
                int rows = SkyClientManager.invoke(reqDbBody, new TypeReference<Integer>() {
                });
                System.out.println(rows);
                //Thread.sleep(1000);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}
