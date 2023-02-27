package com.emily.skydb.core.encoder;

import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.HeadProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @Description :  基于MessagePack的编码器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/27 9:53 AM
 */
public class MessagePackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack pack = new MessagePack();
        out.writeBytes(pack.write(msg));
    }

}
