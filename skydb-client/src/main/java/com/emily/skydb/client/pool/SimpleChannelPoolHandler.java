package com.emily.skydb.client.pool;

import com.emily.skydb.client.handler.HeartBeatChannelHandler;
import com.emily.skydb.client.handler.IoChannelHandler;
import com.emily.skydb.core.decoder.MessagePackDecoder;
import com.emily.skydb.core.encoder.MessagePackEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.pool.AbstractChannelPoolHandler;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.ByteOrder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description :  为ChannelPool处理各种操作调用的处理程序，如：创建、释放、获取处理程序
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/15 10:31 AM
 */
public class SimpleChannelPoolHandler extends AbstractChannelPoolHandler {
    /**
     * 缓存Channel与handler的映射关系
     */
    public static final Map<ChannelId, IoChannelHandler> ioHandlerMap = new ConcurrentHashMap<>();

    @Override
    public void channelAcquired(Channel ch) throws Exception {
        super.channelAcquired(ch);
        System.out.println("--------------------------------------------------------channelAcquired");
    }

    @Override
    public void channelReleased(Channel ch) throws Exception {
        super.channelReleased(ch);
        System.out.println("--------------------------------------------------------channelReleased");
    }

    /**
     * 在创建ChannelPool连接池时会调用此方法对Channel进行初始化
     *
     * @param ch
     * @throws Exception
     */
    @Override
    public void channelCreated(Channel ch) throws Exception {
        //缓存当前Channel对应的handler
        ioHandlerMap.put(ch.id(), new IoChannelHandler());

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
        pipeline.addLast(ioHandlerMap.get(ch.id()));
        //空闲状态处理器，参数说明：读时间空闲时间，0禁用时间|写事件空闲时间，0则禁用|读或写空闲时间，0则禁用
        pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
        //心跳处理器
        pipeline.addLast(new HeartBeatChannelHandler());
    }
}
