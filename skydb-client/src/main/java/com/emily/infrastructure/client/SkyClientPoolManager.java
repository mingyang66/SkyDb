package com.emily.infrastructure.client;

import com.emily.infrastructure.client.loadbalance.LoadBalance;
import com.emily.infrastructure.client.loadbalance.RoundLoadBalance;
import com.emily.infrastructure.client.pool.SkyConnection;
import com.emily.infrastructure.client.pool.SkyObjectPool;
import com.emily.infrastructure.client.pool.SkyPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @program: SkyDb
 * @description: RPC客户端代理配置类
 * @author: Emily
 * @create: 2021/09/22
 */
public class SkyClientPoolManager {


    private static SkyObjectPool pool;

    public static SkyObjectPool getObjectPool() {
        if (pool != null) {
            return pool;
        }
        SkyClientProperties properties = new SkyClientProperties();
        LoadBalance loadBalance = new RoundLoadBalance();
        SkyPooledObjectFactory factory = new SkyPooledObjectFactory(properties, loadBalance);
        //设置对象池的相关参数
        GenericObjectPoolConfig<SkyConnection> poolConfig = new GenericObjectPoolConfig<>();
        //最大空闲连接数
        poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
        //最小空闲连接数
        poolConfig.setMinIdle(properties.getPool().getMinIdle());
        //最大链接数
        poolConfig.setMaxTotal(properties.getPool().getMaxTotal());
        //当对象池没有空闲对象时，新的获取对象的请求是否阻塞，true-阻塞(maxWait才生效)
        poolConfig.setBlockWhenExhausted(true);
        //对象池中无对象时最大等待时间
        poolConfig.setMaxWaitMillis(100);
        //向调用者输出"链接"资源时，是否检测有效性，如果无效则从连接池中移除，并继续尝试获取，默认：false
        poolConfig.setTestOnBorrow(true);
        //向链接池归还链接时，是否检测链接对象的有效性，默认：false
        poolConfig.setTestOnReturn(true);
        //向调用者输出链接对象时，是否检测它的空闲超时，默认：false
        poolConfig.setTestWhileIdle(true);
        //空闲链接检测线程，检测周期，单位：毫秒，如果为负值，标识不运行检测线程，默认：-1
        poolConfig.setTimeBetweenEvictionRunsMillis(60 * 1000);
        //一定要关闭jmx，不然springboot启动会报已经注册了某个jmx的错误
        poolConfig.setJmxEnabled(false);

        //新建一个对象池,传入对象工厂和配置
        pool = new SkyObjectPool(factory, poolConfig);

        initPool(properties.getPool().getInitialSize(), properties.getPool().getMaxIdle());
        return pool;
    }

    /**
     * 预先加载testObject对象到对象池中
     *
     * @param initialSize 初始化连接数
     * @param maxIdle     最大空闲连接数
     */
    private static void initPool(int initialSize, int maxIdle) {
        if (initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize, maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                pool.addObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
