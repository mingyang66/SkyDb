package com.emily.skydb.core.protocol;

/**
 * @Description :  请求头
 * @Author :  Emily
 * @CreateDate :  Created in 2023/2/25 3:39 PM
 */
public class HeadProtocol {
    /**
     * 请求头长度
     */
    public static final int LENGTH = 5;
    /**
     * 包类型，0-正常RPC请求，1-心跳包
     */
    private byte packageType = 0;
    /**
     * 消息体长度
     */
    private int packageLength;

    public HeadProtocol() {
    }

    public HeadProtocol(byte packageType, int packageLength) {
        this.packageType = packageType;
        this.packageLength = packageLength;
    }

    public byte getPackageType() {
        return packageType;
    }

    public void setPackageType(byte packageType) {
        this.packageType = packageType;
    }

    public int getPackageLength() {
        return packageLength;
    }

    public void setPackageLength(int packageLength) {
        this.packageLength = packageLength;
    }
}
