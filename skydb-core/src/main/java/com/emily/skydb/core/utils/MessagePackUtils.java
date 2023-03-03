package com.emily.skydb.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.msgpack.jackson.dataformat.MessagePackMapper;

import java.io.IOException;

/**
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
    }

    public static byte[] serialize(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(value);
    }

    public static <T> T deSerialize(byte[] buffer, Class<? extends T> cls) throws IOException {
        return objectMapper.readValue(buffer, cls);
    }

    public static <T> T deSerialize(byte[] buffer, TypeReference<? extends T> reference) throws IOException {
        return objectMapper.readValue(buffer, reference);
    }
}
