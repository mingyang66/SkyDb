package com.emily.skydb.server.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.server.db.constant.DbName;
import com.emily.skydb.server.db.helper.DbHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.db.pool.DataSourceProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/1 3:02 PM
 */
public class DruidQueryTest {
    public static void main(String[] args) {
        DataSourceProperties properties = new DataSourceProperties();
        properties.setDbType(DbName.ACCOUNT);
        properties.setDriver("com.mysql.cj.jdbc.Driver");
        properties.setUrl("jdbc:mysql://127.0.0.1:3306/ocean_sky?characterEncoding=utf-8&rewriteBatchedStatements=true");
        properties.setUsername("root");
        properties.setPassword("smallgrain");
        List<DataSourceProperties> list = new ArrayList<>();
        list.add(properties);

        DataSourcePoolManager.bootstrap(list);
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(DbName.ACCOUNT);

        for (int i = 0; i < 1; i++) {
            String sql = "SELECT * FROM sailboat s";
            List<Map<String, DbModelItem>> list1 = DbHelper.executeQuery(dataSource, sql);
            System.out.println(list1);
        }

    }
}
