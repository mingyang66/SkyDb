package com.emily.skydb.client.handler;

import com.emily.skydb.client.future.DefaultFuture;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.TransContent;
import com.emily.skydb.core.protocol.TransHeader;
import com.emily.skydb.core.utils.MessagePackUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: SkyDb
 * @description: 由于需要在 handler 中发送消息给服务端，并且将服务端返回的消息读取后返回给消费者,所以实现了 Callable 接口，这样可以运行有返回值的线程
 * @author: Emily
 * @create: 2021/09/17
 */
public class DbClientChannelHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, DefaultFuture> futureMap = new ConcurrentHashMap<>();
    /**
     * 通道
     */
    private Channel channel;

    /**
     * 读取超时时间，单位：毫秒
     */
    private Duration readTimeOut;

    public DbClientChannelHandler(Duration readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端信息：{}" + ctx.channel().remoteAddress());
        //初始化通道
        this.channel = ctx.channel();
        //继续传播事件
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("服务断开连接：{}" + ctx.channel().remoteAddress());
        super.channelInactive(ctx);
    }

    /**
     * 根据服务器返回值赋予返回值变量
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String traceId = null;
        try {
            //将消息对象转换为指定消息体
            DataPacket packet = (DataPacket) msg;
            //请求头
            TransHeader transHeader = MessagePackUtils.deSerialize(packet.header, TransHeader.class);
            //请求唯一标识
            traceId = transHeader.tracedId;
            //设置响应结果
            futureMap.get(traceId).set(packet.content);
        } finally {
            if (StringUtils.isNotEmpty(traceId)) {
                futureMap.remove(traceId);
            }
        }
    }

    /**
     * 发送TCP请求，并等待返回结果
     *
     * @param transHeader  请求头
     * @param transContent 请求体
     * @param reference    返回值目标类型
     */
    public <T> T send(TransHeader transHeader, TransContent transContent, TypeReference<? extends T> reference) throws IOException {
        //请求唯一标识序列化
        byte[] headerBytes = MessagePackUtils.serialize(transHeader);
        //请求体序列化
        byte[] contentBytes = MessagePackUtils.serialize(transContent);
        //TCP发送数据包，并对发送数据序列化
        DataPacket packet = new DataPacket(headerBytes, contentBytes);
        //将当前请求和Future映射
        this.futureMap.put(transHeader.tracedId, new DefaultFuture());
        //发送TCP请求
        this.channel.writeAndFlush(packet);
        //获取响应体
        byte[] response = this.futureMap.get(transHeader.tracedId).get(this.readTimeOut.toMillis());

        return MessagePackUtils.deSerialize(response, reference);
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
        System.out.println("------发生异常-------" + cause.getMessage());
    }

}
