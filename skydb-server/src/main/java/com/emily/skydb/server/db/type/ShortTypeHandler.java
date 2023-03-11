package com.emily.skydb.server.db.type;

import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description :  short处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 3:41 PM
 */
public class ShortTypeHandler implements TypeHandler {
    @Override
    public DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        DbModelItem item = new DbModelItem();
        item.valueType = JdbcType.Short;
        item.name = rs.getMetaData().getColumnName(columnIndex);
        item.value = rs.getString(columnIndex);
        return item;
    }
}
