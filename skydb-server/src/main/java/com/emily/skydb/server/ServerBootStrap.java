package com.emily.skydb.server;


import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.ReqDbBody;
import com.emily.skydb.server.db.constant.DbName;
import com.emily.skydb.server.db.helper.SqlHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.db.pool.DataSourceProperties;
import com.emily.skydb.server.handler.SkyBusinessHandler;
import com.emily.skydb.server.manager.SkyServerManager;
import com.emily.skydb.server.manager.SkyServerProperties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Emily
 */
public class ServerBootStrap {
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
            List<Map<String, Object>> list1 = SqlHelper.executeQuery(dataSource, sql);
            System.out.println(list1);
        }

//--------------------------------------------------------------------------
        SkyServerProperties properties1 = new SkyServerProperties();
        SkyBusinessHandler handler = new SkyBusinessHandler() {
            @Override
            public Object handler(ReqDbBody reqDbBody) throws IOException {
                return SkyBusinessHandler.super.handler(reqDbBody);
            }
        };

        SkyServerManager.bootstrap(handler, properties1);
    }

}
