package com.emily.skydb.client.connection;

import com.emily.skydb.client.handler.HeartBeatChannelHandler;
import com.emily.skydb.client.handler.SkyClientChannelHandler;
import com.emily.skydb.client.manager.SkyClientProperties;
import com.emily.skydb.core.constant.CharacterInfo;
import com.emily.skydb.core.decoder.MessagePackDecoder;
import com.emily.skydb.core.encoder.MessagePackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.concurrent.TimeUnit;

/**
 * @program: SkyDb
 * @description: 创建Netty客户端及自定义处理器
 * @author: Emily
 * @create: 2021/09/17
 */
public class SkyClientConnection extends AbstractConnection<Channel> {

    /**
     * 线程工作组
     */
    private static final EventLoopGroup GROUP = new NioEventLoopGroup();
    /**
     * 创建客户端的启动对象 bootstrap ，不是 serverBootStrap
     */
    private static final Bootstrap BOOTSTRAP = new Bootstrap();
    /**
     * 处理器
     */
    private SkyClientChannelHandler clientChannelHandler;

    private SkyClientProperties properties;

    public SkyClientConnection(SkyClientProperties properties) {
        this.properties = properties;
    }

    static {
        //设置线程组
        BOOTSTRAP.group(GROUP);
        //初始化通道
        BOOTSTRAP.channel(NioSocketChannel.class)
                /**
                 * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，
                 * 这套机制才会被激活
                 */
                .option(ChannelOption.SO_KEEPALIVE, true)
                /**
                 * 1.在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。
                 * 为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，
                 * 避免网络中充斥着许多小数据块。
                 * 2.TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；
                 * 如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
                 */
                .option(ChannelOption.TCP_NODELAY, true);
    }

    /**
     * 创建连接
     *
     * @param address 主机地址:端口号
     * @return
     */
    @Override
    public boolean connect(String address) {
        try {
            clientChannelHandler = new SkyClientChannelHandler(properties.getReadTimeOut());
            BOOTSTRAP
                    /**
                     * The timeout period of the connection.
                     * If this time is exceeded or the connection cannot be established, the connection fails.
                     */
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NumberUtils.toInt(String.valueOf(properties.getConnectTimeOut().toMillis())))
                    //加入自己的处理器
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //长度编码解码器
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 2, 0, 2));
                            //自定义解码器
                            pipeline.addLast(new MessagePackDecoder());
                            //在消息前面加上前缀的编码器
                            pipeline.addLast(new LengthFieldPrepender(2));
                            //自定义编码器
                            pipeline.addLast(new MessagePackEncoder());
                            //自定义handler处理
                            pipeline.addLast(clientChannelHandler);
                            //空闲状态处理器，参数说明：读时间空闲时间，0禁用时间|写事件空闲时间，0则禁用|读或写空闲时间，0则禁用
                            pipeline.addLast(new IdleStateHandler(0, 0, properties.getIdleTimeOut().getSeconds(), TimeUnit.SECONDS));
                            //心跳处理器
                            pipeline.addLast(new HeartBeatChannelHandler());
                        }
                    });
            //分割Rpc服务器地址
            String[] addr = StringUtils.split(address, CharacterInfo.COLON_EN);
            //连接服务器
            ChannelFuture channelFuture = BOOTSTRAP.connect(addr[0], NumberUtils.toInt(addr[1])).sync();
            channelFuture.addListener(listener -> {
                if (listener.isSuccess()) {
                    System.out.println("connect success...");
                } else {
                    System.out.println("RPC客户端重连接...");
                }
            });
            //获取channel
            Channel channel = channelFuture.channel();
            //将通道赋值给连接对象
            this.setConnection(channel);
            //判定通道是否可用
            if (this.isAvailable()) {
                return true;
            }
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Socket连接是否可用
     *
     * @return
     */
    @Override
    public boolean isAvailable() {
        return null != this.getConnection() && this.getConnection().isActive() && this.getConnection().isWritable();
    }

    /**
     * 关闭连接通道
     */
    @Override
    public void close() {
        this.getConnection().close();
    }

    public SkyClientChannelHandler getClientChannelHandler() {
        return clientChannelHandler;
    }
}
