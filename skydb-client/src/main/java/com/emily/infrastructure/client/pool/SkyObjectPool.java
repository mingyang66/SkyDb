package com.emily.infrastructure.client.pool;

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
public class SkyObjectPool extends GenericObjectPool<SkyConnection> {
    public SkyObjectPool(PooledObjectFactory<SkyConnection> factory) {
        super(factory);
    }

    public SkyObjectPool(PooledObjectFactory<SkyConnection> factory, GenericObjectPoolConfig<SkyConnection> config) {
        super(factory, config);
    }

    public SkyObjectPool(PooledObjectFactory<SkyConnection> factory, GenericObjectPoolConfig<SkyConnection> config, AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}
