package com.emily.skydb.core.encoder;

import com.emily.skydb.core.utils.MessagePackUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: SkyDb
 * @description: Rpc编码器 protobuf[https://github.com/protocolbuffers/protobuf/releases]
 * @author: Emily
 * @create: 2021/09/23
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(MessagePackUtils.serialize(msg));
    }
}
