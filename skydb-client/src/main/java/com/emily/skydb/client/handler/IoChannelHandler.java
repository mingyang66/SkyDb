package com.emily.skydb.client.handler;

import com.emily.skydb.client.pool.SimpleChannelPoolHandler;
import com.emily.skydb.core.protocol.DataPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @program: SkyDb
 * @description: 由于需要在 handler 中发送消息给服务端，并且将服务端返回的消息读取后返回给消费者,所以实现了 Callable 接口，这样可以运行有返回值的线程
 * @author: Emily
 * @create: 2021/09/17
 */
public class IoChannelHandler extends SimpleChannelInboundHandler<DataPacket> {
    /**
     * 锁对象
     */
    public final Object object = new Object();

    public DataPacket result;

    public IoChannelHandler() {
        System.out.println("新建handler------------DbClientChannelHandler");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataPacket response) throws Exception {
        if (response.packageType == 0) {
            synchronized (this.object) {
                result = response;
                this.object.notify();
            }
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
        System.out.println(cause.getMessage());
        if (ctx.channel().id() != null && SimpleChannelPoolHandler.ioHandlerMap.containsKey(ctx.channel().id())) {
            SimpleChannelPoolHandler.ioHandlerMap.remove(ctx.channel().id());
        }
        ctx.close();
    }

}
