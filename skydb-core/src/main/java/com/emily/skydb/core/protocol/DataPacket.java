package com.emily.skydb.core.protocol;

/**
 * @program: SkyDb
 * @description: Rpc客户端及服务端交互消息
 * @author: Emily
 * @create: 2021/10/09
 */
public class DataPacket {
    private HeadProtocol head;
    /**
     * 消息
     */
    private byte[] body;

    public DataPacket() {
    }

    public HeadProtocol getHead() {
        return head;
    }

    public void setHead(HeadProtocol head) {
        this.head = head;
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
        this.head = new HeadProtocol(packageType, body.length + HeadProtocol.LENGTH);
        this.body = body;
    }
}
