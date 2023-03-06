package com.emily.skydb.server.db.repository.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.server.db.constant.DbName;
import com.emily.skydb.server.db.entity.MiddleWare;
import com.emily.skydb.server.db.helper.DbHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.db.repository.MiddleWareRepository;

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
            middleWare.dbName = item.get("dbName").value;
            middleWare.dbTag = item.get("dbTag").value;
            middleWare.dbType = item.get("dbType").value;
            middleWare.sqlText = item.get("sqlText").value;
            dataMap.put(middleWare.dbTag, middleWare);
        });
        return dataMap;
    }
}
