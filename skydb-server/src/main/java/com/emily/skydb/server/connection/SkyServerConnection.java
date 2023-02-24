package com.emily.skydb.server.connection;

import com.emily.skydb.core.decoder.SkyDecoder;
import com.emily.skydb.core.encoder.SkyEncoder;
import com.emily.skydb.core.entity.SkyTail;
import com.emily.skydb.server.handler.SkyBusinessHandler;
import com.emily.skydb.server.handler.SkyServerChannelHandler;
import com.emily.skydb.server.manager.SkyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.util.Objects;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class SkyServerConnection {

    /**
     * 用于处理客户端的连接请求
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * 用于处理各个客户端的I/O操作
     */
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 后置业务处理类
     */
    private SkyBusinessHandler handler;
    /**
     * 属性配置
     */
    private SkyServerProperties properties;

    public SkyServerConnection(SkyBusinessHandler handler, SkyServerProperties properties) {
        this.handler = handler;
        this.properties = properties;
    }

    /**
     * 启动netty服务端
     */
    public void start() {
        try {
            //创建服务端的启动对象，并使用链式编程来设置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置两个线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    /**
                     * 使用NioServerSocketChannel 作为服务器的通道实现
                     * 用来处理客户端连接操作
                     */
                    .channel(NioServerSocketChannel.class)
                    /**
                     * 用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
                     * 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)
                    /**
                     * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，
                     * 这套机制才会被激活
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    /**
                     * 1.在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
                     * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，
                     * 避免网络中充斥着许多小数据块。
                     * 2.TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                     * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                     */
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    /**
                     * 用来处理用户I/O操作
                     */
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //空闲状态处理器，参数说明：读时间空闲时间，0禁用时间|写事件空闲时间，0则禁用|读或写空闲时间，0则禁用
                            //pipeline.addLast(new IdleStateHandler(0, 0, properties.getIdleTimeOut().getSeconds(), TimeUnit.SECONDS));
                            //分隔符解码器
                            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Unpooled.copiedBuffer(SkyTail.TAIL)));
                            //自定义编码器
                            pipeline.addLast(new SkyEncoder());
                            //自定义解码器
                            pipeline.addLast(new SkyDecoder());
                            //自定义处理器
                            pipeline.addLast(new SkyServerChannelHandler(handler));
                        }
                    });
            //启动服务器，并绑定端口并且同步
            ChannelFuture channelFuture = serverBootstrap.bind(properties.getPort()).sync();
            System.out.println("Rpc server start success，port is {" + properties.getPort() + "}");
            //对关闭通道进行监听,监听到通道关闭后，往下执行
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            //logger.error("occur exception when start server: {}", PrintExceptionInfo.printErrorInfo(e));
        } finally {
            System.out.println("shutdown bossGroup and workerGroup");
            if (Objects.nonNull(bossGroup)) {
                bossGroup.shutdownGracefully();
            }
            if (Objects.nonNull(workerGroup)) {
                workerGroup.shutdownGracefully();
            }
        }
    }

}
