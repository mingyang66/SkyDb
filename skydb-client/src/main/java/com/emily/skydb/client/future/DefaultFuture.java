package com.emily.skydb.client.future;

/**
 * @Description : 异步请求响应
 * @Author :  Emily
 * @CreateDate :  Created in 2023/3/8 2:43 PM
 */
public class DefaultFuture {
    /**
     * 传输消息体
     */
    private byte[] transContent;
    /**
     * 请求消息体设置成功标记
     */
    private volatile boolean flag = false;
    /**
     * 锁对象
     */
    private final Object object = new Object();

    /**
     * 获取响应结果
     *
     * @param timeout 等待最长时间，单位：毫秒
     * @return
     */
    public byte[] get(long timeout) {
        synchronized (object) {
            while (!flag) {
                try {
                    object.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return transContent;
        }
    }

    /**
     * 设置响应结果
     *
     * @param transContent 响应结果
     */
    public void set(byte[] transContent) {
        if (flag) {
            return;
        }
        synchronized (object) {
            this.transContent = transContent;
            this.flag = true;
            object.notify();
        }
    }
}
