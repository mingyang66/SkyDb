package com.emily.skydb.core.protocol;

/**
 * @program: SkyDb
 * @description: Rpc消息尾部
 * @author: Emily
 * @create: 2021/10/09
 */
public class SkyTransTail {
    public static final byte[] TAIL = new byte[]{'\r', '\n'};
}
