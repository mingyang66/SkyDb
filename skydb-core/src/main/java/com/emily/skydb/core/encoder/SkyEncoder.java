package com.emily.skydb.core.encoder;

import com.emily.skydb.core.SkyMessage;
import com.emily.skydb.core.SkyTail;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: SkyDb
 * @description: Rpc编码器 protobuf[https://github.com/protocolbuffers/protobuf/releases]
 * @author: Emily
 * @create: 2021/09/23
 */
public class SkyEncoder extends MessageToByteEncoder<SkyMessage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, SkyMessage message, ByteBuf byteBuf) throws Exception {
        if (message == null) {
            return;
        }
        //写入包类型
        byteBuf.writeByte(message.getPackageType());
        //请求|响应体长度
        byteBuf.writeInt(message.getLen());
        //写入编码数据字节流
        byteBuf.writeBytes(message.getBody());
        //写入编码数据结束的行尾标识
        byteBuf.writeBytes(SkyTail.TAIL);
    }
}
