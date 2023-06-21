package com.emily.middleware.datasource.type;

import com.emily.skydb.core.db.DbModelItem;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html
 *
 * @Description :  数据类型
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 3:39 PM
 */
public interface TypeHandler {
    /**
     * 获取可以为空的字段结果
     *
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    DbModelItem getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

}
