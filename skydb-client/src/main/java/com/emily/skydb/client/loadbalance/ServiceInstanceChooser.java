package com.emily.skydb.client.loadbalance;

import com.emily.skydb.client.pool.SkyConnection;

import java.util.List;

/**
 * 选取可用负载均衡器
 */
@Deprecated
public interface ServiceInstanceChooser {
    /**
     * 选择可用服务连接对象
     *
     * @param serviceAddress 服务器地址
     * @return
     */
    SkyConnection choose(List<String> serviceAddress);
}
