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
    public byte packageType = 0;


    public HeadProtocol() {
    }

    public HeadProtocol(byte packageType) {
        this.packageType = packageType;
    }

    public byte getPackageType() {
        return packageType;
    }

    public void setPackageType(byte packageType) {
        this.packageType = packageType;
    }
}
