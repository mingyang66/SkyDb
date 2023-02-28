package com.emily.skydb.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    }

    public static byte[] serialize(Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(value);
    }

    public static <T> T deSerialize(byte[] buffer, Class<? extends T> cls) throws IOException {
        return objectMapper.readValue(buffer, cls);
    }

}
