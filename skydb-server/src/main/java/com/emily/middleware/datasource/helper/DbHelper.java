package com.emily.middleware.datasource.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.emily.middleware.datasource.type.TypeHandler;
import com.emily.middleware.datasource.type.TypeHandlerRegistry;
import com.emily.skydb.core.db.DbModelItem;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description :  执行sql帮助类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/1 5:46 PM
 */
public class DbHelper {
    private static final TypeHandlerRegistry registry = new TypeHandlerRegistry();

    /**
     * 查询数据库中符合条件的数据
     *
     * @param dataSource 数据源对象
     * @param sql        sql语句
     * @return
     */
    public static List<Map<String, DbModelItem>> executeQuery(DruidDataSource dataSource, String sql) {
        List<Map<String, DbModelItem>> list = new ArrayList<>();
        DruidPooledConnection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            //获取数据库连接
            connection = dataSource.getConnection();
            //预编译的SQL语句对象
            statement = connection.prepareStatement(sql);
            //执行查询操作
            rs = statement.executeQuery();
            //获取一个可以获取ResultSet对象列的名称、数量等信息的对象
            ResultSetMetaData rsmd = rs.getMetaData();
            //获取查询到的属性个数
            int numberOfColumns = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, DbModelItem> dataMap = new HashMap<>(numberOfColumns);
                for (int j = 1; j <= numberOfColumns; j++) {
                    String className = rsmd.getColumnClassName(j);
                    TypeHandler handler = registry.getTypeHandler(className);
                    if (handler != null) {
                        DbModelItem item = handler.getNullableResult(rs, j);
                        dataMap.put(item.name, item);
                    }
                }
                list.add(dataMap);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                rs.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return list;
    }

    /**
     * 查询数据库中符合条件的数据
     *
     * @param dataSource 数据源对象
     * @param sql        sql语句
     * @return
     */
    public static int executeUpdate(DruidDataSource dataSource, String sql) {
        DruidPooledConnection connection = null;
        PreparedStatement statement = null;
        try {
            //获取数据库连接
            connection = dataSource.getConnection();
            //预编译的SQL语句对象
            statement = connection.prepareStatement(sql);
            //执行更新操作
            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
