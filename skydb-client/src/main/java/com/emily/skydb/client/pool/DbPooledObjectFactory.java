package com.emily.skydb.client.pool;

import com.emily.skydb.client.connection.DbClientConnection;
import com.emily.skydb.client.loadbalance.LoadBalance;
import com.emily.skydb.client.manager.DbClientProperties;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.util.Objects;

/**
 * @program: SkyDb
 * @description: 池化工厂类
 * @author: Emily
 * @create: 2021/09/28
 */
public class DbPooledObjectFactory implements PooledObjectFactory<DbClientConnection> {


    private DbClientProperties properties;
    private LoadBalance loadBalance;

    public DbPooledObjectFactory(DbClientProperties properties, LoadBalance loadBalance) {
        this.properties = properties;
        this.loadBalance = loadBalance;
    }

    /**
     * 创建可由对象池服务的实例，并将其包装到PooledObject对象交由池管理
     *
     * @return
     * @throws Exception
     */
    @Override
    public PooledObject<DbClientConnection> makeObject() throws Exception {
        System.out.println("创建对象...");
        //获取RPC服务器地址
        String address = loadBalance.selectServiceAddress(properties.getAddress());
        //RPC连接对象
        DbClientConnection connection = new DbClientConnection(properties);
        //建立Rpc连接
        connection.connect(address);
        return new DefaultPooledObject<>(connection);
    }

    /**
     * 销毁对象
     *
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void destroyObject(PooledObject<DbClientConnection> pooledObject) throws Exception {
        System.out.println("销毁对象...");
        DbClientConnection connection = pooledObject.getObject();
        if (Objects.nonNull(connection)) {
            connection.close();
        }
    }

    /**
     * 激活对象
     *
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void activateObject(PooledObject<DbClientConnection> pooledObject) throws Exception {
        System.out.println("激活对象...");
        DbClientConnection connection = pooledObject.getObject();
        if (!connection.isAvailable()) {
            //获取RPC服务器地址
            String address = loadBalance.selectServiceAddress(properties.getAddress());
            //建立Rpc连接
            connection.connect(address);
        }
    }

    /**
     * 钝化(初始化|归还)一个对象，也可以理解为反初始化
     *
     * @param pooledObject
     * @throws Exception
     */
    @Override
    public void passivateObject(PooledObject<DbClientConnection> pooledObject) throws Exception {
        System.out.println("钝化对象...");
    }

    /**
     * 验证对象是否可用
     *
     * @param pooledObject
     * @return
     */
    @Override
    public boolean validateObject(PooledObject<DbClientConnection> pooledObject) {
        DbClientConnection connection = pooledObject.getObject();
        if (!connection.isAvailable()) {
            //连接不可用，关闭连接
            connection.close();
            //获取RPC服务器地址
            String address = loadBalance.selectServiceAddress(properties.getAddress());
            //重新连接
            connection.connect(address);
        }
        System.out.println("验证对象是否可用:{}" + connection.isAvailable());
        return connection.isAvailable();
    }

}