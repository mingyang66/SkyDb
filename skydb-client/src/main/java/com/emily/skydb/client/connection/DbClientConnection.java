package com.emily.skydb.client.connection;

import com.emily.skydb.client.handler.DbClientChannelHandler;
import com.emily.skydb.client.handler.HeartBeatChannelHandler;
import com.emily.skydb.client.manager.DbClientProperties;
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

import java.nio.ByteOrder;
import java.util.concurrent.TimeUnit;

/**
 * @program: SkyDb
 * @description: 创建Netty客户端及自定义处理器
 * @author: Emily
 * @create: 2021/09/17
 */
public class DbClientConnection extends AbstractConnection<Channel> {

    /**
     * 线程工作组
     */
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 创建客户端的启动对象 bootstrap ，不是 serverBootStrap
     */
    private static final Bootstrap bootstrap = new Bootstrap();
    /**
     * 处理器
     */
    private DbClientChannelHandler clientChannelHandler;

    private DbClientProperties properties;

    public DbClientConnection(DbClientProperties properties) {
        this.properties = properties;
    }

    static {
        //设置线程组
        bootstrap.group(workerGroup);
        //初始化通道
        bootstrap.channel(NioSocketChannel.class)
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
            clientChannelHandler = new DbClientChannelHandler(properties.getReadTimeOut());
            bootstrap
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
                            /**
                             * 基于消息中的长度字段动态的分割接收到的ByteBuf
                             * byteOrder:表示协议中Length字段的字节是大端还是小端
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
            ChannelFuture channelFuture = bootstrap.connect(addr[0], NumberUtils.toInt(addr[1])).sync();
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

    public DbClientChannelHandler getClientChannelHandler() {
        return clientChannelHandler;
    }
}
