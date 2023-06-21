package com.emily.middleware.db.type;

import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description :  布尔处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 3:41 PM
 */
public class BooleanTypeHandler implements TypeHandler {
    @Override
    public DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        DbModelItem item = new DbModelItem();
        item.valueType = JdbcType.Boolean;
        item.name = rs.getMetaData().getColumnName(columnIndex);
        item.value = rs.getString(columnIndex);
        return item;
    }
}
