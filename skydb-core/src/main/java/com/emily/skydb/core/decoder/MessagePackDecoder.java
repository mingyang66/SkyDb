package com.emily.skydb.core.decoder;

import com.emily.skydb.core.protocol.DataPacket;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.util.List;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/23
 */
public class MessagePackDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());
        DataPacket dataPacket = objectMapper.readValue(data, DataPacket.class);
        list.add(dataPacket);
    }
}
