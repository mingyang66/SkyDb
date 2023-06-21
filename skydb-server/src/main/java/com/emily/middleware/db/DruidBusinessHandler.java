package com.emily.middleware.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.middleware.db.entity.MiddleWare;
import com.emily.middleware.db.pool.DataSourcePoolManager;
import com.emily.skydb.core.db.DbType;
import com.emily.skydb.core.protocol.TransContent;
import com.emily.skydb.core.utils.StrUtils;
import com.emily.middleware.db.helper.DbCacheHelper;
import com.emily.middleware.db.helper.DbHelper;
import com.emily.middleware.server.handler.DbBusinessHandler;

import java.io.IOException;

/**
 * @Description :  基于Druid的数据库连接池后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/2 7:51 PM
 */
public class DruidBusinessHandler implements DbBusinessHandler {
    @Override
    public Object handler(TransContent content) {
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(content.dbName);
        if (!DbCacheHelper.CACHE.containsKey(content.dbTag)) {
            //todo 抛异常处理
        }
        MiddleWare middleWare = DbCacheHelper.CACHE.get(content.dbTag);
        //String sql = "insert into sailboat(name,colour,age,price,insertTime,updateTime,year) VALUES(:name,:color,:age,:price,str_to_date(:insertTime,'%Y-%m-%d %H:%i:%s'),str_to_date(:updateTime,'%Y-%m-%d %H:%i:%s'),:year)";
        String newSql = StrUtils.replacePlaceHolder(middleWare.sqlText, content.params);
        Object response = null;
        switch (middleWare.dbType) {
            case DbType.INSERT:
            case DbType.DELETE:
            case DbType.UPDATE:
                response = DbHelper.executeUpdate(dataSource, newSql);
                System.out.println("插入数据行数：" + response);
                break;
            case DbType.SELECT:
                response = DbHelper.executeQuery(dataSource, newSql);
                System.out.println("查询结果是：" + JsonUtils.toJSONString(response));
                break;
        }
        return response;
    }
}
