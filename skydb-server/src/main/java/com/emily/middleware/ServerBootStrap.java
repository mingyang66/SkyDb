package com.emily.middleware;


import com.emily.infrastructure.json.JsonUtils;
import com.emily.middleware.db.pool.DataSourceProperties;
import com.emily.middleware.manager.DbServerManager;
import com.emily.middleware.db.DruidBusinessHandler;
import com.emily.middleware.db.constant.DbName;
import com.emily.middleware.db.entity.MiddleWare;
import com.emily.middleware.db.helper.DbCacheHelper;
import com.emily.middleware.db.pool.DataSourcePoolManager;
import com.emily.middleware.db.repository.MiddleWareRepository;
import com.emily.middleware.db.repository.impl.MiddleWareRepositoryImpl;
import com.emily.middleware.server.handler.DbBusinessHandler;
import com.emily.middleware.manager.DbServerProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Emily
 */
public class ServerBootStrap {
    public static void main(String[] args) {
        //---------------------------数据库连接池初始化-----------------------------------
        DataSourceProperties properties = new DataSourceProperties();
        properties.setDbType(DbName.ACCOUNT);
        properties.setDriver("com.mysql.cj.jdbc.Driver");
        properties.setUrl("jdbc:mysql://127.0.0.1:3306/ocean_sky?characterEncoding=utf-8&rewriteBatchedStatements=true&yearIsDateType=false");
        properties.setUsername("root");
        properties.setPassword("smallgrain");
        List<DataSourceProperties> list = new ArrayList<>();
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
