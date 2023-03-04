package com.emily.skydb.server.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.DbReqBody;
import com.emily.skydb.core.utils.StrUtils;
import com.emily.skydb.server.db.helper.DbHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.handler.SkyBusinessHandler;

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

        String sql = "insert into sailboat(name,colour,age,price,insertTime,updateTime,year) " +
                "VALUES(:name,'男',:age,:price," +
                "str_to_date(:insertTime,'%Y-%m-%d %H:%i:%s')," +
                "str_to_date(:updateTime,'%Y-%m-%d %H:%i:%s')," +
                ":year)";
        String newSql = StrUtils.replacePlaceHolder(sql, dbReqBody.params);

        int rows = DbHelper.executeUpdate(dataSource, newSql);
        System.out.println("插入数据行数：" + rows);
        return rows;
    }
}
