package com.emily.skydb.server.manager;

import java.time.Duration;

/**
 * @program: SkyDb
 * @description: RPC服务端配置
 * @author: Emily
 * @create: 2021/09/22
 */
public class DbServerProperties {
    /**
     * 端口号,默认：9999
     */
    private int port = 9999;
    /**
     * 超过多长时间未发生读写就发送一次心跳包，默认：30秒
     */
    private Duration idleTimeOut = Duration.ofSeconds(30);

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Duration getIdleTimeOut() {
        return idleTimeOut;
    }

    public void setIdleTimeOut(Duration idleTimeOut) {
        this.idleTimeOut = idleTimeOut;
    }
}
