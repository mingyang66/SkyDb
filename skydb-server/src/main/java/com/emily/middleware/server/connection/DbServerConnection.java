package com.emily.middleware.server.connection;

import com.emily.middleware.server.handler.DbBusinessHandler;
import com.emily.middleware.server.handler.DbServerChannelHandler;
import com.emily.skydb.core.decoder.MessagePackDecoder;
import com.emily.skydb.core.encoder.MessagePackEncoder;
import com.emily.middleware.manager.DbServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.ByteOrder;
import java.util.Objects;

/**
 * @program: SkyDb
 * @description:
 * @author: Emily
 * @create: 2021/09/17
 */
public class DbServerConnection {

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
    private DbBusinessHandler handler;
    /**
     * 属性配置
     */
    private DbServerProperties properties;

    public DbServerConnection(DbBusinessHandler handler, DbServerProperties properties) {
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
                            /**
                             * 基于消息中的长度字段动态的分割接收到的ByteBuf
                             * byteOrder:表示协议中Length字段的字节是大端还是小端
                             *         ByteOrder#BIG_ENDIAN：大端是高位字节在前，低位字节在后
                             *         ByteOrder#LITTLE_ENDIAN：小端是低位字节在前，高位字节在后
                             * maxFrameLength：表示协议中Content字段的最大长度，如果超出，则抛出TooLongFrameException异常
                             * lengthFieldOffset：表示Length字段的偏移量，即在读取一个二进制流时，跳过指定长度个字节之后的才是Length字段。如果Length字段之前没有其他报文头，指定为0即可。如果Length字段之前还有其他报文头，则需要跳过之前的报文头的字节数。
                             * lengthFieldLength：表示Length字段占用的字节数。指定为多少，需要看实际要求，不同的字节数，限制了Content字段的最大长度。
                             * lengthAdjustment：表示Length字段调整值
                             * initialBytesToStrip：解码后跳过的初始字节数，表示获取完一个完整的数据报文之后，忽略前面指定个数的字节
                             * failFast:如果为true，则表示读取到Length字段时，如果其值超过maxFrameLength，就立马抛出一个 TooLongFrameException
                             */
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, 65535, 0, 2, 0, 2, true));
                            //自定义解码器
                            pipeline.addLast(new MessagePackDecoder());
                            /**
                             * 在消息前面加上前缀的编码器（只能是1、2、3、4、8，默认不包含长度字段的长度）
                             * byteOrder:表示Length字段本身占用的字节数使用的是大端还是小端编码
                             * lengthFieldLength：表示Length字段本身占用的字节数,只可以指定 1, 2, 3, 4, 或 8
                             *     1：8位无符号二进制最大整数255
                             *     2：16位无符号二进制最大整数65535
                             *     3：24位无符号二进制最大整数是16777215
                             *     4：32位无符号二进制最大整数是xxxx
                             *     8: 64位无符号二进制最大整数是xxxx
                             * lengthAdjustment：表示Length字段调整值
                             * lengthIncludesLengthFieldLength:表示Length字段本身占用的字节数是否包含在Length字段表示的值中
                             * Length字段的值=真实数据可读字节数+Length字段调整值
                             */
                            pipeline.addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 2, 0, false));
                            //自定义编码器
                            pipeline.addLast(new MessagePackEncoder());
                            //自定义处理器
                            pipeline.addLast(new DbServerChannelHandler(handler));
                        }
                    });
            //启动服务器，并绑定端口并且同步
            ChannelFuture channelFuture = serverBootstrap.bind(properties.getPort()).sync();
            System.out.println("Rpc server start success，port is " + properties.getPort());
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
