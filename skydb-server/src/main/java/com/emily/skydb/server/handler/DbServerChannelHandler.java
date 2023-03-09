package com.emily.skydb.server.handler;

import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.utils.MessagePackUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class DbServerChannelHandler extends ChannelInboundHandlerAdapter {

    private DbBusinessHandler handler;

    public DbServerChannelHandler(DbBusinessHandler handler) {
        this.handler = handler;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Rpc客户端连接成功：" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().close();
        System.out.println("Rpc服务器连接断开：" + ctx.channel().remoteAddress());
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
            byte packageType = packet.packageType;
            //心跳包
            if (packageType == 1) {
                String heartBeat = MessagePackUtils.deSerialize(packet.content, String.class);
                System.out.println("心跳包是：" + heartBeat);
                return;
            }
            //请求消息体
            DbTransBody reqDbBody = MessagePackUtils.deSerialize(packet.content, DbTransBody.class);
            //获取后置处理结果
            Object value = this.handler.handler(reqDbBody);
            //发送调用方法调用结果
            ctx.writeAndFlush(new DataPacket(packet.tracedId, MessagePackUtils.serialize(value)));
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            //手动释放消息，否则会导致内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
