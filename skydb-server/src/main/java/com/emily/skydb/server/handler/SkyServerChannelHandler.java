package com.emily.skydb.server.handler;

import com.emily.skydb.core.ObjectUtils;
import com.emily.skydb.core.SkyMessage;
import com.emily.skydb.core.SkyRequest;
import com.emily.skydb.core.SkyResponse;
import com.emily.skydb.core.enums.HttpStatusType;
import com.emily.skydb.core.exception.PrintExceptionInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class SkyServerChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("Rpc客户端连接成功：{}" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Rpc服务器连接断开：{}" + ctx.channel().remoteAddress());
        ctx.channel().close();
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
        //请求协议
        SkyRequest request = null;
        //返回结果
        SkyResponse rpcResponse = null;
        try {
            //请求消息
            SkyMessage message = (SkyMessage) msg;
            //消息类型
            byte packageType = message.getPackageType();
            //心跳包
            if (packageType == 1) {
                String heartBeat = new String(message.getBody(), StandardCharsets.UTF_8);
                System.out.println("通道{}的心跳包是：" + ctx.channel().remoteAddress() + "--" + heartBeat);
                return;
            }
            //请求协议
            request = ObjectUtils.deserialize(message.getBody());
            //Rpc响应结果
            rpcResponse = SkyResponse.buildResponse(request);
        } catch (Exception ex) {
            //异常结果
            Object response = PrintExceptionInfo.printErrorInfo(ex);
            //Rpc响应结果
            rpcResponse = SkyResponse.buildResponse(HttpStatusType.EXCEPTION.getStatus(), HttpStatusType.EXCEPTION.getMessage(), response);
        } finally {
            //设置请求上下文的事物唯一标识
            if (Objects.nonNull(request)) {
                rpcResponse.setTraceId(request.getTraceId());
            }
            //发送调用方法调用结果
            ctx.writeAndFlush(SkyMessage.build(ObjectUtils.serialize(rpcResponse)));
            //手动释放消息，否则会导致内存泄漏
            ReferenceCountUtil.release(msg);
            //记录请求相依日志
            //RecordLogger.recordResponse(request, rpcResponse, startTime);
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
