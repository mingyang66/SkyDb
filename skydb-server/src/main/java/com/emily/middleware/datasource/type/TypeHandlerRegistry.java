package com.emily.middleware.datasource.type;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-type-conversions.html
 *
 * @Description :  数据类型处理器注册中心
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/10 3:47 PM
 */
@SuppressWarnings("all")
public class TypeHandlerRegistry {
    private final Map<String, TypeHandler> typeHandlerMap = new ConcurrentHashMap<>();

    public TypeHandlerRegistry() {
        /**
         * CHAR
         * VARCHAR
         * TEXT
         * TINYTEXT
         * MEDIUMTEXT
         * LONGTEXT
         * JSON
         * ENUM('value1','value2',...)
         * SET('value1','value2',...)
         */
        typeHandlerMap.put(String.class.getName(), new StringTypeHandler());
        /**
         * DATETIME
         */
        typeHandlerMap.put(LocalDateTime.class.getName(), new DateTimeTypeHandler());
        /**
         * TIMESTAMP[(M)]
         */
        typeHandlerMap.put(Timestamp.class.getName(), new DateTimeTypeHandler());
        /**
         * DATE
         * YEAR[(2|4)]:如果yearIsDateType 属性设置为true
         */
        typeHandlerMap.put(Date.class.getName(), new DateTypeHandler());
        /**
         * TIME
         */
        typeHandlerMap.put(Time.class.getName(), new TimeTypeHandler());
        /**
         * YEAR[(2|4)]:如果yearIsDateType 属性设置为false
         */
        typeHandlerMap.put(Short.class.getName(), new ShortTypeHandler());
        /**
         * INT,INTEGER[(M)]
         * TINYINT(1) SIGNED某些条件
         * TINYINT( > 1) SIGNED
         * TINYINT( any ) UNSIGNED
         * SMALLINT[(M)] [UNSIGNED]
         * MEDIUMINT[(M)] [UNSIGNED]
         */
        typeHandlerMap.put(Integer.class.getName(), new IntegerTypeHandler());
        /**
         * INT,INTEGER[(M)] UNSIGNED
         * BIGINT[(M)]
         */
        typeHandlerMap.put(Long.class.getName(), new LongTypeHandler());
        /**
         * FLOAT[(M,D)]
         */
        typeHandlerMap.put(Float.class.getName(), new FloatTypeHandler());
        /**
         * DOUBLE[(M,B)] [UNSIGNED]
         */
        typeHandlerMap.put(Double.class.getName(), new DoubleTypeHandler());
        /**
         * BIT(1)
         * TINYINT(1) SIGNED, BOOLEAN	某些条件
         */
        typeHandlerMap.put(Boolean.class.getName(), new BooleanTypeHandler());
        /**
         * DECIMAL[(M[,D])] [UNSIGNED]
         */
        typeHandlerMap.put(BigDecimal.class.getName(), new BigDecimalTypeHandler());
        /**
         * BIGINT[(M)] UNSIGNED
         */
        typeHandlerMap.put(BigInteger.class.getName(), new BigIntegerTypeHandler());
    }

    /**
     * 获取数据库类型处理器对象
     *
     * @param type 数据类型
     * @return
     */
    public TypeHandler getTypeHandler(String className) {
        TypeHandler typeHandler = typeHandlerMap.get(className);
        if (typeHandler == null) {
            typeHandler = typeHandlerMap.get(String.class.getName());
        }
        return typeHandler;
    }
}
