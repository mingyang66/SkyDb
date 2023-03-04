package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.protocol.JdbcType;
import com.emily.skydb.core.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

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

        selectBody();
    }


    public static void insertBody() throws Exception {
        DbTransBody transBody = new DbTransBody();
        transBody.dbName = "account";
        transBody.dbTag = "insert_test";
        transBody.params.add(new DbModelItem("name", "田晓霞"));
        transBody.params.add(new DbModelItem("color", "女"));
        transBody.params.add(new DbModelItem("age", "18", JdbcType.Int32));
        transBody.params.add(new DbModelItem("year", "2023", JdbcType.Year));
        transBody.params.add(new DbModelItem("price", "6183.26", JdbcType.Decimal));
        transBody.params.add(new DbModelItem("updateTime", "2023-03-03 17:23:56", JdbcType.DateTime));
        transBody.params.add(new DbModelItem("insertTime", "2023-03-03 17:23:56", JdbcType.DateTime));
        int rows = SkyClientManager.invoke(transBody, new TypeReference<Integer>() {
        });
        System.out.println(rows);
    }

    public static void selectBody() throws Exception {
        DbTransBody transBody = new DbTransBody();
        transBody.dbName = "account";
        transBody.dbTag = "select_test_tj";
        transBody.params.add(new DbModelItem("age", "1"));
        List<TestUser> list = SkyClientManager.invoke(transBody, new TypeReference<>() {
        });
        System.out.println(JsonUtils.toJSONString(list));

    }
}
