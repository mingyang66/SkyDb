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
    private byte packageType = 0;
    /**
     * 消息
     */
    private byte[] body;

    public DataPacket() {
    }

    public DataPacket(byte packageType, byte[] body) {
        this.packageType = packageType;
        this.body = body;
    }


    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte getPackageType() {
        return packageType;
    }

    public void setPackageType(byte packageType) {
        this.packageType = packageType;
    }

    public DataPacket(byte[] body) {
        this((byte) 0, body);
    }


}
