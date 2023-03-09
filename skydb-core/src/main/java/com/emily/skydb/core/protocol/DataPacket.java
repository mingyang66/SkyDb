package com.emily.skydb.core.protocol;

/**
 * @program: SkyDb
 * @description: Rpc客户端及服务端交互消息
 * @author: Emily
 * @create: 2021/10/09
 */
public class DataPacket {
    /**
     * 包类型，0-正常RPC请求，1-心跳包
     */
    public byte packageType = 0;
    /**
     * 请求唯一标识，34个字节长度
     */
    public byte[] tracedId;
    /**
     * 消息
     */
    public byte[] content;

    public DataPacket() {
    }

    public DataPacket(byte[] tracedId, byte[] content) {
        this((byte) 0, tracedId, content);
    }

    public DataPacket(byte packageType, byte[] tracedId, byte[] content) {
        this.packageType = packageType;
        this.tracedId = tracedId;
        this.content = content;
    }


}
