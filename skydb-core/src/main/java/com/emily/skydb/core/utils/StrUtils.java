package com.emily.skydb.core.utils;

import com.emily.skydb.core.db.DbModelItem;
import com.emily.skydb.core.db.JdbcType;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * @Description :  字符串处理工具类
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/4 2:44 PM
 */
public class StrUtils {
    /**
     * 替换sql占位符，返回新字符串
     *
     * @param sql   带占位符的sql
     * @param items 参数列表
     * @return 替换占位符后的sql
     */
    public static String replacePlaceHolder(String sql, List<DbModelItem> items) {
        if (StringUtils.isEmpty(sql) || items == null || items.isEmpty()) {
            return sql;
        }
        String newSql = sql;
        for (int i = 0; i < items.size(); i++) {
            DbModelItem item = items.get(i);
            switch (item.valueType) {
                case JdbcType.Int:
                case JdbcType.Long:
                case JdbcType.Year:
                case JdbcType.BigDecimal:
                    newSql = StringUtils.replace(newSql, MessageFormat.format(":{0}", item.name), MessageFormat.format("{0}", item.value));
                    break;
                case JdbcType.TimeStamp:
                case JdbcType.Date:
                case JdbcType.Time:
                default:
                    newSql = StringUtils.replace(newSql, MessageFormat.format(":{0}", item.name), MessageFormat.format("{0}{1}{2}", "\'", item.value, "\'"));
                    break;
            }
        }
        return newSql;
    }
}
