package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.BaseResponse;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.utils.SerializeUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class SkyServerChannelHandler extends ChannelInboundHandlerAdapter {

    private SkyBusinessHandler handler;

    public SkyServerChannelHandler(SkyBusinessHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Rpc客户端连接成功：{}" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
        System.out.println("Rpc服务器连接断开：{}" + ctx.channel().remoteAddress());
    }

    /**
     * 接收客户端传入的值，将值解析为类对象，获取其中的属性，然后反射调用实现类的方法
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null) {
            return;
        }
        try {
            //请求消息
            DataPacket packet = (DataPacket) msg;
            //消息类型
            byte packageType = packet.getPackageType();
            //心跳包
            if (packageType == 1) {
                String heartBeat = new String(packet.getBody(), StandardCharsets.UTF_8);
                System.out.println("心跳包是：" + heartBeat);
                return;
            }
            BaseResponse response = this.handler.handler(packet);
            //发送调用方法调用结果
            ctx.writeAndFlush(new DataPacket(SerializeUtils.serialize(response)));
        } finally {
            //手动释放消息，否则会导致内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }


    /**
     * 服务端时间触发，心跳包
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                case WRITER_IDLE:
                case ALL_IDLE:
                    //logger.info("客户端已经超过60秒未读写数据，关闭连接{}。", ctx.channel().remoteAddress());
                    ctx.channel().close();
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        //logger.error(PrintExceptionInfo.printErrorInfo(cause));
    }
}
