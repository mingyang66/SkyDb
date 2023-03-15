package com.emily.skydb.client.pool;

import com.emily.skydb.client.handler.HeartBeatChannelHandler;
import com.emily.skydb.client.handler.IoChannelHandler;
import com.emily.skydb.core.decoder.MessagePackDecoder;
import com.emily.skydb.core.encoder.MessagePackEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.ByteOrder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description :  客户端连接池
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/15 10:31 AM
 */
public class ChannelPoolHandler extends AbstractChannelPoolHandler {
    public static final Map<ChannelId, IoChannelHandler> ioChannelMap = new ConcurrentHashMap<>();

    @Override
    public void channelCreated(Channel ch) throws Exception {
        ioChannelMap.put(ch.id(), new IoChannelHandler());
        SocketChannel socketChannel = (SocketChannel) ch;
        socketChannel.config().setKeepAlive(true);
        socketChannel.config().setTcpNoDelay(true);

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
        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(ByteOrder.BIG_ENDIAN, 65535, 0, 2, 0, 2, true));
        //自定义解码器
        ch.pipeline().addLast(new MessagePackDecoder());
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
        ch.pipeline().addLast(new LengthFieldPrepender(ByteOrder.BIG_ENDIAN, 2, 0, false));
        //自定义编码器
        ch.pipeline().addLast(new MessagePackEncoder());
        //自定义handler处理
        ch.pipeline().addLast(ioChannelMap.get(ch.id()));
        //空闲状态处理器，参数说明：读时间空闲时间，0禁用时间|写事件空闲时间，0则禁用|读或写空闲时间，0则禁用
        ch.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        //心跳处理器
        ch.pipeline().addLast(new HeartBeatChannelHandler());
    }
}
