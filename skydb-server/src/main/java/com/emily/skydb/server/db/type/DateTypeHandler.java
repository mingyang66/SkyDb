package com.emily.skydb.server.db.type;

import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;
import com.emily.skydb.core.enums.DateFormatType;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * @Description :  日期处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 5:22 PM
 */
public class DateTypeHandler implements TypeHandler {
    @Override
    public DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        DbModelItem item = new DbModelItem();
        item.valueType = JdbcType.Date;
        item.name = rs.getMetaData().getColumnName(columnIndex);
        Date date = rs.getDate(columnIndex);
        if (date != null) {
            item.value = date.toLocalDate().format(DateTimeFormatter.ofPattern(DateFormatType.YYYY_MM_DD.getFormat()));
        }
        return item;
    }
}
