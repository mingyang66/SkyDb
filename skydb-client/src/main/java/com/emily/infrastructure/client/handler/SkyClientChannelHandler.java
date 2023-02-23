package com.emily.infrastructure.client.handler;

import com.emily.infrastructure.core.ObjectUtils;
import com.emily.infrastructure.core.SkyMessage;
import com.emily.infrastructure.core.SkyResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.time.Duration;

/**
 * @program: SkyDb
 * @description: 由于需要在 handler 中发送消息给服务端，并且将服务端返回的消息读取后返回给消费者,所以实现了 Callable 接口，这样可以运行有返回值的线程
 * @author: Emily
 * @create: 2021/09/17
 */
@ChannelHandler.Sharable
public class SkyClientChannelHandler extends ChannelInboundHandlerAdapter {


    public final Object object = new Object();
    /**
     * 服务端返回的结果
     */
    public SkyResponse response;
    /**
     * 通道
     */
    private Channel channel;

    /**
     * 读取超时时间，单位：毫秒
     */
    private Duration readTimeOut;

    public SkyClientChannelHandler(Duration readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //logger.info("客户端信息：{}", ctx.channel().remoteAddress());
        //初始化通道
        this.channel = ctx.channel();
        //继续传播事件
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
       // logger.info("服务断开连接：{}", ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    /**
     * 实现channelRead 当我们读到服务器数据,该方法自动执行
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            synchronized (this.object) {
                //将消息对象转换为指定消息体
                SkyMessage message = (SkyMessage) msg;
                //将真实的消息体转换为字符串类型
                this.response = ObjectUtils.deserialize(message.getBody());
                //唤醒等待线程
                this.object.notify();
            }
        } finally {
            //手动释放消息，否则会导致内存泄漏
            ReferenceCountUtil.release(msg);
        }
    }

    /**
     * 发送请求
     *
     * @param message
     */
    public SkyResponse send(SkyMessage message) {
        try {
            synchronized (this.object) {
                //发送Rpc请求
                this.channel.writeAndFlush(message);
                //释放当前线程资源，并等待指定超时时间，默认：10000ms
                this.object.wait(readTimeOut.toMillis());
            }
            return this.response;
        } catch (Exception exception) {
            //throw new BasicException(HttpStatusType.EXCEPTION.getStatus(), PrintExceptionInfo.printErrorInfo(exception));
            return null;
        }
    }

    /**
     * 用户时间触发，心跳
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //logger.info("通道{}已经超过20秒未与服务端进行读写操作，发送心跳包...", ctx.channel().remoteAddress());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                case WRITER_IDLE:
                case ALL_IDLE:
                    SkyMessage message = new SkyMessage();
                    //设置包类型为心跳包
                    message.setPackageType((byte) 1);
                    //设置心跳包内容
                    message.setBody(ObjectUtils.serialize(SkyResponse.buildResponse("heartBeat...")));
                    //消息包长度
                    message.setLen(message.getBody().length);
                    //发送心跳包
                    ctx.channel().writeAndFlush(message);
                    break;
                default:
                    break;
            }
        } else {
            //继续传播事件
            super.userEventTriggered(ctx, evt);
        }
    }

    /**
     * 异常处理
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
       // logger.error(PrintExceptionInfo.printErrorInfo(cause));
    }

}
