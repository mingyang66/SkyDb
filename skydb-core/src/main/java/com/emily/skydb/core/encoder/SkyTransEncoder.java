package com.emily.skydb.core.encoder;

import com.emily.skydb.core.protocol.DataPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: SkyDb
 * @description: Rpc编码器 protobuf[https://github.com/protocolbuffers/protobuf/releases]
 * @author: Emily
 * @create: 2021/09/23
 */
public class SkyTransEncoder extends MessageToByteEncoder<DataPacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, DataPacket packet, ByteBuf byteBuf) throws Exception {
        if (packet == null) {
            return;
        }
        //写入包类型
        byteBuf.writeByte(packet.getHead().getPackageType());
        //请求|响应体长度
        byteBuf.writeInt(packet.getHead().getPackageLength());
        //写入编码数据字节流
        byteBuf.writeBytes(packet.getBody());
        //写入编码数据结束的行尾标识
        //byteBuf.writeBytes(TailProtocol.TAIL);
    }
}
