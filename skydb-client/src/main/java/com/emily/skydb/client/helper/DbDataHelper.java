package com.emily.skydb.client.helper;

import com.emily.skydb.core.enums.DateFormatType;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.core.protocol.JdbcType;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description :  客户端数据库映射帮助类
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/3/7 10:13 AM
 */
public class DbDataHelper {
    /**
     * 解析返回结果为只是数据类型
     *
     * @param list 数据库返回值
     * @param cls  真实数据类型
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> getDbEntity(List<Map<String, DbModelItem>> list, Class<T> cls) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<T> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Map<String, DbModelItem> itemMap = list.get(i);
            T t = cls.getDeclaredConstructor().newInstance();
            itemMap.keySet().forEach(name -> {
                DbModelItem item = itemMap.get(name);
                setFieldValue(item, t);

            });
            result.add(t);
        }
        return result;
    }

    /**
     * 实例设置字段属性值
     *
     * @param item 数据库返回数据类型
     * @param t    实例对象
     * @param <T>
     */
    private static <T> void setFieldValue(DbModelItem item, T t) {
        try {
            //获取当前类的属性对象
            Field field = t.getClass().getDeclaredField(item.name);
            field.setAccessible(true);
            switch (item.valueType) {
                case JdbcType.DateTime:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, LocalDateTime.parse(item.value, DateTimeFormatter.ofPattern(DateFormatType.YYYY_MM_DD_HH_MM_SS_COLON_SSS.getFormat())));
                    }
                    break;
                case JdbcType.Date:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, LocalDate.parse(item.value, DateTimeFormatter.ofPattern(DateFormatType.YYYY_MM_DD.getFormat())));
                    }
                    break;
                case JdbcType.Time:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, LocalTime.parse(item.value, DateTimeFormatter.ofPattern(DateFormatType.HH_MM_SS.getFormat())));
                    }
                    break;
                case JdbcType.Byte:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Byte.valueOf(item.value));
                    }
                    break;
                case JdbcType.Short:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Short.valueOf(item.value));
                    }
                    break;
                case JdbcType.Int:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Integer.valueOf(item.value));
                    }
                    break;
                case JdbcType.Long:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Long.valueOf(item.value));
                    }
                    break;
                case JdbcType.Float:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Float.valueOf(item.value));
                    }
                    break;
                case JdbcType.Double:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Double.valueOf(item.value));
                    }
                    break;
                case JdbcType.Decimal:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, new BigDecimal(item.value));
                    }
                    break;
                case JdbcType.Boolean:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, Integer.valueOf(item.value));
                    }
                    break;
                case JdbcType.Char:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, item.value.charAt(0));
                    }
                    break;
                case JdbcType.Binary:
                    if (StringUtils.isNotEmpty(item.value)) {
                        field.set(t, item.value);
                    }
                    break;
                case JdbcType.String:
                default:
                    field.set(t, item.value);
                    break;
            }
        } catch (NoSuchFieldException e) {
            System.out.println("----------------------NoSuchFieldException-----------------------");
        } catch (IllegalAccessException e) {
            System.out.println("----------------------IllegalAccessException-----------------------");
        }
    }
}