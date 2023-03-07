package com.emily.skydb.client.manager;


import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @program: SkyDb
 * @description: RPC客户端属性配置类
 * @author: Emily
 * @create: 2021/09/22
 */
public class DbClientProperties {
    /**
     * 是否开启RPC客户端配置类
     */
    private boolean enabled = true;
    /**
     * RPC服务器host地址列表，默认：127.0.0.1
     */
    private List<String> address = Arrays.asList("127.0.0.1:9999");
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
     * 连接池
     */
    private Pool pool = new Pool();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
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

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public static class Pool {
        /**
         * 最大空闲数
         */
        private int maxIdle = 8;
        /**
         * 最大链接数
         */
        private int maxTotal = 8;
        /**
         * 最小空闲数
         */
        private int minIdle = 0;

        /**
         * 初始化连接数
         */
        private int initialSize = 3;

        public int getMaxIdle() {
            return maxIdle;
        }

        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(int initialSize) {
            this.initialSize = initialSize;
        }
    }
}
