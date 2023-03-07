package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.DbClientManager;
import com.emily.skydb.client.manager.DbClientProperties;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.protocol.JdbcType;
import com.emily.skydb.core.utils.JsonUtils;

import java.util.List;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {

    public static void main(String[] args) throws Exception {
        DbClientProperties properties = new DbClientProperties();
        LoadBalance loadBalance = new RoundLoadBalance();
        properties.getPool().setMinIdle(1);
        DbClientManager.initPool(properties, loadBalance);

        for (int i = 0; i < 50; i++) {
            selectBody(TestUser.class);
        }
    }


    public static void insertBody() throws Exception {
        DbTransBody transBody = new DbTransBody();
        transBody.dbName = "account";
        transBody.dbTag = "insert_test";
        transBody.params.add(new DbModelItem("name", "田晓霞"));
        transBody.params.add(new DbModelItem("color", "女"));
        transBody.params.add(new DbModelItem("age", "18", JdbcType.Int));
        transBody.params.add(new DbModelItem("year", "2023", JdbcType.Year));
        transBody.params.add(new DbModelItem("price", "6183.26", JdbcType.Decimal));
        transBody.params.add(new DbModelItem("updateTime", "2023-03-03 17:23:56", JdbcType.DateTime));
        transBody.params.add(new DbModelItem("insertTime", "2023-03-03 17:23:56", JdbcType.DateTime));
        int rows = DbClientManager.executeUpdate(transBody);
        System.out.println(rows);
    }

    public static <T> void selectBody(Class<T> cls) {
        try {

            DbTransBody transBody = new DbTransBody();
            transBody.dbName = "account";
            transBody.dbTag = "select_test_tj";
            transBody.params.add(new DbModelItem("age", "45"));
            List<T> list = DbClientManager.executeQuery(transBody, cls);
            System.out.println(JsonUtils.toJSONString(list));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
