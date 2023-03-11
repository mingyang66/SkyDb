package com.emily.skydb.server.db.type;

import java.sql.JDBCType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description :  数据类型处理器注册中心
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 3:47 PM
 */
public class TypeHandlerRegistry {
    private final Map<JDBCType, TypeHandler> typeHandlerMap = new ConcurrentHashMap<>();

    public TypeHandlerRegistry() {
        typeHandlerMap.put(JDBCType.VARCHAR, new StringTypeHandler());
        typeHandlerMap.put(JDBCType.LONGVARCHAR, new StringTypeHandler());

        typeHandlerMap.put(JDBCType.TIMESTAMP, new TimestampTypeHandler());
        typeHandlerMap.put(JDBCType.DATE, new DateTypeHandler());
        typeHandlerMap.put(JDBCType.TIME, new TimeTypeHandler());

        typeHandlerMap.put(JDBCType.TINYINT, new ByteTypeHandler());
        //YEAR
        typeHandlerMap.put(JDBCType.SMALLINT, new ShortTypeHandler());
        typeHandlerMap.put(JDBCType.INTEGER, new IntegerTypeHandler());
        typeHandlerMap.put(JDBCType.BIGINT, new LongTypeHandler());
        typeHandlerMap.put(JDBCType.REAL, new FloatTypeHandler());
        typeHandlerMap.put(JDBCType.DOUBLE, new DoubleTypeHandler());

        //typeHandlerMap.put(JDBCType.BOOLEAN, new BooleanTypeHandler());
        //MYSQL bool实际映射
        typeHandlerMap.put(JDBCType.BIT, new BooleanTypeHandler());

        typeHandlerMap.put(JDBCType.CHAR, new CharacterTypeHandler());

        //MYSQL NUMERIC: 数据库设置直接变成DECIMAL
        //typeHandlerMap.put(JDBCType.NUMERIC, new BigDecimalTypeHandler());
        typeHandlerMap.put(JDBCType.DECIMAL, new BigDecimalTypeHandler());
    }

    /**
     * 获取数据库类型处理器对象
     *
     * @param type 数据类型
     * @return
     */
    public TypeHandler getTypeHandler(JDBCType type) {
        TypeHandler typeHandler = typeHandlerMap.get(type);
        if (typeHandler == null) {
            typeHandler = typeHandlerMap.get(JDBCType.VARCHAR);
        }
        return typeHandler;
    }
}
