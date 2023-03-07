package com.emily.skydb.server.db.helper;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.emily.skydb.core.enums.DateFormatType;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.core.protocol.JdbcType;

import java.sql.*;
import java.time.format.DateTimeFormatter;
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
                    DbModelItem item = new DbModelItem();
                    int type = rsmd.getColumnType(j);
                    JDBCType jdbcType = JDBCType.valueOf(type);
                    item.name = rsmd.getColumnName(j);
                    Object value = rs.getObject(j);
                    switch (jdbcType) {
                        case TIMESTAMP:
                            //DATETIME、TIMESTAMP
                            item.valueType = JdbcType.DateTime;
                            Timestamp timestamp = rs.getTimestamp(j);
                            if (timestamp != null) {
                                item.value = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(DateFormatType.YYYY_MM_DD_HH_MM_SS_COLON_SSS.getFormat()));
                            }
                            break;
                        case DATE:
                            //Year、Date
                            item.valueType = JdbcType.Date;
                            Date date = rs.getDate(j);
                            if (date != null) {
                                item.value = date.toLocalDate().format(DateTimeFormatter.ofPattern(DateFormatType.YYYY_MM_DD.getFormat()));
                            }
                            break;
                        case TIME:
                            //TIME
                            item.valueType = JdbcType.Time;
                            Time time = rs.getTime(j);
                            if (time != null) {
                                item.value = time.toLocalTime().format(DateTimeFormatter.ofPattern(DateFormatType.HH_MM_SS.getFormat()));
                            }
                            break;
                        case TINYINT:
                            item.valueType = JdbcType.Byte;
                            item.value = rs.getString(j);
                            break;
                        case SMALLINT:
                            item.valueType = JdbcType.Short;
                            item.value = rs.getString(j);
                            break;
                        case INTEGER:
                            item.valueType = JdbcType.Int;
                            item.value = rs.getString(j);
                            break;
                        case BIGINT:
                            item.valueType = JdbcType.Long;
                            item.value = rs.getString(j);
                            break;
                        case REAL:
                            //case FLOAT: 数据库中的FLOAT类型映射为REAL
                            item.valueType = JdbcType.Float;
                            item.value = rs.getString(j);
                            break;
                        case DOUBLE:
                            //数据库中REAL类型映射为DOUBLE
                            item.valueType = JdbcType.Double;
                            item.value = rs.getString(j);
                            break;
                        //case NUMERIC: 数据库设置直接变成DECIMAL
                        case DECIMAL:
                            item.valueType = JdbcType.Decimal;
                            item.value = rs.getString(j);
                            break;
                        case BIT:
                            //case BOOLEAN: 数据库设置Bool、Bit映射为BIT
                            item.valueType = JdbcType.Boolean;
                            item.value = rs.getString(j);
                            break;
                        case CHAR:
                            item.valueType = JdbcType.Char;
                            item.value = rs.getString(j);
                            break;
                        case BINARY:
                        case LONGVARBINARY:
                        case VARBINARY:
                            item.valueType = JdbcType.Binary;
                            item.value = rs.getString(j);
                            break;
                        case VARCHAR:
                        case LONGVARCHAR:
                        default:
                            item.valueType = JdbcType.String;
                            item.value = rs.getString(j);
                            break;
                    }
                    dataMap.put(item.name, item);
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
