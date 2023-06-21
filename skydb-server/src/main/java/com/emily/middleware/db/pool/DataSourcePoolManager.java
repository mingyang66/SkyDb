package com.emily.middleware.db.pool;

import com.alibaba.druid.pool.DruidDataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description :  数据库初始化管理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/1 5:12 PM
 */
public class DataSourcePoolManager {
    private static Map<String, DruidDataSource> dataSourceMap = new HashMap<>();

    public static void bootstrap(List<DataSourceProperties> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        list.forEach(properties -> {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setDriverClassName(properties.getDriver());
            dataSource.setUrl(properties.getUrl());
            dataSource.setUsername(properties.getUsername());
            dataSource.setPassword(properties.getPassword());
            dataSourceMap.put(properties.getDbType(), dataSource);
        });
    }

    public static boolean containsKey(String dbType) {
        return dataSourceMap.containsKey(dbType);
    }

    public static DruidDataSource getDataSource(String dbType) {
        return dataSourceMap.get(dbType);
    }
}
