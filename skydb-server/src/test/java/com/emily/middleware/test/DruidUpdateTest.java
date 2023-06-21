package com.emily.middleware.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.emily.middleware.db.constant.DbName;
import com.emily.middleware.db.helper.DbHelper;
import com.emily.middleware.db.pool.DataSourcePoolManager;
import com.emily.middleware.db.pool.DataSourceProperties;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description :
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/1 3:02 PM
 */
public class DruidUpdateTest {
    @Test
    public void update() {
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
            //String sql = "UPDATE sailboat s set s.updateTime=NOW() WHERE s.id=2";
            //String sql = "DELETE FROM sailboat s WHERE s.id=14";
            //String sql = "INSERT INTO sailboat(name,colour,age,updateTime) VALUES('孙少安','男',21,NOW())";
            String sql = "INSERT INTO sailboat(name,colour,age,updateTime) VALUES('孙少安','男',21,NOW()),('田晓霞','女',20,NOW())";
            int row = DbHelper.executeUpdate(dataSource, sql);
            System.out.println("新增数据条数：" + row);
        }

    }
}
