package com.emily.skydb.client.manager;

import com.emily.skydb.client.connection.DbClientConnection;
import com.emily.skydb.client.helper.DbDataHelper;
import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.pool.DbObjectPool;
import com.emily.skydb.client.pool.DbPooledObjectFactory;
import com.emily.skydb.core.protocol.DataPacket;
import com.emily.skydb.core.protocol.DbModelItem;
import com.emily.skydb.core.protocol.DbTransBody;
import com.emily.skydb.core.utils.MessagePackUtils;
import com.emily.skydb.core.utils.UUIDUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.List;
import java.util.Map;

/**
 * @program: SkyDb
 * @description: RPC客户端代理配置类
 * @author: Emily
 * @create: 2021/09/22
 */
public class DbClientManager {


    public static DbObjectPool POOL;

    public static DbObjectPool initPool(DbClientProperties properties, LoadBalance loadBalance) {
        if (POOL != null) {
            return POOL;
        }
        if (properties == null) {
            properties = new DbClientProperties();
        }
        DbPooledObjectFactory factory = new DbPooledObjectFactory(properties, loadBalance);
        //设置对象池的相关参数
        GenericObjectPoolConfig<DbClientConnection> poolConfig = new GenericObjectPoolConfig<>();
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
        POOL = new DbObjectPool(factory, poolConfig);

        initObjectPool(properties.getPool().getInitialSize(), properties.getPool().getMaxIdle());
        return POOL;
    }

    /**
     * 预先加载testObject对象到对象池中
     *
     * @param initialSize 初始化连接数
     * @param maxIdle     最大空闲连接数
     */
    private static void initObjectPool(int initialSize, int maxIdle) {
        if (initialSize <= 0) {
            return;
        }

        int size = Math.min(initialSize, maxIdle);
        for (int i = 0; i < size; i++) {
            try {
                POOL.addObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过连接池发送
     *
     * @return
     */
    private static <T> T invoke(DbTransBody transBody, TypeReference<? extends T> reference) throws Exception {
        //Channel对象
        DbClientConnection connection = null;
        try {
            //请求唯一标识序列化
            byte[] traceId = MessagePackUtils.serialize(UUIDUtils.randomSimpleUUID());
            //请求体序列化
            byte[] bodyBytes = MessagePackUtils.serialize(transBody);
            //TCP发送数据包，并对发送数据序列化
            DataPacket packet = new DataPacket(traceId, bodyBytes);
            //获取连接
            connection = POOL.borrowObject();
            //发送请求并获取返回结果
            byte[] response = connection.getClientChannelHandler().send(packet);
            //返回值反序列化
            return MessagePackUtils.deSerialize(response, reference);
        } finally {
            if (connection != null) {
                //归还链接
                POOL.returnObject(connection);
            }
        }
    }

    /**
     * 查询操作
     *
     * @param transBody 请求参数
     * @param cls       响应数据类型
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> List<T> executeQuery(DbTransBody transBody, Class<T> cls) throws Exception {
        List<Map<String, DbModelItem>> list = DbClientManager.invoke(transBody, new TypeReference<>() {
        });
        return DbDataHelper.getDbEntity(list, cls);
    }

    /**
     * 更新、删除、插入操作
     *
     * @param transBody 请求参数
     * @return 影响行数
     * @throws Exception
     */
    public static int executeUpdate(DbTransBody transBody) throws Exception {
        int rows = DbClientManager.invoke(transBody, new TypeReference<Integer>() {
        });
        return rows;
    }
}
