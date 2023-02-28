package com.emily.skydb.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

/**
 * @Description :  json工具类
 * @Author :  姚明洋
 * @CreateDate :  Created in 2023/2/28 3:57 PM
 */
public class JsonUtils {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
    }

    /**
     * @Description 对象转换为json字符串, 支持List、Map、Collection、字符串
     * @Version 1.0
     */
    public static <T> String toJSONString(T o) {
        return toJSONString(o, JsonInclude.Include.ALWAYS);
    }

    /**
     * @param include 定义javaBean的那些属性需要序列化
     *                ALWAYS：始终包含javaBean的值，与属性的值无关。
     *                NON_NULL：表示只包含非null的属性值。
     *                NON_ABSENT：表示属性值为null,或者JAVA8、Guava中的Optional
     *                NON_EMPTY：表示非null、""和数组集合isEmpty()=false都将会被忽略
     *                NON_DEFAULT：表示POJO类属性的值为缺省值是不序列化，如User类的 int age = 0; String username = null;
     *                CUSTOM:自定义，根据过滤器等
     *                USE_DEFAULTS：...
     * @Description 对象转换为json字符串, 支持List、Map、Collection、字符串
     * @Version 1.0
     */
    public static <T> String toJSONString(T o, JsonInclude.Include include) {
        try {
            if (null == include) {
                include = JsonInclude.Include.ALWAYS;
            }
            objectMapper.setSerializationInclusion(include);
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description JSON字符串转换为java对象, 支持List、Map、Collection、字符串
     * @Version 1.0
     */
    public static <T> T toJavaBean(String str, Class<T> responseType) {
        try {
            return objectMapper.readValue(str, responseType);
        } catch (JsonParseException e) {
        } catch (JsonMappingException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * 将字节数组转化为指定的对象
     *
     * @param bytes        字节数组
     * @param responseType 返回值类型
     * @param <T>
     */
    public static <T> T toObject(byte[] bytes, Class<T> responseType) {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, responseType);
        } catch (IOException e) {
        }
        return null;
    }
}
