package com.emily.skydb.server.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.infrastructure.json.JsonUtils;
import com.emily.skydb.core.db.DbType;
import com.emily.skydb.core.protocol.TransContent;
import com.emily.skydb.core.utils.StrUtils;
import com.emily.skydb.server.db.entity.MiddleWare;
import com.emily.skydb.server.db.helper.DbCacheHelper;
import com.emily.skydb.server.db.helper.DbHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.handler.DbBusinessHandler;

import java.io.IOException;

/**
 * @Description :  基于Druid的数据库连接池后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/2 7:51 PM
 */
public class DruidBusinessHandler implements DbBusinessHandler {
    @Override
    public Object handler(TransContent dbReqBody) throws IOException {
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(dbReqBody.dbName);
       /* String sql = "SELECT * FROM sailboat s";
        List<Map<String, Object>> list = SqlHelper.executeQuery(dataSource, sql);
        System.out.println(JsonUtils.toJSONString(list));
        return list;*/
        if (!DbCacheHelper.CACHE.containsKey(dbReqBody.dbTag)) {
            //todo 抛异常处理
        }
        MiddleWare middleWare = DbCacheHelper.CACHE.get(dbReqBody.dbTag);
        //String sql = "insert into sailboat(name,colour,age,price,insertTime,updateTime,year) VALUES(:name,:color,:age,:price,str_to_date(:insertTime,'%Y-%m-%d %H:%i:%s'),str_to_date(:updateTime,'%Y-%m-%d %H:%i:%s'),:year)";
        String newSql = StrUtils.replacePlaceHolder(middleWare.sqlText, dbReqBody.params);
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
