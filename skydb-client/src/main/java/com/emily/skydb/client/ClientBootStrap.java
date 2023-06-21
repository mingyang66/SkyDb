package com.emily.skydb.client;

import com.emily.infrastructure.json.JsonUtils;
import com.emily.skydb.client.manager.DbClientManager;
import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;
import com.emily.skydb.core.protocol.TransContent;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {

    public static void main(String[] args) throws Exception {
        // DbClientProperties properties = new DbClientProperties();
        //LoadBalance loadBalance = new RoundLoadBalance();
        //properties.getPool().setMinIdle(1);
        //DbClientManager.initPool(properties, loadBalance);
        //Thread.sleep(2 * 1000);
        //System.out.println("------------------休眠结束----------------");
        for (int i = 0; i < 5000; i++) {
            try {
                selectBody(TestUser.class);
                List<String> list = selectBody(String.class, String.valueOf(i));
                if (list.size() == 0) {
                    continue;
                }
                if (StringUtils.equals(i + "", list.get(0))) {
                    System.out.println(i + "---------true");
                } else {
                    System.out.println(i + "---------false");
                }
                Thread.sleep(1000*60*1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void insertBody() throws Exception {
        TransContent transContent = new TransContent();
        transContent.dbName = "account";
        transContent.dbTag = "insert_test";
        transContent.params.add(new DbModelItem("name", "田晓霞"));
        transContent.params.add(new DbModelItem("color", "女"));
        transContent.params.add(new DbModelItem("age", "18", JdbcType.Int));
        transContent.params.add(new DbModelItem("year", "2023", JdbcType.Year));
        transContent.params.add(new DbModelItem("price", "6183.26", JdbcType.BigDecimal));
        transContent.params.add(new DbModelItem("updateTime", "2023-03-03 17:23:56", JdbcType.TimeStamp));
        transContent.params.add(new DbModelItem("insertTime", "2023-03-03 17:23:56", JdbcType.TimeStamp));
        int rows = DbClientManager.executeUpdate(transContent);
        System.out.println(rows);
    }

    public static <T> List<T> selectBody(Class<T> cls) {
        try {

            TransContent transContent = new TransContent();
            transContent.dbName = "account";
            transContent.dbTag = "select_test_tj";
            transContent.params.add(new DbModelItem("age", "6277"));
            List<T> list = DbClientManager.executeQuery(transContent, cls);
            System.out.println(JsonUtils.toJSONString(list));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> selectBody(Class<T> cls, String i) {
        try {

            TransContent transContent = new TransContent();
            transContent.dbName = "account";
            transContent.dbTag = "select_test_dual";
            transContent.params.add(new DbModelItem("testText", i));
            List<T> list = DbClientManager.executeQuery(transContent, cls);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
