package com.emily.middleware.db.type;

import com.emily.infrastructure.date.DatePatternInfo;
import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

/**
 * @Description :  日期处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 5:22 PM
 */
public class TimeTypeHandler implements TypeHandler {
    @Override
    public DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        DbModelItem item = new DbModelItem();
        item.valueType = JdbcType.Time;
        item.name = rs.getMetaData().getColumnName(columnIndex);
        Time time = rs.getTime(columnIndex);
        if (time != null) {
            item.value = time.toLocalTime().format(DateTimeFormatter.ofPattern(DatePatternInfo.HH_MM_SS));
        }
        return item;
    }
}
