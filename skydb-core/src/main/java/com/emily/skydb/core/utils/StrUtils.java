package com.emily.skydb.core.utils;

import com.emily.skydb.core.protocol.DbParamItem;
import com.emily.skydb.core.protocol.DbType;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;

/**
 * @Description :  字符串处理工具类
 * @Author :  姚明洋
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
    public static String replacePlaceHolder(String sql, List<DbParamItem> items) {
        if (StringUtils.isEmpty(sql) || items == null || items.isEmpty()) {
            return sql;
        }
        String newSql = sql;
        for (int i = 0; i < items.size(); i++) {
            DbParamItem item = items.get(i);
            switch (item.valueType) {
                case DbType.Int32:
                case DbType.Int64:
                case DbType.Year:
                case DbType.Decimal:
                    newSql = StringUtils.replace(newSql, MessageFormat.format(":{0}", item.name), MessageFormat.format("{0}", item.value));
                    break;
                case DbType.DateTime:
                case DbType.TimeStamp:
                case DbType.Date:
                case DbType.Time:
                default:
                    newSql = StringUtils.replace(newSql, MessageFormat.format(":{0}", item.name), MessageFormat.format("{0}{1}{2}", "\'", item.value, "\'"));
                    break;
            }
        }
        return newSql;
    }
}
