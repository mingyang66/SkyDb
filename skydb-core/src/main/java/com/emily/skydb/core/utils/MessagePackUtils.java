package com.emily.skydb.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msgpack.jackson.dataformat.MessagePackMapper;

import java.io.IOException;

/**
 * MessagePack是一种高效的二进制序列化格式，它允许你在多种语言（如JSON）之间交换数据。但它更快、更小。
 * 小整数被编码成一个字节，典型的短字符串除了字符串本身外，只需要一个额外的字节。
 * @Description :  编码解码
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/28 2:59 PM
 */
public class MessagePackUtils {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new MessagePackMapper();
        //序列化和反序列化java.Time时间对象
        objectMapper.registerModule(new JavaTimeModule());
        //忽略，在json字符串中存在但是在java对象中不存在的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static byte[] serialize(Object value) throws JsonProcessingException {
        if (value == null) {
            return null;
        }
        return objectMapper.writeValueAsBytes(value);
    }

    public static <T> T deSerialize(byte[] buffer, Class<? extends T> cls) throws IOException {
        if (buffer == null) {
            return null;
        }
        return objectMapper.readValue(buffer, cls);
    }

    public static <T> T deSerialize(byte[] buffer, TypeReference<? extends T> reference) throws IOException {
        if (buffer == null) {
            return null;
        }
        return objectMapper.readValue(buffer, reference);
    }
}
