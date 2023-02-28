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
     * 消息
     */
    public byte[] body;

    public DataPacket() {
    }

    public DataPacket(byte[] body) {
        this((byte) 0, body);
    }

    public DataPacket(byte packageType, byte[] body) {
        this.packageType = packageType;
        this.body = body;
    }


}
