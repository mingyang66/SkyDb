package com.emily.skydb.client.pool;

import com.emily.skydb.client.handler.IoChannelHandler;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.TransContent;
import com.emily.skydb.core.protocol.TransHeader;
import com.emily.skydb.core.utils.MessagePackUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.InetSocketAddress;

/**
 * @program: SkyDb
 * @description: 创建Netty客户端及自定义处理器
 * @author: Emily
 * @create: 2021/09/17
 */
public class PoolClient {
    /**
     * 线程工作组
     */
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 创建客户端的启动对象 bootstrap ，不是 serverBootStrap
     */
    private static final Bootstrap bootstrap = new Bootstrap();
    private static ChannelPoolMap<InetSocketAddress, SimpleChannelPool> poolMap;

    private static SimpleChannelPool simpleChannelPool;

    private PoolProperties properties;

    public PoolClient(PoolProperties properties) {
        this.properties = properties;
        build();
        simpleChannelPool = poolMap.get(new InetSocketAddress(properties.getIp(), properties.getPort()));
    }

    private void build() {
        //设置线程组
        bootstrap.group(workerGroup)
                //初始化通道
                .channel(NioSocketChannel.class)
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
                .option(ChannelOption.TCP_NODELAY, true)
                /**
                 * The timeout period of the connection.
                 * If this time is exceeded or the connection cannot be established, the connection fails.
                 */
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NumberUtils.toInt(String.valueOf(properties.getConnectTimeOut().toMillis())));

        poolMap = new AbstractChannelPoolMap<InetSocketAddress, SimpleChannelPool>() {
            @Override
            protected SimpleChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(bootstrap.remoteAddress(key), new ChannelPoolHandler(), properties.getMaxConnection());
            }
        };
    }

    public <T> T sendRequest(TransHeader transHeader, TransContent transContent, TypeReference<? extends T> reference) throws Exception {
        T response = null;
        try {
            Future<Channel> future = simpleChannelPool.acquire();
            future.await();
            if (future.isSuccess()) {
                Channel ch = future.get();
                if (ch != null && ch.isActive() && ch.isWritable()) {
                    IoChannelHandler ioHandler = ChannelPoolHandler.ioChannelMap.get(ch.id());
                    if (ioHandler != null) {
                        //请求唯一标识序列化
                        byte[] headerBytes = MessagePackUtils.serialize(transHeader);
                        //请求体序列化
                        byte[] contentBytes = MessagePackUtils.serialize(transContent);
                        //TCP发送数据包，并对发送数据序列化
                        DataPacket packet = new DataPacket(headerBytes, contentBytes);
                        synchronized (ioHandler.object) {
                            //发送TCP请求
                            ch.writeAndFlush(packet);
                            ioHandler.object.wait(10000);
                        }
                        if (ioHandler.result == null) {
                            //todo
                        } else {
                            response = MessagePackUtils.deSerialize(ioHandler.result.content, reference);
                        }
                        //释放返回结果
                        ioHandler.result = null;
                    } else {
                        //todo
                    }
                } else {
                    //todo
                }
                if (ch != null) {
                    simpleChannelPool.release(ch);
                }

            } else {
                //todo
            }
        } catch (Exception exception) {
            //todo
        }
        return response;
    }
}
