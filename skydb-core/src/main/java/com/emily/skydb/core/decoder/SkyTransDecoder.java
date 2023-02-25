package com.emily.skydb.core.decoder;

import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.HeadProtocol;
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
public class SkyTransDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        //包类型，0-正常RPC请求，1-心跳包
        byte packageType = buf.readByte();
        //读取消息长度
        int length = buf.readInt();
        if (length - HeadProtocol.LENGTH == 0) {
            return;
        }
        //初始化存储数据字节数组
        byte[] data = new byte[length - HeadProtocol.LENGTH];
        //将字节流中的数据读入到字节数组
        buf.readBytes(data);
        //添加消息体
        list.add(new DataPacket(packageType, data));
        //重置readerIndex和writerIndex为0
        buf.clear();
    }
}
