package com.emily.middleware.datasource.repository.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.middleware.datasource.entity.MiddleWare;
import com.emily.middleware.datasource.pool.DataSourcePoolManager;
import com.emily.skydb.core.db.DbModelItem;
import com.emily.middleware.datasource.constant.DbName;
import com.emily.middleware.datasource.helper.DbHelper;
import com.emily.middleware.datasource.repository.MiddleWareRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description :  数据库中间件仓储服务
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/4 3:35 PM
 */
public class MiddleWareRepositoryImpl implements MiddleWareRepository {
    @Override
    public Map<String, MiddleWare> queryMiddleWare() {
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(DbName.ACCOUNT);
        String sql = "SELECT d.sqlText,d.dbName,d.dbType,d.dbTag FROM DbMiddleware d ";
        List<Map<String, DbModelItem>> list = DbHelper.executeQuery(dataSource, sql);
        Map<String, MiddleWare> dataMap = new HashMap<>();
        list.stream().forEach(item -> {
            MiddleWare middleWare = new MiddleWare();
            middleWare.dbName = item.get("dbName").value.toString();
            middleWare.dbTag = item.get("dbTag").value.toString();
            middleWare.dbType = item.get("dbType").value.toString();
            middleWare.sqlText = item.get("sqlText").value.toString();
            dataMap.put(middleWare.dbTag, middleWare);
        });
        return dataMap;
    }
}
