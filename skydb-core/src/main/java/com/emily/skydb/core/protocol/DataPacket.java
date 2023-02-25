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
     * 消息体长度
     */
    private int len;
    /**
     * 消息
     */
    private byte[] body;

    public DataPacket() {
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte getPackageType() {
        return packageType;
    }

    public void setPackageType(byte packageType) {
        this.packageType = packageType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public DataPacket(byte[] body) {
        this((byte) 0, body);
    }

    public DataPacket(byte packageType, byte[] body) {
        DataPacket message = new DataPacket();
        //设置包类型为心跳包
        message.setPackageType(packageType);
        //包长度
        message.setLen(body.length);
        //设置心跳包内容
        message.setBody(body);
        this.packageType = packageType;
        this.len = body.length;
        this.body = body;
    }
}
