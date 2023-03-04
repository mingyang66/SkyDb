package com.emily.skydb.server.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.DbParamItem;
import com.emily.skydb.core.protocol.DbReqBody;
import com.emily.skydb.server.db.helper.DbHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.handler.SkyBusinessHandler;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @Description :  基于Druid的数据库连接池后置业务处理
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/2 7:51 PM
 */
public class DruidBusinessHandler implements SkyBusinessHandler {
    @Override
    public Object handler(DbReqBody reqDbBody) throws IOException {
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(reqDbBody.dbName);
       /* String sql = "SELECT * FROM sailboat s";
        List<Map<String, Object>> list = SqlHelper.executeQuery(dataSource, sql);
        System.out.println(JsonUtils.toJSONString(list));
        return list;*/

        String sql = "insert into sailboat(name,colour,age,price,insertTime,updateTime) VALUES('田福军','男',36,4534.22,str_to_date(:insertTime,'%Y-%m-%d %H:%i:%s'),str_to_date(:updateTime,'%Y-%m-%d %H:%i:%s'))";
        for (int i = 0; i < reqDbBody.params.size(); i++) {
            DbParamItem item = reqDbBody.params.get(i);
            sql = StringUtils.replace(sql, MessageFormat.format(":{0}", item.name), MessageFormat.format("{0}{1}{2}", "\'", item.value, "\'"));
        }

        int rows = DbHelper.executeUpdate(dataSource, sql);
        System.out.println("插入数据行数：" + rows);
        return rows;
    }
}
