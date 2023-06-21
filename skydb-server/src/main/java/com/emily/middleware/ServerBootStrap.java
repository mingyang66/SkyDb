package com.emily.middleware;


import com.emily.infrastructure.common.PropertiesUtils;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.middleware.datasource.pool.DataSourceProperties;
import com.emily.middleware.server.manager.DbServerManager;
import com.emily.middleware.datasource.DruidBusinessHandler;
import com.emily.middleware.datasource.constant.DbName;
import com.emily.middleware.datasource.entity.MiddleWare;
import com.emily.middleware.datasource.helper.DbCacheHelper;
import com.emily.middleware.datasource.pool.DataSourcePoolManager;
import com.emily.middleware.datasource.repository.MiddleWareRepository;
import com.emily.middleware.datasource.repository.impl.MiddleWareRepositoryImpl;
import com.emily.middleware.server.handler.DbBusinessHandler;
import com.emily.middleware.server.manager.DbServerProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) throws IOException {
        //---------------------------数据库连接池初始化-----------------------------------
        Properties prop = PropertiesUtils.loadConfig("classpath:application.properties");
        List<DataSourceProperties> list = new ArrayList<>();
        DataSourceProperties properties = new DataSourceProperties();
        properties.setDbType(prop.getProperty("db.test.type"));
        properties.setDriver(prop.getProperty("db.test.driver"));
        properties.setUrl(prop.getProperty("db.test.url"));
        properties.setUsername(prop.getProperty("db.test.username"));
        properties.setPassword(prop.getProperty("db.test.password"));
        list.add(properties);

        DataSourcePoolManager.bootstrap(list);

        //-------------------------------------数据库中间件配置初始化------------------------------
        MiddleWareRepository middleWareRepository = new MiddleWareRepositoryImpl();
        Map<String, MiddleWare> cacheMap = middleWareRepository.queryMiddleWare();
        DbCacheHelper.CACHE.putAll(cacheMap);
        System.out.println(JsonUtils.toJSONString(cacheMap));

        //-------------------------------基于Netty的TCP服务器启动-------------------------------------------
        DbServerProperties properties1 = new DbServerProperties();
        DbBusinessHandler handler = new DruidBusinessHandler();

        DbServerManager.bootstrap(handler, properties1);
    }

}
