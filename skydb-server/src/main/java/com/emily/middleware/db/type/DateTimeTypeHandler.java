package com.emily.middleware.db.type;

import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

/**
 * @Description :  日期处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 5:13 PM
 */
public class DateTimeTypeHandler implements TypeHandler {
    @Override
    public DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        DbModelItem item = new DbModelItem();
        item.valueType = JdbcType.TimeStamp;
        item.name = rs.getMetaData().getColumnName(columnIndex);
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        if (timestamp != null) {
            item.value = timestamp.toLocalDateTime().format(DateTimeFormatter.ofPattern(DatePatternInfo.YYYY_MM_DD_HH_MM_SS));
        }
        return item;
    }
}
