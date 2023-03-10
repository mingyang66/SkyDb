package com.emily.skydb.server.db.helper;

import com.emily.skydb.core.enums.DateFormatType;
import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;

import java.sql.*;
import java.time.format.DateTimeFormatter;

/**
 * @Description :  数据库数据处理帮助类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/7 9:53 AM
 */
public class DbDataHelper {
    /**
     * 获取数据库映射数据-MYSQL
     *
     * @param rs
     * @param rsmd
     * @param j
     * @return
     */
    public static DbModelItem getDbModelItem(ResultSet rs, ResultSetMetaData rsmd, int j) throws SQLException {
        DbModelItem item = new DbModelItem(rsmd.getColumnName(j));
        JDBCType jdbcType = JDBCType.valueOf(rsmd.getColumnType(j));
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
        return item;
    }
}
