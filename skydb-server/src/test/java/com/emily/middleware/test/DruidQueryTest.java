package com.emily.middleware.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.middleware.datasource.constant.DbName;
import com.emily.middleware.datasource.helper.DbHelper;
import com.emily.middleware.datasource.pool.DataSourcePoolManager;
import com.emily.middleware.datasource.pool.DataSourceProperties;
import com.emily.skydb.core.db.DbModelItem;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/1 3:02 PM
 */
public class DruidQueryTest {
    @Test
    public void query() {
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
