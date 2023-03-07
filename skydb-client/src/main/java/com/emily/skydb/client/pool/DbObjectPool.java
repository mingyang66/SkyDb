package com.emily.skydb.client.pool;

import com.emily.skydb.client.connection.DbClientConnection;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @program: SkyDb
 * @description: 自定义对象池
 * @author: Emily
 * @create: 2021/09/28
 */
public class DbObjectPool extends GenericObjectPool<DbClientConnection> {
    public DbObjectPool(PooledObjectFactory<DbClientConnection> factory) {
        super(factory);
    }

    public DbObjectPool(PooledObjectFactory<DbClientConnection> factory, GenericObjectPoolConfig<DbClientConnection> config) {
        super(factory, config);
    }

    public DbObjectPool(PooledObjectFactory<DbClientConnection> factory, GenericObjectPoolConfig<DbClientConnection> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
