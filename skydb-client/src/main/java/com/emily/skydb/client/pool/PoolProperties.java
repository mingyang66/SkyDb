package com.emily.skydb.client.pool;


import java.time.Duration;

/**
 * @program: SkyDb
 * @description: RPC客户端属性配置类
 * @author: Emily
 * @create: 2021/09/22
 */
public class PoolProperties {
    /**
     * RPC服务器host地址列表，默认：127.0.0.1
     */
    private String ip = "127.0.0.1";
    /**
     * 端口号 默认：9999
     */
    private int port = 9999;
    /**
     * 读取超时时间，默认：10秒
     */
    private Duration readTimeOut = Duration.ofSeconds(10);
    /**
     * 连接超时时间，默认：5秒
     */
    private Duration connectTimeOut = Duration.ofSeconds(5);
    /**
     * 超过多长时间未发生读写就发送一次心跳包，默认：30秒
     */
    private Duration idleTimeOut = Duration.ofSeconds(30);
    /**
     * 最大连接数，默认：50
     */
    private int maxConnection = 50;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Duration getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(Duration readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    public Duration getConnectTimeOut() {
        return connectTimeOut;
    }

    public void setConnectTimeOut(Duration connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public Duration getIdleTimeOut() {
        return idleTimeOut;
    }

    public void setIdleTimeOut(Duration idleTimeOut) {
        this.idleTimeOut = idleTimeOut;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }
}