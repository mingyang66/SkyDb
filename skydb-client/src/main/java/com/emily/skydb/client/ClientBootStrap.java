package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.protocol.DbParamItem;
import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.protocol.JDBCType;
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
        transBody.params.add(new DbParamItem("name", "田晓霞"));
        transBody.params.add(new DbParamItem("color", "女"));
        transBody.params.add(new DbParamItem("age", "18", JDBCType.Int32));
        transBody.params.add(new DbParamItem("year", "2023", JDBCType.Year));
        transBody.params.add(new DbParamItem("price", "6183.26", JDBCType.Decimal));
        transBody.params.add(new DbParamItem("updateTime", "2023-03-03 17:23:56", JDBCType.DateTime));
        transBody.params.add(new DbParamItem("insertTime", "2023-03-03 17:23:56", JDBCType.DateTime));
        int rows = SkyClientManager.invoke(transBody, new TypeReference<Integer>() {
        });
        System.out.println(rows);
    }

    public static void selectBody() throws Exception {
        DbTransBody transBody = new DbTransBody();
        transBody.dbName = "account";
        transBody.dbTag = "select_test_tj";
        transBody.params.add(new DbParamItem("age", "1"));
        List<TestUser> list = SkyClientManager.invoke(transBody, new TypeReference<>() {
        });
        System.out.println(JsonUtils.toJSONString(list));

    }
}
