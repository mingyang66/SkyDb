package com.emily.skydb.server.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.skydb.core.protocol.ReqDbBody;
import com.emily.skydb.core.utils.JsonUtils;
import com.emily.skydb.server.db.constant.DbName;
import com.emily.skydb.server.db.helper.SqlHelper;
import com.emily.skydb.server.db.pool.DataSourcePoolManager;
import com.emily.skydb.server.handler.SkyBusinessHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description :  基于Druid的数据库连接池后置业务处理
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/3/2 7:51 PM
 */
public class DruidBusinessHandler implements SkyBusinessHandler {
    @Override
    public Object handler(ReqDbBody reqDbBody) throws IOException {
        DruidDataSource dataSource = DataSourcePoolManager.getDataSource(DbName.ACCOUNT);

        String sql = "SELECT * FROM sailboat s";
        List<Map<String, Object>> list = SqlHelper.executeQuery(dataSource, sql);
        //System.out.println(JsonUtils.toJSONString(list));
        return list;
    }
}
