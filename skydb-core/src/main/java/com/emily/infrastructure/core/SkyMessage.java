package com.emily.infrastructure.core;

/**
 * @program: SkyDb
 * @description: Rpc客户端及服务端交互消息
 * @author: Emily
 * @create: 2021/10/09
 */
public class SkyMessage {
    /**
     * 包类型，0-正常RPC请求，1-心跳包
     */
    private byte packageType = (byte) 0;
    /**
     * 消息体长度
     */
    private int len;
    /**
     * 消息
     */
    private byte[] body;

    public SkyMessage() {
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

    public static SkyMessage build(byte[] body) {
        return build((byte) 0, body);
    }

    public static SkyMessage build(byte packageType, byte[] body) {
        SkyMessage message = new SkyMessage();
        //设置包类型为心跳包
        message.setPackageType(packageType);
        //包长度
        message.setLen(body.length);
        //设置心跳包内容
        message.setBody(body);
        return message;
    }
}
