package com.emily.skydb.core.decoder;

import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.utils.MessagePackUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

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
        list.add(MessagePackUtils.deSerialize(data, DataPacket.class));
    }
}
