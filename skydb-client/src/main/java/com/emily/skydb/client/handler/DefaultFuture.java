package com.emily.skydb.client.handler;

/**
 * @Description : 异步请求响应
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/8 2:43 PM
 */
public class DefaultFuture {
    /**
     * 传输消息体
     */
    private byte[] transBody;
    /**
     * 请求消息体设置成功标记
     */
    private volatile boolean flag = false;
    /**
     * 锁对象
     */
    private final Object object = new Object();

    public byte[] get(long timeout) {
        synchronized (object) {
            while (!flag) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return transBody;
        }
    }

    public void set(byte[] transBody) {
        if (flag) {
            return;
        }
        synchronized (object) {
            this.transBody = transBody;
            this.flag = true;
            object.notify();
        }
    }
}
