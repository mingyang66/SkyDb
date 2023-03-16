package com.emily.skydb.client.pool;

import com.emily.skydb.client.handler.IoChannelHandler;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.TransContent;
import com.emily.skydb.core.protocol.TransHeader;
import com.emily.skydb.core.utils.MessagePackUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.apache.commons.lang3.math.NumberUtils;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: SkyDb
 * @description: 创建Netty客户端及自定义处理器
 * @author: Emily
 * @create: 2021/09/17
 */
public class ChannelPoolClient {
    /**
     * 线程工作组
     */
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup();
    /**
     * 创建客户端的启动对象 bootstrap
     */
    private static final Bootstrap bootstrap = new Bootstrap();
    /**
     * 允许将特定的key映射到ChannelPool，可以获取匹配的ChannelPool,如果不存在则会创建一个新的对象
     * 即：根据不同的服务器地址初始化ChannelPoolMap
     */
    private static ChannelPoolMap<InetSocketAddress, ChannelPool> poolMap;
    /**
     * 计数器
     */
    private AtomicInteger counter = new AtomicInteger();
    private PoolProperties properties;

    public ChannelPoolClient(PoolProperties properties) {
        this.properties = properties;
        build();
        //初始化ChannelPoolMap
        properties.getAddress().stream().forEach(address -> {
            poolMap.get(new InetSocketAddress(address.getIp(), address.getPort()));
        });
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

        //ChannelPool存储、创建、删除管理Map类
        poolMap = new AbstractChannelPoolMap<>() {
            //如果ChannelPool不存在，则会创建一个新的对象
            @Override
            protected ChannelPool newPool(InetSocketAddress key) {
                return new FixedChannelPool(bootstrap.remoteAddress(key), new SimpleChannelPoolHandler(), properties.getMaxConnections());
            }
        };
    }


    /**
     * 通过轮询机制公平获取ChannelPool
     *
     * @return ChannelPool
     */
    public ChannelPool selectChannelPool() {
        AbstractChannelPoolMap temp = ((AbstractChannelPoolMap) poolMap);
        List<Map.Entry<InetSocketAddress, FixedChannelPool>> list = Lists.newArrayList(temp.iterator());
        //数据增加到最大Integer.MAX_VALUE后绝对值开始减小
        int pos = Math.abs(counter.getAndIncrement());
        return list.get(pos % list.size()).getValue();
    }

    /**
     * 发送请求
     *
     * @param transHeader  请求头
     * @param transContent 请求体
     * @param reference    返回值数据类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T sendRequest(TransHeader transHeader, TransContent transContent, TypeReference<? extends T> reference) throws Exception {
        T response = null;
        try {
            //获取ChannelPool连接池
            ChannelPool channelPool = selectChannelPool();
            //从ChannelPool中获取一个Channel
            final Future<Channel> future = channelPool.acquire();
            //等待future完成
            future.await();
            //判定I/O操作是否成功完成
            if (future.isSuccess()) {
                //无阻塞获取Channel对象
                final Channel ch = future.getNow();
                if (ch != null && ch.isActive() && ch.isWritable()) {
                    //获取信道对应的handler对象
                    final IoChannelHandler ioHandler = SimpleChannelPoolHandler.IO_HANDLER_MAP.get(ch.id());
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
                            //等待请求返回结果
                            ioHandler.object.wait(this.properties.getReadTimeOut().toMillis());
                        }
                        //根据返回结果做后续处理
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
                    //释放Channel到ChannelPool
                    channelPool.release(ch);
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
