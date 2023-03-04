package com.emily.skydb.server.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.DbOperationType;
import com.emily.skydb.core.protocol.DbReqBody;
import com.emily.skydb.core.utils.StrUtils;
import com.emily.skydb.server.db.entity.MiddleWare;
import com.emily.skydb.server.db.helper.DbCacheHelper;
import com.emily.skydb.server.db.helper.DbHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.handler.SkyBusinessHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @Description :  基于Druid的数据库连接池后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/2 7:51 PM
 */
public class DruidBusinessHandler implements SkyBusinessHandler {
    @Override
    public Object handler(DbReqBody dbReqBody) throws IOException {
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(dbReqBody.dbName);
       /* String sql = "SELECT * FROM sailboat s";
        List<Map<String, Object>> list = SqlHelper.executeQuery(dataSource, sql);
        System.out.println(JsonUtils.toJSONString(list));
        return list;*/
        if (!DbCacheHelper.CACHE.containsKey(dbReqBody.id)) {
            //todo 抛异常处理
        }
        MiddleWare middleWare = DbCacheHelper.CACHE.get(dbReqBody.id);
        //String sql = "insert into sailboat(name,colour,age,price,insertTime,updateTime,year) VALUES(:name,:color,:age,:price,str_to_date(:insertTime,'%Y-%m-%d %H:%i:%s'),str_to_date(:updateTime,'%Y-%m-%d %H:%i:%s'),:year)";
        String newSql = StrUtils.replacePlaceHolder(middleWare.sqlText, dbReqBody.params);
        if (StringUtils.equals(middleWare.dbType, DbOperationType.INSERT)) {
            int rows = DbHelper.executeUpdate(dataSource, newSql);
            System.out.println("插入数据行数：" + rows);
            return rows;
        }
        return null;
    }
}
