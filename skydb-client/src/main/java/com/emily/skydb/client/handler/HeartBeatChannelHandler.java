package com.emily.skydb.client.handler;

import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.TransHeader;
import com.emily.skydb.core.utils.MessagePackUtils;
import com.emily.skydb.core.utils.UUIDUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Description :  心跳处理器
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/25 2:16 PM
 */
public class HeartBeatChannelHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        {
            System.out.println("通道已经超过20秒未与服务端进行读写操作，发送心跳包..." + ctx.channel().remoteAddress());
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                switch (e.state()) {
                    case READER_IDLE:
                    case WRITER_IDLE:
                        TransHeader transHeader = new TransHeader(UUIDUtils.randomSimpleUUID());
                        //序列化请求头
                        byte[] header = MessagePackUtils.serialize(transHeader);
                        //发送心跳包
                        ctx.channel().writeAndFlush(new DataPacket((byte) 1, header, MessagePackUtils.serialize("heartBeat...")));
                        break;
                    case ALL_IDLE:
                    default:
                        break;
                }
            } else {
                //继续传播事件
                super.userEventTriggered(ctx, evt);
            }
        }
    }
}
