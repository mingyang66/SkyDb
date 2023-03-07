package com.emily.skydb.client;

import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.loadbalance.RoundLoadBalance;
import com.emily.skydb.client.manager.SkyClientManager;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.enums.DateFormatType;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.protocol.JdbcType;
import com.emily.skydb.core.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: RPC服务调用
 * @author: Emily
 * @create: 2021/09/18
 */

public class ClientBootStrap {

    public static void main(String[] args) throws Exception {
        SkyClientProperties properties = new SkyClientProperties();
        LoadBalance loadBalance = new RoundLoadBalance();
        properties.getPool().setMinIdle(1);
        SkyClientManager.initPool(properties, loadBalance);

        selectBody(TestUser.class);
    }


    public static void insertBody() throws Exception {
        DbTransBody transBody = new DbTransBody();
        transBody.dbName = "account";
        transBody.dbTag = "insert_test";
        transBody.params.add(new DbModelItem("name", "田晓霞"));
        transBody.params.add(new DbModelItem("color", "女"));
        transBody.params.add(new DbModelItem("age", "18", JdbcType.Int));
        transBody.params.add(new DbModelItem("year", "2023", JdbcType.Year));
        transBody.params.add(new DbModelItem("price", "6183.26", JdbcType.Decimal));
        transBody.params.add(new DbModelItem("updateTime", "2023-03-03 17:23:56", JdbcType.DateTime));
        transBody.params.add(new DbModelItem("insertTime", "2023-03-03 17:23:56", JdbcType.DateTime));
        int rows = SkyClientManager.invoke(transBody, new TypeReference<Integer>() {
        });
        System.out.println(rows);
    }

    public static <T> void selectBody(Class<T> cls) throws Exception {
        DbTransBody transBody = new DbTransBody();
        transBody.dbName = "account";
        transBody.dbTag = "select_test_tj";
        transBody.params.add(new DbModelItem("age", "45"));
        List<Map<String, DbModelItem>> list = SkyClientManager.invoke(transBody, new TypeReference<>() {
        });
        List<T> result = new ArrayList<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            Map<String, DbModelItem> itemMap = list.get(i);
            T t = cls.getDeclaredConstructor().newInstance();
            itemMap.keySet().forEach(name -> {
                try {
                    DbModelItem item = itemMap.get(name);
                    //获取当前类的属性对象
                    Field field = t.getClass().getDeclaredField(name);
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
            });
            result.add(t);
        }
        System.out.println(JsonUtils.toJSONString(result));

    }
}
