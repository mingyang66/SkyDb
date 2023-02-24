package com.emily.skydb.core.entity;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @program: SkyDb
 * @description: Rpc客户端及服务端交互消息
 * @author: Emily
 * @create: 2021/10/09
 */
public class SkyTransMessage {
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

    public SkyTransMessage() {
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

    public static SkyTransMessage build(byte[] body) {
        return build((byte) 0, body);
    }

    public static SkyTransMessage build(byte packageType, byte[] body) {
        SkyTransMessage message = new SkyTransMessage();
        //设置包类型为心跳包
        message.setPackageType(packageType);
        //包长度
        message.setLen(body.length);
        //设置心跳包内容
        message.setBody(body);
        //ByteBuf buf = Unpooled.buffer().writeByte(packageType).readBytes(body.length).writeBytes(body);
        return message;
    }
}
