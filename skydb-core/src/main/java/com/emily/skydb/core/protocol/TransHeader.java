package com.emily.skydb.core.protocol;

/**
 * @Description :  请求头
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/9 4:47 PM
 */
public class TransHeader {
    /**
     * 请求唯一标识，32个字符，34个字节长度
     */
    public String tracedId;

    public TransHeader() {
    }

    public TransHeader(String tracedId) {
        this.tracedId = tracedId;
    }
}
